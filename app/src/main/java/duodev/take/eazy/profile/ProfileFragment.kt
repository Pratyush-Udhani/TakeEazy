package duodev.take.eazy.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import duodev.take.eazy.R
import duodev.take.eazy.base.BaseFragment
import duodev.take.eazy.home.HomeActivity
import duodev.take.eazy.utils.USERS
import duodev.take.eazy.utils.log
import duodev.take.eazy.utils.makeVisible
import duodev.take.eazy.utils.trimString
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setupUI()
        setListeners()
    }

    private fun setListeners() {

        // Add the edit buttons and uncomment the code below to make it happen

//        phoneNumberEdit.setOnClickListener {
//            firebaseFirestore.collection(USERS).document(phoneNumber.trimString()).update("userPhone", "<new number>").addOnSuccessListener {
//                log("Updated successfully")
//            }
//        }
//
//        addressEdit.setOnClickListener {
//            firebaseFirestore.collection(USERS).document(phoneNumber.trimString()).update("userAddress", "<new address>").addOnSuccessListener {
//                log("Updated successfully")
//            }
//        }
    }

    private fun setupUI() {
        (activity as HomeActivity).backButton.makeVisible()
        phoneNumber.text = pm.phone
        address.text = pm.address
    }

    companion object {

        fun newInstance() = ProfileFragment()
    }
}