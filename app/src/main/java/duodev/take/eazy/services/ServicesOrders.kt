package duodev.take.eazy.services

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import duodev.take.eazy.R
import duodev.take.eazy.SharedViewModel.SharedViewModel
import duodev.take.eazy.base.BaseFragment
import duodev.take.eazy.home.HomeActivity
import duodev.take.eazy.utils.makeGone
import duodev.take.eazy.utils.makeVisible
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_services_orders.*

class ServicesOrders : BaseFragment() {

    private val serviceAdapter by lazy { ServicesAdapter(mutableListOf()) }
    private val sharedViewModel by viewModels<SharedViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_services_orders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setUpObserver()
        setUpRecycler()
        setUpUI()
    }

    private fun setUpUI() {
        (activity as HomeActivity).backButton.makeVisible()
    }

    private fun setUpObserver() {
        sharedViewModel.fetchServices().observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                loader.makeGone()
                serviceAdapter.addData(it)
            } else {
                loader.makeGone()
                noOrdersText.makeVisible()
            }
        })
    }

    private fun setUpRecycler() {
        serviceRecycler.apply {
            adapter = this@ServicesOrders.serviceAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    companion object {
        fun newInstance() = ServicesOrders()
    }
}