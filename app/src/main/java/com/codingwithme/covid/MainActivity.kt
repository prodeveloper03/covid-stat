package com.codingwithme.covid

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.covidtracking.com/v1/"
private const val TAG =  "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var perStateDailyData: Map<String, List<CovidData>>
    private lateinit var nationalDailyData: List<CovidData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd 'T':HH:mm:ss").create()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
       val covidService = retrofit.create(CovidService::class.java)
        //Fetch National data
        covidService.getNationalData().enqueue(object : Callback<List<CovidData>>{
            override fun onResponse(call: Call<List<CovidData>>, response: Response<List<CovidData>>) {

                Log.i(TAG,"onResponse $response")
                val nationalData = response.body()
                if(nationalData==null){
                    Log.w(TAG,"Did not recieved valid response")
                     return
                }
                nationalDailyData = nationalData.reversed()
                Log.i(TAG,"Update graph wih national data")
                // we need to update graph with national data

            }

            override fun onFailure(call: Call<List<CovidData>>, t: Throwable) {
                Log.e(TAG , "onFailure $t ")
            }

        })


        //Fetch States Data
        covidService.getStatesData().enqueue(object  : Callback<List<CovidData>>{
            override fun onResponse(call: Call<List<CovidData>>, response: Response<List<CovidData>>) {

                Log.i(TAG,"onResponse")
                val statesData = response.body()
                if(statesData==null){
                    Log.w(TAG,"Did not recieved response")
                    return
                }
                perStateDailyData  = statesData.reversed().groupBy { it.state }
                Log.i(TAG,"Updated spinner with sta tes data")
            }

            override fun onFailure(call: Call<List<CovidData>>, t: Throwable) {
                Log.e(TAG,"onFailure $t")
            }

        })


    }
}