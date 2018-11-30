package br.com.mobapps.dynamicfeature

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recycler_item_bill.view.*
import java.text.NumberFormat

class BillsAdapter(
        private val data: MutableList<Bill>
): RecyclerView.Adapter<BillsAdapter.ViewHolder>() {

    private val numberFormat = NumberFormat.getCurrencyInstance()

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_bill, null, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(data[position])
    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindView(bill: Bill) {
            itemView.tvDescription.text = bill.description
            itemView.tvValue.text = numberFormat.format(bill.value)
        }
    }

}