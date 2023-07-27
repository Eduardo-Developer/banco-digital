package com.edudev.bancodigital.presenter.features.recharge

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.edudev.bancodigital.R
import com.edudev.bancodigital.data.model.Deposit
import com.edudev.bancodigital.data.model.Recharge
import com.edudev.bancodigital.databinding.FragmentDepositReceiptBinding
import com.edudev.bancodigital.databinding.FragmentRechargeReceiptBinding
import com.edudev.bancodigital.util.GetMask
import com.edudev.bancodigital.util.StateView
import com.edudev.bancodigital.util.initToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RechargeReceiptFragment : Fragment() {
    private var _binding: FragmentRechargeReceiptBinding? = null
    private val binding get() = _binding!!

    private val rechargeReceiptViewModel : RechargeReceiptViewModel by viewModels()

    private val args : RechargeReceiptFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRechargeReceiptBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(binding.toolbar, light = false)
        getRecharge()
        initListener()
    }

    private fun getRecharge() {
        rechargeReceiptViewModel.getRecharge(args.idRecharge).observe(viewLifecycleOwner) { stateView ->
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
            findNavController().popBackStack()
        }
    }

    private fun configData(recharge: Recharge) {
        binding.textCodeTransaction.text = recharge.id
        binding.textDateTransaction.text = GetMask.getFormatedDate(recharge.date, GetMask.DAY_MONTH_YEAR_HOUR_MINUTE)
        binding.textAmountRecharge.text = getString(R.string.text_formated_value, GetMask.getFormatedValue(recharge.amount))
        binding.txtNumber.text = recharge.number
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}