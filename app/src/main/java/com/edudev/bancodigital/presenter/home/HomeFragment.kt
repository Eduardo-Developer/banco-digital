package com.edudev.bancodigital.presenter.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.edudev.bancodigital.R
import com.edudev.bancodigital.data.enum.TransactionOperation
import com.edudev.bancodigital.data.enum.TransactionType
import com.edudev.bancodigital.data.model.Transaction
import com.edudev.bancodigital.databinding.FragmentHomeBinding
import com.edudev.bancodigital.util.GetMask
import com.edudev.bancodigital.util.StateView
import com.edudev.bancodigital.util.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var transactionsAdapter: TransactionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configRecyclerView()
        getTransactions()
        initListener()
    }

    private fun configRecyclerView() {
        transactionsAdapter = TransactionsAdapter(requireContext()) { transaction ->

            when(transaction.operation) {
                TransactionOperation.DEPOSIT -> {
                    val action = HomeFragmentDirections.actionHomeFragmentToDepositReceiptFragment(transaction.id, true)

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

    private fun getTransactions() {
        homeViewModel.getTransactions().observe(viewLifecycleOwner) { stateView ->
            when(stateView){
                is StateView.Loading -> {
                    binding.progressBar.isVisible = true
                }
                is StateView.Sucess -> {
                    binding.progressBar.isVisible = false
                    transactionsAdapter.submitList(stateView.data?.reversed()?.take(6))
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
        binding.cardDeposit.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_depositFormFragment)
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
        _binding = null
    }
}