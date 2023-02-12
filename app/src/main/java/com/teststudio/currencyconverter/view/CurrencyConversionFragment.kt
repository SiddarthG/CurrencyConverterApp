package com.teststudio.currencyconverter.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.teststudio.currencyconverter.databinding.FragmentCurrencyConversionBinding
import com.teststudio.currencyconverter.util.FragmentListener
import com.teststudio.currencyconverter.viewmodel.CurrencyListViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class CurrencyConversionFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var fragmentListener: FragmentListener
    private lateinit var binding : FragmentCurrencyConversionBinding
    private val vm by lazy {
        ViewModelProvider(requireActivity())[CurrencyListViewModel::class.java]
    }
    private val currenciesList = ArrayList<String>()
    private lateinit var adapter : ArrayAdapter<String>
    private lateinit var ratesAdapter : RatesAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentListener = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCurrencyConversionBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObservers()
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.dropdown.onItemSelectedListener = this
    }

    private fun initView() {
        adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, currenciesList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.dropdown.adapter = adapter

        binding.rvRates.apply {
            layoutManager = GridLayoutManager(context, 3)
            ratesAdapter = RatesAdapter(context, )
            adapter = ratesAdapter
        }

        binding.btnGo.setOnClickListener {
            val amt = binding.etAmount.text.toString()
            val currency = binding.dropdown.selectedItem.toString()
            getLatestValue(amt, currency)
        }
    }

    private fun getLatestValue(amt: String, currency: String) {
        if (vm.validate(amt)) {
            vm.getLatestValue(currency, amt)
        } else {
            showSnackBar("Enter valid Input")
        }
    }

    private fun showSnackBar(msg: String) {
        val mySnackbar = Snackbar.make(binding.container, msg, Snackbar.LENGTH_SHORT)
        mySnackbar.show()
    }

    private fun initObservers() {
        vm.showProgress.observe(viewLifecycleOwner) {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
                binding.rvRates.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.GONE
                binding.rvRates.visibility = View.VISIBLE
            }
        }

        vm.showSnackBar.observe(viewLifecycleOwner) {
            showSnackBar(it)
        }

        vm.currencyList.observe(viewLifecycleOwner) { list ->
            list?.let {
                val symbolList = list.map {
                    it.symbol
                }
                currenciesList.addAll(symbolList)
                adapter.notifyDataSetChanged()
            }
        }

        vm.currencyConversionResult.observe(viewLifecycleOwner) {
            it?.let { it1 -> ratesAdapter.setData(it1) }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = CurrencyConversionFragment()
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        val currency = parent.getItemAtPosition(pos).toString()
        val amt = binding.etAmount.text.toString()
        getLatestValue(amt, currency)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}