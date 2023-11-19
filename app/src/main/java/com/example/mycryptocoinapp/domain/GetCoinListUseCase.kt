package com.example.mycryptocoinapp.domain

class GetCoinListUseCase(
    private val repository: CoinRepository
) {
    operator fun invoke() = repository.getCoinList()
}