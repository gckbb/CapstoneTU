package com.example.kakaotest.Utility.tmap

import android.content.ContentValues
import android.util.Log
import com.example.kakaotest.DataModel.tmap.DayRouteData
import com.example.kakaotest.DataModel.tmap.SearchRouteData
import com.example.kakaotest.DataModel.tmap.SelectedPlaceData
import com.example.kakaotest.DataModel.metaRoute.*
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

    private var totalRouteList2 = LinkedList<MetaDayRoute>()
    private var routeList2 = LinkedList<SearchMetaData>()
    private var saveList2 = LinkedList<SearchMetaData>()
    private var dayRouteList2 = LinkedList<SearchMetaData>()

    private val apiAdapter = ApiAdapter()
    private val apiAdapter2 = ApiAdapter2()
    private lateinit var startPoint: SelectedPlaceData

    val AllRouteList = LinkedList<DayRouteData>()

    suspend fun apiRequest(startLongitude: Double, startLatitude: Double, endLongitude: Double, endLatitude: Double): Number? {
        return coroutineScope {
            val deferredTime = async(Dispatchers.IO) {
                apiAdapter.apiRequest(startLongitude, startLatitude, endLongitude, endLatitude)
            }
            val time = deferredTime.await()?.toInt()
            Log.d("PLAN", "거리 계산: 시작($startLatitude, $startLongitude) -> 도착($endLatitude, $endLongitude), 시간: $time")
            time
        }
    }
    suspend fun apiRequest2(startLongitude: Double, startLatitude: Double, endLongitude: Double, endLatitude: Double): MetaRoute? {
        return coroutineScope {
            val deferredData = async(Dispatchers.IO) {
                apiAdapter2.apiRequest2(startLongitude, startLatitude, endLongitude, endLatitude)
            }
            val metaRouteData: MetaRoute? = deferredData.await()
            Log.d("PLAN", "거리 계산: 시작($startLatitude, $startLongitude) -> 도착($endLatitude, $endLongitude), 시간: $")
            metaRouteData
        }
    }

    suspend fun routeSet(selectedPlaceList: ArrayList<SelectedPlaceData>, startPoint: SelectedPlaceData,type:Int) {
        try {
            Log.d("PLAN", "routeSet - 선택된 장소 리스트 \n $selectedPlaceList")
            this.startPoint = startPoint
            if(type == 0 || type == 2) { // 자차,택시,도보
                coroutineScope {
                    for (i in 1 until selectedPlaceList.count()) {
                        val deferredTime = async(Dispatchers.IO) {
                            apiAdapter.apiRequest(
                                startPoint.tpoint.longitude,
                                startPoint.tpoint.latitude,
                                selectedPlaceList[i].tpoint.longitude,
                                selectedPlaceList[i].tpoint.latitude
                            )
                        }
                        val time = deferredTime.await()
                        if (time != null) {
                            if(type == 2){
                                val routeData = SearchRouteData(selectedPlaceList[i], time)
                                routeList.add(routeData!!)
                                println("time is $time for ${selectedPlaceList[i].placeName}")
                            }
                            else {
                                val routeData = SearchRouteData(selectedPlaceList[i], time!!)
                                routeList.add(routeData!!)
                                println("time is $time for ${selectedPlaceList[i].placeName}")
                            }
                        } else {
                            Log.e(
                                "PLAN",
                                "Received null time for ${selectedPlaceList[i].placeName}"
                            )
                        }
                    }
                }
            }
            else if(type == 1){ // 대중교통(버스,지하철)
                coroutineScope {
                    for (i in 1 until selectedPlaceList.count()) {
                        val deferredData = async(Dispatchers.IO) {
                            apiAdapter2.apiRequest2(
                                startPoint.tpoint.longitude,
                                startPoint.tpoint.latitude,
                                selectedPlaceList[i].tpoint.longitude,
                                selectedPlaceList[i].tpoint.latitude
                            )
                        }
                        val time = deferredData.await()?.metaData?.plan?.itineraries?.get(0)?.totalTime
                        if (time != null) {
                            val routeData = SearchMetaData(deferredData.await()?.metaData!!,selectedPlaceList[i],time!!)
                            routeList2.add(routeData!!)
                            println("time is $time for ${selectedPlaceList[i].placeName}")
                        } else {
                            Log.e(
                                "PLAN",
                                "Received null time for ${selectedPlaceList[i].placeName}"
                            )
                        }
                    }
                }
            }
            if(type == 0 || type == 2) {
                routeList.removeAt(0)
                saveList = routeList
            }
            else if (type == 1) {
                routeList2.removeAt(0)
                saveList2 = routeList2
            }




        } catch (e: Exception) {
            Log.e("PLAN", "routeSet - Exception in routeSet: ${e.toString()}")
            e.printStackTrace()
        }
    }

    suspend fun routeStart2(totalDate: Int, maxDayTime: Int, stayTimePerPlace: Int, foodDataList: ArrayList<SelectedPlaceData>,restaurant:String,type:Int) { // 대중교통
        coroutineScope {
            try {
                for (k in 0 until totalDate+1) {
                    var totalTime: Double = 0.0 // 하루의 총 소요 시간
                    var currentDayTime: Double = 0.0  // 현재까지 경로의 소요시간
                    var remainingTime: Int = maxDayTime * 3600 // 남은 시간을 초 단위로 계산
                    var lunchcheck = 0  // 점심 식사 여부 체크하는 변수

                    dayRouteList2.add(SearchMetaData(null,startPoint,0))

                    // 최단 시간 경로를 구하는 대신, 최대한 많은 장소를 방문하는 로직 추가
                    while (routeList2.isNotEmpty() && currentDayTime + routeList2.first().time.toInt() <= remainingTime) {

                        if (currentDayTime > 4 * 3600 && lunchcheck == 0 && restaurant=="yes") {  // 4인 이유는 am8로 생각하고 4시간 후인 12시를 점심시간이라고 가정함
                            var minfood = 999999
                            var mindata: MetaData? = null
                            var minfoodindex:Int? = null

                            for (i in 0 until foodDataList.size) {
                                val temp = apiRequest2(
                                    dayRouteList2.last.pointdata?.tpoint?.longitude!!, dayRouteList2.last.pointdata?.tpoint?.latitude!!,
                                    foodDataList[i].tpoint.longitude, foodDataList[i].tpoint.latitude
                                )
                                // 최소거리보다 더 짧은 거리인 음식점
                                if (temp!!.metaData?.plan?.itineraries?.get(0)?.totalTime!! < minfood) {
                                    minfood = temp!!.metaData?.plan?.itineraries?.get(0)?.totalTime!!
                                    mindata = temp.metaData
                                    minfoodindex = i

                                }
                                Log.d("mindata", "$mindata")
                            }

                            mindata?.let {
                                dayRouteList2.add(SearchMetaData(it,foodDataList[minfoodindex!!], minfood))
                                currentDayTime += minfood + 3600 // 점심 시간 1시간 추가
                                lunchcheck = 1
                                foodDataList.removeAt(minfoodindex)
                                Log.d("PLAN", "점심 장소 추가: ${dayRouteList2.last.pointdata?.placeName}, 거리: $minfood")
                            }

                            continue
                        }
                        var minIndex: SearchMetaData
                        minIndex = findMinPoint2(dayRouteList2.last(),1)
                        if(minIndex == null) {
                            Log.d("Error","Route Start findMinPoint error ")
                            break
                        }

                        if (currentDayTime + minIndex.time.toInt() > remainingTime) break
                        if (!dayRouteList2.any { route -> route.pointdata?.placeName == minIndex.pointdata?.placeName }) { // 중복 체크
                            dayRouteList2.add(minIndex) // 가장 적게 걸리는 장소 추가
                            currentDayTime += minIndex.time.toInt() + 3600
                            Log.d("PLAN", "다음 장소 추가: ${minIndex.pointdata?.placeName}, 거리: ${minIndex.time.toInt()}")
                        }

                    }

                    // 총 이동 시간 계산 (이동 시간 + 체류 시간)
                    totalTime = currentDayTime + stayTimePerPlace * (dayRouteList2.size - 1)

                    // 현재 일자의 경로를 추가
                    totalRouteList2.add(MetaDayRoute(totalTime, LinkedList(dayRouteList2)))

                    // 현재 일자의 경로가 선택되면, dayRouteList를 초기화
                    dayRouteList2.clear()
                }
            } catch (e: Exception) {
                Log.e("PLAN", "routeStart - Exception: ${e.toString()}", e)
            }
        }
    }

    suspend fun routeStart(totalDate: Int, maxDayTime: Int, stayTimePerPlace: Int, foodDataList: ArrayList<SelectedPlaceData>,restaurant:String,type:Int) {
        coroutineScope {
            try {
                for (k in 0 until totalDate+1) {
                    var totalTime: Double = 0.0 // 하루의 총 소요 시간
                    var currentDayTime: Double = 0.0  // 현재까지 경로의 소요시간
                    var remainingTime: Int = maxDayTime * 3600 // 남은 시간을 초 단위로 계산
                    var lunchcheck = 0  // 점심 식사 여부 체크하는 변수

                    dayRouteList.add(SearchRouteData(startPoint, 0))

                    // 최단 시간 경로를 구하는 대신, 최대한 많은 장소를 방문하는 로직 추가
                    while (routeList.isNotEmpty() && currentDayTime + routeList.first().time.toInt() <= remainingTime) {

                        if (currentDayTime > 4 * 3600 && lunchcheck == 0 && restaurant=="YES") {  // 4인 이유는 am8로 생각하고 4시간 후인 12시를 점심시간이라고 가정함
                            var minfood = 999999
                            var mindata: SelectedPlaceData? = null

                            for (i in 0 until foodDataList.size) {
                                val temp = apiRequest(
                                    dayRouteList.last.pointdata?.tpoint?.longitude!!, dayRouteList.last.pointdata?.tpoint?.latitude!!,
                                    foodDataList[i].tpoint.longitude, foodDataList[i].tpoint.latitude
                                )
                                // 최소거리보다 더 짧은 거리인 음식점
                                if (temp!!.toInt() < minfood) {
                                    minfood = temp.toInt()
                                    mindata = foodDataList[i]
                                }
                                Log.d("mindata", "$mindata")
                            }

                            mindata?.let {
                                dayRouteList.add(SearchRouteData(it, minfood))
                                currentDayTime += minfood + 3600 // 점심 시간 1시간 추가
                                lunchcheck = 1
                                foodDataList.remove(it)
                                routeList.removeAll { route -> route.pointdata?.placeName == it.placeName } // routeList에서도 제거
                                Log.d("PLAN", "점심 장소 추가: ${it.placeName}, 거리: $minfood")
                            }

                            continue
                        }
                        var minIndex:Int = -1
                        if(type == 0) minIndex = findMinPoint(dayRouteList.last(),1) // routeList에서 가장 시간이 적게 걸리는 장소 선택 / 자차, 택시
                        else if(type == 2) minIndex = findMinPoint(dayRouteList.last(),5) //도보
                        if(minIndex == -1) {
                            Log.d("Error","Route Start findMinPoint error ")
                            break
                        }

                        if (currentDayTime + routeList[minIndex].time.toInt() > remainingTime) break
                        if (!dayRouteList.any { route -> route.pointdata?.placeName == routeList[minIndex].pointdata?.placeName }) { // 중복 체크
                            dayRouteList.add(routeList[minIndex]) // 가장 적게 걸리는 장소 추가
                            currentDayTime += routeList[minIndex].time.toInt() + 3600
                            Log.d("PLAN", "다음 장소 추가: ${routeList[minIndex].pointdata?.placeName}, 거리: ${routeList[minIndex].time.toInt()}")
                        }

                        routeList.removeAt(minIndex) // 경로 리스트에서 제거
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

    suspend fun findMinPoint(startRouteData: SearchRouteData, multi:Int): Int { // multi는 기본1, 도보5
        var minIndex: Int = 0
        var minTime: Int? = apiRequest(
            startRouteData.pointdata!!.tpoint.longitude,
            startRouteData.pointdata.tpoint.latitude,
            routeList[0].pointdata!!.tpoint.longitude,
            routeList[0].pointdata!!.tpoint.latitude
        )?.toInt()
        var tempTime: Int?

        for (i in 0 until routeList.count()) {
            tempTime = routeList[i].pointdata?.tpoint?.let {
                apiRequest(
                    startRouteData.pointdata.tpoint.longitude,
                    startRouteData.pointdata.tpoint.latitude,
                    it.longitude, it.latitude
                )?.toInt()
            }
            if (tempTime == null || minTime == null) {
                Log.d(ContentValues.TAG, "findMinPoint break")
                return -1
            } else if (minTime > tempTime) {
                minIndex = i
                minTime = tempTime
            }
        }
        routeList[minIndex].time = routeList[minIndex].time.toInt().times(multi)
        return minIndex
    }

    suspend fun findMinPoint2(startRouteData: SearchMetaData, multi: Int): SearchMetaData { // 대중교통
        var minIndex: Int = 0
        var minTime: Int? = apiRequest2(
            startRouteData.pointdata!!.tpoint.longitude,
            startRouteData.pointdata.tpoint.latitude,
            routeList2[0].pointdata!!.tpoint.longitude,
            routeList2[0].pointdata!!.tpoint.latitude
        )?.metaData?.plan?.itineraries?.get(0)?.totalTime
        var tempTime: Int?

        for (i in 1 until routeList2.count()) {
            tempTime = routeList2[i].pointdata?.tpoint?.let {
                apiRequest2(
                    startRouteData.pointdata.tpoint.longitude,
                    startRouteData.pointdata.tpoint.latitude,
                    it.longitude, it.latitude
                )?.metaData?.plan?.itineraries?.get(0)?.totalTime
            }
            if (tempTime == null || minTime == null) {
                Log.d(ContentValues.TAG, "findMinPoint break")
            } else if (minTime > tempTime) {
                minIndex = i
                minTime = tempTime
            }
        }
        var tempData = apiRequest2(
            startRouteData.pointdata.tpoint.longitude,
            startRouteData.pointdata.tpoint.latitude,
            routeList2[minIndex].pointdata?.tpoint?.longitude!!,
            routeList2[minIndex].pointdata?.tpoint?.latitude!!
        )
        var minData: SearchMetaData
        minData = SearchMetaData(
            tempData?.metaData,
            routeList2[minIndex].pointdata,
            tempData?.metaData?.plan?.itineraries?.get(0)?.totalTime!!
        )
        routeList2.removeAt(minIndex)
        return minData
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

    fun PrintTotalRoute() {
        for (i in 0 until totalRouteList.count()) {
            println("${i + 1} Day")
            for (k in 0 until (totalRouteList[i].dayRoute?.count()!!)) {
                println("${totalRouteList[i].dayRoute?.get(k)?.pointdata?.placeName}")
            }
            println("총 이동시간 : ${totalRouteList[i].totalTime}")
            println("----------------------------")
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
                // Log.d("PLAN","${i}일째 총 이동시간 : "+hour.toString())
                val totalHour: Double = String.format("%.1f", hour).toDouble()
                Log.d("PLAN", "${i + 1}일째 총 이동시간 : " + totalHour)
                val totalTime = "총 이동시간 : ${totalHour} 시간"
                // println(totalTime)
            }
        } catch (e: Exception) {
            Log.e("PLAN", "getTotalRouteList - Exception: ${e.toString()}", e)
        }

        return totalRouteList
    }

    fun printTotalRoute2(): LinkedList<MetaDayRoute> {
        try {
            for (i in 0 until totalRouteList2.count()) {
                println("${i + 1} Day")
                for (k in 0 until (totalRouteList2[i].dayRoute?.count() ?: 0)) {
                    val placeInfo = totalRouteList2[i].dayRoute?.get(k)
                    Log.d("PLAN", placeInfo?.pointdata!!.placeName)
                }

                val hour = totalRouteList2[i].totalTime.toDouble() / 3600
                // Log.d("PLAN","${i}일째 총 이동시간 : "+hour.toString())
                val totalHour: Double = String.format("%.1f", hour).toDouble()
                Log.d("PLAN", "${i + 1}일째 총 이동시간 : " + totalHour)
                val totalTime = "총 이동시간 : ${totalHour} 시간"
                // println(totalTime)
            }
        } catch (e: Exception) {
            Log.e("PLAN", "getTotalRouteList - Exception: ${e.toString()}", e)
        }

        return totalRouteList2
    }

    fun printAllRoute() {
        try {
            for (i in 0 until dayRouteList.count()) {
                println("${dayRouteList[i].pointdata?.placeName}")
                Log.d("printAllRoute", "${dayRouteList[i].pointdata?.placeName}")
            }
        } catch (e: Exception) {
            Log.e("PLAN", "printAllRoute - Exception: ${e.toString()}", e)
        }
    }
}
