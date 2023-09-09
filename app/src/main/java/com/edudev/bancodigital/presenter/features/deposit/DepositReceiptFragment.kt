package com.edudev.bancodigital.presenter.features.deposit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.edudev.bancodigital.R
import com.edudev.bancodigital.data.model.Deposit
import com.edudev.bancodigital.databinding.FragmentDepositReceiptBinding
import com.edudev.bancodigital.util.GetMask
import com.edudev.bancodigital.util.StateView
import com.edudev.bancodigital.util.initToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DepositReceiptFragment : Fragment() {

    private var _binding: FragmentDepositReceiptBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DepositReceiptViewModel by viewModels()

    private val args: DepositReceiptFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDepositReceiptBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(binding.toolbar, args.homeAsUpEnabled, light = false)
        getDeposit()
        initListener()
    }

    private fun getDeposit() {
        viewModel.getDeposit(args.idDeposit).observe(viewLifecycleOwner) { stateView ->
            when(stateView) {
                is StateView.Loading -> {
                    
                }

                is StateView.Sucess -> {
                    stateView.data?.let { configData(it) }
                }

                is StateView.Error -> {
                    Toast.makeText(requireContext(), "Ocorreu um Erro", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun initListener() {
        binding.btnContinue.setOnClickListener {
            if (args.homeAsUpEnabled) {
                findNavController().popBackStack()
            } else {
                val navOptions: NavOptions = NavOptions.Builder().setPopUpTo(R.id.depositFormFragment, true).build()
                findNavController().navigate(R.id.action_global_homeFragment, null, navOptions)
            }
        }
    }

    private fun configData(deposit: Deposit) {
        binding.textCodeTransaction.text = deposit.id
        binding.textDateTransaction.text = GetMask.getFormatedDate(deposit.date, GetMask.DAY_MONTH_YEAR_HOUR_MINUTE)
        binding.textAmountTransaction.text = getString(R.string.text_formated_value, GetMask.getFormatedValue(deposit.amount))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}