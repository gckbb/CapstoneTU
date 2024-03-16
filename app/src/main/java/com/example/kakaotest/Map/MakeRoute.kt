package com.example.kakaotest.Map

import android.content.ContentValues
import android.util.Log
import java.util.LinkedList
import java.util.ArrayList

class MakeRoute {
    private var totalRouteList = LinkedList<DayRouteData>()
    private var routeList = LinkedList<SearchRouteData>()
    private var saveList = LinkedList<SearchRouteData>()
    private var dayRouteList = LinkedList<SearchRouteData>()
    private val apiAdapter = ApiAdapter()
    private lateinit var startPoint : SearchData

    //경로찾기를 시작할 기본데이터인 routeList를 채워주는 함수, time에는 숙소와 해당장소와의 이동시간이 저장됨
    fun routeSet(searchDataList: ArrayList<SearchData>, startPoint: SearchData) {
        this.startPoint = startPoint
        var routeData: SearchRouteData
        var time:Number?

        for(i in 1 until searchDataList.count()) {
            time = apiAdapter.apiRequest(startPoint.tpoint.longitude,startPoint.tpoint.latitude,searchDataList[i].tpoint.longitude,searchDataList[i].tpoint.latitude)
            routeData = SearchRouteData(searchDataList[i],time!!)
            routeList.add(routeData)
            println("time is $time")
        }
        routeList.removeAt(0)
        saveList = routeList
    }
    //totalDate만큼 진행,maxDayTime이 하루의 최대여행시간(단위는 시간), 장소 하나당 1시간 가정
    fun routeStart(totalDate:Int, maxDayTime:Int) {
        for(k in 0 until totalDate) {
            lateinit var totalTime: Number
            var minIndex: Int = 0
            var minTime: Number? = routeList[0].time
            dayRouteList.add(SearchRouteData(startPoint, 0))
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
            var tempRouteList = DayRouteData(totalTime,LinkedList<SearchRouteData>())
            tempRouteList.dayRoute?.addAll(dayRouteList)
            totalRouteList.add(tempRouteList)
            dayRouteList.clear()
        }

    }
    //경로의 마지막 장소와 가장 가까운 장소를 다음 장소로 추가하는 함수
    fun findMinPoint(startRouteData: SearchRouteData): Boolean{
        var minIndex:Int = 0
        var minTime:Int? = apiAdapter.apiRequest(startRouteData.pointdata!!.tpoint.longitude,
                                                 startRouteData.pointdata.tpoint.latitude,
                                                 routeList[0].pointdata!!.tpoint.longitude,
                                                 routeList[0].pointdata!!.tpoint.latitude)?.toInt()
        var tempTime:Int? = 0

        for(i in 0 until routeList.count()) {
            tempTime = routeList[i].pointdata?.tpoint?.let {
                routeList[i].pointdata?.tpoint?.let { it1 ->
                    apiAdapter.apiRequest(startRouteData.pointdata.tpoint.longitude,
                        startRouteData.pointdata.tpoint.latitude,
                        it.longitude,
                        it1.latitude)?.toInt()
                }
            }
            if(tempTime == null || minTime == null) {
                Log.d(ContentValues.TAG, "findMinPoint break")
                return false
            }
            else if(minTime > tempTime) {
                minIndex = i
                minTime = tempTime
            }
        }
        dayRouteList.add(routeList.get(minIndex))
        routeList.removeAt(minIndex)
        dayRouteList[0].time = dayRouteList[0].time.toInt() + minTime!!
        return true
    }

    fun findInList(findData: SearchRouteData):Int {
        for(i in 0 until saveList.count()) {
            if(saveList[i] == findData) {
                return i
            }
        }
        return -1
    }

    fun printTotalRoute() {
        for(i in 0 until totalRouteList.count()){
            println("${i+1} Day")
            for(k in 0 until (totalRouteList[i].dayRoute?.count()!!)) {
                println("${totalRouteList[i].dayRoute?.get(k)?.pointdata?.id}")
            }
            println("총 이동시간 : ${totalRouteList[i].totalTime}")
            println("----------------------------")
        }
    }
    fun printAllRoute() {
        for(i in 0 until dayRouteList.count()) {
            println("${dayRouteList[i].pointdata?.id}")
        }
    }

}