package com.edudev.bancodigital.presenter.auth.register

import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.edudev.bancodigital.R
import com.edudev.bancodigital.data.model.User
import com.edudev.bancodigital.data.model.Wallet
import com.edudev.bancodigital.databinding.FragmentRecoverBinding
import com.edudev.bancodigital.databinding.FragmentRegisterBinding
import com.edudev.bancodigital.presenter.profile.ProfileViewModel
import com.edudev.bancodigital.presenter.wallet.WalletViewModel
import com.edudev.bancodigital.util.BaseFragment
import com.edudev.bancodigital.util.FirebaseHelper
import com.edudev.bancodigital.util.StateView
import com.edudev.bancodigital.util.initToolbar
import com.edudev.bancodigital.util.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : BaseFragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val registerViewModel : RegisterViewModel by viewModels()
    private val profileViewModel : ProfileViewModel by viewModels()
    private val walletViewModel : WalletViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(binding.toolbar)
        initListeners()
    }

    private fun initListeners() {
        binding.btnSend.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {
        val name = binding.editName.text.toString().trim()
        val email = binding.editEmail.text.toString().trim()
        val phone = binding.editPhone.unMaskedText
        val password = binding.editPassword.text.toString().trim()

        if (name.isNotEmpty()) {
            if (email.isNotEmpty()) {
                if (phone?.isNotEmpty() == true) {
                    if (phone.length == 11) {
                        if (password.isNotEmpty()) {
                            binding.progressRegister.isVisible = true
                            registerUser(name, email, phone, password)
                        } else {
                            showBottomSheet(message = getString(R.string.text_password_empty))
                        }
                    } else {
                        showBottomSheet(message = getString(R.string.text_phone_valid))
                    }
                } else {
                    showBottomSheet(message = getString(R.string.text_phone_valid))
                }
            } else {
                showBottomSheet(message = getString(R.string.text_email_empty))
            }
        } else {
            showBottomSheet(message = getString(R.string.text_name_empty))
        }
    }

    private fun registerUser(name: String, email: String, phone: String, password: String) {
        registerViewModel.register(name, email, phone, password).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {
                    binding.progressRegister.isVisible = true
                }

                is StateView.Sucess -> {
                    stateView.data?.let {
                        saveProfile(it)
                    }
                }

                is StateView.Error -> {
                    binding.progressRegister.isVisible = false
                    Log.i("INFOTESTE", "loginUser: ${stateView.message}")
                    showBottomSheet(message = getString(FirebaseHelper.validError(stateView.message ?: "")))
                }
            }
        }
    }

    private fun saveProfile(user: User){
        profileViewModel.saveProfile(user).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {}

                is StateView.Sucess -> {
                    initWallet()
                }

                is StateView.Error -> {
                    binding.progressRegister.isVisible = false
                    Log.i("INFOTESTE", "loginUser: ${stateView.message}")
                    showBottomSheet(message = getString(FirebaseHelper.validError(stateView.message ?: "")))
                }
            }
        }
    }

    private fun initWallet(){
        walletViewModel.initWallet(Wallet(
            userId = FirebaseHelper.getUserId()
        )).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {}

                is StateView.Sucess -> {
                    binding.progressRegister.isVisible = false
                    findNavController().navigate(R.id.action_global_homeFragment)
                }

                is StateView.Error -> {
                    binding.progressRegister.isVisible = false
                    Log.i("INFOTESTE", "loginUser: ${stateView.message}")
                    showBottomSheet(message = getString(FirebaseHelper.validError(stateView.message ?: "")))
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}