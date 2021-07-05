package com.example.nomadtestingapp.ui.fragments

import android.app.KeyguardManager
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.movielist.other.Status
import com.example.nomadtestingapp.R
import com.example.nomadtestingapp.data.retrofit.SessionManager
import com.example.nomadtestingapp.databinding.FragmentLoginBinding
import com.example.nomadtestingapp.other.Constants
import com.example.nomadtestingapp.ui.viewmodels.LoginRegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.log

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding

    private val loginRegisterViewModel: LoginRegisterViewModel by viewModels()

    private lateinit var sessionManager: SessionManager

    private var cancellationSignal: CancellationSignal? = null

    private val authenticationCallback: BiometricPrompt.AuthenticationCallback
        get() =
            @RequiresApi(Build.VERSION_CODES.P)
            object : BiometricPrompt.AuthenticationCallback(){
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(requireContext(), "Ошибка аутентификации $errString", Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(requireContext(), "Успешная аутентификация!", Toast.LENGTH_SHORT).show()

                    view?.findNavController()?.navigate(R.id.action_loginFragment_to_photoGalleryFragment)
                }
            }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLoginBinding.bind(view)
        sessionManager = SessionManager(requireContext())

        binding.btnLogin.setOnClickListener {

            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()


             if (email.isNotEmpty() && password.isNotEmpty()){
                 loginRegisterViewModel.login(email, password)
             }
        }

        binding.btnBack.setOnClickListener {
            view.findNavController().popBackStack()
        }


        binding.tvUseTouchId.setOnClickListener {

            if(!sessionManager.fetchRefreshToken().isNullOrBlank()){
                val biometricPrompt = BiometricPrompt.Builder(requireContext())
                    .setTitle("Вход по отпечатку пальцев")
                    .setNegativeButton("Отмена", requireContext().mainExecutor, DialogInterface.OnClickListener { dialog, which ->
                        Toast.makeText(requireContext(), "Аутентификация отменена!", Toast.LENGTH_SHORT).show()
                    }).build()

                biometricPrompt.authenticate(getCancellationSignal(), requireContext().mainExecutor, authenticationCallback)
            }else{
                Toast.makeText(requireContext(), "Биометрическая авторизация будет доступна полсе одной успешной авторизации", Toast.LENGTH_SHORT).show()
            }



        }

        subscribeToObserver()
    }


    private fun subscribeToObserver() {
        loginRegisterViewModel.currentUser.observe(viewLifecycleOwner){result ->
            when(result.status){
                Status.SUCCESS -> {
                    Toast.makeText(requireContext(), "Успешная авторизация", Toast.LENGTH_SHORT).show()
                    requireView().findNavController().navigate(R.id.action_loginFragment_to_photoGalleryFragment)
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(), "Ошибка авторизации", Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {

                }
            }

        }
    }

    private fun getCancellationSignal(): CancellationSignal{
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            Toast.makeText(requireContext(), "Аутентификация запрещена пользователем!", Toast.LENGTH_SHORT).show()
        }
        return cancellationSignal as CancellationSignal
    }

}