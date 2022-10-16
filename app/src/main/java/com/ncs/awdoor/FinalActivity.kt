package com.ncs.awdoor

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.ncs.awdoor.databinding.ActivityFinalBinding


class FinalActivity : AppCompatActivity() {

    lateinit var binding : ActivityFinalBinding
    lateinit var anim : Animation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_AwDoor_loading);

        binding = ActivityFinalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setLoading("https://media0.giphy.com/media/xUA7bdjDQYJiNauKfm/giphy.gif?cid=790b761145eb579c767428beb0ae56650f95089e9e867b99&rid=giphy.gif&ct=g")

        anim = AnimationUtils.loadAnimation(this, R.anim.fadeout)
        Handler(Looper.getMainLooper()).postDelayed({
            binding.loadingScreen.startAnimation(anim)
            removeLoading()
        },2000)


    }

    fun setLoading(url:String){
        binding.loadingScreen.visibility = View.VISIBLE
        Glide.with(this)
            .asGif()
            .load(url)
            .into(binding.gif)
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