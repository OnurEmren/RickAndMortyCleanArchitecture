package com.onuremren.breakingbadapp.view.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.onuremren.breakingbadapp.R
import com.onuremren.breakingbadapp.core.base.BaseFragment
import com.onuremren.breakingbadapp.core.util.loadWithGlide
import com.onuremren.breakingbadapp.databinding.FragmentDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class DetailFragment @Inject constructor(

) : Fragment(R.layout.fragment_detail) {
    private var characterId = 0
    private lateinit var dataBinding: com.onuremren.breakingbadapp.databinding.FragmentDetailBinding
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        return dataBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val characters = args.info

        dataBinding.apply {
            dataBinding.selectedChar = characters
        }
        dataBinding.characterId.text = characters.id.toString()
        dataBinding.episodeTv.text = characters.episode.size.toString()

        Glide.with(this).load(characters.image).into(dataBinding.imgCharacter)


    }


}