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

data class TransactionShow(
    val completeOrNot: Boolean, // 체결 or 미체결
    val sellORBuy: Char,    // 매수 or 매도
    val time: Date,  // 주문시간 or 채결시간
    val tokenCode: String, //토큰 코드
    val price: Long,  //가격
    val quantity: Double,  //수량
    val remainQuantityOrPrice: Double,  //미체결량

)

data class Candle(
    val createdAt: Long,
    val open: Float,
    val close: Float,
    val shadowHigh: Float,
    val shadowLow: Float
)