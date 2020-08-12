package duodev.take.eazy.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import duodev.take.eazy.R
import duodev.take.eazy.SharedViewModel.SharedViewModel
import duodev.take.eazy.base.BaseFragment
import duodev.take.eazy.cart.Adapter.CartItemParentAdapter
import duodev.take.eazy.cart.ViewModel.CartViewModel
import duodev.take.eazy.pojo.CartItems
import duodev.take.eazy.pojo.OrderItems
import kotlinx.android.synthetic.main.fragment_cart.*

class CartFragment : BaseFragment(), CartItemParentAdapter.OnClick {

    private val cartParentAdapter by lazy { CartItemParentAdapter(mutableListOf(), this) }
    private val cartViewModel by viewModels<CartViewModel> { viewModelFactory }
    private val sharedViewModel by viewModels<SharedViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setUpObserver()
        setUpRecycler()
    }

    private fun setUpObserver() {
        cartViewModel.fetchItems()
        cartViewModel.items.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                cartParentAdapter.addData(it)
            }
        })
    }

    private fun setUpRecycler() {
        itemsCartParentRecycler.apply {
            adapter = this@CartFragment.cartParentAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    companion object {
        fun newInstance() = CartFragment()
    }

    override fun addToCart(item: CartItems) {
        sharedViewModel.setData(item)
    }

    override fun subFromCart(item: CartItems) {
        sharedViewModel.subtractData(item)
    }

    override fun removeFromCart(itemId: String) {
        sharedViewModel.removeFromCart(itemId)
    }
}