package com.edudev.bancodigital.presenter.features.deposit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.edudev.bancodigital.R
import com.edudev.bancodigital.data.enum.TransactionOperation
import com.edudev.bancodigital.data.enum.TransactionType
import com.edudev.bancodigital.data.model.Deposit
import com.edudev.bancodigital.data.model.Transaction
import com.edudev.bancodigital.databinding.FragmentDepositFormBinding
import com.edudev.bancodigital.databinding.FragmentRegisterBinding
import com.edudev.bancodigital.util.BaseFragment
import com.edudev.bancodigital.util.FirebaseHelper
import com.edudev.bancodigital.util.StateView
import com.edudev.bancodigital.util.initToolbar
import com.edudev.bancodigital.util.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DepositFormFragment : BaseFragment() {

    private var _binding: FragmentDepositFormBinding? = null
    private val binding get() = _binding!!

    private val depositViewModel: DepositViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDepositFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(toolbar = binding.toolbar, light = true)
        initListener()
    }

    private fun initListener() {
        binding.btnContinue.setOnClickListener { validadeDeposit() }
    }

    private fun validadeDeposit() {
        val amount = binding.editTextDepositAmount.text.toString().trim()

        if (amount.isNotEmpty()) {
            val deposit = Deposit(amount = amount.toFloat())
            saveDeposit(deposit)
            hideKeyboard()
        } else {
            Toast.makeText(requireContext(), "Digite um valor", Toast.LENGTH_LONG).show()
        }
    }

    private fun saveDeposit(deposit: Deposit) {
        depositViewModel.saveDeposit(deposit)
            .observe(viewLifecycleOwner) { stateView ->
                when (stateView) {
                    is StateView.Loading -> {
                        binding.progressBar.isVisible = true
                    }

                    is StateView.Sucess -> {
                        binding.progressBar.isVisible = false
                        stateView.data?.let { saveTransaction(it) }
                    }

                    is StateView.Error -> {
                        binding.progressBar.isVisible = false
                        showBottomSheet(message = stateView.message)
                    }
                }
            }
    }

    private fun saveTransaction(deposit: Deposit) {
        val transaction = Transaction(
            id = deposit.id,
            operation = TransactionOperation.DEPOSIT,
            date = deposit.date,
            amount = deposit.amount,
            type = TransactionType.CASH_IN
        )


        depositViewModel.saveTransaction(transaction)
            .observe(viewLifecycleOwner) { stateView ->
                when (stateView) {
                    is StateView.Loading -> {
                        binding.progressBar.isVisible = true
                    }

                    is StateView.Sucess -> {
                        binding.progressBar.isVisible = false
                        val action = DepositFormFragmentDirections
                            .actionDepositFormFragmentToDepositReceiptFragment(deposit.id, false)

                        findNavController().navigate(action)
                    }

                    is StateView.Error -> {
                        binding.progressBar.isVisible = false
                        showBottomSheet(message = stateView.message)
                    }
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}