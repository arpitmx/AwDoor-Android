 package com.ncs.awdoor.ProgressiveTextLayout

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.MaterialDatePicker
import com.ncs.awdoor.FinalActivity
import com.ncs.awdoor.R
import com.ncs.awdoor.databinding.FragmentProgressiveTextBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

 class ProgressiveTextFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() = ProgressiveTextFragment()
    }

    private lateinit var viewModel: ProgressiveTextViewModel
    private var _binding : FragmentProgressiveTextBinding? = null
    private val binding get()  = _binding!!
    lateinit var arrayAdapter :ArrayAdapter<String?>

     var date: String = "22/10/2022"
      var destination : String = "Kasol"
      var start : String = "Delhi"
     lateinit var pref : SharedPreferences
      var nodays : String = "3"

     // TODO: Change thses
     var tripType : Int = 1
     var transport : Int = 2
     var willing : Int = -1
     var stars : Int = 2
     var noOfpeople : String = "2"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProgressiveTextBinding.inflate(inflater,container,false)
        binding.nextintent.setOnClickListener(this)


        pref = requireContext().getSharedPreferences("UserDetailPref", Context.MODE_PRIVATE)
        start = pref.getString("city",null).toString()

        val languages = resources.getStringArray(R.array.triptype)
        arrayAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item , languages)
        val autocompleteTV =binding.autoCompleteTextView
        autocompleteTV.setAdapter(arrayAdapter)
        autocompleteTV.setOnItemClickListener { adapterView, view, i, l ->
            tripType = l.toInt()
            binding.autoCompleteTextView.error = null

            if (tripType == 1){
                binding.detail45.visibility = View.VISIBLE
            }else {
                binding.detail45.visibility = View.GONE
                tripType = 0
            }
        }


        val transports = resources.getStringArray(R.array.transport)
        val transportAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item , transports)
        val transportView =binding.transportView
        transportView.setAdapter(transportAdapter)
        transportView.setOnItemClickListener{
                adapterView, view, i, l ->
            transport = l.toInt()
            binding.transportView.error = null


        }


        val will = resources.getStringArray(R.array.will)
        val willAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item , will)
        val willView =binding.hotelwill
        willView.setAdapter(willAdapter)
        willView.setOnItemClickListener{
            adapterView, view , i, l ->
            binding.hotelwill.error = null
            willing = l.toInt()

            if (willing == 0){
                binding.detail6.visibility = View.VISIBLE
            }else {
                binding.detail6.visibility = View.GONE
                willing = 1
            }


        }

        val star = resources.getStringArray(R.array.stars)
        val starAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item , star)
        val starView =binding.hotelstars
        starView.setAdapter(starAdapter)

        starView.setOnItemClickListener{
                adapterView, view, i, l ->
                stars = l.toInt()
        }


        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        binding.dateInp.setOnClickListener{
            datePicker.show(requireActivity().supportFragmentManager, "ewf")
            datePicker.addOnPositiveButtonClickListener{

//                val timeZoneUTC: TimeZone = TimeZone.getDefault()
//
//                val offsetFromUTC: Int = timeZoneUTC.getOffset(Date().getTime()) * -1
//
//
//                val simpleFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
//                val date = Date(it + offsetFromUTC)
//
//
//                val text =

                date= outputDateFormat.format(it).toString()
                Toast.makeText(requireContext(),date,Toast.LENGTH_SHORT).show()
                binding.dateInp.text = date


            }
        }

        return binding.root
    }

     private val outputDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
         timeZone = TimeZone.getTimeZone("UTC")
     }


     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
         super.onViewCreated(view, savedInstanceState)

        binding.next.setOnClickListener{
            var tempDestination = binding.destination.text.toString().trim()
            if(!tempDestination.equals("")){

                binding.detail2.visibility = View.VISIBLE
                binding.detail1.alpha = 0.5f
                destination = tempDestination

                binding.b.p1.setImageResource(R.drawable.ic_se)
                binding.b.l1.setBackgroundColor(resources.getColor(R.color.primary))
                binding.b.p2.setImageResource(R.drawable.ic_do)

                if (!binding.noofdays.toString().trim().equals("") && !binding.dateInp.text.toString().trim().equals("") ){

                    binding.detail3.visibility = View.VISIBLE
                    binding.detail2.alpha = 0.5f
                    nodays= binding.noofdays.toString().trim()

                    binding.b.p2.setImageResource(R.drawable.ic_se)
                    binding.b.l2.setBackgroundColor(resources.getColor(R.color.primary))
                    binding.b.p3.setImageResource(R.drawable.ic_do)



                    handleGroupType()

                }else {
                    Toast.makeText(requireContext(),"Some field still left", Toast.LENGTH_SHORT).show()
                }


            }else {
                binding.destination.error = "Empty"
            }
        }



     }



     fun handleGroupType(){
         if(tripType!=-1){
             binding.autoCompleteTextView.error = null
             if (tripType == 1 && !binding.noofpeople.text.toString().trim().equals("")){
                 noOfpeople = binding.noofpeople.text.toString().trim()
                 showTransport(tripType)
             }else {
                 showTransport(tripType)
             }


         }else {
             Toast.makeText(requireContext(),"Select Solo or Group trip", Toast.LENGTH_SHORT).show()
         }
     }



     fun showTransport(trip : Int){
         binding.b.p3.setImageResource(R.drawable.ic_se)
         binding.b.l3.setBackgroundColor(resources.getColor(R.color.primary))
         binding.b.p4.setImageResource(R.drawable.ic_do)
         binding.detail3.alpha = 0.5f
         if (trip == 1){
            binding.detail45.alpha = 0.5f
        }
         binding.detail4.visibility = View.VISIBLE

         if (transport != -1){
             showHotel()
         }else {
             Toast.makeText(requireContext(),"Select transport", Toast.LENGTH_SHORT).show()
         }

     }

     fun showHotel(){
         binding.b.p4.setImageResource(R.drawable.ic_se)
         binding.b.l4.setBackgroundColor(resources.getColor(R.color.primary))
         binding.b.p5.setImageResource(R.drawable.ic_do)
         binding.detail4.alpha = 0.5f
         binding.detail5.visibility = View.VISIBLE
         if (willing!= -1){
                if (willing == 0){
                    showStars()
                }else {
                    binding.detail5.alpha = 0.5f
                    binding.detail6.alpha = 0.5f
                    binding.b.p5.setImageResource(R.drawable.ic_se)
                    next()
                }
         }else {
             Toast.makeText(requireContext(),"Will you stay in hotel ? ", Toast.LENGTH_SHORT).show()
         }
     }
     fun showStars(){
         binding.detail6.visibility =View.VISIBLE

         if (stars!=-1){
             binding.detail5.alpha = 0.5f
             binding.detail6.alpha = 0.5f
             binding.b.p5.setImageResource(R.drawable.ic_se)
             next()
         }else {
             Toast.makeText(requireContext(),"Hotel stars ? ", Toast.LENGTH_SHORT).show()
         }

     }

     fun next(){
         binding.next.visibility = View.GONE
         binding.nextintent.visibility = View.VISIBLE

     }

     override fun onAttach(context: Context) {
         super.onAttach(context)
         viewModel = ViewModelProvider(this).get(ProgressiveTextViewModel::class.java)
     }

     override fun onClick(p0: View?) {
         var i = Intent(context, FinalActivity::class.java )
         i.putExtra("tripType",tripType)
         i.putExtra("start",start)
         i.putExtra("dest",destination)
         i.putExtra("date",date)
         i.putExtra("people",noOfpeople)
         i.putExtra("days",nodays)
         i.putExtra("transp",transport)
         i.putExtra("stars",stars)
         startActivity(i)

     }

 }