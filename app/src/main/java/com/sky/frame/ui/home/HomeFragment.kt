package com.sky.frame.ui.home

import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.sky.frame.R
import com.sky.frame.base.BaseFragment
import com.sky.frame.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val homeViewModel: HomeViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun getLayout(): Int {
        return R.layout.fragment_home
    }

    override fun initView() {
        _binding = mBinding
        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
    }

    override fun initData() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}