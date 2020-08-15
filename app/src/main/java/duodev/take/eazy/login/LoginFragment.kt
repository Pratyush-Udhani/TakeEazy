package duodev.take.eazy.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import duodev.take.eazy.R
import duodev.take.eazy.base.BaseFragment
import duodev.take.eazy.home.HomeActivity
import duodev.take.eazy.utils.*
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseFragment() {

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
        setUpObserver()
        setUpListeners()
    }


    private fun setUpObserver() {
        isAuth.observe(viewLifecycleOwner, Observer {
            if (isAuth.value!!) {
                pm.phone = userPhone.text.toString()
                pm.account = true
                isAuth.value = false
                startActivity(HomeActivity.newInstance(requireContext()))
            }
        })

        noPassword.observe(viewLifecycleOwner, Observer {
            if (noPassword.value!!) {
                activity?.toast("Invalid password")
            }
        })

        noEmail.observe(viewLifecycleOwner, Observer {
            if (noEmail.value!!) {
                activity?.toast("No user found")
            }
        })
    }

    private fun setUpListeners() {
        loginButton.setOnClickListener {
            if (userPhone.text.isNotEmpty()) {
                if (userPassword.text.isNotEmpty()) {
                    checkAuth("+91${userPhone.trimString()}", userPassword.trimString())
                } else {
                    activity?.toast("Enter password")
                }
            } else {
                activity?.toast("Enter phone number")
            }
        }

        signUpButton.setOnClickListener {
            changeFragment(SignUpFragment.newInstance())
        }
    }

    private fun changeFragment(fragment: Fragment) {
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.loginContainer, fragment)
        fragmentTransaction?.commit()
        fragmentTransaction?.addToBackStack(null)
    }

    companion object {
        fun newInstance() = LoginFragment()
    }
}