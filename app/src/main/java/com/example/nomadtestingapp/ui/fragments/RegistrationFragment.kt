package com.example.nomadtestingapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.movielist.other.Status
import com.example.nomadtestingapp.R
import com.example.nomadtestingapp.databinding.FragmentRegistrationBinding
import com.example.nomadtestingapp.other.Constants
import com.example.nomadtestingapp.ui.viewmodels.LoginRegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationFragment : Fragment(R.layout.fragment_registration) {

    private lateinit var binding: FragmentRegistrationBinding

    private val loginRegisterViewModel: LoginRegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegistrationBinding.bind(view)

        binding.btnSignup.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()

            validateAndRegister(email, password, confirmPassword)
        }

        binding.btnBack.setOnClickListener {
            view.findNavController().popBackStack()
        }

        subscribeToObserver()
    }


    private fun validateAndRegister(email: String, password: String, confirmPassword: String){
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(requireContext(), "Заполните обязательные поля!", Toast.LENGTH_SHORT).show()
            return
        }

        if(password != confirmPassword){
            Toast.makeText(requireContext(), "Пароли неодинаковы!", Toast.LENGTH_SHORT).show()
            return
        }

        if(!isValidEmail(email)){
            Toast.makeText(requireContext(), "Невалидный адрес электронной почты!", Toast.LENGTH_SHORT).show()
            return
        }

        if(!isValidPassword(password)){
            Toast.makeText(requireContext(), "Пароль должен быть не менее 6 знаков, цифры и латинские буквы", Toast.LENGTH_SHORT).show()
            return
        }

        loginRegisterViewModel.register(email, password)

    }

    private fun subscribeToObserver() {
        loginRegisterViewModel.currentUser.observe(viewLifecycleOwner){result ->
            when(result.status){
                Status.SUCCESS -> {
                    requireView().findNavController().navigate(R.id.action_registrationFragment_to_photoGalleryFragment)
                }
                Status.ERROR -> {

                }
                Status.LOADING -> {

                }
            }

        }
    }

    private fun isValidPassword(password: String): Boolean{
        return Constants.PASSWORD_PATTERN.matcher(password).matches()
    }

    private fun isValidEmail(email: String): Boolean{
        return Constants.EMAIL_ADDRESS_PATTERN.matcher(email).matches()
    }

}