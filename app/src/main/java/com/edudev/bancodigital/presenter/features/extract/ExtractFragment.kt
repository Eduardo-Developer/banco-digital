package com.edudev.bancodigital.presenter.features.extract

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.edudev.bancodigital.MainGraphDirections
import com.edudev.bancodigital.R
import com.edudev.bancodigital.data.enum.TransactionOperation
import com.edudev.bancodigital.databinding.FragmentExtractBinding
import com.edudev.bancodigital.presenter.home.HomeFragmentDirections
import com.edudev.bancodigital.presenter.home.TransactionsAdapter
import com.edudev.bancodigital.util.StateView
import com.edudev.bancodigital.util.initToolbar
import com.edudev.bancodigital.util.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExtractFragment : Fragment() {

    private var _binding: FragmentExtractBinding? = null
    private val binding get() = _binding!!
    private lateinit var transactionsAdapter: TransactionsAdapter
    private val extractViewModel : ExtractViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentExtractBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar, light = false)

        configRecyclerView()
        getTransactions()
    }

    private fun getTransactions() {
        extractViewModel.getTransactions().observe(viewLifecycleOwner) { stateView ->
            when(stateView){
                is StateView.Loading -> {
                    binding.progressBar.isVisible = true
                }
                is StateView.Sucess -> {
                    binding.progressBar.isVisible = false
                    binding.textMessage.isVisible = stateView.data?.isEmpty() == true
                    transactionsAdapter.submitList(stateView.data?.reversed())
                }
                is StateView.Error -> {
                    binding.progressBar.isVisible = false
                    showBottomSheet(message = stateView.message)
                }
            }
        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
}