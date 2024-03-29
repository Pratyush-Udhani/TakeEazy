package duodev.take.eazy.cart

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import duodev.take.eazy.R
import duodev.take.eazy.SharedViewModel.SharedViewModel
import duodev.take.eazy.base.BaseFragment
import duodev.take.eazy.cart.Adapter.CartItemChildAdapter
import duodev.take.eazy.cart.ViewModel.CartViewModel
import duodev.take.eazy.home.HomeActivity
import duodev.take.eazy.login.SignUpActivity
import duodev.take.eazy.login.SignUpActivity.Companion.ADDRESS_ADDED
import duodev.take.eazy.payment.PaymentActivity
import duodev.take.eazy.payment.PaymentActivity.Companion.PAYMENT_SUCCESS
import duodev.take.eazy.pojo.CartItems
import duodev.take.eazy.utils.log
import duodev.take.eazy.utils.makeGone
import duodev.take.eazy.utils.makeVisible
import duodev.take.eazy.utils.toast
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_cart.*

class CartFragment : BaseFragment(), CartItemChildAdapter.OnClick {

    private val cartChildAdapter by lazy { CartItemChildAdapter(mutableListOf(), this) }
    private val cartViewModel by viewModels<CartViewModel> { viewModelFactory }
    private val sharedViewModel by viewModels<SharedViewModel> { viewModelFactory }
    private var storeId: String = ""
    private val fetched = MutableLiveData<Boolean>(false)
    private var totalPrice: Int = 0

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
        setUpUI()
        setUpListeners()
        setUpObserver()
        setUpRecycler()
    }

    private fun setUpUI() {
        (activity as HomeActivity).headingText.text = "Cart"
    }

    private fun setUpListeners() {
        buyItemsButton.setOnClickListener {
            totalPrice = CartItemChildAdapter.getTotal()
            if (storeId != "") {
                if (pm.address != "") {
                startActivityForResult(PaymentActivity.newInstance(requireContext(), totalPrice),  PAYMENT)
                } else {
                    startActivityForResult(SignUpActivity.newInstance(requireContext()), ADDRESS)
                }
            }
        }

        clearCartButton.setOnClickListener {
            clearCart(storeId)
        }
    }

    private fun clearCart(storeId: String) {
        cartViewModel.clearCart(storeId)
        noItemsText.makeVisible()
        clearCartButton.makeGone()
        cartChildAdapter.clearCart()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PAYMENT && resultCode == PAYMENT) {
            sharedViewModel.orderItems(storeId)
            cartChildAdapter.removeData()
            cartChildAdapter.notifyDataSetChanged()
            clearCartButton.makeGone()
            totalPriceText.text = "-"
            toast("Items bought")
        }
        if (requestCode == ADDRESS && resultCode == ADDRESS_ADDED) {
            startActivityForResult(PaymentActivity.newInstance(requireContext(), totalPrice), PAYMENT)
        }
    }

    private fun setUpObserver() {
        cartViewModel.getStoreId().observe(viewLifecycleOwner, Observer {
            if (it != "") {
                storeId = it.toString()
                fetched.value = true
                loader.makeGone()
                clearCartButton.makeVisible()
            } else {
                loader.makeGone()
                noItemsText.makeVisible()
                buyItemsButton.isClickable = false
                buyItemsButton.setCardBackgroundColor(Color.parseColor("#71AA9E"))
                totalPriceText.text = "-"
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

        const val PAYMENT = 13
        const val ADDRESS = 11

        fun newInstance() = CartFragment()
    }

    override fun addToCart(item: CartItems) {
        sharedViewModel.setData(item)
        totalPrice = CartItemChildAdapter.getTotal()
        totalPriceText.text = "Rs. $totalPrice"
        log("called add $totalPrice")

    }

    override fun subFromCart(item: CartItems) {
        sharedViewModel.subtractData(item)
        totalPrice = CartItemChildAdapter.getTotal()
        totalPriceText.text = "Rs. $totalPrice"
        log("called subtract $totalPrice")
    }

    override fun removeFromCart(itemId: String, storeId: String) {
        sharedViewModel.removeFromCart(itemId, storeId).observe(viewLifecycleOwner) {
            noItemsText.makeVisible()
            buyItemsButton.isClickable = false
            buyItemsButton.setCardBackgroundColor(Color.parseColor("#71AA9E"))
            totalPriceText.text = "-"
        }
        totalPrice = CartItemChildAdapter.getTotal()
        totalPriceText.text = "Rs. $totalPrice"
        log("called remove $totalPrice")
    }

    override fun updatePrice(totalPrice: Int) {
        log("called interface $totalPrice")
        totalPriceText.text = "Rs. $totalPrice"
        log("called again value $totalPrice textview value is ${totalPriceText.text}")
    }
}