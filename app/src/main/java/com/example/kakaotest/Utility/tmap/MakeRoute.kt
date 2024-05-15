package com.example.kakaotest.Utility.tmap

import android.content.ContentValues
import android.util.Log
import com.example.kakaotest.DataModel.tmap.DayRouteData
import com.example.kakaotest.DataModel.tmap.SearchRouteData
import com.example.kakaotest.DataModel.tmap.SelectedPlaceData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.lang.Exception
import java.util.LinkedList
import java.util.ArrayList

class MakeRoute {
    private var totalRouteList = LinkedList<DayRouteData>()
    private var routeList = LinkedList<SearchRouteData>()
    private var saveList = LinkedList<SearchRouteData>()
    private var dayRouteList = LinkedList<SearchRouteData>()
    private val apiAdapter = ApiAdapter()
    private lateinit var startPoint: SelectedPlaceData
    suspend fun apiRequest(startLongitude: Double, startLatitude: Double, endLongitude: Double, endLatitude: Double): Number? {
        return coroutineScope {
            val deferredTime = async(Dispatchers.IO) {
                apiAdapter.apiRequest(startLongitude, startLatitude, endLongitude, endLatitude)
            }
            deferredTime.await()?.toInt()
        }
    }

    suspend fun routeSet(selectedPlaceList: ArrayList<SelectedPlaceData>, startPoint: SelectedPlaceData) {
        try {
            Log.d("makeroute", "Start Point: $startPoint")
            Log.d("makeroute", "routelist : $selectedPlaceList")
            this.startPoint = startPoint
            var routeData: SearchRouteData
            var time: Number?

            for (i in 1 until selectedPlaceList.size) {
                time = apiRequest(
                    startPoint.tpoint.longitude, startPoint.tpoint.latitude,
                    selectedPlaceList[i].tpoint.longitude, selectedPlaceList[i].tpoint.latitude
                )

                if (time != null) {
                    routeData = SearchRouteData(selectedPlaceList[i], time)
                    routeList.add(routeData)

                } else {
                    Log.e("PLAN", "Received null time for ${selectedPlaceList[i].placeName}")
                }
            }
          //  routeList.removeAt(0)
          //  saveList = routeList
            saveList.addAll(routeList)

            Log.d("PLAN", "Route List:")
            routeList.forEachIndexed { index, routeData ->
                Log.d("PLAN", "Index $index: ${routeData.pointdata}")
            }
        } catch (e: Exception) {
            Log.e("PLAN", "routeSet - Exception in routeSet: ${e.toString()}")
            e.printStackTrace()
        }
    }



    suspend fun routeStart(totalDate: Int, maxDayTime: Int,stayTimePerPlace : Int, foodDataList:ArrayList<SelectedPlaceData>) {
        coroutineScope {
            try {

                for (k in 0 until totalDate) {
                    var totalTime: Double = 0.0
                    var currentDayTime: Double = 0.0
                    var remainingTime: Int = maxDayTime * 3600 // 남은 시간을 초 단위로 계산
                    var lunchcheck = 0



                    dayRouteList.add(SearchRouteData(startPoint, 0))

                    // 최단 시간 경로를 구하는 대신, 최대한 많은 장소를 방문하는 로직 추가
                    while (routeList.isNotEmpty() && currentDayTime + routeList.first().time.toInt() <= remainingTime) {
                        val minTime = routeList.minByOrNull { it.time.toInt() } ?: break

                        if (currentDayTime + minTime.time.toInt() > remainingTime) break

                        if(currentDayTime > 4 * 3600 && lunchcheck == 0){
                            var minfood = 999999
                            var mindata : SelectedPlaceData? = null
                            for ( i in 0 until foodDataList.size){

                                val temp = apiRequest(dayRouteList.last.pointdata?.tpoint?.longitude!!,dayRouteList.last.pointdata?.tpoint?.latitude!!,
                                    foodDataList[i].tpoint.longitude,foodDataList[i].tpoint.latitude)

                                if (temp!!.toInt() < minfood.toInt()) {
                                    minfood = temp.toInt()

                                    mindata = foodDataList[i]



                                }
                            }


                            dayRouteList.add(SearchRouteData(mindata,minfood))
                            currentDayTime += minfood.toInt()

                            currentDayTime += 3600

                            lunchcheck = 1
                            foodDataList.remove(mindata)
                            continue
                        }
                        dayRouteList.add(minTime)


                        currentDayTime += minTime.time.toInt() + 3600

                        routeList.remove(minTime)
                    }



                    // 총 이동 시간 계산 (이동 시간 + 체류 시간)
                    totalTime = currentDayTime + stayTimePerPlace * (dayRouteList.size - 1)

                    // 현재 일자의 경로를 추가
                    totalRouteList.add(DayRouteData(totalTime, LinkedList(dayRouteList)))

                    // 현재 일자의 경로가 선택되면, dayRouteList를 초기화
                    dayRouteList.clear()
                }
            } catch (e: Exception) {
                Log.e("PLAN", "routeStart - Exception: ${e.toString()}", e)
            }
        }
    }



    //경로의 마지막 장소와 가장 가까운 장소를 다음 장소로 추가하는 함수
    suspend fun findMinPoint(startRouteData: SearchRouteData): Boolean {
        return try {
            if (routeList.isEmpty()) {
                Log.d("PLAN", "Route list is empty in findMinPoint")
                return false
            }

            val asyncResults = routeList.map { route ->
                coroutineScope {
                    async(Dispatchers.IO) {
                        apiAdapter.apiRequest(
                            startRouteData.pointdata!!.tpoint.longitude,
                            startRouteData.pointdata.tpoint.latitude,
                            route.pointdata!!.tpoint.longitude,
                            route.pointdata!!.tpoint.latitude
                        )
                    }
                }
            }

            // 모든 비동기 작업 완료 및 결과를 times에 매핑
            val times = asyncResults.mapNotNull { asyncResult -> asyncResult.await()?.toInt() }

            if (times.isEmpty()) {
                Log.d("PLAN", "Time list is empty in findMinPoint")
                return false
            }

            // 최소 이동 시간 및 해당 인덱스 찾기
            val minTime = times.minOrNull() ?: return false
            val minIndex = times.indexOf(minTime)

            // 최소 이동 시간의 장소를 dayRouteList에 추가하고 routeList에서 제거
            dayRouteList.add(routeList[minIndex])
            Log.d("PLAN", "dayRouteList : ${dayRouteList.toString()}")
            routeList.removeAt(minIndex)
            // 이동 시간 추가
            dayRouteList[0].time = dayRouteList[0].time.toInt() + minTime

            true // 성공적으로 최소 이동 시간을 찾았으므로 true 반환
        } catch (e: Exception) {
            // 예외 발생 시 로그 출력 및 false 반환
            Log.e("PLAN", "findMinPoint - Exception: ${e.toString()}", e)
            false
        }
    }

    fun findInList(findData: SearchRouteData):Int {
        try {
            for (i in 0 until saveList.count()) {
                if (saveList[i] == findData) {

                    return i
                }
            }
            return -1
        } catch (e: Exception) {
            Log.e("PLAN", "Exception: ${e.toString()}", e)
            return -1
        }
    }



    fun printTotalRoute(): LinkedList<DayRouteData> {

        try {
            for (i in 0 until totalRouteList.count()) {
                println("${i + 1} Day")
                for (k in 0 until (totalRouteList[i].dayRoute?.count() ?: 0)) {
                    val placeInfo = totalRouteList[i].dayRoute?.get(k)
                    Log.d("PLAN", placeInfo?.pointdata!!.placeName)
                }

                val hour = totalRouteList[i].totalTime.toDouble() / 3600
                //  Log.d("PLAN","${i}일째 총 이동시간 : "+hour.toString())
                val totalHour: Double = String.format("%.1f", hour).toDouble()
                Log.d("PLAN", "${i+1}일째 총 이동시간 : " + totalHour)
                val totalTime = "총 이동시간 : ${totalHour} 시간"
                //  println(totalTime)


            }

        } catch (e: Exception) {
            Log.e("PLAN", "getTotalRouteList - Exception: ${e.toString()}", e)
        }


        return totalRouteList
    }

    fun printAllRoute() {
        try{
            for(i in 0 until dayRouteList.count()) {
                println("${dayRouteList[i].pointdata?.placeName}")
                Log.d("printAllRoute","${dayRouteList[i].pointdata?.placeName}")
            }
        }catch (e:Exception){

            Log.e("PLAN", "printAllRoute - Exception: ${e.toString()}", e)
        }    }

}

