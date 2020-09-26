package duodev.take.eazy.stores

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.storage.FirebaseStorage
import duodev.take.eazy.R
import duodev.take.eazy.SharedViewModel.SharedViewModel
import duodev.take.eazy.base.BaseFragment
import duodev.take.eazy.home.HomeActivity
import duodev.take.eazy.pojo.CartItems
import duodev.take.eazy.pojo.Items
import duodev.take.eazy.pojo.Store
import duodev.take.eazy.stores.Adapter.StoreItemGroupAdapter
import duodev.take.eazy.stores.ViewModel.StoreViewModel
import duodev.take.eazy.utils.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.custom_snackbar.*
import kotlinx.android.synthetic.main.fragment_stores_items.*
import kotlinx.android.synthetic.main.fragment_stores_items.loader
import kotlinx.android.synthetic.main.fragment_stores_items.noItemsText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StoresItemsFragment : BaseFragment(), StoreItemGroupAdapter.OnItemClicked {

    private lateinit var store: Store
    private val itemsGroupAdapter by lazy { StoreItemGroupAdapter(mutableListOf(), this, store) }
    private val storeViewModel by viewModels<StoreViewModel> { viewModelFactory }
    private val sharedViewModel by viewModels<SharedViewModel> { viewModelFactory }
    private val fetchedCategories = MutableLiveData<Boolean>(false)
    private val sorted = MutableLiveData<Boolean>(false)
    private val itemsFetched = MutableLiveData<Boolean>(false)
    private var categoryList: List<String> = emptyList()
    private var cartList: MutableList<CartItems> = mutableListOf()
    private var itemList: MutableList<CartItems> = mutableListOf()
    private var imageUrl: Uri = Uri.EMPTY
    private var image_url: Uri = Uri.EMPTY
    private var storageReference = FirebaseStorage.getInstance().reference
    private lateinit var snackBar: Snackbar
    private val dialog by lazy { BottomSheetDialog(requireContext()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            store = it.getParcelable(STORE)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_stores_items, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        checkPrescription()
        setupUI()
        setListenters()
        setUpObserver()
        setUpRecycler()
    }

    private fun setListenters() {
        uploadPrescriptionText.setOnClickListener {
            openFilePicker(12);
        }
    }

    private fun checkPrescription() {
        if (store.storeCategory == MEDICINES) {
//            if (pm.prescription == "") {
//                snackBar = Snackbar.make(parentLayout,"You have not uploaded a prescription",
//                    Snackbar.LENGTH_INDEFINITE)
//                    .setAction("UPLOAD") {
//                        openFilePicker(12)
//                    }
//                snackBar.setActionTextColor(ActivityCompat.getColor(requireContext(),R.color.new_blue))
//
//                snackBar.show()
//
//            } else {
//                snackBar = Snackbar.make(parentLayout,"You have uploaded a prescription.", Snackbar.LENGTH_SHORT)
//                    .setAction("UPLOAD NEW") {
//
//                    }
//                snackBar.setActionTextColor(ActivityCompat.getColor(requireContext(),R.color.new_blue))
//                snackBar.show()
//            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (::snackBar.isInitialized)
            snackBar.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
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

    private fun setupUI() {
        (activity as HomeActivity).backButton.makeVisible()
        storeName.text = store.storeName
        storeCategory.text = store.storeCategory
        storeLocality.text = store.storeAddress

        if (store.storeCategory == MEDICINES) {
            uploadPrescriptionText.makeVisible()
        }
    }

    private fun setUpObserver() {
        storeViewModel.fetchCategories(store.storePhone).observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                categoryList = it
                fetchedCategories.value = true
            } else {
                loader.makeGone()
                noItemsText.makeVisible()
            }
        })

        fetchedCategories.observe(viewLifecycleOwner, Observer {
            if (fetchedCategories.value!!) {
                fetchItems()
            }
        })

        storeViewModel.fetchCartList(store.storePhone).observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                cartList.addAll(it)
                sortData()
            }
        })

        itemsFetched.observe(viewLifecycleOwner, Observer {
            if (itemsFetched.value!!) {
                sorted.value = true
            }
        })
    }

    private fun fetchItems() {
        categoryList.forEachIndexed { _, group ->
            storeViewModel.fetchSingleItems(group, store.storePhone).observe(viewLifecycleOwner, Observer {
                if (it.isNotEmpty()) {
                    itemList.addAll(it)
                    loader.makeGone()
                    itemsGroupAdapter.addData(Items(group, it))
                    itemsFetched.value = true
                }
            })
        }
    }

    private fun sortData() {
        sorted.observe(viewLifecycleOwner, Observer {
            if (sorted.value!!) {
                log("called update item: ${cartList.size}")
                for (element in cartList)
                    itemsGroupAdapter.updateItem(element)
            }
        })
    }


    private fun setUpRecycler() {
        storeItemGroupRecycler.apply {
            adapter = this@StoresItemsFragment.itemsGroupAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    companion object {

        private const val STORE = "store"

        fun newInstance(store: Store) = StoresItemsFragment().apply {
            arguments = Bundle().apply {
                putParcelable(STORE, store)
            }
        }
    }

    override fun addToCart(item: CartItems) {
        log("add to cart called+")
        firebaseFirestore.collection(USERS).document(pm.phone).collection(CART).get().addOnSuccessListener {
            log(it.documents.isEmpty().toString())
            if (it.documents.isEmpty().not()) {
                if (convertToPojo(it.documents[0].data!!, CartItems::class.java).storeId != item.storeId) {
                    toast("You cannot order from multiple stores at once")
                    return@addOnSuccessListener
                } else {
                    log("called else")
                    sharedViewModel.setData(item)
                }
            } else {
                sharedViewModel.setData(item)
            }
        }
    }

    override fun subFromCart(item: CartItems) {
        sharedViewModel.subtractData(item)
    }

    override fun removeFromCart(itemId: String, storeId: String) {
        sharedViewModel.removeFromCart(itemId, storeId)
    }

}