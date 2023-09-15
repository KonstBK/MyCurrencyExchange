package com.example.mycurrencyexchange

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrenciesFragment: Fragment() {

    val viewModel by viewModels<CurrenciesViewModel>()

    fun showCurrenciesPairs(){
        findNavController().navigate(R.id.currencyPairsFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_currencies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.currencies.observe(viewLifecycleOwner, object: Observer<List<HolderItem>>{
            override fun onChanged(value: List<HolderItem>) {
                initRecycler(value)
            }

        })
    }

    private fun initRecycler(list: List<HolderItem>) {
        val recycler = view?.findViewById<RecyclerView>(R.id.currenciesRecycler)
        recycler?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recycler?.adapter = CurrenciesAdapter(
            context = requireContext().applicationContext,
            list = list
        )
    }
}

class CurrenciesAdapter(private val context: Context, private val list: List<HolderItem>): RecyclerView.Adapter<CurrenciesAdapter.CurrenciesHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrenciesHolder {
       val itemView = LayoutInflater.from(parent.context).inflate(R.layout.currency_item, parent, false)
        return CurrenciesHolder(itemView)
    }

    override fun onBindViewHolder(holder: CurrenciesHolder, position: Int) {
        holder.currencyNameTextView.text = list[position].name
        holder.currencyChangeTextView.text = list[position].change.toString() + "%"
        val drawable = when{
            list[position].change > 0 -> R.drawable.baseline_keyboard_arrow_up_24
            list[position].change < 0 -> R.drawable.baseline_keyboard_arrow_down_24
            list[position].change == 0.0 -> R.drawable.baseline_circle_24
            else -> {throw IllegalArgumentException()}
        }
        holder.currencyDynamicImageView.setImageDrawable(context.getDrawable(drawable))
    }

    override fun getItemCount(): Int = list.count()


    class CurrenciesHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val currencyNameTextView: TextView = itemView.findViewById(R.id.currencyName)
        val currencyChangeTextView: TextView = itemView.findViewById(R.id.currencyChange)
        val currencyDynamicImageView: ImageView = itemView.findViewById(R.id.currencyDynamic)
    }
}

