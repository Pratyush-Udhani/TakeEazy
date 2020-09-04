package duodev.take.eazy.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import duodev.take.eazy.R
import duodev.take.eazy.base.BaseActivity
import duodev.take.eazy.cart.CartFragment
import duodev.take.eazy.pojo.Users
import duodev.take.eazy.utils.*
import kotlinx.android.synthetic.main.activity_sign_up2.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit


class SignUpActivity : BaseActivity(), View.OnClickListener {

    private lateinit var auth: FirebaseAuth
    private var verificationInProgress = true

    private var storedVerificationId: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var phoneNumber = ""
    private val AUTOCOMPLETE_REQUEST_CODE = 1
    private var address: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up2)
        init()
    }

    private fun init() {
        auth = Firebase.auth
        setUpUI()
        setUpListeners()
        setCallbacks()
        initPlaces()
        val apiKey = getString(R.string.api_key)

        if (!Places.isInitialized()) {
            applicationContext?.let { Places.initialize(it, apiKey) }
        }

        val placesClient = Places.createClient(this)
    }

    private fun initPlaces() {


        (autocomplete_fragment as AutocompleteSupportFragment).setPlaceFields(listOf(Place.Field.NAME, Place.Field.ID, Place.Field.ADDRESS))

        (autocomplete_fragment as AutocompleteSupportFragment).setOnPlaceSelectedListener(object :
            PlaceSelectionListener {
            override fun onPlaceSelected(p0: Place) {
                address = p0.address.toString()
                userAddress.setText(address)
                log("${p0.address}")
            }

            override fun onError(p0: Status) {
                log(p0.toString())
            }

        })
    }

    private fun setCallbacks() {
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                log("called verify success")
                verificationInProgress = false
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                verificationInProgress = false
                loader.makeGone()
                log("called failure")
                toast("Verification failed. Please enter this device's phone number.")
                if (e is FirebaseAuthInvalidCredentialsException) {
                } else if (e is FirebaseTooManyRequestsException) {
                    toast(e.toString())
                }
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                log(verificationId+ "$token")
                storedVerificationId = verificationId
                resendToken = token
            }
        }
    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Users(
                        userPhone = phoneNumber,
                        userPassword = generateHash(userPassword.trimString()),
                        userAddress = userAddress.trimString()
                    ).also {
                        lifecycleScope.launch {
                            withContext(Dispatchers.Default) {
                                firebaseFirestore.collection(USERS).document(it.userPhone).set(it)
                                return@withContext
                            }
                            return@launch
                        }
                        pm.phone = phoneNumber
                        pm.address = userAddress.trimString()
                        toast("Account added")
                        loader.makeGone()
                        startActivity(LoginActivity.newInstance(this))
                    }
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        toast(task.exception.toString())
                        loader.makeGone()
                    }
                }
            }
    }


    private fun setUpUI() {
        userPhone.addTextChangedListener {
            if (userPhone.text.length == 10) {
                closeKeyboard(this, userPhone)
            }
        }
    }

    private fun setUpListeners() {
        loginButton.setOnClickListener {
            startActivity(LoginActivity.newInstance(this))
        }

        userAddress.setOnClickListener { initPlaces() }

        signUpButton.setOnClickListener {
            if (userAddress.text.isNotEmpty()) {

                loader.makeVisible()
                if (userPhone.hasFocus()) {
                    closeKeyboard(this, userPhone)
                } else {
                    if (userAddress.hasFocus()) {
                        closeKeyboard(this, userAddress)
                    }
                }
                pm.address = address

                firebaseFirestore.collection(USERS).document(pm.phone).update("userAddress",pm.address)
                setResult(ADDRESS_ADDED)
                finish()
//            setUpAccount()
            } else {
                toast("Please select address")
            }
        }
    }

    private fun checkAccount() {
        log("called check")
        firebaseFirestore.collection(USERS).document(phoneNumber).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    log("success")
                    if (it.result!!.exists()) {
                        loader.makeGone()
                        toast("Account already exists for this number")
                    } else {
                        if (verificationInProgress && validatePhoneNumber()) {
                            log(phoneNumber)
                            log("called here")
                            startPhoneNumberVerification(phoneNumber)
                        }
                    }
                }
            }
    }

    private fun setUpAccount() {
        if (userPhone.text.isNotEmpty() && userAddress.text.isNotEmpty()) {
            if (userPhone.text.length == 10) {
                phoneNumber = "+91${userPhone.trimString()}"
                checkAccount()
            } else {
                toast("Enter a valid phone number")
                loader.makeGone()
            }
        } else {
            toast("Enter all details")
            loader.makeGone()
        }
    }

//    private fun changeFragment(fragment: Fragment) {
//        val fragmentTransaction = supportFragmentManager?.beginTransaction()
//        fragmentTransaction?.replace(R.id.loginContainer, fragment)
//        fragmentTransaction?.commit()
//        fragmentTransaction?.addToBackStack(null)
//    }

    private fun validatePhoneNumber(): Boolean {

        return true
    }

    private fun startPhoneNumberVerification(phoneNumber: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            30,
            TimeUnit.SECONDS,
            this,
            callbacks)
    }

    companion object {

        const val ADDRESS = "address"
        const val ADDRESS_ADDED = 21

        fun newInstance(context: Context) = Intent(context, SignUpActivity::class.java)

    }

    override fun onClick(p0: View?) {

    }
}