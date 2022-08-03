package com.sample.nsfw

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import com.content.nsfw.Analyzer
import com.sample.nsfw.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var analyzer: Analyzer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        analyzer = Analyzer((this))
        initListeners()
    }

    private fun initListeners() {
        binding.fabAdd.setOnClickListener {
            openGallery()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, 200)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 200) {
                val uri = data?.data
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                binding.ivMain.setImageBitmap(bitmap)
                classifyImage(bitmap)
            }
        }
    }

    private fun classifyImage(bitmap: Bitmap?) {
        if (::analyzer.isInitialized) {
            bitmap?.let {
                analyzer.classify(it) { result ->
                    binding.tvResult.text = result.name
                }
            }
        }
    }
}