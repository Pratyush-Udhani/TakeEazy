package duodev.take.eazy.tAndC

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import duodev.take.eazy.R
import duodev.take.eazy.base.BaseFragment
import duodev.take.eazy.home.HomeActivity
import duodev.take.eazy.utils.makeVisible
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_terms.*

class TermsFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_terms, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setUpUI()
    }

    private fun setUpUI() {
        (activity as HomeActivity).backButton.makeVisible()
        (activity as HomeActivity).headingText.text = "Terms and Conditions"
        privacyPolicy.settings.javaScriptEnabled = true
        privacyPolicy.loadUrl("https://takeeazy-0.flycricket.io/privacy.html")
    }

    companion object {
        fun newInstance() = TermsFragment()
    }
}