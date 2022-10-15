 package com.ncs.awdoor.ProgressiveTextLayout

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ncs.awdoor.R

 class ProgressiveTextFragment : Fragment() {

    companion object {
        fun newInstance() = ProgressiveTextFragment()
    }

    private lateinit var viewModel: ProgressiveTextViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_progressive_text, container, false)
    }





//     override fun onAttach(context: Context) {
//         super.onAttach(context)
//         viewModel = ViewModelProvider(this).get(ProgressiveTextViewModel::class.java)
//
//     }

}