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
    suspend fun routeStart(totalDate:Int, maxDayTime:Int) {
        coroutineScope {
            try {
                for (k in 0 until totalDate) {
                    var totalTime: Int = 0 // totalTime을 Int 타입으로 선언
                    var minIndex: Int = 0
                    var minTime: Number? = routeList[0].time
                    dayRouteList.add(PSearchRouteData(startPoint, 0))
                    for (i in 0 until routeList.count()) {
                        if (minTime != null) {
                            if (minTime.toInt() > routeList[i].time.toInt()) {
                                minTime = routeList.get(i).time
                                minIndex = i
                            }
                        }
                    }
                    dayRouteList.add(routeList.get(minIndex))

                    if (minTime != null) {
                        dayRouteList[0].time = minTime
                        Log.d("PLAN","mintime -  ${minTime.toString()}")
                    }
                    for (i in 1..maxDayTime) {
                        val currentDayTotalTime =
                            (dayRouteList.count() - 1) * 3600 + dayRouteList.last.time.toInt() // 현재 루프에서의 totalTime 계산
                        totalTime += currentDayTotalTime // 새로운 totalTime 변수에 현재 루프에서의 totalTime 추가

                        Log.d("PLAN","totalTime $i : ${totalTime.toString()}")

                        if (totalTime > maxDayTime * 3600) {
                            dayRouteList[0].time =
                                dayRouteList[0].time.toInt() - dayRouteList.last.time.toInt()
                            dayRouteList.removeLast()
                            break
                        }
                        if (!findMinPoint(dayRouteList.last)) {
                            Log.d("PLAN", "routeStart break")
                            dayRouteList.clear()
                            totalRouteList.clear()
                            routeList = saveList
                            return@coroutineScope
                        }
                    }
                    var tempRouteList = DRouteData(totalTime, LinkedList<PSearchRouteData>())
                    tempRouteList.dayRoute?.addAll(dayRouteList)
                    totalRouteList.add(tempRouteList)
                    dayRouteList.clear()
                }
            } catch(e: Exception) {
                Log.e("PLAN", "${e.message}")
                e.printStackTrace()
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
                // 비동기 호출을 위해 coroutineScope 사용
                coroutineScope {
                    async(Dispatchers.IO) {
                        // 비동기로 apiRequest 호출 및 결과 반환
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

    fun printTotalRoute():List<String>  {
        val routeStringList = mutableListOf<String>()
        try {
            for (i in 0 until totalRouteList.count()) {
                val dayStringBuilder = StringBuilder()
                dayStringBuilder.append("${i + 1} Day\n")
                for (k in 0 until (totalRouteList[i].dayRoute?.count()!!)) {
                    dayStringBuilder.append("${totalRouteList[i].dayRoute?.get(k)?.pointdata?.placeName}\n")
                }
                dayStringBuilder.append("총 이동시간 : ${totalRouteList[i].totalTime}\n")
                dayStringBuilder.append("----------------------------\n")
                routeStringList.add(dayStringBuilder.toString())
            }
        } catch (e: Exception) {
            // 예외가 발생하면 로그로 출력
            Log.e("PLAN", "getTotalRouteList - Exception: ${e.toString()}", e)
        }
        return routeStringList
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


