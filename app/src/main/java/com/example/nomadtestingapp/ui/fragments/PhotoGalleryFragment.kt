package com.example.nomadtestingapp.ui.fragments

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movielist.other.Status
import com.example.nomadtestingapp.R
import com.example.nomadtestingapp.adapters.GalleryAdapter
import com.example.nomadtestingapp.data.models.File
import com.example.nomadtestingapp.data.retrofit.SessionManager
import com.example.nomadtestingapp.databinding.ButtonItemBinding
import com.example.nomadtestingapp.databinding.FragmentPhotoGalleryBinding
import com.example.nomadtestingapp.other.extensions.saveMediaToStorage
import com.example.nomadtestingapp.ui.viewmodels.LoginRegisterViewModel
import com.example.nomadtestingapp.ui.viewmodels.PhotoGalleryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.FileOutputStream
import java.io.OutputStream
import java.lang.Exception
import javax.inject.Inject


@AndroidEntryPoint
class PhotoGalleryFragment : Fragment(R.layout.fragment_photo_gallery) {

    private val galleryViewModel: PhotoGalleryViewModel by viewModels()

    private lateinit var binding: FragmentPhotoGalleryBinding
    private lateinit var sessionManager: SessionManager

    @Inject
    lateinit var galleryAdapter: GalleryAdapter

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == Activity.RESULT_OK){
            val takenImage = result.data?.extras?.get("data") as Bitmap
            lifecycleScope.launch {
                val imagePath = takenImage.saveMediaToStorage(requireContext(),takenImage)
                if (imagePath != null) {
                    galleryViewModel.addPhotoToDataBase(imagePath)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())

        binding = FragmentPhotoGalleryBinding.bind(view)

        binding.btnAddPhoto.setOnClickListener {
            if (requireActivity().packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) { // First check if camera is available in the device
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                resultLauncher.launch(takePictureIntent)
            }
        }

        binding.btnExit.setOnClickListener{
            sessionManager.clearAccessToken()
            view.findNavController().navigate(R.id.action_photoGalleryFragment_to_splashScreenFragment)
        }

        galleryAdapter.setItemClickListener {
            if (requireActivity().packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) { // First check if camera is available in the device
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                resultLauncher.launch(takePictureIntent)
            }
        }

        binding.tvCurrUserEmail.text =
            if(sessionManager.fetchCurrentUser() == null)
                "_"
            else
                sessionManager.fetchCurrentUser()


        galleryViewModel.getAllPhotos()

        subscribeToObserver()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = galleryAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
        }
    }

    private fun subscribeToObserver(){
        galleryViewModel.photoList.observe(viewLifecycleOwner){result ->
            when(result.status){
                Status.SUCCESS -> {
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE

                    galleryAdapter.photoList = result.data!!
                }
                Status.LOADING -> {
                    binding.recyclerView.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(), "Unknown error occurred", Toast.LENGTH_SHORT).show()
                }
            }

        }

    }


}