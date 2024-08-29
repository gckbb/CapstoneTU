package com.example.kakaotest.Utility

import android.util.Log
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.example.kakaotest.DataModel.ScheduleData
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import kotlinx.coroutines.tasks.await
import java.util.concurrent.ExecutionException

class Database {
    private val db = Firebase.firestore

    fun AddPlan(data : ScheduleData) {
        db.collection("travelPlans").document("${data.scheduleid}").set(data)
            .addOnSuccessListener { Log.d("database", "${data.scheduleid}가 성공적으로 추가됨")}
            .addOnFailureListener { Log.d("database", "${data.scheduleid}의 추가가 실패함")}
    }

    fun DeletePlan(data : ScheduleData) {
        db.collection("travelPlans").document("${data.scheduleid}").delete()
            .addOnSuccessListener { Log.d("database", "${data.scheduleid}가 성공적으로 삭제됨")}
            .addOnFailureListener { Log.d("database", "${data.scheduleid}의 삭제가 실패함")}
    }



    suspend fun getData(id: String) : Task<QuerySnapshot> {
        return db.collection("travelPlans").whereEqualTo("mainId",id).get()
    }


}