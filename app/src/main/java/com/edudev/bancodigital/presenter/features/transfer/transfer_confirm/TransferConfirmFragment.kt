package com.edudev.bancodigital.presenter.features.transfer.transfer_confirm

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.edudev.bancodigital.MainGraphDirections
import com.edudev.bancodigital.R
import com.edudev.bancodigital.data.enum.TransactionOperation
import com.edudev.bancodigital.data.enum.TransactionType
import com.edudev.bancodigital.data.model.Deposit
import com.edudev.bancodigital.data.model.Transaction
import com.edudev.bancodigital.data.model.Transfer
import com.edudev.bancodigital.databinding.FragmentTransferConfirmBinding
import com.edudev.bancodigital.presenter.features.deposit.DepositFormFragmentDirections
import com.edudev.bancodigital.util.FirebaseHelper
import com.edudev.bancodigital.util.GetMask
import com.edudev.bancodigital.util.StateView
import com.edudev.bancodigital.util.initToolbar
import com.edudev.bancodigital.util.showBottomSheet
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransferConfirmFragment : Fragment() {
    private var _binding: FragmentTransferConfirmBinding? = null
    private val binding get() = _binding!!

    private val args: TransferConfirmFragmentArgs by navArgs()
    private val tagPicasso = "tagPicasso"

    private val viewModel: TransferConfirmViewmodel by viewModels()

    private var useBalance = 0f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransferConfirmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar, homeAsUpEnabled = true, light = false)
        configData()
        initListener()
    }

    private fun initListener() {
        binding.btnConfirmTransfer.setOnClickListener {
            binding.btnConfirmTransfer.isEnabled = false
            getBalance() }
    }

    private fun configData() {
        val fadeInAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)

        if (args.user.image.isNotEmpty()) {
            Picasso.get()
                .load(args.user.image)
                .fit().centerCrop()
                .tag(tagPicasso)
                .into(binding.imageUser, object : Callback {
                    override fun onSuccess() {
                        binding.progressImage.isVisible = false
                        binding.imageUser.startAnimation(fadeInAnimation)
                        binding.imageUser.isVisible = true
                    }

                    override fun onError(e: java.lang.Exception?) {
                    }
                })
        } else {
            binding.imageUser.setImageResource(R.drawable.ic_user_place_holder)
            binding.progressImage.isVisible = false
            binding.imageUser.startAnimation(fadeInAnimation)
            binding.imageUser.isVisible = true
        }

        binding.txtUserName.text = args.user.name
        binding.textAmountTransfered.text =
            getString(R.string.text_formated_value, GetMask.getFormatedValue(args.amount))
    }

    private fun saveTransfer(transfer: Transfer) {
        viewModel.saveTransfer(transfer).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {
                }

                is StateView.Sucess -> {
                    updateTransfer(transfer)
                }

                is StateView.Error -> {
                    binding.progressConfirmTransfer.isVisible = false
                    showBottomSheet(message = stateView.message)
                }
            }
        }
    }

    private fun updateTransfer(transfer: Transfer) {
        viewModel.updateTransfer(transfer).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {
                }

                is StateView.Sucess -> {
                    saveTransaction(transfer)
                }

                is StateView.Error -> {
                    binding.progressConfirmTransfer.isVisible = false
                    showBottomSheet(message = stateView.message)
                }
            }
        }
    }

    private fun updateTransactionTransfer(transfer: Transfer) {
        viewModel.updateTransactionTransfer(transfer).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {
                }

                is StateView.Sucess -> {
                    val action = MainGraphDirections
                        .actionGlobalTransferReceiptFragment(transfer.id, false)
                    findNavController().navigate(action)
                }

                is StateView.Error -> {
                    binding.progressConfirmTransfer.isVisible = false
                    showBottomSheet(message = stateView.message)
                }
            }
        }
    }

    private fun saveTransaction(transfer: Transfer) {
        viewModel.saveTransferTransaction(transfer)
            .observe(viewLifecycleOwner) { stateView ->
                when (stateView) {
                    is StateView.Loading -> {
                        binding.progressConfirmTransfer.isVisible = true
                    }

                    is StateView.Sucess -> {
                        binding.progressConfirmTransfer.isVisible = false
                        updateTransactionTransfer(transfer)
                    }

                    is StateView.Error -> {
                        binding.progressConfirmTransfer.isVisible = false
                        showBottomSheet(message = stateView.message)
                    }
                }
            }
    }

    private fun getBalance() {
        viewModel.getBalance().observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {
                    binding.progressConfirmTransfer.isVisible = true
                }

                is StateView.Sucess -> {
                    binding.progressConfirmTransfer.isVisible = false
                    if ((stateView.data ?: 0f) >= args.amount) {
                        val transfer = Transfer(
                            idUserReceived = args.user.id!!,
                            idUserSent = FirebaseHelper.getUserId(),
                            amount = args.amount
                        )
                        saveTransfer(transfer = transfer)
                    } else {
                        binding.btnConfirmTransfer.isEnabled = true
                        showBottomSheet(message = "Saldo insuficiente!")
                    }
                }

                is StateView.Error -> {
                    binding.btnConfirmTransfer.isEnabled = true
                    binding.progressConfirmTransfer.isVisible = false
                    showBottomSheet(message = stateView.message)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Picasso.get().cancelTag(tagPicasso)
        _binding = null
    }
}