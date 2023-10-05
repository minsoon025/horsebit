package com.a406.horsebit

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.LocalDateTime

interface APIS {

    // 로그인
    @POST("/api/login/auth/signIn")
    fun login(
        @Body request: LoginRequestBodyModel
    ):Call<LoginResponseBodyModel>

    // ----------------------------------
    // 회원가입
    @PUT("/api/login/auth/signUp")
    fun SingUp(
        @Header("Authorization") authorization: String,
        @Body request: SignUpRequestBodyModel
    ):Call<SignUpResponseBodyModel>

    // 닉네임 중복체크

    // 마이페이지 총 자산 불러오기
    @GET("/api/assets")
    fun MyTotalAsset(
        @Header("Authorization") authorization: String,
    ):Call<MyTotalAssetResponseBodyModel>

    // 입출금 페이지 조회
    @GET("/api/assets/investments")
    fun ExchangeDataModel(
        @Header("Authorization") authorization: String,
    ):Call<ArrayList<ExchangeDataResponseBodyModel>>


    // 마이페이지 코인 불러오기
    @GET("/api/assets/horses")
    fun MyCoins(
        @Header("Authorization") authorization: String,
    ):Call<ArrayList<MyAssetResponseBodyModel>>

    // 미체결 내역 조회
    @GET("/api/exchange/orders/{tokenNo}")
    fun notConcluded(
        @Path("tokenNo") tokenNo: Long,
        @Header("Authorization") authorization: String,
    ): Call<ArrayList<NotConcludedResponseBodyOrderModel>>

    // 체결 내역 조회
    @GET("/api/exchange/executions/{tokenNo}")
    fun concluded(
        @Path("tokenNo") tokenNo: Long,
        @Header("Authorization") authorization: String,
    ): Call<ArrayList<ConcludedResponseBodyOrderModel>>

    // 코인 목록 조회 (SSE)
    @GET("/api/exchange/tokens")
    fun tokenList(
        @Header("Authorization") authorization: String,
    ): Call<ArrayList<Token>>

    // 보유 코인 목록 조회
    @GET("/api/exchange/tokens/possess")
    fun holding(
        @Header("Authorization") authorization: String,
    ): Call<ArrayList<Token>>

    // 즐겨찾기 코인 목록 조회
    @GET("/api/exchange/tokens/favorites")
    fun favorites(
        @Header("Authorization") authorization: String,
    ): Call<ArrayList<Token>>

    // 즐겨찾기 추가
    @POST("/api/exchange/tokens/favorites/{tokenNo}")
    fun addFavorite(
        @Path("tokenNo") tokenNo: Long,
        @Header("Authorization") authorization: String,
    ): Call<FavoriteResponseBodyModel>

    // 즐겨찾기 삭제
    @DELETE("/api/exchange/tokens/favorites/{tokenNo}")
    fun deleteFavorite(
        @Path("tokenNo") tokenNo: Long,
        @Header("Authorization") authorization: String,
    ): Call<FavoriteResponseBodyModel>

    // 캔들 차트 조회
    @GET("/api/exchange/tokens/{tokenNo}/chart")
    fun candleChartData(
        @Path("tokenNo") tokenNo: Long,
        @Query("quantity") quantity: Long,
        @Query("endTime") endTime: LocalDateTime,
        @Query("candleTypeIndex") candleTypeIndex: Int,
        @Query("margin") margin: Long
    ): Call<ArrayList<CandleChartDataResponseBodyBodyModel>>

    // 코인 상세 조회 (SSE)
    @GET("/api/exchange/tokens/{tokenNo}")
    fun tokenListDetail(
        @Path("tokenNo") tokenNo: Long,
        @Header("Authorization") authorization: String,
    ): Call<TokenListDetailResponseBodyModel>

    // 현금 입출금 요청
    @POST("/api/assets/depositwithdraw")
    fun krwInOut(
        @Header("Authorization") authorization: String,
        @Body request: KrwInOutRequestBodyModel // 요청 데이터를 Body로 전달
    ): Call<KrwInOutRequestBodyModel>

    // 회원탈퇴
    @DELETE("/api/user/delete")
    fun deleteUser(
        @Header("Authorization") authorization: String,
    ): Call<UserRequestBodyDelete>

    // 코인 주문 현황 상세 조회 (SSE)
    @GET("/api/exchange/tokens/{tokenNo}/volumes")
    fun coinOrderSituation(
        @Path("tokenNo") tokenNo: Long,
    ): Call<ArrayList<Order>>

    // 매수 주문 요청
    @POST("/api/exchange/order/buy")
    fun orderRequest(
        @Header("Authorization") authorization: String,
        @Body request: OrderRequestRequestBodyModel,
    ): Call<OrderRequestResponseBodyModel>

    // 매도 주문 요청
    @POST("/api/exchange/order/sell")
    fun sellRequest(
        @Header("Authorization") authorization: String,
        @Body request: SellRequestRequestBodyModel,
    ): Call<SellRequestResponseBodyModel>

    // 보유 마패 특정 조회
    @GET("/api/assets/horses/{tokenNo}")
    fun specificSearch(
        @Path("tokenNo") tokenNo: Long,
        @Header("Authorization") authorization: String,
    ): Call<SpecificSearchResponseBodyModel>

    // 코인 경주마 정보 조회
    @GET("/api/exchange/tokens/{tokenNo}/info")
    fun tokenInfo(
        @Path("tokenNo") tokenNo: Long,
    ): Call<TokenInfoResponseBodyModel>


    companion object {
        private const val BASE_URL = "https://j9a406.p.ssafy.io"

        fun create(): APIS {
            val gson : Gson = GsonBuilder().setLenient().create();

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(APIS::class.java)
        }
    }
}