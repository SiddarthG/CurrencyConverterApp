package com.teststudio.currencyconverter.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.teststudio.currencyconverter.R
import com.teststudio.currencyconverter.data.uimodel.Currency
import com.teststudio.currencyconverter.databinding.RatesCardItemRowBinding

class RatesAdapter(context: Context) : RecyclerView.Adapter<RatesAdapter.ViewHolder>() {

    private lateinit var binding : RatesCardItemRowBinding
    private var dataList : List<Currency> = listOf()

    fun setData(list: List<Currency>) {
        dataList = list
        notifyDataSetChanged()
    }

    class ViewHolder(private val itemRowBinding: RatesCardItemRowBinding) : RecyclerView.ViewHolder(itemRowBinding.root) {
        fun bindTo(item : Currency) {
            itemRowBinding.data = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),R.layout.rates_card_item_row, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(dataList[position])
    }

    override fun getItemCount() = dataList.size

}
