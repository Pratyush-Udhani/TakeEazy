package duodev.take.eazy.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import duodev.take.eazy.R
import duodev.take.eazy.base.BaseFragment
import duodev.take.eazy.pojo.Users
import duodev.take.eazy.utils.*
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class SignUpFragment : BaseFragment(), View.OnClickListener {

    private lateinit var auth: FirebaseAuth
    private var verificationInProgress = true

    private var storedVerificationId: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var phoneNumber = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        auth = Firebase.auth
        setUpUI()
        setUpListeners()
        setCallbacks()
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
                    activity?.toast(e.toString())
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
            .addOnCompleteListener(requireActivity()) { task ->
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
                        activity?.toast("Account added")
                        loader.makeGone()
                        changeFragment(LoginFragment.newInstance())
                    }
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        activity?.toast(task.exception.toString())
                        loader.makeGone()
                    }
                }
            }
    }


    private fun setUpUI() {
        userPhone.addTextChangedListener {
            if (userPhone.text.length == 10) {
                closeKeyboard(requireContext(), userPhone)
            }
        }
    }

    private fun setUpListeners() {
        loginButton.setOnClickListener {
            changeFragment(LoginFragment.newInstance())
        }

        signUpButton.setOnClickListener {
            loader.makeVisible()
            if (userPhone.hasFocus()) {
                closeKeyboard(requireContext(), userPhone)
            } else {
                if (userAddress.hasFocus()) {
                    closeKeyboard(requireContext(), userAddress)
                }
            }
            setUpAccount()
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
                        activity?.toast("Account already exists for this number")
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
                activity?.toast("Enter a valid phone number")
                loader.makeGone()
            }
        } else {
            activity?.toast("Enter all details")
            loader.makeGone()
        }
    }

    private fun changeFragment(fragment: Fragment) {
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.loginContainer, fragment)
        fragmentTransaction?.commit()
        fragmentTransaction?.addToBackStack(null)
    }

    private fun validatePhoneNumber(): Boolean {

        return true
    }

    private fun startPhoneNumberVerification(phoneNumber: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            30,
            TimeUnit.SECONDS,
            requireActivity(),
            callbacks)
    }

    companion object {
        fun newInstance() = SignUpFragment()
    }

    override fun onClick(p0: View?) {

    }
}