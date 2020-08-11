package duodev.take.eazy.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import duodev.take.eazy.R
import duodev.take.eazy.stores.StoresItemsFragment
import duodev.take.eazy.base.BaseFragment
import duodev.take.eazy.home.Adapter.CategoryHomeAdapter
import duodev.take.eazy.home.Adapter.StoreHomeAdapter
import duodev.take.eazy.home.ViewModel.HomeViewModel
import duodev.take.eazy.pojo.Store
import duodev.take.eazy.utils.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlin.collections.LinkedHashMap


class HomeFragment : BaseFragment(), StoreHomeAdapter.OnClick, CategoryHomeAdapter.OnClick {

    private var longitude: Double? = 0.0
    private var latitude: Double? = 0.0
    private val homeViewModel by viewModels<HomeViewModel> { viewModelFactory }
    private val storeAdapter by lazy { StoreHomeAdapter(mutableMapOf<Store, String>() as LinkedHashMap<Store, String>, this) }
    private val categoryAdapter by lazy { CategoryHomeAdapter(mutableListOf(), this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setUpListeners()
        setUpRecycler()
        getLocation()
        setUpCategories()
    }

    private fun setUpCategories() {
        val categoryArray = resources.getStringArray(R.array.categories)
        val categoryList: MutableList<String> = mutableListOf()
        for(element in categoryArray) {
            categoryList.add(element.toString())
        }
        categoryAdapter.addData(categoryList)
    }

    private fun setUpListeners() {
        permissionText.setOnClickListener {
            getLocation()
        }
    }

    private fun setUpRecycler() {
        storesRecycler.apply {
            adapter = this@HomeFragment.storeAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        categoriesRecycler.apply {
            adapter = this@HomeFragment.categoryAdapter
            layoutManager = GridLayoutManager(requireContext(), 4, GridLayoutManager.VERTICAL, false)
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
                    homeViewModel.fetchData().observe(viewLifecycleOwner, Observer {list ->
                        if (list.isNotEmpty()) {
                            sortData(list)
                        }
                    })

                }
            }

//            longitude = location?.longitude!!
//            latitude = location?.latitude
        }

    }

    private fun sortData(list: List<Store>) {
        Log.d("TETETE", list.toString() + " here")
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
        loader.makeGone()
        storeAdapter.addData(sortedMap)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchStores()
                permissionText.makeGone()
            } else {
                requireContext().toast("Please grant permissions")
                loader.makeGone()
                permissionText.makeVisible()
            }
        }
    }

    private fun changeFragment(fragment: Fragment) {
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.homeContainer, fragment)
        fragmentTransaction?.commit()
    }

    companion object {

        fun newInstance() = HomeFragment()
    }
    override fun onStoreClicked(store: Store, distance: String) {
        StoreLocation.storeGeo = store.storeLocation
        changeFragment(StoresItemsFragment.newInstance(store))
    }

    override fun onCategoryClicked(category: String) {

    }

}