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
    val tokenNo: Long,  // 토큰 번호
    val name: String,  //토큰 이름
    val code: String,  // 토큰 코드
    val currentPrice: Long,  // 토큰 현재가
    val priceRateOfChange: Double, //변동추이
    val volume: Double, //거래 금액
    val newFlag: Boolean,   // 새로운 코인 여부
)


data class TokenShow(
    val tokenNo: Long,  // 토큰 번호
    val name: String,   // 토큰 이름
    val code: String,   // 토큰 코드
    val currentPrice: Long,   // 토큰 현재가
    val priceRateOfChange: Double, // 변동추이
    val volume: Double, // 거래 금액
    val newFlag: Boolean,   // 새로운 코인 여부
    val interest: Boolean,  // 즐겨찾기면 true, 아니면 false
)

data class Order(
    val price: Long,    // 호가 가격
    val volume: Double,   // 주문량
    val priceRateOfChange: Double,  // 변동추이
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

// 입출금 토큰 만들기
data class ExchangeDataResponseBodyModel(
    val executionTime: String, //체결시간
    val tokenNo: Long, //토큰번호
    val code: String, //토큰코드
    val transactionType: String, //종류
    val volume: String, //거래수량
    val price: String, //거래단가
    val transactionAmount: String, //거래금액
    val fee: String, //수수료
    val amount: String, //정산금액
    val orderTime: String, //주문시간
)

data class CandleChartDataResponseBodyBodyModel(
    val startTime: String, // 시간
    val open: Long, // 시가
    val close: Long, // 종가
    val high: Long, // 고가
    val low: Long, // 저가
    val volume: Double, // 거래량
)

data class CandleShow(
    val x: Float,
    val shadowH: Float,
    val shadowL: Float,
    val open: Float,
    val close: Float,
)

data class BarShow(
    val x: Float,
    val value: Float,
    val colorFlag: Boolean,
)

// 마이페이지 코인 component
data class MyAssetResponseBodyModel(
    val horseImage: Int,
    val tokenNo : Long,
    val name: String,
    val code: String,
    val profitOrLoss: String,
    val returnRate: String
)

// 마이페이지 총자산 component
data class MyTotalAssetResponseBodyModel(
    val totalAsset : Double,
    val cashBalance: Double,
    val totalPurchase: Double,
    val totalEvaluation: Double,
    val profitOrLoss: Double,
    val returnRate: Double,
)

// 로그인, 내가 받는 모델
data class LoginResponseBodyModel(
    val accessToken: String,
    val refreshToken: String,
    val userDTO: UserDTO
)

data class UserDTO(

    val id : Long,  //유저번호
    val refreshToken: String,  //리프레시토큰
    val nickname: String,  //유저 닉네임(구글계정 닉네임)
    val email: String,  //유저 이메일
    val userName: String  //유저 이름(앱에서 사용해야함)

)

// 로그인, 백서버에 넘겨줘야하는 모델
data class LoginRequestBodyModel(
    //val providerName: String,
    val token: String,


)




//-----------------------------

// 회원가입
data class SignUpResponseBodyModel(
    val user: User
)

data class User(
    val email : String, //유저 이메일
    val nickname : String, // 유저 구글 이름
    val userName: String, // 유저 홀스빗 이름
    val role: String
)


// 회원가입 요청 모델

data class SignUpRequestBodyModel(
    val token: String,
    val userName: String

)







// 즐겨찾기 추가 / 삭제의 응답을 담을 ResponseBodyModel
data class FavoriteResponseBodyModel(
    val result: String,
)
// 입출금 요청
data class KrwInOutRequestBodyModel(
    val reqAmount: Long
)

// 회원탈퇴
data class UserRequestBodyDelete(
    val userId: Long
)

// 코인 상세 조회 (SSE)
data class TokenListDetailResponseBodyModel(
    val availableDeposit: Double, //유저 잔여금액
    val tokenNo: Long, //토큰번호
    val name: String, //토큰명
    val code: String, //토큰코드명
    val currentPrice: String, //현재가
    val priceRateOfChange: Double, //변동추이
    val priceOfChange: Long,    //변동금액
)

// 매수 주문 요청
data class OrderRequestRequestBodyModel(
    val tokenNo: Long, //토큰번호
    val volume: Double, //수량
    val price: Long, //단위가격
)

data class OrderRequestResponseBodyModel(
    val result: String, // 결과 코드
)

// 매도 주문 요청
data class SellRequestRequestBodyModel(
    val tokenNo: Long, //토큰번호
    val volume: Double, //수량
    val price: Long, //단위가격
)

data class SellRequestResponseBodyModel(
    val result: String, // 결과 코드
)

// 보유 마패 특정 조회
data class SpecificSearchResponseBodyModel(
    val tokenNo: Long, //토큰번호
    val possessQuantity: Double, //보유개수
    val currentPrice: Long, //단위 현재가
)

// 코인 경주마 정보 조회
data class TokenInfoResponseBodyModel(
    val hrNo: Long, //마번
    val hrName: String, //마명
    val code: String, //토큰코드명
    val content: String, //말 소개글
    val publishDate: Date, //발행일자
    val supply: Double, //총 발행량
    val marketCap: Double, //시가총액
    val owName: String, //마주 이름
    val birthPlace: String, //출생지
    val fatherHrName: String, //부마 이름
    val motherHrName: String, //모마 이름
    val raceRank: String, //경주마 등급 (경주마 일 때만)
)