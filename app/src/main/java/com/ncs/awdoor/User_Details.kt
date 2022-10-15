package com.ncs.awdoor

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ncs.awdoor.databinding.ActivityUserDetailsBinding

class User_Details : AppCompatActivity() {

    lateinit var sharedPref : SharedPreferences
    lateinit var editor : SharedPreferences.Editor
    lateinit var binding : ActivityUserDetailsBinding

    lateinit var name: String
    lateinit var state: String
    lateinit var city: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = getSharedPreferences("UserDetailPref", Context.MODE_PRIVATE)
        editor = sharedPref.edit()
        setViews()
    }

    fun takeDetails() : Boolean {
        val valid = false
        name = binding.name.text.toString().trim()
        state = binding.state.text.toString().trim()
        city  = binding.city.text.toString().trim()

        if (name != ""){
            if (state != ""){
                if (city != ""){
                    binding.nameInp.error = ""
                    binding.cityInp.error = ""
                    binding.stateInp.error = ""
                    return true
                }else{
                    binding.cityInp.error = "City cant be empty"
                    return  false
                }
            }else {
                binding.stateInp.error = "State cant be empty"
                return false
            }
        }else {
            binding.nameInp.error = "Name cant be empty"
            return false
        }

    }

    fun setViews() : Unit {

        binding.begin.setOnClickListener{


            if (takeDetails()){

                editor.putString("username",name)
                editor.putString("state",state)
                editor.putString("city",city)
                editor.commit()
                startActivity(Intent(this, TripDetails::class.java))
            }else {
                Toast.makeText(this,"Please fill form", Toast.LENGTH_SHORT).show()
            }



        }

    }
}