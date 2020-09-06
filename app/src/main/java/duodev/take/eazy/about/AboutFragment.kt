package duodev.take.eazy.about

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import duodev.take.eazy.R
import duodev.take.eazy.home.HomeActivity
import duodev.take.eazy.utils.makeVisible
import kotlinx.android.synthetic.main.activity_home.*


class AboutFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setUpUI()
    }

    private fun setUpUI() {
        (activity as HomeActivity).headingText.text = "About Us"
        (activity as HomeActivity).backButton.makeVisible()
    }

    companion object {

        fun newInstance() = AboutFragment()
    }
}