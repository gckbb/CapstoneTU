package com.example.kakaotest.Plan

import android.util.Log
import com.example.kakaotest.Map.ApiAdapter
import com.skt.tmap.TMapPoint
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
    data class RouteData(
        val routeStringList: List<List<String>>,
        val routeTpointList: List<List<String>>
    )

    fun printTotalRoute(): RouteData {
        val routeStringList = mutableListOf<List<String>>()
        val routeTpointList = mutableListOf<List<String>>()
        try {
            for (i in 0 until totalRouteList.count()) {
                val dayRouteList = mutableListOf<String>()
                val tpointList = mutableListOf<String>()
                for (k in 0 until (totalRouteList[i].dayRoute?.count()!!)) {
                    val placeName = totalRouteList[i].dayRoute?.get(k)?.pointdata?.placeName
                    val tpoint = totalRouteList[i].dayRoute?.get(k)?.pointdata?.tpoint
                    val address = totalRouteList[i].dayRoute?.get(k)?.pointdata?.address
                    if (placeName != null) {
                        dayRouteList.add(placeName)
                    }
                    if (tpoint != null) {
                        tpointList.add(tpoint.toString())
                    }
                }
                routeStringList.add(dayRouteList)
                routeTpointList.add(tpointList)
            }
        } catch (e: Exception) {
            // 예외가 발생하면 로그로 출력
            Log.e("PLAN", "getTotalRouteList - Exception: ${e.toString()}", e)
        }
        return RouteData(routeStringList, routeTpointList)
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

