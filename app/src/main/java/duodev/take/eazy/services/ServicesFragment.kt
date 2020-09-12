package duodev.take.eazy.services

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import duodev.take.eazy.R
import duodev.take.eazy.SharedViewModel.SharedViewModel
import duodev.take.eazy.base.BaseFragment
import duodev.take.eazy.pojo.Service
import duodev.take.eazy.utils.*
import kotlinx.android.synthetic.main.fragment_services.*

class ServicesFragment : BaseFragment() {

    private var storeCategory = ""
    private var storeAdd = ""
    private var deliveryAdd = ""
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
        return inflater.inflate(R.layout.fragment_services, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setUpUI()
        setUpListeners()
    }

    private fun setUpListeners() {
        storeAddress.setOnClickListener {
            startActivityForResult(ReturnLocationActivity.newInstance(requireContext(), STORE_ADDRESS), STORE_ADDRESS)
        }

        deliveryAddress.setOnClickListener {
            startActivityForResult(ReturnLocationActivity.newInstance(requireContext(), DELIVERY_ADDRESS), DELIVERY_ADDRESS)
        }

        buyServiceButton.setOnClickListener {
            if (storeCategory != "") {
                if (deliveryAdd != "") {
                    if (storeAdd != "") {
                        if (items.text.isNotEmpty()) {
                            confirmBooking()
                        } else {
                            toast("Enter items")
                        }
                    } else {
                        toast("Select store address")
                    }
                } else {
                    toast("Select delivery address")
                }
            } else {
                toast("Select Category")
            }
        }
    }

    private fun confirmBooking() {
        sharedViewModel.confirmServiceBooking(
            Service(
                storeAddress = storeAdd,
                deliveryAddress = deliveryAdd,
                items = items.trimString(),
                orderId = getRandomString(20)
            )
        )
        setDefs()
        toast("The service has been successfully booked.")
    }

    private fun initPlaces() {


        (autocomplete_fragment as AutocompleteSupportFragment).setPlaceFields(listOf(Place.Field.NAME, Place.Field.ID, Place.Field.ADDRESS))

        (autocomplete_fragment as AutocompleteSupportFragment).setOnPlaceSelectedListener(object :
            PlaceSelectionListener {
            override fun onPlaceSelected(p0: Place) {
                storeAddress.text = p0.address.toString()
                log("${p0.address}")
            }

            override fun onError(p0: Status) {
                log(p0.toString())
            }

        })
    }

    private fun setDefs() {
        storeAddress.text = ""
        deliveryAddress.text = ""
        items.text.clear()
        storeAdd = ""
        deliveryAdd = ""
        storeCategory = ""
        storeCategoryText.setText("")
    }

    private fun setUpUI() {
        val storeCategoryAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.serviceCategories,
            R.layout.autocomplete_text
        )

        storeCategoryText.threshold = 0
        storeCategoryText.setAdapter(storeCategoryAdapter)
        storeCategoryText.setDropDownBackgroundResource(R.drawable.autocomplete_dropdown)
        storeCategoryText.dropDownVerticalOffset = 20

        storeCategoryText.setOnItemClickListener { adapterView, view, pos, l ->
            storeCategory = storeCategoryAdapter.getItem(pos).toString()
        }

        storeCategoryText.doOnTextChanged { text, start, before, count ->
            storeCategory = ""
        }

        storeCategoryText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                storeCategoryText.showDropDown()
                storeCategoryText.addTextChangedListener {
                    if (it.isNullOrEmpty()) {
                        storeCategoryText.showDropDown()
                    }
                }
            } else {
                closeKeyboard(requireContext(), view)
                view.clearFocus()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == STORE_ADDRESS) {
            if (data != null) {
                storeAdd = data.getStringExtra(ReturnLocationActivity.ADDRESS).toString()
                storeAddress.text = storeAdd
            }
        }

        if (requestCode == DELIVERY_ADDRESS) {
            if (data != null) {
                deliveryAdd = data.getStringExtra(ReturnLocationActivity.ADDRESS).toString()
                deliveryAddress.text = deliveryAdd
            }
        }
    }

    companion object {

        private const val STORE_ADDRESS = 13
        private const val DELIVERY_ADDRESS = 14

        fun newInstance() = ServicesFragment()
    }
}