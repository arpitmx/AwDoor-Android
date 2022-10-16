package com.ncs.awdoor

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.ncs.awdoor.databinding.ActivityFinalBinding
import org.json.JSONObject


class FinalActivity : AppCompatActivity() {

    lateinit var binding : ActivityFinalBinding
    lateinit var anim : Animation


    lateinit var tripMode: String
    lateinit var start : String
    lateinit var dest : String
    lateinit var date : String
    lateinit var people : String
    lateinit var days : String
    lateinit var i : Intent
    lateinit var requestQueue : RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_AwDoor_loading);

        binding = ActivityFinalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        i = intent
        setData()




        setLoading("https://media0.giphy.com/media/xUA7bdjDQYJiNauKfm/giphy.gif?cid=790b761145eb579c767428beb0ae56650f95089e9e867b99&rid=giphy.gif&ct=g")

        anim = AnimationUtils.loadAnimation(this, R.anim.fadeout)
        Handler(Looper.getMainLooper()).postDelayed({
            binding.loadingScreen.startAnimation(anim)
            removeLoading()
        },2000)

    }

    fun setData(){
        tripMode = i.getStringExtra("tripMode").toString()
        start = i.getStringExtra("start").toString()
        dest = i.getStringExtra("dest").toString()
        date = i.getStringExtra("date").toString()
        people = i.getStringExtra("people").toString()
        days = i.getStringExtra("days").toString()
        Log.d("TAG", "setData: tripmode"+date)

       // fetchData(tripMode,start,dest,date)
        fetchData("train","Delhi","kasol","18/10/2022")

    }

    fun setLoading(url:String){
        binding.loadingScreen.visibility = View.VISIBLE
        Glide.with(this)
            .asGif()
            .load(url)
            .into(binding.gif)
    }

    fun fetchData( mode: String, src: String, dest : String , date : String){

        val requestQueue = Volley.newRequestQueue(this)

        val url = "https://young-bastion-57330.herokuapp.com/fare?mode=${mode}&src=${src}&dest=${dest}&date=${date}"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->

                    var link = response.getJSONObject("tripDetail")
                    var cheap = link.getJSONObject("cheap")
                    var expensive = link.getJSONObject("expensive")

                setDataInViews(cheap,expensive)

                Log.d("Log", "fetchData: "+link)

            },
            { error ->
                Log.d("vol",error.toString())
            }
        )

        requestQueue.add(jsonObjectRequest)
    }

    fun setDataInViews(cheap : JSONObject, expensive: JSONObject){

        if (tripMode=="flight"){

            var totalCheapFare : Int = 0
            var fareC : Int = cheap.get("price") as Int
            var nameC = cheap.get("name")
            var depC = cheap.get("dept")
            var hotelprice = 1500
            totalCheapFare = fareC + hotelprice*days.toInt()
            totalCheapFare * people.toInt()



        }else if (tripMode=="train"){

        }
        else {

        }
    }



    fun removeLoading(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.setStatusBarColor(resources.getColor(R.color.status))
        }
        binding.loadingScreen.visibility = View.GONE
    }


}