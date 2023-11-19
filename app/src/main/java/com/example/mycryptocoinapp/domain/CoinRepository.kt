package com.example.mycryptocoinapp.domain

import androidx.lifecycle.LiveData

interface CoinRepository {

    fun getCoinList(): LiveData<List<CoinInfo>>

    fun getCoinInfo(fromSymbol: String): LiveData<CoinInfo>

    suspend fun loadData()
}