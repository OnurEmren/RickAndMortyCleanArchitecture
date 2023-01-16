package com.onuremren.breakingbadapp.view.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.onuremren.breakingbadapp.R
import com.onuremren.breakingbadapp.core.base.BaseFragment
import com.onuremren.breakingbadapp.core.util.getTextButtonChecked
import com.onuremren.breakingbadapp.core.util.getTextChipChecked
import com.onuremren.breakingbadapp.core.util.setButtonChecked
import com.onuremren.breakingbadapp.core.util.setChipChecked
import com.onuremren.breakingbadapp.databinding.FragmentSearchBinding
import com.onuremren.breakingbadapp.view.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SearchFragment @Inject constructor(): BottomSheetDialogFragment() {

    private lateinit var binding : FragmentSearchBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_search,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.filterValue.observe(viewLifecycleOwner, Observer {
            binding.status.setChipChecked(it[0])
            binding.radiogroupGender.setButtonChecked(it[1])
        })


        binding.btnMakeFilter.setOnClickListener {
            if(binding.status.getTextChipChecked().isNotEmpty() && binding.radiogroupGender.getTextButtonChecked().isNotEmpty()) {
                viewModel.getByStatusAndGender(binding.status.getTextChipChecked(), binding.radiogroupGender.getTextButtonChecked(), 1)
            }else{
                if(binding.status.getTextChipChecked().isNotEmpty()){
                    viewModel.getByStatus(binding.status.getTextChipChecked(), 1)
                }else{
                    viewModel.getByGender(binding.radiogroupGender.getTextButtonChecked(), 1)
                }
            }

            viewModel.filterValue.value = arrayOf(binding.status.checkedChipId, binding.radiogroupGender.checkedRadioButtonId)

            findNavController().popBackStack(R.id.homeFragment, false)
        }
    }
}