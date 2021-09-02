package com.sky.frame.ui.notifications

import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.sky.frame.R
import com.sky.frame.base.BaseFragment
import com.sky.frame.databinding.FragmentNotificationsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationsFragment : BaseFragment<FragmentNotificationsBinding>() {

    private val notificationsViewModel: NotificationsViewModel by viewModels()
    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    override fun getLayout(): Int {
        return R.layout.fragment_notifications
    }

    override fun initView() {
        _binding = mBinding
        val textView: TextView = binding.textNotifications
        notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
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