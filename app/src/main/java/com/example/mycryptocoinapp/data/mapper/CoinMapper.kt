package com.example.mycryptocoinapp.data.mapper

import com.example.mycryptocoinapp.data.database.CoinInfoDbModel
import com.example.mycryptocoinapp.data.network.model.CoinInfoDto
import com.example.mycryptocoinapp.data.network.model.CoinInfoJsonContainerDto
import com.example.mycryptocoinapp.data.network.model.CoinNameListDto
import com.example.mycryptocoinapp.domain.CoinInfo
import com.google.gson.Gson
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class CoinMapper {

    fun mapDtoToDbModel(dto: CoinInfoDto) = CoinInfoDbModel(
        fromSymbol = dto.fromSymbol,
        toSymbol = dto.toSymbol,
        price = dto.price,
        lastUpdate = dto.lastUpdate,
        highDay = dto.highDay,
        lowDay = dto.lowDay,
        lastMarket = dto.lastMarket,
        imageUrl = BASE_IMAGE_URL + dto.imageUrl
    )

    fun mapDbModelToEntity(dbModel: CoinInfoDbModel) = CoinInfo(
        fromSymbol = dbModel.fromSymbol,
        toSymbol = dbModel.toSymbol,
        price = convertPrice(dbModel.price),
        lastUpdate = convertTimestampToTime(dbModel.lastUpdate),
        highDay = convertPrice(dbModel.highDay),
        lowDay = convertPrice(dbModel.lowDay),
        lastMarket = dbModel.lastMarket,
        imageUrl = dbModel.imageUrl
    )

    fun mapJsonContainerToListCoinInfo(jsonContainer: CoinInfoJsonContainerDto): List<CoinInfoDto> {
        val result = mutableListOf<CoinInfoDto>()
        val jsonObject = jsonContainer.json ?: return result
        val coinKeySet = jsonObject.keySet()
        for (coinKey in coinKeySet) {
            val currencyJson = jsonObject.getAsJsonObject(coinKey)
            val currencyKeySet = currencyJson.keySet()
            for (currencyKey in currencyKeySet) {
                val priceInfo = Gson().fromJson(
                    currencyJson.getAsJsonObject(currencyKey),
                    CoinInfoDto::class.java
                )
                result.add(priceInfo)
            }
        }
        return result
    }

    fun mapNamesListToString(nameListDto: CoinNameListDto): String {
        return nameListDto.names?.map {
            it.coinName?.name
        }?.joinToString(",").toString()
    }

    private fun convertTimestampToTime(timestamp: Long?): String {
        if (timestamp == null) return ""
        val stamp = Timestamp(timestamp * 1000)
        val date = Date(stamp.time)
        val pattern = "HH:mm:ss"
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(date)
    }

    private fun convertPrice(price: String?): String {
        return String.format("%.4f", price?.toFloat())
    }

    companion object {

        const val BASE_IMAGE_URL = "https://cryptocompare.com"
    }
}