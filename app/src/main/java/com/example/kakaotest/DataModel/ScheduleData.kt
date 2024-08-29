package com.example.kakaotest.DataModel

data class ScheduleData(
    val mainId : String?, //여행계획을 만든사람의 아이디
    val subId : ArrayList<String>?, //여행계획을 공유받은사람의 아이디
    val latdata : ArrayList<Double>,  //날짜별 구별은 1일차 좌표 - 2일차 좌표 사이에 특정문자를 집어넣어서 구별
    val londata : ArrayList<Double>,
    val placenamelist : ArrayList<String>,// 이름 구별 또한 동일
    val type : String,
    val startday : Date,
    val endday : Date,
    val scheduleid : String //스케줄 고유의 식별아이디
)
