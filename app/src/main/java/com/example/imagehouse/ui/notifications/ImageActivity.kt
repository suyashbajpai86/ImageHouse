package com.example.imagehouse.ui.notifications

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.imagehouse.ImageHouseApplication
import com.example.imagehouse.databinding.ActivityImageBinding
import com.example.imagehouse.ui.model.PhotoUiModel

class ImageActivity : AppCompatActivity() {

    private lateinit var imageViewModel: ImageViewModel
    // This property is only valid between onCreateView and
    // onDestroyView.
    private lateinit var binding: ActivityImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        imageViewModel = ViewModelProvider(this).get(ImageViewModel::class.java)
        val url = intent?.extras?.getString("URL")
        Glide.with(baseContext).load(url).into(binding.image)
        supportActionBar?.hide()
        binding.favourite.setOnClickListener {
            (application as? ImageHouseApplication)?.favourites?.add(PhotoUiModel(url!!))
            binding.favourite.isClickable = false
        }
    }
}