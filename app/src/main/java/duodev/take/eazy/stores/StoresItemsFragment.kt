package duodev.take.eazy.stores

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import duodev.take.eazy.R
import duodev.take.eazy.base.BaseFragment
import duodev.take.eazy.pojo.CartItems
import duodev.take.eazy.pojo.Store
import duodev.take.eazy.stores.Adapter.StoreItemGroupAdapter
import duodev.take.eazy.SharedViewModel.SharedViewModel
import duodev.take.eazy.stores.ViewModel.StoreViewModel
import kotlinx.android.synthetic.main.fragment_stores_items.*

class StoresItemsFragment : BaseFragment(), StoreItemGroupAdapter.OnItemClicked {

    private lateinit var store: Store
    private val itemsGroupAdapter by lazy { StoreItemGroupAdapter(mutableListOf(), this, store) }
    private val storeViewModel by viewModels<StoreViewModel> { viewModelFactory }
    private val sharedViewModel by viewModels<SharedViewModel> { viewModelFactory }

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
        setUpObserver()
        setUpRecycler()
    }

    private fun setUpObserver() {
        storeViewModel.fetchItems()
        storeViewModel.items.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                Log.d("ITEMS", it.toString())
                itemsGroupAdapter.addData(it)
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

        fun newInstance(store:Store) = StoresItemsFragment().apply {
            arguments = Bundle().apply {
                putParcelable(STORE, store)
            }
        }
    }

    override fun addToCart(item: CartItems) {
        sharedViewModel.setData(item)
    }

    override fun subFromCart(item: CartItems) {
        sharedViewModel.subtractData(item)
    }

    override fun removeFromCart(itemId: String, storeId: String) {
        sharedViewModel.removeFromCart(itemId, storeId)
    }

}