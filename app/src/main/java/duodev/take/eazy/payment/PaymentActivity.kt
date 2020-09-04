package duodev.take.eazy.payment

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import duodev.take.eazy.R
import duodev.take.eazy.base.BaseActivity
import duodev.take.eazy.cart.CartFragment
import duodev.take.eazy.utils.toast
import kotlinx.android.synthetic.main.activity_payment.*
import org.json.JSONObject
import kotlin.math.ceil

class PaymentActivity : BaseActivity(), PaymentResultListener {

    private var totalPrice: Int = 0
    private var mode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        intent?.let {
            totalPrice = it.getIntExtra(PRICE, 0)
        }
        init()
    }

    private fun init() {
        setUpListeners()
    }

    private fun setUpListeners() {
        buyItemsButton.setOnClickListener {
            if (mode != "") {
                checkDetails()
            } else {
                toast("Select a method")
            }
        }

        payment_radio_group.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                (R.id.payment_debit) -> {
                    mode = "card"
                }
                (R.id.payment_credit) -> {
                    mode = "card"
                }
                (R.id.payment_paytm) -> {
                    mode = "upi"
                }
            }
        }
    }

    private fun checkDetails() {
        razorPayPayment(mode, (totalPrice * 100).toString())
    }


    private fun razorPayPayment(method: String, amount: String) {
        val checkout = Checkout()
        try {
            val options = JSONObject()
            options.put("name", "Take Eazy")
            options.put("description", "Payment for items")
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("currency", "INR")
            options.put("amount", amount)
            options.put("send_sms_hash", true)
            val prefill = JSONObject()
            prefill.put("email", "takeeazy1@gmail.com")
            prefill.put("contact", pm.phone)
            prefill.put("method", method)
            options.put("prefill", prefill)
            checkout.open(this, options)
        } catch (e: Exception) {
            // Handle Exception
            //  System.out.println(e.getMessage())
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    companion object {

        private const val PRICE = "price"
        const val PAYMENT_SUCCESS = 1

        fun newInstance(context: Context, price: Int) = Intent(context, PaymentActivity::class.java).apply {
            putExtra(PRICE, price)
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        setResult(CartFragment.PAYMENT)
        finish()
        setResult(PAYMENT_SUCCESS)
        overridePendingTransition(R.anim.slide_down, R.anim.slide_up)
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        toast("Payment error. Please try again")
        finish()
    }

}