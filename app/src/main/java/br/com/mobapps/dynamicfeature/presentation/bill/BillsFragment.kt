package br.com.mobapps.dynamicfeature.presentation.bill

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.mobapps.dynamicfeature.R
import br.com.mobapps.dynamicfeature.date.Bill
import br.com.mobapps.dynamicfeature.date.Category
import br.com.mobapps.dynamicfeature.presentation.bill.adapter.BillsAdapter
import br.com.mobapps

/**
 * A simple [Fragment] subclass.
 *
 */
class BillsFragment: Fragment() {

    private lateinit var billsAdapter: BillsAdapter
    private lateinit var bills: MutableList<Bill>

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_bills, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bills = MutableList(20) {
            val category = when (it) {
                0, 1, 2, 14 -> Category.Food
                3, 4, 5, 12, 13 -> Category.Health
                6, 7, 8, 9, 10, 11-> Category.Clothing
                else -> Category.Recreation
            }

            return@MutableList Bill(description = "Description $it", value = it * 10.0, category = category)
        }

        billsAdapter = BillsAdapter(bills)

        rvBills.layoutManager = LinearLayoutManager(this
        rvBills.adapter = billsAdapter
        rvBills.setHasFixedSize(true)
    }


}
