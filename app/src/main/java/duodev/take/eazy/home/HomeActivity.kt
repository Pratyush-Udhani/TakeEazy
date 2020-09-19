package duodev.take.eazy.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import duodev.take.eazy.R
import duodev.take.eazy.about.AboutFragment
import duodev.take.eazy.base.BaseActivity
import duodev.take.eazy.cart.CartFragment
import duodev.take.eazy.changePassword.ChangePasswordFragment
import duodev.take.eazy.login.LoginActivity
import duodev.take.eazy.login.LoginFragment
import duodev.take.eazy.orders.OrdersFragment
import duodev.take.eazy.profile.ProfileFragment
import duodev.take.eazy.services.ServicesFragment
import duodev.take.eazy.services.ServicesOrders
import duodev.take.eazy.stores.StoresItemsFragment
import duodev.take.eazy.stores.StoresListFragment
import duodev.take.eazy.tAndC.TermsFragment
import duodev.take.eazy.utils.isAuth
import duodev.take.eazy.utils.makeGone
import duodev.take.eazy.utils.toast
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.side_drawer.*

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

        sideNavButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.END)
        }

        backButton.setOnClickListener {
            onBackPressed()
        }

        navigationTerms.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.END)
            changeFragment(TermsFragment.newInstance())
        }
        navigationServices.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.END)
            changeFragment(ServicesOrders.newInstance())
        }

        navigationChangePass.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.END)
            changeFragment(ChangePasswordFragment.newInstance())
        }

        navigationLogout.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.END)
            pm.phone = ""
            pm.address = ""
            pm.account = false
            startActivity(Intent(this, LoginActivity::class.java))
        }

        navigationAbout.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.END)
            changeFragment(AboutFragment.newInstance())
        }

        navigationProfile.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.END)
            changeFragment(ProfileFragment.newInstance())
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
            backButton.makeGone()
            supportFragmentManager.popBackStackImmediate()
            setUpFragment()
        } else {
            if (currentFragment is StoresListFragment) {
                backButton.makeGone()
                supportFragmentManager.popBackStackImmediate()
                setUpFragment()
            } else {
                if (currentFragment is ServicesFragment) {
                    supportFragmentManager.popBackStackImmediate()
                } else {
                    if (currentFragment is ProfileFragment) {
                        backButton.makeGone()
                        setUpFragment()
                        supportFragmentManager.popBackStackImmediate()
                    } else {
                        if (currentFragment is ServicesOrders) {
                            supportFragmentManager.popBackStackImmediate()
                            setUpFragment()
                            backButton.makeGone()
                        } else {
                            if (currentFragment is TermsFragment) {
                                supportFragmentManager.popBackStackImmediate()
                                setUpFragment()
                                backButton.makeGone()
                            } else {
                                if (currentFragment is AboutFragment) {
                                    supportFragmentManager.popBackStackImmediate()
                                    setUpFragment()
                                    backButton.makeGone()
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
                    }
                }
            }
        }
    }

    companion object {
        fun newInstance(context: Context) = Intent(context, HomeActivity::class.java)
    }
}