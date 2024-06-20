package com.example.kakaotest.TourApi

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.TableLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.kakaotest.DataModel.Recommend
import com.example.kakaotest.R
import com.example.kakaotest.databinding.ActivityRecommendDetailBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class RecommendDetailActivity : AppCompatActivity() {
    private val tourApiManager = TourApiManager()
    private val scope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRecommendDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Restaurant 객체를 인텐트에서 가져옴
        val recommend = intent.getSerializableExtra("recommend") as? Recommend

        if(recommend != null) {
            getDetailIntro_39(recommend.contentid,recommend.contenttypeid)
            Glide.with(this)
                .load(recommend.firstimage)
                .into(binding.mainImage)

            binding.textViewTitle.text = recommend.title
            binding.textViewAddress.text = recommend.addr1
            binding.textViewTel.text = recommend.tel
            binding.textViewAddressDetail.text = recommend.addr2
            Log.d("Recommend","searchCategory 실행")
            searchCategory(recommend.cat1,recommend.cat2,recommend.cat3)
            when(recommend.contenttypeid){
                "12" -> binding.tablerowTour.visibility = TableLayout.VISIBLE
                "14" -> binding.tablerowCulture.visibility = TableLayout.VISIBLE
                "15" -> binding.tablerowFestival.visibility = TableLayout.VISIBLE
                "28" -> binding.tablerowLeports.visibility = TableLayout.VISIBLE
                "38" -> binding.tablerowShopping.visibility = TableLayout.VISIBLE
                "39" -> binding.tablerowFood.visibility = TableLayout.VISIBLE

            }
        }

        //뒤로가기 버튼
        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.button.setOnClickListener {
            //클릭하면 해당 장소를 기기에 저장
            // SharedPreferences 객체 가져오기
            val sharedPreferences = getSharedPreferences("MySavedRestaurants", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            // 사용자가 선택한 음식점의 정보를 SharedPreferences에 저장
            if (recommend != null) {
                val mapx = String.format("%.8f", recommend.mapx.toDouble())
                val mapy = String.format("%.8f", recommend.mapy.toDouble())
                //Log.d("add_test","DetailActivity: ${mapx}_${mapy}")
                editor.putString("recommend_${mapx}_${mapy}_${recommend.addr1}", recommend.title)
                Log.d("plan",recommend.title.toString())
                Toast.makeText(this@RecommendDetailActivity, "${recommend.title} 저장", Toast.LENGTH_SHORT).show()
            }
            editor.apply()
        }
    }
    private fun searchCategory(cat1: String, cat2: String, cat3: String)  {
        scope.launch {
            try {
                Log.d("Restaurant","tourApiManager.searchCategory(cat1,cat2,cat3)")
                val cate_1 = tourApiManager.searchCategory(cat1,cat2,cat3)
                val cate_3 = tourApiManager.searchCategory3(cat1)
                val cate_4 = tourApiManager.searchCategory4()
                var cate_items_1 = cate_1.response.body.items.item
                var cate_items_3 = cate_3.response.body.items.item
                var cate_items_4 = cate_4.response.body.items.item

                var cate1 : TextView = findViewById(R.id.cat1)
                for(item in cate_items_4){
                    if(item.code.equals(cat1))
                        cate1.text = item.name
                }
                Log.d("Restaurant","${cate1.text}")
                var cate2 : TextView = findViewById(R.id.cat2)
                for(item in cate_items_3){
                    if(item.code == cat2)
                        cate2.text = item.name
                }
                Log.d("Restaurant","${cate2.text}")
                var cate3 : TextView = findViewById(R.id.cat3)
                cate3.text = cate_items_1[0].name
                Log.d("Restaurant","${cate3.text}")

            } catch (e: Exception) {
                // 오류 처리
                Log.e("TourApiActivity", "음식점 검색 오류: $e")
            }
        }
    }

    private fun getDetailIntro_39(contentId: String, contentTypeId: String) {
        scope.launch {
            try {
                // API를 통해 데이터 가져오기
                Log.d("detailCode","contentId: ${contentId}, ${contentTypeId} 실행")
                val details = tourApiManager.getDetailIntro(contentId, contentTypeId)
                Log.d("detailCode","getDetailIntro 실행")
                var details_info = details.response.body.items.item[0]
                Log.d("detailCode","${details_info}")

                val binding = ActivityRecommendDetailBinding.inflate(layoutInflater)

                // 신용카드 가능 정보
                val chkcreditcardfoodTextView = findViewById<TextView>(R.id.text_chkcreditcardfood)
                chkcreditcardfoodTextView.text = details_info.chkcreditcardfood ?: "정보 없음"

                // 할인 정보
                val discountinfofoodTextView = findViewById<TextView>(R.id.text_discountinfofood)
                discountinfofoodTextView.text = details_info.discountinfofood ?: "정보 없음"

                // 대표 메뉴
                val firstmenuTextView = findViewById<TextView>(R.id.text_firstmenu)
                firstmenuTextView.text = details_info.firstmenu ?: "정보 없음"

                // 문의 및 안내
                val infocenterfoodTextView = findViewById<TextView>(R.id.text_infocenterfood)
                infocenterfoodTextView.text = details_info.infocenterfood ?: "정보 없음"

                // 어린이 놀이 방 여부
                val kidsfacilityTextView = findViewById<TextView>(R.id.text_kidsfacility)
                kidsfacilityTextView.text = details_info.kidsfacility ?: "정보 없음"

                // 개업일
                val opendatefoodTextView = findViewById<TextView>(R.id.text_opendatefood)
                opendatefoodTextView.text = details_info.opendatefood ?: "정보 없음"

                // 영업 시간
                val opentimefoodTextView = findViewById<TextView>(R.id.text_opentimefood)
                opentimefoodTextView.text = details_info.opentimefood ?: "정보 없음"

                // 포장 가능 여부
                val packingTextView = findViewById<TextView>(R.id.text_packing)
                packingTextView.text = details_info.packing ?: "정보 없음"

                // 주차 시설
                val parkingfoodTextView = findViewById<TextView>(R.id.text_parkingfood)
                parkingfoodTextView.text = details_info.parkingfood ?: "정보 없음"

                // 예약 안내
                val reservationfoodTextView = findViewById<TextView>(R.id.text_reservationfood)
                reservationfoodTextView.text = details_info.reservationfood ?: "정보 없음"

                // 휴무일
                val restdatefoodTextView = findViewById<TextView>(R.id.text_restdatefood)
                restdatefoodTextView.text = details_info.restdatefood ?: "정보 없음"

                // 규모
                val scalefoodTextView = findViewById<TextView>(R.id.text_scalefood)
                scalefoodTextView.text = details_info.scalefood ?: "정보 없음"

                // 좌석 수
                val seatTextView = findViewById<TextView>(R.id.text_seat)
                seatTextView.text = details_info.seat ?: "정보 없음"

                // 금연/흡연 여부
                val smokingTextView = findViewById<TextView>(R.id.text_smoking)
                smokingTextView.text = details_info.smoking ?: "정보 없음"

                // 취급 메뉴
                val treatmenuTextView = findViewById<TextView>(R.id.text_treatmenu)
                treatmenuTextView.text = details_info.treatmenu ?: "정보 없음"

                // 인허가 번호
                val lcnsnoTextView = findViewById<TextView>(R.id.text_lcnsno)
                lcnsnoTextView.text = details_info.lcnsno ?: "정보 없음"



//                //신용카드 가능 정보
//                val chkcreditcardfood = findViewById<TextView>(R.id.text_chkcreditcardfood)
//                chkcreditcardfood.text = details_info.chkcreditcardfood
//
//                //할인 정보
//                val discountinfofood = findViewById<TextView>(R.id.text_discountinfofood)
//                discountinfofood.text = details_info.discountinfofood
//
//                //대표 메뉴
//                val firstmenu = findViewById<TextView>(R.id.text_firstmenu)
//                firstmenu.text = details_info.firstmenu
//
//                //문의 및 안내
//                val infocenterfood = findViewById<TextView>(R.id.text_infocenterfood)
//                infocenterfood.text = details_info.infocenterfood
//
//                //어린이 놀이 방 여부
//                val kidsfacility = findViewById<TextView>(R.id.text_kidsfacility)
//                kidsfacility.text = details_info.kidsfacility
//
//                //개업일
//                val opendatefood = findViewById<TextView>(R.id.text_opendatefood)
//                opendatefood.text = details_info.opendatefood
//
//                val opentimefood = findViewById<TextView>(R.id.text_opentimefood)
//                opentimefood.text = details_info.opentimefood
//
//                val packing = findViewById<TextView>(R.id.text_packing)
//                packing.text = details_info.packing
//
//                val parkingfood = findViewById<TextView>(R.id.text_parkingfood)
//                parkingfood.text = details_info.parkingfood
//
//                val reservationfood = findViewById<TextView>(R.id.text_reservationfood)
//                reservationfood.text = details_info.reservationfood
//
//                val restdatefood = findViewById<TextView>(R.id.text_restdatefood)
//                restdatefood.text = details_info.restdatefood
//
//                val scalefood = findViewById<TextView>(R.id.text_scalefood)
//                scalefood.text = details_info.scalefood
//
//                val seat = findViewById<TextView>(R.id.text_seat)
//                seat.text = details_info.seat
//
//                val smoking = findViewById<TextView>(R.id.text_smoking)
//                smoking.text = details_info.smoking
//
//                val treatmenu = findViewById<TextView>(R.id.text_treatmenu)
//                treatmenu.text = details_info.treatmenu
//
//                val lcnsno = findViewById<TextView>(R.id.text_lcnsno)
//                lcnsno.text = details_info.lcnsno



                val heritage1 = findViewById<TextView>(R.id.text_heritage1)
                heritage1.text = details_info.heritage1

                val heritage2 = findViewById<TextView>(R.id.text_heritage2)
                heritage2.text = details_info.heritage2

                val heritage3 = findViewById<TextView>(R.id.text_heritage3)
                heritage3.text = details_info.heritage3

                val infocenter = findViewById<TextView>(R.id.text_infocenter)
                infocenter.text = details_info.infocenter

                val opendate = findViewById<TextView>(R.id.text_opendate)
                opendate.text = details_info.opendate

                val restdate = findViewById<TextView>(R.id.text_restdate)
                restdate.text = details_info.restdate

                val expguide = findViewById<TextView>(R.id.text_expguide)
                expguide.text = details_info.expguide

                val expagerange =findViewById<TextView>(R.id.text_expagerange)
                expagerange.text = details_info.expagerange

                val accomcount = findViewById<TextView>(R.id.text_accomcount)
                accomcount.text = details_info.accomcount

                val useseason = findViewById<TextView>(R.id.text_useseason)
                useseason.text = details_info.useseason

                val usetime = findViewById<TextView>(R.id.text_usetime)
                usetime.text = details_info.usetime

                val parking = findViewById<TextView>(R.id.text_parking)
                parking.text = details_info.parking

                val chkbabycarriage = findViewById<TextView>(R.id.text_chkbabycarriage)
                chkbabycarriage.text = details_info.chkbabycarriage

                val chkpet = findViewById<TextView>(R.id.text_chkpet)
                chkpet.text = details_info.chkpet

                val chkcreditcard = findViewById<TextView>(R.id.text_chkcreditcard)
                chkcreditcard.text = details_info.chkcreditcard




                val chkcreditcardculture = findViewById<TextView>(R.id.text_chkcreditcardculture)
                chkcreditcardculture.text = details_info.chkcreditcardculture

                val chkbabycarriageculture = findViewById<TextView>(R.id.text_chkbabycarriageculture)
                chkbabycarriageculture.text = details_info.chkbabycarriageculture

                val chkpetculture = findViewById<TextView>(R.id.text_chkpetculture)
                chkpetculture.text = details_info.chkpetculture

                val discountinfo = findViewById<TextView>(R.id.text_discountinfo)
                discountinfo.text = details_info.discountinfo

                val infocenterculture = findViewById<TextView>(R.id.text_infocenterculture)
                infocenterculture.text = details_info.infocenterculture

                val parkingculture = findViewById<TextView>(R.id.text_parkingculture)
                parkingculture.text = details_info.parkingculture

                val parkingfee = findViewById<TextView>(R.id.text_parkingfee)
                parkingfee.text = details_info.parkingfee

                val restdateculture = findViewById<TextView>(R.id.text_restdateculture)
                restdateculture.text = details_info.restdateculture

                val usefee = findViewById<TextView>(R.id.text_usefee)
                usefee.text = details_info.usefee

                val usetimeculture = findViewById<TextView>(R.id.text_usetimeculture)
                usetimeculture.text = details_info.usetimeculture

                val scale = findViewById<TextView>(R.id.text_scale)
                scale.text = details_info.scale

                val spendtime = findViewById<TextView>(R.id.text_spendtime)
                spendtime.text = details_info.spendtime

                val accomcountculture = findViewById<TextView>(R.id.text_accomcountculture)
                accomcountculture.text = details_info.accomcountculture






                val sponsor1 = findViewById<TextView>(R.id.text_sponsor1)
                sponsor1.text = details_info.sponsor1

                val sponsor1tel = findViewById<TextView>(R.id.text_sponsor1tel)
                sponsor1tel.text = details_info.sponsor1tel

                val sponsor2 = findViewById<TextView>(R.id.text_sponsor2)
                sponsor2.text = details_info.sponsor2

                val sponsor2tel = findViewById<TextView>(R.id.text_sponsor2tel)
                sponsor2tel.text = details_info.sponsor2tel

                val eventenddate = findViewById<TextView>(R.id.text_eventenddate)
                eventenddate.text = details_info.eventenddate

                val playtime = findViewById<TextView>(R.id.text_playtime)
                playtime.text = details_info.playtime

                val eventplace = findViewById<TextView>(R.id.text_eventplace)
                eventplace.text = details_info.eventplace

                val eventhomepage = findViewById<TextView>(R.id.text_eventhomepage)
                eventhomepage.text = details_info.eventhomepage

                val agelimit = findViewById<TextView>(R.id.text_agelimit)
                agelimit.text = details_info.agelimit

                val placeinfo = findViewById<TextView>(R.id.text_placeinfo)
                placeinfo.text = details_info.placeinfo

                val bookingplace = findViewById<TextView>(R.id.text_bookingplace)
                bookingplace.text = details_info.bookingplace

                val subevent = findViewById<TextView>(R.id.text_subevent)
                subevent.text = details_info.subevent

                val program = findViewById<TextView>(R.id.text_program)
                program.text = details_info.program

                val eventstartdate = findViewById<TextView>(R.id.text_eventstartdate)
                eventstartdate.text = details_info.eventstartdate

                val usetimefestival = findViewById<TextView>(R.id.text_usetimefestival)
                usetimefestival.text = details_info.usetimefestival

                val discountinfofestival = findViewById<TextView>(R.id.text_discountinfofestival)
                discountinfofestival.text = details_info.discountinfofestival

                val spendtimefestival = findViewById<TextView>(R.id.text_spendtimefestival)
                spendtimefestival.text = details_info.spendtimefestival

                val festivalgrade = findViewById<TextView>(R.id.text_festivalgrade)
                festivalgrade.text = details_info.festivalgrade






                val openperiod = findViewById<TextView>(R.id.text_openperiod)
                openperiod.text = details_info.openperiod

                val reservation = findViewById<TextView>(R.id.text_reservation)
                reservation.text = details_info.reservation

                val infocenterleports = findViewById<TextView>(R.id.text_infocenterleports)
                infocenterleports.text = details_info.infocenterleports

                val scaleleports = findViewById<TextView>(R.id.text_scaleleports)
                scaleleports.text = details_info.scaleleports

                val accomcountleports = findViewById<TextView>(R.id.text_accomcountleports)
                accomcountleports.text = details_info.accomcountleports

                val restdateleports = findViewById<TextView>(R.id.text_restdateleports)
                restdateleports.text = details_info.restdateleports

                val usetimeleports = findViewById<TextView>(R.id.text_usetimeleports)
                usetimeleports.text = details_info.usetimeleports

                val usefeeleports = findViewById<TextView>(R.id.text_usefeeleports)
                usefeeleports.text = details_info.usefeeleports

                val expagerangeleports = findViewById<TextView>(R.id.text_expagerangeleports)
                expagerangeleports.text = details_info.expagerangeleports

                val parkingleports = findViewById<TextView>(R.id.text_parkingleports)
                parkingleports.text = details_info.parkingleports

                val parkingfeeleports = findViewById<TextView>(R.id.text_parkingfeeleports)
                parkingfeeleports.text = details_info.parkingfeeleports

                val chkbabycarriageleports = findViewById<TextView>(R.id.text_chkbabycarriageleports)
                chkbabycarriageleports.text = details_info.chkbabycarriageleports

                val chkpetleports = findViewById<TextView>(R.id.text_chkpetleports)
                chkpetleports.text = details_info.chkpetleports

                val chkcreditcardleports = findViewById<TextView>(R.id.text_chkcreditcardleports)
                chkcreditcardleports.text = details_info.chkcreditcardleports






                val saleitem = findViewById<TextView>(R.id.text_saleitem)
                saleitem.text = details_info.saleitem

                val saleitemcost = findViewById<TextView>(R.id.text_saleitemcost)
                saleitemcost.text = details_info.saleitemcost

                val fairday = findViewById<TextView>(R.id.text_fairday)
                fairday.text = details_info.fairday

                val opendateshopping = findViewById<TextView>(R.id.text_opendateshopping)
                opendateshopping.text = details_info.opendateshopping

                val shopguide = findViewById<TextView>(R.id.text_shopguide)
                shopguide.text = details_info.shopguide

                val culturecenter = findViewById<TextView>(R.id.text_culturecenter)
                culturecenter.text = details_info.culturecenter

                val restroom = findViewById<TextView>(R.id.text_restroom)
                restroom.text = details_info.restroom

                val infocentershopping = findViewById<TextView>(R.id.text_infocentershopping)
                infocentershopping.text = details_info.infocentershopping

                val scaleshopping = findViewById<TextView>(R.id.text_scaleshopping)
                scaleshopping.text = details_info.scaleshopping

                val restdateshopping = findViewById<TextView>(R.id.text_restdateshopping)
                restdateshopping.text = details_info.restdateshopping

                val parkingshopping = findViewById<TextView>(R.id.text_parkingshopping)
                parkingshopping.text = details_info.parkingshopping

                val chkbabycarriageshopping = findViewById<TextView>(R.id.text_chkbabycarriageshopping)
                chkbabycarriageshopping.text  =details_info.chkbabycarriageshopping

                val chkpetshopping = findViewById<TextView>(R.id.text_chkpetshopping)
                chkpetshopping.text = details_info.chkpetshopping

                val chkcreditcardshopping = findViewById<TextView>(R.id.text_chkcreditcardshopping)
                chkcreditcardshopping.text = details_info.chkcreditcardshopping

                val opentime = findViewById<TextView>(R.id.text_opentime)
                opentime.text = details_info.opentime

            } catch (e: Exception) {
                Log.e("Exception", "기타 오류: $e")
            }
        }
    }

    private fun updateUIWithDetails(details_info: Recommend) {

    }

}