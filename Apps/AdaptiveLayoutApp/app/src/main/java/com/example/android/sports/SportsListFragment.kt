/*
 * Copyright (c) 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.sports

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import com.example.android.sports.databinding.FragmentSportsListBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */

class SportsListFragment : Fragment() {

    private val sportsViewModel: SportsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle? ): View? {  return FragmentSportsListBinding.inflate(inflater, container, false).root }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSportsListBinding.bind(view)

        val slidingPaneLayout = binding.slidingPaneLayout
        // lock the gesture navigation on Sliding Pane Layout to Prevent Left-Right Drag option
        slidingPaneLayout.lockMode = SlidingPaneLayout.LOCK_MODE_LOCKED

        // register the back-pressed callback custom class
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, SportsListOnBackPressedCallback(slidingPaneLayout))

        // Initialize the adapter and set it to the RecyclerView.
        val adapter = SportsAdapter {
            // Update the user selected sport as the current sport in the shared viewmodel
            // This will automatically update the dual pane content
            sportsViewModel.updateCurrentSport(it)
            // Navigate to the details screen
            // val action = SportsListFragmentDirections.actionSportsListFragmentToNewsFragment()
            // this.findNavController().navigate(action)

            // Navigate using SlidingPanelLayout
            binding.slidingPaneLayout.openPane()
        }
        binding.recyclerView.adapter = adapter
        adapter.submitList(sportsViewModel.sportsData)
    }
}

class SportsListOnBackPressedCallback (private val slidingPaneLayout: SlidingPaneLayout ): OnBackPressedCallback(slidingPaneLayout.isSlideable && slidingPaneLayout.isOpen), SlidingPaneLayout.PanelSlideListener {



    override fun handleOnBackPressed() {
        Log.d("Backpressure Callback", "handleOnBackPressed: ")

        slidingPaneLayout.closePane()
    }

    override fun onPanelSlide(panel: View, slideOffset: Float) {
        Log.d("ListenerSlidingEvents", "onPanelSlide: its sliding")
    }

    override fun onPanelOpened(panel: View) {
        this.isEnabled = true
    }

    override fun onPanelClosed(panel: View) {
        this.isEnabled = false
    }

    init {
        Log.d("SportsListOnBackPressed", "Init: ")
        slidingPaneLayout.addPanelSlideListener(this)
    }

}