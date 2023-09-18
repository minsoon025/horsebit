package com.a406.horsebit

import java.util.Date

data class NotConcludedRequestBodyModel(
    val userNo: Int, //유저번호
    val tokenNo: Int, //토큰번호
    val startDate: Date, //시작일자
    val endDate: Date, //종료일자
)

data class NotConcludedResponseBodyModel(
    val orders: ArrayList<NotConcludedResponseBodyOrderModel>,
)

data class NotConcludedResponseBodyOrderModel(
    val orderNo: Int,  //주문번호
    val userNo: Int,  //유저번호
    val tokenNo: Int,  //토큰번호
    val tokenCode: String, //토큰코드명
    val price: Int,  //가격
    val quantity: Double,  //주문수량
    val remain_quantity: Double,  //잔여수량
    val orderTime: Date,  //주문일자
    val sellOrBuy: Char, //매수 매도 여부
)

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

// ExchangeData.kt

data class ExchangeData(
    val orderTime: String,
    val coinName: String,
    val type: String,
    val seep: String,
    val one: String,
    val money: String,
    val fee: String,
    val realMoney: String,
    val orderTime2: String
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