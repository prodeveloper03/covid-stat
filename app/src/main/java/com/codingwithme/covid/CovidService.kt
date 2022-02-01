package com.codingwithme.covid

import android.telecom.Call
import retrofit2.http.GET

interface  CovidService {
    @GET("us/daily.json")
    fun getNationalData():  retrofit2.Call<List<CovidData>>
    @GET("states/daily.json")
    fun getStatesData() : retrofit2.Call<List<CovidData>>
}