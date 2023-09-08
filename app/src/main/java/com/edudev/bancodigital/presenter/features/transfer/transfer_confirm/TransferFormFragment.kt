package com.edudev.bancodigital.presenter.features.transfer.transfer_confirm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.edudev.bancodigital.R
import com.edudev.bancodigital.databinding.FragmentTransferFormBinding
import com.edudev.bancodigital.util.BaseFragment
import com.edudev.bancodigital.util.initToolbar
import com.edudev.bancodigital.util.showBottomSheet
import com.example.bancodigital.util.MoneyTextWatcher
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TransferFormFragment : BaseFragment() {

    private var _binding: FragmentTransferFormBinding? = null
    private val binding get() = _binding!!

    private val args: TransferFormFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransferFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(toolbar = binding.toolbar, light = true)
        initListener()
    }

    private fun initListener() {
        with(binding.editTextDepositAmount) {
            addTextChangedListener(MoneyTextWatcher(binding.editTextDepositAmount))
            addTextChangedListener {
                if (MoneyTextWatcher.getValueUnMasked(this) > 99999.99F) {
                    this.setText("R$ 0,00")
                }
            }

            doAfterTextChanged {
                this.text?.length?.let {this.setSelection(it) }
            }
        }
        binding.btnContinue.setOnClickListener { validadeAmount() }
    }

    private fun validadeAmount() {
        val amount = MoneyTextWatcher.getValueUnMasked(binding.editTextDepositAmount)
        if (amount > 0f) {
            hideKeyboard()
            val action =
                TransferFormFragmentDirections.actionTransferFormFragmentToTransferConfirmFragment(
                    args.user,
                    amount
                )
            findNavController().navigate(action)
        } else {
            showBottomSheet(message = getString(R.string.text_message_empty_amount_transfer_fragment))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}