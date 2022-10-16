package com.ncs.awdoor

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
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
import java.util.*


class FinalActivity : AppCompatActivity() {

    lateinit var binding : ActivityFinalBinding
    lateinit var anim : Animation


    lateinit var tripMode: String
    lateinit var transport: String
    lateinit var start : String
    lateinit var dest : String
    lateinit var date : String
    lateinit var people : String
    lateinit var days : String
    lateinit var i : Intent
    lateinit var stars : String
    var cheapMode : Boolean = true
    var totalCFare : Int = 0
    var totalExFare : Int = 0


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

    }

    fun setData(){

        var tripTemp = i.getStringExtra("tripMode").toString()
        start = i.getStringExtra("start").toString()
        dest = i.getStringExtra("dest").toString()
        date = i.getStringExtra("date").toString()
        people = i.getStringExtra("people").toString()
        days = i.getStringExtra("days").toString()
        stars = i.getIntExtra("stars", -1).toString()
        var transtemp = i.getIntExtra("transp",-1).toString()

        if (transtemp.equals("0"))    {
            transport = "bus"
        }else if (transtemp.equals("1")){
            transport = "flight"
        }else if (transtemp.equals("2")){
            transport = "train"
        }

        if (tripTemp.equals("0")){
            tripMode = "Solo"
        }else {
            tripMode = "Group"
        }



        Log.d("TAG", "setData: tripmode"+start+" "+date+" "+transtemp)


      setCardData(dest,start,days,tripMode,transport)
        fetchData(transport,start,dest.lowercase(),date,stars)

        binding.de.hotelStars.text = "Hotel Type :"+stars+" Star"



    }

    fun setCardData(dest: String, start: String, day: String , tripType: String , transport : String){
        binding.destination.text = dest
        binding.from.text = "$start-$dest"
        binding.days.text = "$day day"
        binding.tripType.text = "$tripType trip"
        binding.transport.text = "by $transport"
    }

    fun setLoading(url:String){
        binding.loadingScreen.visibility = View.VISIBLE
        Glide.with(this)
            .asGif()
            .load(url)
            .into(binding.gif)
    }

    fun setCardImage(url : String){
        Glide.with(this)
            .asBitmap()
            .load(url)
            .into(binding.locImg)
    }

    lateinit var cheapData : JSONObject
    lateinit var expensiveData : JSONObject


    fun fetchData( mode: String, src: String, dest : String , date : String, stars:String ){

        val requestQueue = Volley.newRequestQueue(this)
        val url = "https://young-bastion-57330.herokuapp.com/fare?mode=${mode}&src=${src}&dest=${dest}&date=${date}&stars=${stars}"

        Log.d("Log", "URL "+url)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->


                Handler(Looper.getMainLooper()).postDelayed({
                    binding.loadingScreen.startAnimation(anim)
                    removeLoading()
                },200)

                    var link = response.getJSONObject("tripDetail")
                    var cheap = link.getJSONObject("cheap")
                    var expensive = link.getJSONObject("expensive")
                var hotelRate = link.getJSONObject("hotelData").getString("price").toInt()


                cheapData = cheap
                expensiveData = expensive


                setCardImage(response.getString("destinationImage"))
                setDataInViews(cheap,expensive,link.getString("link"), hotelRate)

                Log.d("Log", " Response : "+response+"\n"+"Cheap: "+cheap+" n rate :"+hotelRate)

            },
            { error ->
                Toast.makeText(this,"volley-error"+error.toString(),Toast.LENGTH_SHORT).show()
                Log.d("volley-error",error.toString())
            }
        )

        requestQueue.add(jsonObjectRequest)
    }

    fun setBudgetBreakdown(transport:String, stay : String, food: String, total:Int, people: String){
        binding.travelExpense.text = "₹$transport"
        binding.hotelExpense.text = "₹$stay"
        binding.foodExpense.text = "₹$food"
        binding.peopleExpense.text = "x$people Person"
        binding.totalExpense.text = "₹ $total"

    }



    fun setSelectedBudget(budget : Int){
        cheapMode = budget == 1
    }

    @SuppressLint("SetTextI18n")
    fun setDataInViews(cheap : JSONObject, expensive: JSONObject, url : String, hotelprice : Int){


        binding.hotelExpense.text = hotelprice.toString()
        var food = 0

        if (transport=="flight"){



            // Cheap
            var fareC: Int = cheap.getString("price").toInt()
            var nameC = cheap.getString("name").toString()
            var depC = cheap.getString("dept").toString()

            food = 1000


            var totalCheapFare = 0
            totalCheapFare = fareC + hotelprice * days.toInt() + food
            totalCheapFare *= people.toInt()

            var cheapData = DataHolder.Flight(fareC, depC, nameC, totalCheapFare)
            binding.lowBudget.text = "₹"+totalCheapFare.toString()
            totalCFare = totalCheapFare



            //Expensive

            var totalEx: Int = 0
            var fareEx: Int = expensive.getString("price").toInt()
            var nameEx = expensive.get("name").toString()
            var depEx = expensive.get("dept").toString()

            food = 1500
            totalEx = fareEx + hotelprice * days.toInt()
            totalEx *= people.toInt()


            var expensiveData = DataHolder.Flight(fareEx, depEx, nameEx, totalEx)
            binding.highBudget.text = "₹"+totalEx.toString()
            totalExFare = totalEx

            binding.de.flightTypeCard.visibility = View.VISIBLE
            binding.de.flightBookBtn.setOnClickListener{
                val uri: Uri = Uri.parse(url)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)

            }



            if (cheapMode){
                setBudgetBreakdown(fareC.toString(),hotelprice.toString(),food.toString(), totalCheapFare ,people)
                binding.de.flightName.text = nameC
                binding.de.flightprice.text = fareC.toString()
                binding.de.depDateFlight.text = depC

            }else {
                setBudgetBreakdown(fareEx.toString(),hotelprice.toString(),food.toString() ,totalEx, people)
                binding.de.flightName.text = nameEx
                binding.de.flightprice.text = fareEx.toString()
                binding.de.depDateFlight.text = depEx

            }




        }else if (transport=="train"){

            // Cheap
            var fareC: Int = cheap.getString("price").toInt()
            var nameC = cheap.getString("name").toString()
            var trainNumberC = cheap.getString("trainNumber").toString()
            var boardStationC = cheap.getString("boardStation").toString()
            var deboardStationC = cheap.getString("deboardStation").toString()
            var departDateC = cheap.getString("departDate").toString()
            var arrivalDateC = cheap.getString("arrivalDate").toString()
            var departTimeC = cheap.getString("departTime").toString()
            var arrivalTimeC = cheap.getString("arrivalTime").toString()


            food = 1000


            var totalCheapFare = 0
            totalCheapFare = fareC + hotelprice * days.toInt() + food
            totalCheapFare *= people.toInt()

          //  var cheapData = DataHolder.Flight(fareC, depC, nameC, totalCheapFare)
            binding.lowBudget.text = "₹"+totalCheapFare.toString()
            totalCFare = totalCheapFare



            //Expensive

            var totalEx: Int = 0
            var fareEx: Int = expensive.getString("price").toInt()
            var nameEx =expensive.getString("name").toString()
            var trainNumberEx = expensive.getString("trainNumber").toString()
            var boardStationE = expensive.getString("boardStation").toString()
            var deboardStationex=expensive.getString("deboardStation").toString()
            var departDateex =  expensive.getString("departDate").toString()
            var arrivalDateex = expensive.getString("arrivalDate").toString()
            var departTimeex =  expensive.getString("departTime").toString()
            var arrivalTimeex = expensive.getString("arrivalTime").toString()

            food = 1500
            totalEx = fareEx + hotelprice * days.toInt()
            totalEx *= people.toInt()


           // var expensiveData = DataHolder.Flight(fareEx, depEx, nameEx, totalEx)
            binding.highBudget.text = "₹$totalEx"
            totalExFare = totalEx

            binding.de.trainTypeCard.visibility = View.VISIBLE
            binding.de.flightBookBtn.setOnClickListener{
                val uri: Uri = Uri.parse(url)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)

            }



            if (cheapMode){
                setBudgetBreakdown(fareC.toString(),hotelprice.toString(),food.toString(), totalCheapFare ,people)
                binding.de.trainPrice.text = "Rate: $fareC"
                binding.de.trainName.text = "Train Name: $nameC"
                binding.de.trainNumber.text = "Train Number: $trainNumberC"
                binding.de.boardingStationTrain.text= "Boarding Station :$boardStationC"
                binding.de.deboardStationTrain.text = "Deboarding Station :$deboardStationC"
                binding.de.departTime.text = "Depart time: $departTimeC"
                binding.de.departDate.text = "Depart Date: $departDateC"
                binding.de.arrivalTime.text = "Arrival time: $arrivalTimeC"
                binding.de.arrivalDate.text = "Arrival Date: $arrivalDateC"

            }else {
                setBudgetBreakdown(fareEx.toString(),hotelprice.toString(),food.toString() ,totalEx, people)
                binding.de.trainPrice.text = "Rate: " +  binding.de.trainPrice.text.toString() + fareEx.toString()
                binding.de.trainName.text = "Train Name: $nameEx"
                binding.de.trainNumber.text = "Train Number: $trainNumberEx"
                binding.de.boardingStationTrain.text = "Boarding Station :$boardStationE"
                binding.de.deboardStationTrain.text = "Deboarding Station :$deboardStationex"
                binding.de.departTime.text = "Depart time: $departTimeex"
                binding.de.departDate.text = "Depart Date: $departDateex"
                binding.de.arrivalTime.text = "Arrival time: $arrivalTimeex"
                binding.de.arrivalDate.text = "Arrival Date: $arrivalDateex"

            }


        }

        else {

            // Cheap

            var fareC: Int = cheap.getString("price").toInt()
            var companyC = cheap.getString("company").toString()
            var busSpeC = cheap.getString("bus").toString()
            var boardingStationC = cheap.getString("BoardingStation").toString()
            var endStationC = cheap.getString("EndStation").toString()



            food = 1000

            var totalCheapFare = 0
            totalCheapFare = fareC + hotelprice * days.toInt() + food
            totalCheapFare *= people.toInt()

            //  var cheapData = DataHolder.Flight(fareC, depC, nameC, totalCheapFare)
            binding.lowBudget.text = "₹"+totalCheapFare.toString()
            totalCFare = totalCheapFare



            //Expensive

            var totalEx: Int = 0
            var fareex: Int = expensive.getString("price").toInt()
            var companyex = expensive.getString("company").toString()
            var busSpeex = expensive.getString("bus").toString()
            var boardingStationex = expensive.getString("BoardingStation").toString()
            var endStationex = expensive.getString("EndStation").toString()


            food = 1500
            totalEx = fareex + hotelprice * days.toInt()
            totalEx *= people.toInt()


            // var expensiveData = DataHolder.Flight(fareEx, depEx, nameEx, totalEx)
            binding.highBudget.text = "₹$totalEx"
            totalExFare = totalEx

            binding.de.trainTypeCard.visibility = View.VISIBLE
            binding.de.flightBookBtn.setOnClickListener{
                val uri: Uri = Uri.parse(url)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)

            }

            binding.de.busTypeCard.visibility = View.VISIBLE

            if (cheapMode){
                setBudgetBreakdown(fareC.toString(),hotelprice.toString(),food.toString(), totalCheapFare ,people)
                binding.de.busCompany.text = "Bus Service Provider : $companyC"
                binding.de.busSpecs.text = "Seat specification : $busSpeC"
                binding.de.boadingStationBus.text = "Boarding station: $boardingStationC"
                binding.de.endStationBus.text= "Terminating Station :$endStationC"
                binding.de.priceBus.text = "Bus rate :$fareC"


            }else {
                setBudgetBreakdown(fareex.toString(),hotelprice.toString(),food.toString() ,totalEx, people)
                binding.de.busCompany.text = "Bus Service Provider : $companyex"
                binding.de.busSpecs.text = "Seat specification : $busSpeex"
                binding.de.boadingStationBus.text = "Boarding station: $boardingStationex"
                binding.de.endStationBus.text= "Terminating Station :$endStationex"
                binding.de.priceBus.text = "Bus rate :$fareex"

            }


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