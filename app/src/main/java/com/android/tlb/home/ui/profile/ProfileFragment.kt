package com.android.tlb.home.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.tlb.R
import com.android.tlb.databinding.FragmentProfileBinding
import com.android.tlb.factory.ViewModelFactory


class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        // With ViewModelFactory
        viewModel = ViewModelProvider(this@ProfileFragment, ViewModelFactory()).get(
            ProfileViewModel::class.java
        )
        return binding.root
    }

}
