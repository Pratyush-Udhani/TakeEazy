package duodev.take.eazy.stores

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import duodev.take.eazy.R
import duodev.take.eazy.base.BaseFragment
import duodev.take.eazy.pojo.Store
import duodev.take.eazy.stores.Adapter.StoreItemsAdapter
import duodev.take.eazy.stores.ViewModel.StoreViewModel
import kotlinx.android.synthetic.main.fragment_stores_items.*

class StoresItemsFragment : BaseFragment(), StoreItemsAdapter.OnClick {

    private lateinit var store: Store
    private val itemAdapter by lazy { StoreItemsAdapter(mutableListOf(), this) }

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
        setUpRecycler()
    }

    private fun setUpRecycler() {
        itemAdapter.addData(store.itemsList)
        storeItemsRecycler.apply {
            adapter = this@StoresItemsFragment.itemAdapter
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
}