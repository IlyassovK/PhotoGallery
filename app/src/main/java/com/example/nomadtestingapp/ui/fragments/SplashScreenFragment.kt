package com.example.nomadtestingapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.nomadtestingapp.R
import com.example.nomadtestingapp.data.retrofit.SessionManager
import com.example.nomadtestingapp.databinding.FragmentSplashScreenBinding

class SplashScreenFragment : Fragment(R.layout.fragment_splash_screen) {

    private lateinit var binding: FragmentSplashScreenBinding

    private lateinit var sessionManager: SessionManager


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())

        if (!sessionManager.fetchAccessToken().isNullOrBlank()){
            view.findNavController().navigate(R.id.action_splashScreenFragment_to_photoGalleryFragment)
        }

        binding = FragmentSplashScreenBinding.bind(view)

        binding.btnLogin.setOnClickListener {
            view.findNavController().navigate(R.id.action_splashScreenFragment_to_loginFragment)
        }

        binding.btnSignup.setOnClickListener {
            view.findNavController().navigate(R.id.action_splashScreenFragment_to_registrationFragment)
        }
    }
}