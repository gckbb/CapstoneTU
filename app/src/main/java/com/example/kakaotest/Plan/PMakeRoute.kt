package com.example.kakaotest.Plan

import android.content.ContentValues
import android.util.Log
import com.example.kakaotest.ApiAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
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
    private lateinit var startPoint :SelectedPlaceData

    fun routeSet(selectedPlaceList: ArrayList<SelectedPlaceData>, startPoint: SelectedPlaceData) {
        try {
            Log.d("routeset", "Start Point: $startPoint")
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
                    Log.e("routeset", "Received null time for ${selectedPlaceList[i].placeName}")
                }
            }
            routeList.removeAt(0)
            saveList = routeList

        } catch (e: Exception) {
            Log.e("routeset", "Exception in routeSet: ${e.toString()}")
            e.printStackTrace()
        }
    }

    //totalDate만큼 진행,maxDayTime이 하루의 최대여행시간(단위는 시간), 장소 하나당 1시간 가정
    fun routeStart(totalDate:Int, maxDayTime:Int) {
        try {
            for (k in 0 until totalDate) {
                lateinit var totalTime: Number
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
                routeList.removeAt(minIndex)
                if (minTime != null) {
                    dayRouteList[0].time = minTime
                }
                for (i in 1..maxDayTime) {
                    //출발숙소를 제외한 루트의 장소수*3600 + 마지막장소와 숙소의 이동시간
                    totalTime =
                        (dayRouteList.count() - 1) * 3600 + dayRouteList.last.time.toInt()
                    if (totalTime > maxDayTime * 3600) {
                        dayRouteList[0].time =
                            dayRouteList[0].time.toInt() - dayRouteList.last.time.toInt()
                        dayRouteList.removeLast()
                        totalTime =
                            (dayRouteList.count() - 1) * 3600 + dayRouteList.last.time.toInt()
                        break
                    }
                    if (!findMinPoint(dayRouteList.last)) {
                        Log.d(ContentValues.TAG, "routeStart break")
                        dayRouteList.clear()
                        totalRouteList.clear()
                        routeList = saveList
                        return
                    }
                }
                var tempRouteList = DRouteData(totalTime, LinkedList<PSearchRouteData>())
                tempRouteList.dayRoute?.addAll(dayRouteList)
                totalRouteList.add(tempRouteList)
                dayRouteList.clear()
            }
        }catch(e:Exception){
            Log.e("routestart", "${e.message}")
            e.printStackTrace()
        }
    }


    //경로의 마지막 장소와 가장 가까운 장소를 다음 장소로 추가하는 함수
    fun findMinPoint(startRouteData:PSearchRouteData): Boolean{
        try {
            var minIndex: Int = 0
            var minTime: Int? = runBlocking {
                async(Dispatchers.IO) {
                    if (routeList.isNotEmpty()) {
                        apiAdapter.apiRequest(
                            startRouteData.pointdata!!.tpoint.longitude,
                            startRouteData.pointdata.tpoint.latitude,
                            routeList[0].pointdata!!.tpoint.longitude,
                            routeList[0].pointdata!!.tpoint.latitude
                        )?.toInt()
                    } else {
                        null
                    }
                }.await()
            }


            if (minTime == null) {
                Log.d("findMinPoint", "minTime is null in findMinPoint")
                return false
            }

            for (i in 0 until routeList.count()) {
                val tempTime = runBlocking {
                    async(Dispatchers.IO) {
                        routeList[i].pointdata?.tpoint?.let {
                            routeList[i].pointdata?.tpoint?.let { it1 ->
                                apiAdapter.apiRequest(
                                    startRouteData.pointdata!!.tpoint.longitude,
                                    startRouteData.pointdata.tpoint.latitude,
                                    it.longitude,
                                    it1.latitude
                                )?.toInt()
                            }
                        }
                    }.await()
                }

                if (tempTime == null || minTime == null) {
                    Log.d("findMinPoint", "findMinPoint break")
                    return false
                } else if (minTime > tempTime) {
                    minIndex = i
                    minTime = tempTime
                }
            }

            dayRouteList.add(routeList[minIndex])
            routeList.removeAt(minIndex)
            dayRouteList[0].time = dayRouteList[0].time.toInt() + minTime!!
            return true
        } catch (e: Exception) {
            Log.e("findMinPoint", "Exception: ${e.toString()}", e)
            return false
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
            Log.e("findInList", "Exception: ${e.toString()}", e)
            return -1
        }
    }

    fun printTotalRoute() {
        try {
            for (i in 0 until totalRouteList.count()) {
                //  println("${i + 1} Day")
                Log.d("printTotalRoute","${i + 1} Day")
                for (k in 0 until (totalRouteList[i].dayRoute?.count()!!)) {
                    //    println("${totalRouteList[i].dayRoute?.get(k)?.pointdata?.placeName}")
                    Log.d("printTotalRoute", "${totalRouteList[i].dayRoute?.get(k)?.pointdata?.placeName}")
                }
                //   println("총 이동시간 : ${totalRouteList[i].totalTime}")
                Log.d("printTotalRoute", "총 이동시간 : ${totalRouteList[i].totalTime}")
                //  println("----------------------------")
                Log.d("printTotalRoute", "----------------------------")
            }
        } catch (e: Exception) {
            // 예외가 발생하면 로그로 출력
            Log.e("printTotalRoute", "Exception: ${e.toString()}", e)
        }
    }

    fun printAllRoute() {
        try{
            for(i in 0 until dayRouteList.count()) {
                println("${dayRouteList[i].pointdata?.placeName}")
                Log.d("printAllRoute","${dayRouteList[i].pointdata?.placeName}")
            }
        }catch (e:Exception){

            Log.e("printAllRoute", "Exception: ${e.toString()}", e)
        }    }

}