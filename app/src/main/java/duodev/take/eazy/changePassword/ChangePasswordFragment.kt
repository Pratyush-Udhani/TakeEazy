package duodev.take.eazy.changePassword

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import duodev.take.eazy.R
import duodev.take.eazy.base.BaseFragment
import duodev.take.eazy.utils.*
import kotlinx.android.synthetic.main.fragment_change_password.*

class ChangePasswordFragment : BaseFragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_change_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        changePasswordButton.setOnClickListener {
            firebaseFirestore.collection(USERS).document(pm.phone).get().addOnSuccessListener {
                loader.makeVisible()
                if (generateHash(userCurrentPassword.trimString()) == it["userPassword"]) {
                    firebaseFirestore.collection(USERS).document(pm.phone).update("userPassword",
                        generateHash(userNewPassword.trimString())).addOnSuccessListener {
                        toast("Password updated successfully")
                        userCurrentPassword.setText("")
                        userNewPassword.setText("")
                        loader.makeGone()
                    }
                } else {
                    toast("Please enter the correct password")
                    loader.makeGone()
                }
            }
        }
    }

    companion object {

        fun newInstance() = ChangePasswordFragment()
    }
}