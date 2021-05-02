package com.originalstocks.thenewsapp.ui.fragments

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.model.KeyPath
import com.google.android.material.snackbar.Snackbar
import com.originalstocks.thenewsapp.R
import com.originalstocks.thenewsapp.data.model.NewsHeadlineModel
import com.originalstocks.thenewsapp.databinding.FragmentHomeBinding
import com.originalstocks.thenewsapp.ui.adapters.ItemClickListener
import com.originalstocks.thenewsapp.ui.adapters.NewsPagingAdapter
import com.originalstocks.thenewsapp.ui.viewModels.NewsViewModel
import com.originalstocks.thenewsapp.utils.ConnectionLiveData
import com.originalstocks.thenewsapp.utils.showSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "HomeFragment"
@AndroidEntryPoint
class HomeFragment : Fragment(), ItemClickListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var newsPagingDataAdapter: NewsPagingAdapter
    private val newsViewModel by viewModels<NewsViewModel>()
    private lateinit var connectionLiveData: ConnectionLiveData
    private var isNetworkAvailable: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater)
        binding.loadingAnimationView.addValueCallback(
            KeyPath("**"),
            LottieProperty.COLOR_FILTER,
            { PorterDuffColorFilter(requireContext().getColor(R.color.design_default_color_primary), PorterDuff.Mode.SRC_ATOP) }
        )
        return binding.root
    }

    @ExperimentalPagingApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        connectionLiveData = ConnectionLiveData(requireContext())
        super.onViewCreated(view, savedInstanceState)

        newsPagingDataAdapter = NewsPagingAdapter(this)
        connectionLiveData.observe(viewLifecycleOwner, Observer { status ->
            isNetworkAvailable = status
            if (isNetworkAvailable) {
                Log.i(TAG, "onCreate: isNetworkAvailable $isNetworkAvailable")
            } else {
                Log.e(TAG, "onCreate: isNetworkAvailable : $isNetworkAvailable")
                showSnackbar(binding.root, "No Internet Connection available...", 2)
            }
        })

        viewLifecycleOwner.lifecycleScope.launch {
            newsViewModel.pager.collectLatest { data ->
                newsPagingDataAdapter.submitData(data)
            }
        }

        newsPagingDataAdapter.addLoadStateListener { state ->
            when (state.refresh) {
                is LoadState.Loading -> {
                    //will show progress
                    showProgress(true)
                    Log.i(TAG, "onCreate_LoadState Loading")
                }
                is LoadState.Error -> {
                    //will Hide progress
                    showProgress(false)
                    Log.e(TAG, "onCreate_LoadState Error")
                }
                is LoadState.NotLoading -> {
                    //will Hide progress
                    showProgress(false)
                    Log.i(TAG, "onCreate_LoadState Success")
                }
            }
        }

        binding.newsRecyclerView.adapter = newsPagingDataAdapter
        /* setting up State Restoration Policy to save scroll state */
        newsPagingDataAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

    }

    private fun showProgress(isLoading: Boolean) {
        if (isLoading) {
            binding.progressView.visibility = View.VISIBLE
            binding.loadingAnimationView.playAnimation()
        } else {
            binding.progressView.visibility = View.GONE
            binding.loadingAnimationView.pauseAnimation()
        }
    }

    override fun fetchItem(data: NewsHeadlineModel) {
        val bundle = Bundle()
        bundle.putParcelable("data_model", data)
        findNavController().navigate(R.id.action_homeFragment_to_detailedNewsFragment, bundle)
    }

}