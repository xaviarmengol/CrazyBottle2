package com.example.crazybottle2

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.crazybottle2.databinding.FragmentOptionsBinding

class OptionsFragment : Fragment() {

    lateinit var binding: FragmentOptionsBinding
    lateinit var viewModel: OptionsViewModel

    lateinit var sharedViewModel: MainActivity.SharedViewModel

    // Inflate segment and declare listeners
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentOptionsBinding.inflate(inflater, container, false)
        binding.segmentsEditText.addTextChangedListener(textWatcher)
        binding.actionsEditText.addTextChangedListener(textActionsWatcher)

        viewModel = ViewModelProvider(this)[OptionsViewModel::class.java]

        return binding.root
    }

    // Connect to model (data interchange)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel = ViewModelProvider(requireActivity())[MainActivity.SharedViewModel::class.java]
        binding.segmentsEditText.text = sharedViewModel.writeSegmentsText()
        binding.actionsEditText.text = sharedViewModel.writeActionsText()
    }

    private val textWatcher = object : TextWatcher {
        // All methods should be implemented
        override fun afterTextChanged(s: Editable?) = sharedViewModel.readSegmentsFromText(s)
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    private val textActionsWatcher = object : TextWatcher {
        // All methods should be implemented
        override fun afterTextChanged(s: Editable?) = sharedViewModel.readActionsFromText(s)
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

}