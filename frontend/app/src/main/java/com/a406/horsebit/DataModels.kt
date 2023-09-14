package com.a406.horsebit

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