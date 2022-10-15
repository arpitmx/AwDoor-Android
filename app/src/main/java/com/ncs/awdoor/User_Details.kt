package com.ncs.awdoor

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ncs.awdoor.databinding.ActivityUserDetailsBinding

class User_Details : AppCompatActivity() {

    lateinit var sharedPref : SharedPreferences
    lateinit var editor : SharedPreferences.Editor
    lateinit var binding : ActivityUserDetailsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = getSharedPreferences("UserDetailPref", Context.MODE_PRIVATE)
        editor = sharedPref.edit()
        setViews()
    }

    fun setViews() : Unit {

        binding.begin.setOnClickListener{

            val name = binding.name.text.toString()
            val state = binding.state.text.toString()
            val city = binding.city.text.toString()

            editor.putString("username",name)
            editor.putString("state",state)
            editor.putString("city",city)
            editor.commit()

            startActivity(Intent(this, TripDetails::class.java))
        }

    }
}