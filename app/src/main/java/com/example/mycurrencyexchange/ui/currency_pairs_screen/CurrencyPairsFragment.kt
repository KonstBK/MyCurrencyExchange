package com.example.mycurrencyexchange.ui.currency_pairs_screen

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mycurrencyexchange.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CurrencyPairsFragment : Fragment() {

    private val args by navArgs<CurrencyPairsFragmentArgs>()
    private val viewModel by viewModels<CurrencyPairsViewModel>()

    companion object {
        //fun newInstance() = CurrencyPairsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_currency_pairs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pairsProgressBar = view.findViewById<View>(R.id.pairsProgressBar)
        viewModel.pairsLoading.observe(viewLifecycleOwner) {isLoading ->
            if (isLoading){
                pairsProgressBar.visibility = View.VISIBLE
            } else pairsProgressBar.visibility = View.GONE
        }

        viewLifecycleOwner.lifecycleScope.launch {
            args.selectedCurrency?.let {
                viewModel.subscribeOn(it)
                view.findViewById<Button>(R.id.search_pair_button).setOnClickListener {
                    val text = view.findViewById<TextView>(R.id.pair_edit_text).text.toString()
                    viewModel.searchByCurrencyName(text)
                }
            }
        }
        viewModel.pairs.observe(
            viewLifecycleOwner
        ) { value -> initRecycler(value) }

    }

    private fun initRecycler(list: List<CurrencyPairsItem>) {
        val recycler = view?.findViewById<RecyclerView>(R.id.currencyPairsRecycler)
        recycler?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recycler?.adapter = CurrencyPairsAdapter(
            context = requireContext().applicationContext,
            list = list
        )
    }
}

class CurrencyPairsAdapter(
    private val context: Context,
    private val list: List<CurrencyPairsItem>
) :
    RecyclerView.Adapter<CurrencyPairsAdapter.CurrencyPairsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyPairsHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.currency_pairs_item,
                parent,
                false
            )
        return CurrencyPairsHolder(itemView)
    }

    override fun getItemCount(): Int = list.count()


    override fun onBindViewHolder(holder: CurrencyPairsHolder, position: Int) {
        holder.currencyNameTextView.text = list[position].pairName
        holder.currencyChangeTextView.text = context.resources.getString(R.string.currency_change,
            "%.2f".format(list[position].pairChange.toString().toFloat()))

        val drawable = when {
            list[position].pairChange > 0 -> R.drawable.baseline_keyboard_arrow_up_24
            list[position].pairChange < 0 -> R.drawable.baseline_keyboard_arrow_down_24
            list[position].pairChange == 0.0 -> R.drawable.baseline_circle_24
            else -> {
                throw IllegalArgumentException()
            }
        }

        holder.currencyDynamicImageView.setImageDrawable(AppCompatResources.getDrawable(context, drawable))
    }

    class CurrencyPairsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val currencyNameTextView: TextView = itemView.findViewById(R.id.currencyPairName)
        val currencyChangeTextView: TextView = itemView.findViewById(R.id.currencyPairChange)
        val currencyDynamicImageView: ImageView = itemView.findViewById(R.id.currencyPairDynamic)
    }
}

