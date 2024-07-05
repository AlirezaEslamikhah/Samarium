package com.example.myapplication.database

import androidx.room.PrimaryKey
data class NetworkInfo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val eventTime: Long,
    val latitude: Double,
    val longitude: Double,
    val cellTechnology: String,
    val plmnId: String?,
    val rac: String?,
    val tac: String?,
    val lac: String?,
    val cellId: String?,
    val signalStrength: Int?,
    val rsrq: Int?,
    val rsrp: Int?,
    val rscp: Int?,
    val ecNo: Int?,
    val signalQuality: String,
    val situation: String
)

