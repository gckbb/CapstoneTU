package com.example.kakaotest.DataModel

import com.example.kakaotest.DataModel.metaRoute.MetaDayRoute
import com.example.kakaotest.DataModel.tmap.SearchRouteData
import java.util.*



data class ScheduleData(
    var mainId : String? = null, //여행계획을 만든사람의 아이디
    var subId : ArrayList<String>? = null, //여행계획을 공유받은사람의 아이디
    var latdata : ArrayList<Double>? = null,  //날짜별 구별은 1일차 좌표 - 2일차 좌표 사이에 특정문자를 집어넣어서 구별
    var londata : ArrayList<Double>? = null,
    var placenamelist : ArrayList<String>? = null,// 이름 구별 또한 동일
    var type : String? = null, // 이동수단
    var startday : Date? = null,
    var endday : Date? = null,
    var scheduleid : String? = null, //스케줄 고유의 식별아이디
    var timedata : ArrayList<Int>? = null,
    var dayroutedata : ArrayList<ArrayList<SearchRouteData>?>? = null,
    var dayroutedata2 : ArrayList<MetaDayRoute>? = null
)

