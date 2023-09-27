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
import retrofit2.http.Path
import java.util.Date

interface APIS {

    // 로그인
    @POST("/api/auth/login")
    fun login(
        @Header("Authorization") authorization: String,
        @Body params: LoginRequestBodyModel
    ):Call<ArrayList<LoginResponseBodyModel>>
//

    // 회원가입
    @POST("/api/user/signup")
    fun SingUp(
        @Header("Authorization") authorization: String,
    )
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

    // 즐겨찾기 코인 목록 조회
    @GET("/api/exchange/favorites")
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
    @GET("/api/exchange/tokens/{tokenNo}/chart?quantity={quantity}&endTime={endTime}&candleTypeIndex={candleTypeIndex}&margin={margin}")
    fun candleChartData(
        @Path("tokenNo") tokenNo: Long,
        @Path("quantity") quantity: Long,
        @Path("endTime") endTime: Date,
        @Path("candleTypeIndex") candleTypeIndex: Int,
        @Path("margin") margin: Long
    ): Call<ArrayList<CandleChartDataResponseBodyBodyModel>>

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