package com.edudev.bancodigital.presenter.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.edudev.bancodigital.R
import com.edudev.bancodigital.data.model.User
import com.edudev.bancodigital.databinding.FragmentProfileBinding
import com.edudev.bancodigital.util.BaseFragment
import com.edudev.bancodigital.util.FirebaseHelper
import com.edudev.bancodigital.util.StateView
import com.edudev.bancodigital.util.initToolbar
import com.edudev.bancodigital.util.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    private var user: User? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar, true, light = false)
        getProfile()
        initListener()

    }

    private fun initListener() {
        binding.btnSend.setOnClickListener {
            if (user != null)
                validateData()
        }
    }

    private fun getProfile() {
        viewModel.getProfile().observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {
                    binding.progressBar.isVisible = true
                }

                is StateView.Sucess -> {
                    binding.progressBar.isVisible = false
                    stateView.data?.let { user = it }
                    configData()
                }

                is StateView.Error -> {
                    binding.progressBar.isVisible = false
                    Log.i("INFOTESTE", "loginUser: ${stateView.message}")
                    showBottomSheet(
                        message = getString(
                            FirebaseHelper.validError(
                                stateView.message ?: ""
                            )
                        )
                    )
                }
            }
        }
    }

    private fun saveProfile() {
        user?.let {
            viewModel.saveProfile(it).observe(viewLifecycleOwner) { stateView ->
                when (stateView) {
                    is StateView.Loading -> {
                        binding.progressBar.isVisible = true
                    }

                    is StateView.Sucess -> {
                        binding.progressBar.isVisible = false
                        showBottomSheet(
                            message = getString(R.string.text_message_save_success)
                        )
                    }

                    is StateView.Error -> {
                        binding.progressBar.isVisible = false
                        Log.i("INFOTESTE", "loginUser: ${stateView.message}")
                        showBottomSheet(
                            message = getString(
                                FirebaseHelper.validError(
                                    stateView.message ?: ""
                                )
                            )
                        )
                    }
                }
            }
        }
    }

    private fun configData() {
        binding.editName.setText(user?.name)
        binding.editPhone.setText(user?.phone)
        binding.editEmail.setText(user?.email)
    }

    private fun validateData() {
        val name = binding.editName.text.toString().trim()
        val phone = binding.editPhone.unMaskedText

        if (name.isNotEmpty()) {
            if (phone?.isNotEmpty() == true) {
                if (phone.length == 11) {
                    user?.name = name
                    user?.phone = phone

                    saveProfile()

                } else {
                    showBottomSheet(message = getString(R.string.text_phone_valid))
                }
            } else {
                showBottomSheet(message = getString(R.string.text_phone_valid))
            }
        } else {
            showBottomSheet(message = getString(R.string.text_name_empty))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}