package com.originalstocks.thenewsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.originalstocks.thenewsapp.R
import com.originalstocks.thenewsapp.data.model.NewsHeadlineModel
import com.originalstocks.thenewsapp.databinding.FragmentDetailedNewsBinding

private const val TAG = "DetailedNewsFragment"

class DetailedNewsFragment : Fragment() {

    private lateinit var binding: FragmentDetailedNewsBinding
    private lateinit var data: NewsHeadlineModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDetailedNewsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.toolbar.navigationIcon = requireContext().getDrawable(R.drawable.ic_baseline_arrow_back_ios_24)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        super.onViewCreated(view, savedInstanceState)

        data = arguments?.getParcelable<NewsHeadlineModel>("data_model")!!
        Log.i(TAG, "onViewCreated_data = ${data.id}")

        binding.toolbarLayout.title = data.author
        Glide.with(view).load(data.urlToImage).into(binding.newsImageView)
        binding.descriptionTextView.text = data.description
        binding.contentTextView.text = data.content
    }

}