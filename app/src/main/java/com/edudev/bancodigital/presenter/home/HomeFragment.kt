package com.edudev.bancodigital.presenter.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.edudev.bancodigital.MainGraphDirections
import com.edudev.bancodigital.R
import com.edudev.bancodigital.data.enum.TransactionOperation
import com.edudev.bancodigital.data.enum.TransactionType
import com.edudev.bancodigital.data.model.Transaction
import com.edudev.bancodigital.data.model.User
import com.edudev.bancodigital.databinding.FragmentHomeBinding
import com.edudev.bancodigital.presenter.features.extract.ExtractFragmentDirections
import com.edudev.bancodigital.util.BaseFragment
import com.edudev.bancodigital.util.FirebaseHelper
import com.edudev.bancodigital.util.GetMask
import com.edudev.bancodigital.util.StateView
import com.edudev.bancodigital.util.showBottomSheet
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var transactionsAdapter: TransactionsAdapter
    private val tagPicasso = "tagPicasso"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getProfile()
        configRecyclerView()
        getTransactions()
        initListener()
    }

    private fun configRecyclerView() {
        transactionsAdapter = TransactionsAdapter(requireContext()) { transaction ->

            when(transaction.operation) {
                TransactionOperation.DEPOSIT -> {
                    val action = MainGraphDirections.actionGlobalDepositReceiptFragment(transaction.id, true)
                    findNavController().navigate(action)
                }

                TransactionOperation.TRANSFER -> {
                    val action = MainGraphDirections.actionGlobalTransferReceiptFragment(transaction.id, true)
                    findNavController().navigate(action)
                }

                TransactionOperation.RECHARGE -> {
                    val action = MainGraphDirections.actionGlobalRechargeReceiptFragment(transaction.id, true)
                    findNavController().navigate(action)
                }

                else -> {
                }
            }
        }
        with(binding.rvTransactions) {
            setHasFixedSize(true)
            adapter = transactionsAdapter
        }
    }

    private fun configData(user: User) {
        val fadeInAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
        if (user.image.isNotEmpty()) {
            Picasso.get()
                .load(user.image)
                .fit().centerCrop()
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

    }

    private fun getTransactions() {
        homeViewModel.getTransactions().observe(viewLifecycleOwner) { stateView ->
            when(stateView){
                is StateView.Loading -> {
                    binding.progressBar.isVisible = true
                }
                is StateView.Sucess -> {
                    binding.progressBar.isVisible = false
                    transactionsAdapter.submitList(stateView.data?.reversed()?.take(6))

                    binding.textMessage.isVisible = stateView.data?.isEmpty() == true
                    showBalance(stateView.data ?: emptyList())
                }
                is StateView.Error -> {
                    binding.progressBar.isVisible = false
                    showBottomSheet(message = stateView.message)
                }
            }
        }
    }

    private fun initListener() {

        binding.cardTransfer.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_transferUserListFragment)
        }

        binding.cardRecharge.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_rechargeFormFragment)
        }
        binding.btnLogout.setOnClickListener {
            FirebaseHelper.getAuth().signOut()

            val navOptions: NavOptions = NavOptions.Builder().setPopUpTo(R.id.homeFragment, true).build()
            findNavController().navigate(R.id.action_global_authentication, null, navOptions)
        }
        binding.btnShowAll.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_extractFragment)
        }

        binding.btnCardExtract.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_extractFragment)
        }

        binding.cardDeposit.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_depositFormFragment)
        }

        binding.cardProfile.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }
    }

    private fun getProfile() {
        homeViewModel.getProfile().observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {
                    binding.progressBar.isVisible = true
                }

                is StateView.Sucess -> {
                    binding.progressBar.isVisible = false
                    stateView.data?.let { configData(it)}
                }

                is StateView.Error -> {
                    binding.progressBar.isVisible = false
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

    private fun showBalance(transactions : List<Transaction>) {
        var cashIn = 0f
        var cashOut = 0f

        transactions.forEach { transaction ->
            if (transaction.type == TransactionType.CASH_IN) {
                cashIn += transaction.amount
            } else {
                cashOut += transaction.amount
            }
        }

        binding.textBalance.text = getString(R.string.text_formated_value, GetMask.getFormatedValue(cashIn - cashOut))
    }
    override fun onDestroyView() {
        super.onDestroyView()
        Picasso.get().cancelTag(tagPicasso)
        _binding = null
    }
}