package duodev.take.eazy.orders

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import duodev.take.eazy.R
import duodev.take.eazy.base.BaseFragment
import duodev.take.eazy.orders.Adapter.OrdersAdapter
import duodev.take.eazy.orders.ViewModel.OrdersViewModel
import duodev.take.eazy.utils.log
import kotlinx.android.synthetic.main.fragment_orders.*


class OrdersFragment : BaseFragment() {

    private val ordersAdapter by lazy { OrdersAdapter(mutableListOf()) }
    private val orderViewModel by viewModels<OrdersViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_orders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setUpObserver()
        setUpRecycler()
    }

    private fun setUpRecycler() {
        orderRecycler.apply {
            adapter = this@OrdersFragment.ordersAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setUpObserver() {
        orderViewModel.fetchOrders().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                ordersAdapter.addData(it)
                log("orders: ${it.size}")
            }
        })
    }

    companion object {
        fun newInstance() = OrdersFragment()
    }
}