package com.mohamed.prayerapp.ui.prayer

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.app.base.BaseFragment
import com.app.data.model.pray_times.PrayerTimes
import com.mohamed.prayerapp.R
import com.mohamed.prayerapp.databinding.FragmentPrayersTimeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrayersTimeFragment : BaseFragment(R.layout.fragment_prayers_time) {

    // binding nullable
    private var _binding: FragmentPrayersTimeBinding? = null
    private val binding get() = _binding!!

    // view model by viewmodels
    private val viewModel: PrayersTimeViewModel by activityViewModels()


    // adapter
    private val viewpagerAdaper by lazy {
        PrayerPageAdapter(
            onNextPress = ::goNextPage,
            onPrevPress = ::goPrevPage
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPrayersTimeBinding.bind(view)

        setupRecyclerView()

        setupFlow()


        setupActions()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // setupRecyclerView
    private fun setupRecyclerView() {
        binding.viewPager.adapter = viewpagerAdaper
    }

    // setupFlow
    private fun setupFlow() {
        handleSharedFlow(viewModel.prayersFlow, onSuccess = {
            it as List<PrayerTimes>
            viewpagerAdaper.addData(it as List<PrayerTimes>)
        })
    }


    private fun goNextPage() {
        if (binding.viewPager.currentItem == viewpagerAdaper.itemCount - 1) return
        binding.viewPager.currentItem = binding.viewPager.currentItem + 1
    }

    private fun goPrevPage() {
        if (binding.viewPager.currentItem == 0) return
        binding.viewPager.currentItem = binding.viewPager.currentItem - 1
    }

    private fun setupActions() {
        binding.btnShowQiblaDirection.setOnClickListener { findNavController().navigate(R.id.action_prayersTimeFragment_to_qiblaFragment) }
    }

}