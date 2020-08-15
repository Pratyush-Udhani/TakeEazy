package duodev.take.eazy.stores

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import duodev.take.eazy.R
import duodev.take.eazy.base.BaseFragment
import duodev.take.eazy.pojo.Store
import duodev.take.eazy.stores.Adapter.StoreListAdapter
import duodev.take.eazy.stores.ViewModel.StoreViewModel
import duodev.take.eazy.utils.*
import kotlinx.android.synthetic.main.fragment_stores_list.*


class StoresListFragment : BaseFragment(), StoreListAdapter.OnClick {

    private var category: String = ""
    private val storeAdapter by lazy { StoreListAdapter(mutableMapOf<Store, String>() as LinkedHashMap<Store, String>, this) }
    private val storeViewModel by viewModels<StoreViewModel> { viewModelFactory }
    private var longitude: Double? = 0.0
    private var latitude: Double? = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            category = it.getString(CATEGORY)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_stores_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setUpUI()
        setUpRecycler()
        getLocation()
    }

    private fun setUpUI() {
        categoryText.text = category
        if (category != ""){
            categoryText.makeVisible()
        }
    }

    private fun setUpRecycler() {
        storesRecycler.apply {
            adapter = this@StoresListFragment.storeAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun getLocation() {

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE), 1)
        } else {
            fetchStores()
        }

    }

    private fun fetchStores() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED
        ) {

        } else {
//            val location =  lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            val locationRequest = LocationRequest()
            locationRequest.interval = 10000
            locationRequest.fastestInterval = 3000
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            LocationServices.getFusedLocationProviderClient(requireContext()).lastLocation.addOnSuccessListener {
                log("success")
                if (it != null) {
                    log("not null")
                    latitude = it.latitude
                    longitude = it.longitude
                    storeViewModel.fetchData().observe(viewLifecycleOwner, Observer {list ->
                        if (list.isNotEmpty()) {
                            if (category == ""){
                                sortData(list)
                            } else {
                                filterCategory(list, category)
                            }
                        }
                    })

                }
            }

//            longitude = location?.longitude!!
//            latitude = location?.latitude
        }

    }

    private fun filterCategory(list: List<Store>, category: String) {
        val filteredList: MutableList<Store> = mutableListOf()
        val storeIds : MutableList<String> = mutableListOf()

        for (element in list){
            storeIds.add(element.storeId)
        }

        for (element in list) {
            if (element.storeCategory == category){
                filteredList.add(element)
                storeIds.remove(element.storeId)
                if (storeIds.size == 0) {
                 sortData(filteredList)
                }
            } else {
                storeIds.remove(element.storeId)
                if (storeIds.size == 0) {
                 sortData(filteredList)
                }
            }
        }
    }


    private fun sortData(list: List<Store>) {
        val sortedMap: LinkedHashMap<Store, String> = mutableMapOf<Store, String>() as LinkedHashMap<Store, String>
        val sortedList: MutableList<Store> = list as MutableList<Store>
        for (i in list.indices) {
            for (j in 0 until list.size - i - 1) {
                val distanceOne = getDistance(latitude!!, longitude!!, sortedList[j].storeLocation.latitude, sortedList[j].storeLocation.longitude)
                val distanceTwo = getDistance(latitude!!, longitude!!, sortedList[j+1].storeLocation.latitude, sortedList[j+1].storeLocation.longitude)
                if (distanceOne > distanceTwo) {
                    val temp = sortedList[j]
                    sortedList[j] = sortedList[j+1]
                    sortedList[j+1] = temp
                }
            }
        }

        for (element in sortedList) {
            sortedMap[element] =
                getDistance(latitude!!, longitude!!, element.storeLocation.latitude, element.storeLocation.longitude).toString()
        }
        storeAdapter.addData(sortedMap)
    }

    private fun changeFragment(fragment: Fragment) {
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.homeContainer, fragment)
        fragmentTransaction?.commit()
        fragmentTransaction?.addToBackStack(null)
    }
    companion object {

        private const val CATEGORY = "category"

        fun newInstance(category: String = "") = StoresListFragment().apply {
            arguments = Bundle().apply {
                putString(CATEGORY, category)
            }
        }
    }

    override fun onStoreClicked(store: Store, distance: String) {
        StoreLocation.storeGeo = store.storeLocation
        changeFragment(StoresItemsFragment.newInstance(store))
    }
}