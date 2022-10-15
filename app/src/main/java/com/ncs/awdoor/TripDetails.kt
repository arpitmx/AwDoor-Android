package com.ncs.awdoor

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import com.ncs.awdoor.databinding.ActivityTripDetailsBinding

class TripDetails : AppCompatActivity() {

    lateinit var binding: ActivityTripDetailsBinding
    lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTripDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var anim = AnimationUtils.loadAnimation(this,R.anim.fadein)
        var out = AnimationUtils.loadAnimation(this, androidx.constraintlayout.widget.R.anim.abc_slide_out_top)

        sharedPref = getSharedPreferences("UserDetailPref", Context.MODE_PRIVATE)

        val uname = sharedPref.getString("username",null)
        if (uname!=null){
            binding.startCard.uname.text= "Hey $uname,"
        }else {
            startActivity(Intent(this,MainActivity::class.java))
        }

        binding.startCard.card.visibility = View.VISIBLE
        binding.startCard.card.startAnimation(anim)

        binding.startCard.startBtn.setOnClickListener{
            binding.startCard.card.startAnimation(out)
            Handler(Looper.getMainLooper()).postDelayed({
                binding.startCard.card.visibility = View.GONE
                binding.progressiveFrameContainer.visibility= View.VISIBLE
            },200)
        }





    }
}