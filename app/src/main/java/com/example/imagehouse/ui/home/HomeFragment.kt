package com.example.imagehouse.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.imagehouse.databinding.FragmentHomeBinding
import com.example.imagehouse.ui.EndlessRecyclerWithHeaderOnScrollListener
import com.example.imagehouse.ui.PhotosAdapter
import com.example.imagehouse.ui.WrapContentLinearLayoutManager

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val scrollListener = object : EndlessRecyclerWithHeaderOnScrollListener() {
        override fun onLoadMore() {
            onFetchMore()
        }
    }

    private fun onFetchMore() {
        if (homeViewModel._init.totalCount ?: 0 > homeViewModel.page * 10) {
            scrollListener.setLoading()
            homeViewModel.page++
            homeViewModel.searchImages(binding.searchBox.text, false)
        }
    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.appName
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        val editText: EditText = binding.searchBox
        editText.setOnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds()
                        .width())
                ) {
                    // your action here
                    homeViewModel.searchImages(editText.text, true)
                    true
                }
            }
            false
        }
        val adapter = PhotosAdapter()
        binding.rvResults.adapter = adapter
        val wrapContentLinearLayoutManager = WrapContentLinearLayoutManager(context, LinearLayout.VERTICAL, false)
        binding.rvResults.layoutManager = wrapContentLinearLayoutManager
        scrollListener.mLinearLayoutManager = wrapContentLinearLayoutManager
        scrollListener.setLoading()
        binding.rvResults.addOnScrollListener(scrollListener)
        homeViewModel.updateUi.observe(viewLifecycleOwner, {
            if (it.totalCount?:0 > 0){
                if (homeViewModel.page > 1) {
                    scrollListener.setLoaded()
                }
                adapter.list = it.photos
                adapter.notifyDataSetChanged()
                binding.userInfo.text = "Total results - ${it.totalCount}"
            } else {
                binding.userInfo.text = "No results found"
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}