package duodev.take.eazy.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import duodev.take.eazy.R
import duodev.take.eazy.base.BaseFragment
import duodev.take.eazy.home.HomeActivity
import duodev.take.eazy.pojo.Users
import duodev.take.eazy.utils.*
import kotlinx.android.synthetic.main.fragment_login.loader
import kotlinx.android.synthetic.main.fragment_login.loginButton
import kotlinx.android.synthetic.main.fragment_login.signUpButton
import kotlinx.android.synthetic.main.fragment_login.userPhone
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class LoginFragment : BaseFragment() {

    private var phoneNumber = ""
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var storedVerificationId: String? = ""
    private var verificationInProgress = true
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setCallbacks()
        setUpObserver()
        setUpListeners()
    }


    private fun setUpObserver() {
        isAuth.observe(viewLifecycleOwner, Observer {
            if (isAuth.value!!) {
                pm.phone = userPhone.text.toString()
                pm.account = true
                isAuth.value = false
                loader.makeGone()
                startActivity(HomeActivity.newInstance(requireContext()))
            }
        })

        noPassword.observe(viewLifecycleOwner, Observer {
            if (noPassword.value!!) {
                loader.makeGone()
                activity?.toast("Invalid password")
            }
        })

        noEmail.observe(viewLifecycleOwner, Observer {
            if (noEmail.value!!) {
                loader.makeGone()
                activity?.toast("No user found")
            }
        })
    }

    private fun setUpListeners() {
        loginButton.setOnClickListener {
            if (userPhone.text?.isNotEmpty()!!) {
                if (userPhone.text?.length == 10) {
                    phoneNumber = "+91${userPhone.trimString()}"
                    closeKeyboard(requireContext(), it)
                    loader.makeVisible()
                    startPhoneNumberVerification(phoneNumber)
                } else {
                    activity?.toast("Enter a valid phone number")
                }
            } else {
                activity?.toast("Enter phone number")
            }
        }

        signUpButton.setOnClickListener {
            closeKeyboard(requireContext(), it)
//            changeFragment(SignUpFragment.newInstance())
            startActivity(SignUpActivity.newInstance(requireContext()))
        }
    }

    private fun startPhoneNumberVerification(phoneNumber: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            30,
            TimeUnit.SECONDS,
            requireActivity(),
            callbacks)
    }

    private fun changeFragment(fragment: Fragment) {
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.loginContainer, fragment)
        fragmentTransaction?.commit()
        fragmentTransaction?.addToBackStack(null)
    }

    private fun setCallbacks() {
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                log("called verify success")
                verificationInProgress = false
                signInWithPhoneAuthCredential(credential)
                loader.makeGone()
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
        firebaseFirestore.collection(USERS).document(phoneNumber).get().addOnSuccessListener {
            if (it.exists()) {
                pm.account = true
                pm.phone = phoneNumber
                loader.makeGone()
                firebaseFirestore.collection(USERS).document(pm.phone).get().addOnSuccessListener {
                    pm.address = it["userAddress"].toString()
                    startActivity(Intent(requireContext(), HomeActivity::class.java))
                }
            } else {
                Users(
                    userPhone = phoneNumber
                ).also {
                    lifecycleScope.launch {
                        withContext(Dispatchers.Default) {
                            firebaseFirestore.collection(USERS).document(it.userPhone).set(it)
                            return@withContext
                        }
                        return@launch
                    }
                    pm.phone = phoneNumber
                    pm.account = true
                    toast("Account added")
                    startActivity(HomeActivity.newInstance(requireContext()))
                    loader.makeGone()
                }
            }
        }
    }

    companion object {
        fun newInstance() = LoginFragment()
    }
}