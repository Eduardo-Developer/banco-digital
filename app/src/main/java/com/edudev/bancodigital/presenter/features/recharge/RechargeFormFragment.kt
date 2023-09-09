package com.edudev.bancodigital.presenter.features.recharge

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.edudev.bancodigital.MainGraphDirections
import com.edudev.bancodigital.R
import com.edudev.bancodigital.data.enum.TransactionOperation
import com.edudev.bancodigital.data.enum.TransactionType
import com.edudev.bancodigital.data.model.Deposit
import com.edudev.bancodigital.data.model.Recharge
import com.edudev.bancodigital.data.model.Transaction
import com.edudev.bancodigital.databinding.FragmentDepositFormBinding
import com.edudev.bancodigital.databinding.FragmentRechargeFormBinding
import com.edudev.bancodigital.presenter.features.deposit.DepositFormFragmentDirections
import com.edudev.bancodigital.util.BaseFragment
import com.edudev.bancodigital.util.StateView
import com.edudev.bancodigital.util.initToolbar
import com.edudev.bancodigital.util.showBottomSheet
import com.example.bancodigital.util.MoneyTextWatcher
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RechargeFormFragment : BaseFragment() {

    private var _binding: FragmentRechargeFormBinding? = null
    private val binding get() = _binding!!

    private val rechargeViewModel : RechargeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRechargeFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(binding.toolbar, light = true)
        initListener()
    }

    private fun initListener() {
        with(binding.editTextDepositAmount) {
            addTextChangedListener(MoneyTextWatcher(binding.editTextDepositAmount))
            addTextChangedListener {
                if (MoneyTextWatcher.getValueUnMasked(this) > 100) {
                    this.setText("R$ 0,00")
                }
            }

            doAfterTextChanged {
                this.text?.length?.let {this.setSelection(it) }
            }
        }
        binding.btnContinue.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {
        val amount = MoneyTextWatcher.getValueUnMasked(binding.editTextDepositAmount)
        val phone = binding.editPhone.unMaskedText

        if (amount >= 10f) {
            if (phone?.isNotEmpty() == true) {
                if (phone.length == 11) {

                    hideKeyboard()

                    val recharge = Recharge(
                        amount = amount,
                        number = phone
                    )
                    saveRecharge(recharge)

                } else {
                    showBottomSheet(message = getString(R.string.text_phone_valid))
                }
            } else {
                showBottomSheet(message = getString(R.string.text_phone_valid))
            }
        } else {
            showBottomSheet(message = getString(R.string.text_invalid_recharge_value_receipt_fragment))
        }
    }

    private fun saveRecharge(recharge: Recharge) {
        rechargeViewModel.saveRecharge(recharge)
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

    private fun saveTransaction(recharge: Recharge) {
        val transaction = Transaction(
            id = recharge.id,
            operation = TransactionOperation.RECHARGE,
            date = recharge.date,
            amount = recharge.amount,
            type = TransactionType.CASH_OUT
        )


        rechargeViewModel.saveTransaction(transaction)
            .observe(viewLifecycleOwner) { stateView ->
                when (stateView) {
                    is StateView.Loading -> {
                        binding.progressBar.isVisible = true
                    }

                    is StateView.Sucess -> {
                        binding.progressBar.isVisible = false
                        val action = MainGraphDirections.actionGlobalRechargeReceiptFragment(transaction.id, false)
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