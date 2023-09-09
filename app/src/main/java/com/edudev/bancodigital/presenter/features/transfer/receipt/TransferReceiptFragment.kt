package com.edudev.bancodigital.presenter.features.transfer.receipt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.edudev.bancodigital.R
import com.edudev.bancodigital.data.model.Deposit
import com.edudev.bancodigital.data.model.Transfer
import com.edudev.bancodigital.data.model.User
import com.edudev.bancodigital.databinding.FragmentTransferReceiptBinding
import com.edudev.bancodigital.util.FirebaseHelper
import com.edudev.bancodigital.util.GetMask
import com.edudev.bancodigital.util.StateView
import com.edudev.bancodigital.util.initToolbar
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransferReceiptFragment : Fragment() {

    private var _binding: FragmentTransferReceiptBinding? = null
    private val binding get() = _binding!!
    private val receiptTransferViewModel: ReceiptTransferViewModel by viewModels()
    private val args: TransferReceiptFragmentArgs by navArgs()

    private val tagPicasso = "tagPicasso"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTransferReceiptBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(binding.toolbar, homeAsUpEnabled = args.homeAsUpEnabled, light = false)
        getTransfer()
        initListener()
    }

    private fun initListener() {
        binding.btnContinueTransfer.setOnClickListener {
            if (args.homeAsUpEnabled) {
                findNavController().popBackStack()
            } else {
                val navOptions: NavOptions = NavOptions.Builder().setPopUpTo(R.id.transferUserListFragment, true).build()
                findNavController().navigate(R.id.action_global_homeFragment, null, navOptions)
            }
        }
    }

    private fun getTransfer() {
        receiptTransferViewModel.getTransfer(args.idTransfer)
            .observe(viewLifecycleOwner) { stateView ->
                when (stateView) {
                    is StateView.Loading -> {

                    }

                    is StateView.Sucess -> {
                        stateView.data?.let { transfer ->
                            val userId = if (transfer.idUserSent == FirebaseHelper.getUserId()) {
                                transfer.idUserReceived
                            } else {
                                transfer.idUserSent
                            }
                            getProfile(userId)
                            configTransfer(transfer)
                        }
                    }

                    is StateView.Error -> {
                        Toast.makeText(requireContext(), "Ocorreu um Erro", Toast.LENGTH_SHORT)
                            .show()
                        findNavController().popBackStack()
                    }
                }
            }
    }

    private fun configTransfer(transfer: Transfer) {
        binding.textSendOrReceived.text = if (transfer.idUserSent == FirebaseHelper.getUserId()) {
            getString(R.string.text_label_user_received_transfer_confirm_fragment)
        } else {
            getString(R.string.text_label_user_sent_transfer_confirm_fragment)
        }
        binding.textCodeTransaction.text = transfer.id
        binding.textDateTransaction.text =
            GetMask.getFormatedDate(transfer.date, GetMask.DAY_MONTH_YEAR_HOUR_MINUTE)
        binding.textAmountTransaction.text =
            getString(R.string.text_formated_value, GetMask.getFormatedValue(transfer.amount))
    }

    private fun getProfile(id: String) {
        receiptTransferViewModel.getProfile(id).observe(viewLifecycleOwner) { stateview ->
            when (stateview) {
                is StateView.Loading -> {

                }

                is StateView.Sucess -> {
                    stateview.data?.let { configProfile(it) }
                }

                is StateView.Error -> {
                    Toast.makeText(requireContext(), "Ocorreu um Erro", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun configProfile(user: User) {
        val fadeInAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
        if (user.image.isNotEmpty()) {
            Picasso.get()
                .load(user.image)
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
        binding.txtUserName.text = user.name
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}