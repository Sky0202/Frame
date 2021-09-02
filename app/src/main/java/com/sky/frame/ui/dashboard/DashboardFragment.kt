package com.sky.frame.ui.dashboard

import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.sky.frame.R
import com.sky.frame.base.BaseFragment
import com.sky.frame.databinding.FragmentDashboardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : BaseFragment<FragmentDashboardBinding>() {

    private val dashboardViewModel: DashboardViewModel by viewModels()
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun getLayout(): Int {
        return R.layout.fragment_dashboard
    }

    override fun initView() {
        _binding = mBinding
        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
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