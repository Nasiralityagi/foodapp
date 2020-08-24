package com.android.tlb.home.ui.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.tlb.R
import com.android.tlb.databinding.FragmentDiscoverBinding
import com.android.tlb.factory.ViewModelFactory

class DiscoverFragment : Fragment() {

    private lateinit var viewModel: DiscoverViewModel
    private lateinit var binding: FragmentDiscoverBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_discover, container, false)
        // With ViewModelFactory
        viewModel = ViewModelProvider(this@DiscoverFragment, ViewModelFactory()).get(
            DiscoverViewModel::class.java
        )

        binding.viewModel = viewModel

        return binding.root
    }
}