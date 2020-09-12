package duodev.take.eazy.stores

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.core.app.ActivityCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.storage.FirebaseStorage
import duodev.take.eazy.R
import duodev.take.eazy.base.BaseFragment
import duodev.take.eazy.home.HomeActivity
import duodev.take.eazy.pojo.Store
import duodev.take.eazy.stores.Adapter.StoreListAdapter
import duodev.take.eazy.stores.ViewModel.StoreViewModel
import duodev.take.eazy.utils.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_stores_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class StoresListFragment : BaseFragment(), StoreListAdapter.OnClick {

    private var category: String = ""
    private var search: Boolean = false
    private val storeAdapter by lazy { StoreListAdapter(mutableMapOf<Store, String>() as LinkedHashMap<Store, String>, this) }
    private val storeViewModel by viewModels<StoreViewModel> { viewModelFactory }
    private var longitude: Double? = 0.0
    private var latitude: Double? = 0.0
    private val storeList: MutableList<Store> = mutableListOf()
    private val searchedList: MutableList<Store> = mutableListOf()
    private var imageUrl: Uri = Uri.EMPTY
    private var image_url: Uri = Uri.EMPTY
    private var storageReference = FirebaseStorage.getInstance().reference
    private lateinit var snackBar: Snackbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            category = it.getString(CATEGORY)!!
            search = it.getBoolean(SEARCH)
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
        setUpSearchInterface()
        checkPrescription()
    }

    private fun checkPrescription() {
        if (category == MEDICINES) {
            if (pm.prescription == "") {
                snackBar = Snackbar.make(parentLayout,"You have not uploaded a prescription",Snackbar.LENGTH_INDEFINITE)
                    .setAction("UPLOAD") {
                        openFilePicker(12)
                    }
                snackBar.setActionTextColor(ActivityCompat.getColor(requireContext(),R.color.new_blue))
                snackBar.show()
            } else {
                snackBar = Snackbar.make(parentLayout,"You have uploaded a prescription.",Snackbar.LENGTH_SHORT)
                    .setAction("UPLOAD NEW") {

                    }
                snackBar.setActionTextColor(ActivityCompat.getColor(requireContext(),R.color.new_blue))
                snackBar.show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        log("called destroy view")
        if (::snackBar.isInitialized)
            snackBar.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        log("called destroy")
        if (::snackBar.isInitialized)
            snackBar.dismiss()
    }

    private fun openFilePicker(request: Int) {
        val intent: Intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(intent, request)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 12 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                image_url = data.data!!
                uploadFile()
                log(imageUrl.toString())
            }
        }
    }

    private fun uploadFile() {
        val progress = ProgressDialog(requireContext())
        progress.setMessage("Uploading prescription")
        progress.max = 100
        progress.show()

            if (Uri.EMPTY != image_url) {
              //  progressBar.makeVisible()
                val mStorageReference = storageReference.child(ITEM_IMAGE).child(
                    pm.phone + "." + getFileExtension(image_url)
                )
                lifecycleScope.launch {
                    withContext(Dispatchers.Default) {
                        var uploadTask = mStorageReference.putFile(image_url)
                        val urlTask = uploadTask.continueWithTask { task ->
                            if (!task.isSuccessful) {
                                task.exception?.let {
                                    throw it
                                }
                            }
                            mStorageReference.downloadUrl
                        }.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                pm.prescription = task.result.toString()
                                toast("Prescription Uploaded")
                                progress.dismiss()
                                checkPrescription()
                            } else {

                            }
                        }
                    }
                }
            } else {
                toast("Please select an image")
            }
    }

    private fun getFileExtension(uri: Uri): String? {
        val cr = context?.contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(cr?.getType(uri))
    }

    private fun setUpSearchInterface() {

        searchBox.addTextChangedListener {
            storeAdapter.removeData()
            searchedList.clear()
            for (element in storeList) {
                if (element.storeName.toLowerCase()
                        .contains(searchBox.trimString().toLowerCase())
                ) {
                    searchedList.add(element)
                }
            }

            if (searchedList.isNotEmpty()) {
                sortData(searchedList)
            }

        }
    }

    private fun setUpUI() {
        (activity as HomeActivity).backButton.makeVisible()
        categoryText.text = category
        if (category != ""){
            categoryText.makeVisible()
        }
        if (search) {
            log("here+")
            openKeyboard(requireContext(), searchBox)
            searchBox.requestFocus()
        }

        searchBox.setOnFocusChangeListener { view, b ->
            if (!b) {
                closeKeyboard(requireContext(), view)
            }
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
                                storeList.addAll(list)
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

        for (element in list) {
            storeIds.add(element.storePhone)
        }

        for (element in list) {
            if (element.storeCategory == category) {
                filteredList.add(element)
                storeIds.remove(element.storePhone)
                if (storeIds.size == 0) {
                    storeList.addAll(filteredList)
                    sortData(filteredList)
                }
            } else {
                storeIds.remove(element.storePhone)
                if (storeIds.size == 0) {
                    storeList.addAll(filteredList)
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
        loader.makeGone()
        if (sortedMap.isEmpty()) {
            noItemsText.makeVisible()
            log("called if")
        }
        else {
            storeAdapter.addData(sortedMap)
            log("called else $sortedMap")
        }
    }

    private fun changeFragment(fragment: Fragment) {
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.homeContainer, fragment)
        fragmentTransaction?.commit()
        fragmentTransaction?.addToBackStack(null)
    }
    companion object {

        private const val CATEGORY = "category"
        private const val SEARCH = "search"

        fun newInstance(category: String = "", search: Boolean = false) = StoresListFragment().apply {
            arguments = Bundle().apply {
                putString(CATEGORY, category)
                putBoolean(SEARCH, search)
            }
        }
    }

    override fun onStoreClicked(store: Store, distance: String) {
        if (category == MEDICINES) {
            StoreLocation.storeGeo = store.storeLocation
            changeFragment(StoresItemsFragment.newInstance(store))
        } else {
            StoreLocation.storeGeo = store.storeLocation
            changeFragment(StoresItemsFragment.newInstance(store))
        }
    }
}