package com.teststudio.currencyconverter.network.response

import com.google.gson.annotations.SerializedName

data class LatestRatesResponse(
    @SerializedName("rates") val rates: HashMap<String, Double>
)