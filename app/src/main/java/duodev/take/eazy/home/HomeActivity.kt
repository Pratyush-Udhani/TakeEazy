package duodev.take.eazy.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import duodev.take.eazy.R
import duodev.take.eazy.base.BaseActivity
import duodev.take.eazy.cart.CartFragment
import duodev.take.eazy.orders.OrdersFragment
import duodev.take.eazy.stores.StoresItemsFragment
import duodev.take.eazy.stores.StoresListFragment
import duodev.take.eazy.utils.toast
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity() {

    lateinit var currentFragment: Fragment
    private var backPressed: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        init()
    }

    private fun init() {
        setUpListeners()
        setUpFragment()
    }

    private fun setUpListeners() {
        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigationCart -> {
                    changeFragment(CartFragment.newInstance())
                  return@setOnNavigationItemSelectedListener true 
                }
                R.id.navigationShop -> {
                    changeFragment(HomeFragment.newInstance())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigationOrders -> {
                    changeFragment(OrdersFragment.newInstance())
                    return@setOnNavigationItemSelectedListener true
                }
            }
                false
        }
    }

    private fun setUpFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.homeContainer, HomeFragment.newInstance())
        fragmentTransaction.commit()
        currentFragment = Fragment()

    }

    private fun changeFragment(fragment: Fragment) {
        if (currentFragment != fragment) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.homeContainer, fragment)
            fragmentTransaction.commit()
            fragmentTransaction.addToBackStack(null)
            currentFragment = fragment
        } else {
            toast("here")
        }
    }

    override fun onBackPressed() {
        currentFragment = supportFragmentManager.findFragmentById(R.id.homeContainer)!!
        if (currentFragment is StoresItemsFragment) {
            supportFragmentManager.popBackStackImmediate()
        } else {
            if (currentFragment is StoresListFragment) {
                supportFragmentManager.popBackStackImmediate()
            } else {
                if (backPressed.plus(2000) >= System.currentTimeMillis()) {
                    super.onBackPressed()
                    finishAffinity()
                } else {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.press_again_to_exit),
                        Toast.LENGTH_SHORT
                    ).show()
                    backPressed =
                        System.currentTimeMillis()
                }
            }
        }
    }

    companion object {
        fun newInstance(context: Context) = Intent(context, HomeActivity::class.java)
    }
}