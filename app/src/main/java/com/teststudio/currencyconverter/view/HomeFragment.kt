package com.teststudio.currencyconverter.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.teststudio.currencyconverter.databinding.FragmentHomeBinding
import com.teststudio.currencyconverter.util.FragmentListener
import com.teststudio.currencyconverter.viewmodel.CurrencyListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    lateinit var fragmentListener: FragmentListener
    private lateinit var binding : FragmentHomeBinding
    private val vm by lazy {
        ViewModelProvider(requireActivity())[CurrencyListViewModel::class.java]
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentListener = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        vm.syncCurrencyList()
    }

    private fun initObservers() {
        vm.showProgress.observe(viewLifecycleOwner) {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        vm.currencyList.observe(viewLifecycleOwner) {
            it?.let {
                if (it.isNotEmpty()) {
                    fragmentListener.openFragment(CurrencyConversionFragment.newInstance())
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}