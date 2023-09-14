package com.a406.horsebit

import java.util.Date

data class AssetItem(
    val graph: Int,
    val assetName: String,
    val assetTicker: String,
    val new: Boolean,
    val currentPrice: Double,
    val yesterdayPrice: Double,
    val transactionPrice: Double,
)

data class Order(
    val price: Long,
    val volume: Long,
    val trend: Double,
)

data class Exchange(
    val data1: Int,
    val data2: Int,
    val data3 : Int,
)

data class TransactionOrder(
    val orderNo: Long,  //주문번호
    val userNo: Long,  //유저번호
    val tokenNo: Long,  //토큰번호
    val tokenCode: String, //토큰 코드
    val price: Long,  //가격
    val quantity: Double,  //수량
    val remain_quantity: Double,  //미체결량
    val orderTime: Date,  //주문일자
)