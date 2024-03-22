package com.example.kakaotest.Plan

import android.content.ContentValues
import android.util.Log
import com.example.kakaotest.Map.ApiAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import java.lang.Exception
import java.util.LinkedList
import java.util.ArrayList

class PMakeRoute {
    private var totalRouteList = LinkedList<DRouteData>()
    private var routeList = LinkedList<PSearchRouteData>()
    private var saveList = LinkedList<PSearchRouteData>()
    private var dayRouteList = LinkedList<PSearchRouteData>()
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
            Log.d("PLAN", "Start Point: $startPoint")
            this.startPoint = startPoint
            var routeData: PSearchRouteData
            var time: Number?

            for (i in 1 until selectedPlaceList.size) {
                time = apiRequest(
                    startPoint.tpoint.longitude, startPoint.tpoint.latitude,
                    selectedPlaceList[i].tpoint.longitude, selectedPlaceList[i].tpoint.latitude
                )

                if (time != null) {
                    routeData = PSearchRouteData(selectedPlaceList[i], time)
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


/*
    // routeStart 함수는 주어진 totalDate 만큼의 날짜에 대해 루트를 생성하는 함수입니다.
    suspend fun routeStart(totalDate: Int, maxDayTime: Int) {
        for (k in 0 until totalDate) { // 총 날짜 수만큼 반복합니다.
            lateinit var totalTime: Number // 각 날짜별 총 소요 시간을 나타내는 변수
            var minIndex: Int = 0 // 최소 시간을 가진 루트의 인덱스를 저장할 변수
            var minTime: Number? = routeList[0].time // 최소 시간을 가진 루트의 시간을 저장할 변수
            dayRouteList.add(PSearchRouteData(startPoint, 0)) // 출발지를 추가합니다.

            // 루트 리스트에서 최소 시간을 가진 루트를 찾습니다.
            for (i in 0 until routeList.count()) {
                if (minTime != null) {
                    if (minTime.toInt() > routeList[i].time.toInt()) {
                        minTime = routeList.get(i).time
                        minIndex = i
                    }
                }
            }

            // 최소 시간을 가진 루트를 일자별 루트 리스트에 추가하고, 루트 리스트에서 제거합니다.
            dayRouteList.add(routeList.get(minIndex))
            routeList.removeAt(minIndex)

            if (minTime != null) {
                dayRouteList[0].time = minTime // 출발지의 시간을 최소 시간으로 설정합니다.
            }

            // 각 장소에서 1시간씩 체류하는 코드를 추가합니다.
            for (routeData in dayRouteList) {
                routeData.time = (routeData.time.toInt() + 3600).toLong() // 각 장소의 시간에 1시간을 추가합니다.

            }

            // 일자별 최대 시간을 초과하지 않도록 처리합니다.
            for (i in 1..maxDayTime) {
                totalTime = (dayRouteList.count() - 1) * 3600 + dayRouteList.last.time.toInt()

                // 총 소요 시간이 최대 시간을 초과하는 경우 처리합니다.
                if (totalTime > maxDayTime * 3600) {
                    dayRouteList[0].time =
                        dayRouteList[0].time.toInt() - dayRouteList.last.time.toInt()
                    dayRouteList.removeLast()
                    totalTime = (dayRouteList.count() - 1) * 3600 + dayRouteList.last.time.toInt()
                    break
                }

                // 다음 장소를 찾을 수 없는 경우 처리합니다.
                if (!findMinPoint(dayRouteList.last)) {
                    Log.d("PLAN", "routeStart break")
                    dayRouteList.clear()
                    totalRouteList.clear()
                    routeList = saveList
                    return
                }
            }


            // 일자별 루트를 전체 루트 리스트에 추가하고, 일자별 루트 리스트를 초기화합니다.
            var tempRouteList = DRouteData(totalTime,LinkedList<PSearchRouteData>())
            tempRouteList.dayRoute?.addAll(dayRouteList)
            totalRouteList.add(tempRouteList)
            dayRouteList.clear()
        }
    }*/

    suspend fun routeStart(totalDate: Int, maxDayTime: Int) {
        coroutineScope {
            try {
                for (k in 0 until totalDate) {
                    var totalTime: Double = 0.0
                    var currentDayTime: Double = 0.0
                    var remainingTime: Int = maxDayTime * 3600 // 남은 시간을 초 단위로 계산

                    dayRouteList.add(PSearchRouteData(startPoint, 0))
                    Log.d("PLAN", "dayRouteList : ${dayRouteList.toString()}")

                    // 최단 시간 경로를 구하는 대신, 최대한 많은 장소를 방문하는 로직 추가
                    while (routeList.isNotEmpty() && currentDayTime + routeList.first().time.toInt() <= remainingTime) {
                        val minTime = routeList.minByOrNull { it.time.toInt() } ?: break
                        if (currentDayTime + minTime.time.toInt() > remainingTime) break

                        // 장소 추가
                        dayRouteList.add(minTime)
                        currentDayTime += minTime.time.toInt()
                        remainingTime -= minTime.time.toInt()

                        // 해당 장소를 routeList에서 제거
                        routeList.remove(minTime)
                    }

                    Log.d("PLAN", "dayRouteList : ${dayRouteList.toString()}")

                    // 총 이동 시간 계산 (이동 시간 + 체류 시간)
                    totalTime = currentDayTime +1 * (dayRouteList.size - 1)
                    Log.d("PLAN","totalTime : ${totalTime}")
                    // 현재 일자의 경로를 추가
                    totalRouteList.add(DRouteData(totalTime, LinkedList(dayRouteList)))

                    // 현재 일자의 경로가 선택되면, dayRouteList를 초기화
                    dayRouteList.clear()
                }
            } catch (e: Exception) {
                Log.e("PLAN", "routeStart - Exception: ${e.toString()}", e)
            }
        }
    }

    //경로의 마지막 장소와 가장 가까운 장소를 다음 장소로 추가하는 함수

    suspend fun findMinPoint(startRouteData: PSearchRouteData): Boolean = coroutineScope {
        var minIndex: Int = 0
        var minTime: Int? = null

        val deferredRequests = routeList.map { route ->
            async(Dispatchers.IO) {
                apiAdapter.apiRequest(
                    startRouteData.pointdata!!.tpoint.longitude,
                    startRouteData.pointdata.tpoint.latitude,
                    route.pointdata!!.tpoint.longitude,
                    route.pointdata!!.tpoint.latitude
                )?.toInt() ?: Int.MAX_VALUE // Handle null values gracefully
            }
        }

        val responseTimes = deferredRequests.map { it.await() }

        minTime = responseTimes.minOrNull()

        if (minTime == null) {
            Log.d(ContentValues.TAG, "findMinPoint break")
            return@coroutineScope false
        } else {
            minIndex = responseTimes.indexOf(minTime)
            dayRouteList.add(routeList[minIndex])
            routeList.removeAt(minIndex)
            dayRouteList[0].time = dayRouteList[0].time.toInt() + minTime
            return@coroutineScope true
        }
    }




    fun findInList(findData:PSearchRouteData):Int {
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



    fun printTotalRoute(): LinkedList<DRouteData> {

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

