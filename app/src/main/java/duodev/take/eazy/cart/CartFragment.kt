package duodev.take.eazy.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import duodev.take.eazy.R
import duodev.take.eazy.SharedViewModel.SharedViewModel
import duodev.take.eazy.base.BaseFragment
import duodev.take.eazy.cart.Adapter.CartItemChildAdapter
import duodev.take.eazy.cart.ViewModel.CartViewModel
import duodev.take.eazy.pojo.CartItems
import duodev.take.eazy.utils.log
import kotlinx.android.synthetic.main.fragment_cart.*

class CartFragment : BaseFragment(), CartItemChildAdapter.OnClick {

    private val cartChildAdapter by lazy { CartItemChildAdapter(mutableListOf(), this) }
    private val cartViewModel by viewModels<CartViewModel> { viewModelFactory }
    private val sharedViewModel by viewModels<SharedViewModel> { viewModelFactory }
    private var storeId: String = ""
    private val fetched = MutableLiveData<Boolean>(false)

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
        setUpListeners()
        setUpObserver()
        setUpRecycler()
    }

    private fun setUpListeners() {
        buyItemsButton.setOnClickListener {
            if (storeId != "") {
                sharedViewModel.orderItems(storeId)
                cartChildAdapter.removeData()
                cartChildAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun setUpObserver() {

        cartViewModel.getStoreId().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                storeId = it.toString()
                fetched.value = true
            }
        })

        fetched.observe(viewLifecycleOwner, Observer {
            if (fetched.value!!) {

                cartViewModel.fetchItems(storeId).observe(viewLifecycleOwner, Observer {
                    if (it.isNotEmpty()) {
                        cartChildAdapter.addData(it)
                    }
                })

            }
        })
    }

    private fun setUpRecycler() {
        itemsCartParentRecycler.apply {
            adapter = this@CartFragment.cartChildAdapter
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

    override fun removeFromCart(itemId: String, storeId: String) {
        sharedViewModel.removeFromCart(itemId, storeId)
    }
}