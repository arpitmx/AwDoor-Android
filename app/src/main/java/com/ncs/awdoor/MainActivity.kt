package com.ncs.awdoor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import com.ncs.awdoor.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding


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

        binding.next.setOnClickListener {
            actionSearchFrag(binding.next)
       }


    }
}