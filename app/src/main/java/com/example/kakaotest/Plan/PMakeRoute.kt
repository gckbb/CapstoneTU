package com.example.kakaotest.Plan

import android.content.ContentValues
import android.util.Log
import com.example.kakaotest.Map.ApiAdapter
import com.google.android.play.integrity.internal.i
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import java.lang.Exception
import java.sql.Types.NULL
import java.util.LinkedList
import java.util.ArrayList

class PMakeRoute {
    private var totalRouteList = LinkedList<DRouteData>()
    private var routeList = LinkedList<PSearchRouteData>()
    private var saveList = LinkedList<PSearchRouteData>()
    private var dayRouteList = LinkedList<PSearchRouteData>()
    private val apiAdapter = ApiAdapter()
    private lateinit var startPoint: SelectedPlaceData
    val AllRouteList = LinkedList<DRouteData>()

    fun routeSet(selectedPlaceList: ArrayList<SelectedPlaceData>, startPoint: SelectedPlaceData) {
        try {
            Log.d("PLAN", "Start Point: $startPoint")
            this.startPoint = startPoint
            var routeData: PSearchRouteData
            var time: Number?

            for (i in 1 until selectedPlaceList.count()) {
                val time = runBlocking {
                    async(Dispatchers.IO) {
                        apiAdapter.apiRequest(
                            startPoint.tpoint.longitude, startPoint.tpoint.latitude,
                            selectedPlaceList[i].tpoint.longitude, selectedPlaceList[i].tpoint.latitude
                        )
                    }.await()
                }

                if (time != null) {
                    routeData = PSearchRouteData(selectedPlaceList[i], time)
                    routeList.add(routeData)
                    println("time is $time for ${selectedPlaceList[i].placeName}")
                } else {
                    Log.e("PLAN", "Received null time for ${selectedPlaceList[i].placeName}")
                }
            }
            // routeList.removeAt(0)
            // saveList = routeList
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

    //totalDate만큼 진행,maxDayTime이 하루의 최대여행시간(단위는 시간), 장소 하나당 1시간 가정
    suspend fun routeStart(totalDate: Int, maxDayTime: Int, stayTimePerPlace: Int) {
        coroutineScope {
            try {
                for (k in 0 until totalDate) {
                    var totalTime: Int = 0
                    var currentDayTime: Int = 0
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
                    totalTime = currentDayTime + stayTimePerPlace * (dayRouteList.size - 1)
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
    suspend fun findMinPoint(startRouteData: PSearchRouteData): Boolean {
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
/*
    fun printTotalRoute(): List<String>  {
        val routeStringList = mutableListOf<String>()
        try {
            var dayIndex = 1 // 루트의 일수 인덱스
            for (i in 0 until totalRouteList.count()) {
                val dayStringBuilder = StringBuilder()
                //dayStringBuilder.append("$dayIndex Day\n") // 일수 인덱스를 출력
                for (k in 0 until (totalRouteList[i].dayRoute?.count()!!)) {
                    dayStringBuilder.append("${totalRouteList[i].dayRoute?.get(k)?.pointdata?.placeName}  ")
                }
                dayStringBuilder.append("총 이동시간 : ${totalRouteList[i].totalTime}\n")
                routeStringList.add(dayStringBuilder.toString())

                dayIndex++ // 다음 일수로 이동
            }
        } catch (e: Exception) {
            // 예외가 발생하면 로그로 출력
            Log.e("PLAN", "getTotalRouteList - Exception: ${e.toString()}", e)
        }
        return routeStringList
    }

 */
/*
    fun printTotalRoute(): MutableList<List<String>> {
        val routeList = mutableListOf<List<String>>()

        try {
            var dayIndex = 1 // 루트의 일수 인덱스
            for (i in 0 until totalRouteList.count()) {
                val dayRoute = mutableListOf<String>() // 각 날짜의 경로를 저장할 리스트 초기화
                for (k in 0 until (totalRouteList[i].dayRoute?.count() ?: 0)) {
                    val placeData = totalRouteList[i].dayRoute?.get(k)?.pointdata
                    val placeInfo = mutableListOf<String>()
                    placeInfo.add(placeData?.placeName ?: "")
                    placeInfo.add(placeData?.tpoint.toString())
                    placeInfo.add(placeData?.address ?: "")
                    dayRoute.add(placeInfo.toString())
                }
                routeList.add(dayRoute) // 각 날짜의 경로를 전체 리스트에 추가
                dayIndex++ // 다음 일수로 이동
            }
        } catch (e: Exception) {
            // 예외가 발생하면 로그로 출력
            Log.e("PLAN", "getTotalRouteList - Exception: ${e.toString()}", e)
        }

        return routeList
    }

*/



    fun printTotalRoute(): Array<Array<Array<String>>> {
        val totalDate: Int = totalRouteList.size
        val dayRoute = Array(totalDate) { Array(0) { Array(3) { "" } } }

        try {
            for (i in 0 until totalDate) { // i일째
                val placesPerDay: Int = totalRouteList[i].dayRoute.size // i일째 장소들 개수
                dayRoute[i] = Array(placesPerDay) { Array(3) { "" } }
                for (j in 0 until placesPerDay) { // 해당 날짜의 장소 개수만큼 반복
                    dayRoute[i][j][0] = "${totalRouteList[i].dayRoute[j].pointdata?.placeName}" // 장소 이름 추가
                    dayRoute[i][j][1] = "${totalRouteList[i].dayRoute[j].pointdata?.tpoint}" // 장소 위치 추가
                    dayRoute[i][j][2] = "${totalRouteList[i].dayRoute[j].pointdata?.address}" // 장소 주소 추가


                }

                val totalTime = "총 이동시간 : ${totalRouteList[i].totalTime}\n"
                dayRoute[i][placesPerDay][3] = totalTime
            }
        } catch (e: Exception) {
            // 예외가 발생하면 로그로 출력
            Log.e("PLAN", "getTotalRouteList - Exception: ${e.toString()}", e)
        }
        return dayRoute
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


