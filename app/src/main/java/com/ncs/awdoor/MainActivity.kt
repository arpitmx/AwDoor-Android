package com.ncs.awdoor

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import com.ncs.awdoor.databinding.ActivityMainBinding
import com.ncs.awdoor.databinding.ActivityUserDetailsBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    lateinit var sharedPref : SharedPreferences
    lateinit var editor : SharedPreferences.Editor


    public fun actionSearchFrag(item : View){
        val intent = Intent(this, User_Details::class.java)
        val options : ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this,item,
            ViewCompat.getTransitionName(item).toString()
        )

        startActivity(intent,options.toBundle())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = getSharedPreferences("UserDetailPref", Context.MODE_PRIVATE)

        if (sharedPref.getString("username",null) != null){
            startActivity(Intent(this,TripDetails::class.java))
            finish()
        }


        binding.next.setOnClickListener {
            actionSearchFrag(binding.logo)
       }


    }
}