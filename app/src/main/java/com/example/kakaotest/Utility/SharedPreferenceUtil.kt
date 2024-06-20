package com.example.kakaotest.Utility

import android.content.Context
import android.content.SharedPreferences
import com.example.kakaotest.DataModel.TravelPlan
import com.example.kakaotest.DataModel.tmap.DayRouteData
import com.example.kakaotest.DataModel.tmap.SearchData
import com.example.kakaotest.DataModel.tmap.SelectedPlaceData
import com.example.kakaotest.DataModel.metaRoute.MetaDayRoute
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.LinkedList

object SharedPreferenceUtil {

    private const val Trave_Plan = "travelplan"

    fun saveTravelPlanToSharedPreferences(context: Context, travelPlan: TravelPlan) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(Trave_Plan, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(travelPlan)
        editor.putString(Trave_Plan, json)
        editor.apply()
    }

    fun getTravelPlanFromSharedPreferences(context: Context): TravelPlan? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(Trave_Plan, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString(Trave_Plan, null)
        return if (json != null) {
            val type = object : TypeToken<TravelPlan>() {}.type
            gson.fromJson(json, type)
        }else {
            null
        }
    }


    fun saveRouteToSharedPreferences(context: Context,receivedDataList: ArrayList<SelectedPlaceData>) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("RoutePlace", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(receivedDataList)
        editor.putString("RoutePlace", json)
        editor.apply()
    }



    fun getRouteFromSharedPreferences(context: Context): java.util.ArrayList<SelectedPlaceData>? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("RoutePlace", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("RoutePlace", null)
        val type = object : TypeToken<java.util.ArrayList<SelectedPlaceData>>() {}.type
        return if (json != null) {
            gson.fromJson(json, type)
        } else {
            java.util.ArrayList() // 저장된 데이터
        }
    }


    fun saveDataToSharedPreferences(context: Context,receivedDataList: ArrayList<SelectedPlaceData>) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("MySavedPlaces", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(receivedDataList)
        editor.putString("selectedPlaceDataList", json)
        editor.apply()
    }


    fun getDataFromSharedPreferences(context: Context): java.util.ArrayList<SelectedPlaceData>? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("MySavedPlaces", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("selectedPlaceDataList", null)
        val type = object : TypeToken<java.util.ArrayList<SelectedPlaceData>>() {}.type
        return if (json != null) {
            gson.fromJson(json, type)
        } else {
            java.util.ArrayList() // 저장된 데이터
        }
    }



    fun saveFoodToSharedPreferences(context: Context,receivedDataList: ArrayList<SearchData>) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("foodPlace", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(receivedDataList)
        editor.putString("foodPlace", json)
        editor.apply()
    }

    fun getFoodFromSharedPreferences(context: Context): ArrayList<SearchData>{
        val sharedPreferences : SharedPreferences = context.getSharedPreferences("foodPlace",Context.MODE_PRIVATE)
        val gson=Gson()
        val json = sharedPreferences.getString("foodPlace",null)
        val type = object : TypeToken<ArrayList<SearchData>>(){}.type
        return if (json!=null){
            gson.fromJson(json,type)

        }else {
            ArrayList()
        }
    }




    fun saveDayRouteList(context: Context, list: LinkedList<DayRouteData>) {
        val sharedPreferences = context.getSharedPreferences("carRoute", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(list)
        editor.putString("carRoute", json)
        editor.apply()
    }

    fun getDayRouteList(context: Context): LinkedList<DayRouteData>? {
        val sharedPreferences = context.getSharedPreferences("carRoute", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("carRoute", null)
        val type = object : TypeToken<LinkedList<DayRouteData>>() {}.type
        return gson.fromJson(json, type)
    }

    fun saveMetaDayRouteList(context: Context, list: LinkedList<MetaDayRoute>) {
        val sharedPreferences = context.getSharedPreferences("busRoute", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(list)
        editor.putString("busRoute", json)
        editor.apply()
    }

    fun getMetaDayRouteList(context: Context): LinkedList<MetaDayRoute>? {
        val sharedPreferences = context.getSharedPreferences("busRoute", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("busRoute", null)
        val type = object : TypeToken<LinkedList<MetaDayRoute>>() {}.type
        return gson.fromJson(json, type)
    }
}
