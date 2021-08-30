package com.example.crazybottle2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.crazybottle2.databinding.FragmentAboutBinding

lateinit var binding: FragmentAboutBinding

class AboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAboutBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

}