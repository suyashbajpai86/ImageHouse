package com.example.imagehouse.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.imagehouse.R
import com.example.imagehouse.databinding.FragmentHomeBinding
import com.example.imagehouse.ui.PhotosAdapter

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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
        editText.doOnTextChanged { text, start, before, count ->
            homeViewModel.onSearchTextChange(text)
        }
        val adapter = PhotosAdapter()
        binding.rvResults.adapter = adapter
        homeViewModel.updateUi.observe(viewLifecycleOwner, {
            adapter.list = it.photos
            adapter.notifyDataSetChanged()
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}