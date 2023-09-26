package com.a406.horsebit

import java.util.Date

// 미체결 내역 조회
data class NotConcludedResponseBodyOrderModel(
    val orderNo: Int,  //주문번호
    val userNo: Int,  //유저번호
    val tokenNo: Int,  //토큰번호
    val tokenCode: String, //토큰코드명
    val price: Long,  //가격
    val quantity: Double,  //주문수량
    val remainQuantity: Double,  //잔여수량
    val orderTime: Date,  //주문일자
    val sellOrBuy: String, //매수 매도 여부
)

// 체결 내역 조회
data class ConcludedResponseBodyOrderModel(
    val executionNo: Long, //거래번호
    val tokenNo: Long,  //토큰번호
    val tokenCode: String, //토큰코드명
    val price: Long,  //가격
    val quantity: Double,  //주문수량
    val timestamp: Date,  //체결시간
    val sellOrBuy: String //매수 매도 여부
)

// 체결 혹은 미체결
data class TransactionShow(
    val completeOrNot: Boolean, // 체결 or 미체결
    val sellORBuy: String,    // 매수 or 매도
    val time: Date,  // 주문시간 or 채결시간
    val tokenCode: String, //토큰 코드
    val price: Long,  //가격
    val quantity: Double,  //수량
    val remainQuantityOrPrice: Double,  //미체결량
)

// 코인 목록 조회 (SSE)
data class Token(
    val tokenNo: Long,  //토큰번호
    val name: String,  //토큰 이름
    val code: String,  // 토큰 코드
    val currentPrice: Double,  // 토큰 현재가
    val priceRateOfChange: Double, //변동추이
    val volume: Double, //거래금액
    val newFlag: Boolean,   // 새로운 코인 여부
)

data class TokenShow(
    val name: String,   // 토큰 이름
    val code: String,   // 토큰 코드
    val currentPrice: Double,   // 토큰 현재가
    val priceRateOfChange: Double, // 변동추이
    val volume: Double, // 거래금액
    val newFlag: Boolean,   // 새로운 코인 여부
    val interest: Boolean,  // 즐겨찾기면 true, 아니면 false
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

data class Exchange(
    val data1: Int,
    val data2: Int,
    val data3: Int,
)



data class CandleShow(
    val createdAt: Long,
    val open: Float,
    val close: Float,
    val shadowHigh: Float,
    val shadowLow: Float
)

// 마이페이지 코인 component
data class MyAssetModel(
    val horseImage: Int,
    val tokenNo : Int,
    val name: String,
    val code: String,
    val profitOrLoss: String,
    val returnRate: String
)

// 마이페이지 총자산 component
data class MyTotalAssetModel(
    val totalAsset : Int,
    val cashBalance: Int,
    val totalPurchase: Int,
    val totalEvaluation: Int,
    val profitOrLoss: Int,
    val returnRate: Int,
)

// 로그인
data class LoginModel(
    val id : Long,
    val email: String,
    val nickname: String,
    val userName: String,
    val accessToken: String,
    val refreshToken: String,
)

// 회원가입
data class SignUpModel(
    val userName: String,
)