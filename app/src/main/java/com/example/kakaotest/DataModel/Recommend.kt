package com.example.kakaotest.DataModel

import java.io.Serializable

data class RecommendResponse(
    val response: ResponseData
)

data class ResponseData(
    val header: HeaderData,
    val body: BodyData
)

data class HeaderData(
    val resultCode: String,
    val resultMsg: String
)

data class BodyData(
    val items: ItemsData,
    val numOfRows: Int,
    val pageNo: Int,
    val totalCount: Int
)

data class ItemsData(
    val item: List<Recommend>
)

data class Recommend(
    val addr1: String,                     // 주소
    val addr2: String?,                    // 상세주소
    val areacode: String,                  // 지역코드
    val booktour: String?,                 // 교과서속여행지 여부
    val cat1: String,                      // 대분류
    val cat2: String,                      // 중분류
    val cat3: String,                      // 소분류
    val contentid: String,                 // 콘텐츠 ID
    val contenttypeid: String,             // 콘텐츠 타입 ID
    val createdtime: String,               // 등록일
    val dist: String,                      // 거리
    val firstimage: String,                // 대표이미지(원본)
    val firstimage2: String,               // 대표이미지(섬네일)
    val cpyrhtDivCd: String,              // 저작권 유형 (Type1:제1유형(출처표시-권장), Type3:제3유형(제1유형+변경금지)
    val mapx: String,                     // x좌표
    val mapy: String,                     // y좌표
    val mlevel: String,                   // 맵 레벨
    val modifiedtime: String,             // 수정일
    val sigungucode: String,              // 시군코드
    val tel: String,                      // 전화번호
    val title: String,                    // 제목
    val rnum: String,                     // 일련번호
    val code: String,                     // 코드: 대,중,소분류코드
    val name: String,                     // 코드명: 대,중,소분류코드명

    val chkcreditcardculture: String?,    // 신용카드가능정보
    val chkbabycarriageculture: String?, // 유모차대여정보
    val discountinfo: String?,     // 할인정보
    val chkpetculture: String?,           // 애완동물동반가능정보
    val infocenterculture: String?,       // 문의및안내
    val parkingculture: String?,          // 주차시설
    val parkingfee: String?,       // 주차요금
    val restdateculture: String?,         // 쉬는날
    val usefee: String?,           // 이용요금
    val usetimeculture: String?,          // 이용시간
    val scale: String?,            // 규모
    val spendtime: String?,        // 관람소요시간
    val accomcountculture: String?,       // 수용인원

    val scaleleports: String?,            // 규모 (레포츠)
    val usefeeleports: String?,           // 입장료 (레포츠)
    val chkcreditcardleports: String?,    // 신용카드가능정보 (레포츠)
    val chkbabycarriageleports: String?,  // 유모차대여정보 (레포츠)
    val chkpetleports: String?,           // 애완동물동반가능정보 (레포츠)
    val expagerangeleports: String?,      // 체험가능연령 (레포츠)
    val infocenterleports: String?,       // 문의및안내 (레포츠)
    val openperiod: String?,              // 개장기간 (레포츠)
    val parkingfeeleports: String?,       // 주차요금 (레포츠)
    val restdateleports: String?,         // 쉬는날 (레포츠)
    val usetimeleports: String?,          // 이용시간 (레포츠)
    val reservation: String?,             // 예약안내 (레포츠)
    val accomcountleports: String?,       // 수용인원 (레포츠)
    val parkingleports: String?,          // 주차시설 (레포츠)


    val eventenddate: String?,            // 행사종료일
    val playtime: String?,                // 공연시간
    val eventhomepage: String?,           // 행사홈페이지
    val eventplace: String?,              // 행사장소
    val eventstartdate: String?,          // 행사시작일
    val festivalgrade: String?,           // 축제등급
    val agelimit: String?,                // 관람가능연령
    val bookingplace: String?,            // 예매처
    val usetimefestival: String?,         // 이용요금
    val spendtimefestival: String?,       // 관람소요시간
    val sponsor1: String?,                // 주최자정보
    val sponsor1tel: String?,             // 주최자연락처
    val sponsor2: String?,                // 주관사정보
    val sponsor2tel: String?,             // 주관사연락처
    val subevent: String?,                // 부대행사
    val program: String?,                 // 행사프로그램
    val placeinfo: String?,               // 행사장위치안내
    val discountinfofestival: String?,    // 할인정보

    val firstmenu: String?, // 대표메뉴
    val treatmenu: String?, // 취급메뉴
    val infocenterfood: String?, // 문의및안내 (음식점)
    val opentimefood: String?, // 영업시간
    val restdatefood: String?, // 쉬는날
    val chkcreditcardfood: String?, // 신용카드가능정보 (음식점)
    val packing: String?, // 포장가능
    val parkingfood: String?, // 주차시설
    val reservationfood: String?, // 예약안내 (음식점)
    val scalefood: String?, // 규모
    val seat: String?, // 좌석수
    val smoking: String?, // 금연/흡연여부
    val kidsfacility: String?, // 어린이놀이방여부
    val lcnsno: String?, // 인허가번호
    val discountinfofood: String?,
    val opendatefood: String?,

    val chkcreditcardshopping: String?,    // 신용카드가능정보 (쇼핑)
    val chkpetshopping: String?,           // 애완동물동반가능정보 (쇼핑)
    val culturecenter: String?,            // 문화센터바로가기
    val fairday: String?,                  // 장서는날
    val infocentershopping: String?,       // 문의및안내 (쇼핑)
    val opendateshopping: String?,         // 개장일
    val opentime: String?,                 // 영업시간
    val parkingshopping: String?,          // 주차시설
    val restdateshopping: String?,         // 쉬는날
    val restroom: String?,                 // 화장실설명
    val saleitem: String?,                 // 판매품목
    val saleitemcost: String?,             // 판매품목별가격
    val scaleshopping: String?,            // 규모
    val shopguide: String?,                 // 매장안내
    val chkbabycarriageshopping: String?,   // 유모차대여정보

    val opendate: String?,                // 개장일
    val parking: String?,                 // 주차시설
    val restdate: String?,                // 쉬는날
    val chkcreditcard: String?,           // 신용카드가능정보
    val chkpet: String?,                  // 애완동물동반가능정보
    val accomcount: String?,              // 수용인원
    val chkbabycarriage: String?,         // 유모차대여정보
    val expagerange: String?,             // 체험가능연령
    val expguide: String?,                // 체험안내
    val heritage1: String?,               // 세계문화유산유무
    val heritage2: String?,               // 세계자연유산유무
    val heritage3: String?,               // 세계기록유산유무
    val infocenter: String?,              // 문의및안내
    val useseason: String?,               // 이용시기
    val usetime: String?                  // 이용시간
): Serializable


