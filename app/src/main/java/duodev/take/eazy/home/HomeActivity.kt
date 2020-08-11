package duodev.take.eazy.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import duodev.take.eazy.R
import duodev.take.eazy.base.BaseActivity

class HomeActivity : BaseActivity() {

    lateinit var currentFragment: Fragment
    private var backPressed: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        init()
    }

    private fun init() {
        setUpFragment()
    }

    private fun setUpFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.homeContainer, HomeFragment.newInstance())
        fragmentTransaction.commit()
        currentFragment = Fragment()

    }

    override fun onBackPressed() {
        currentFragment = supportFragmentManager.findFragmentById(R.id.homeContainer)!!
        if (currentFragment is HomeFragment) {
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

    companion object {
        fun newInstance(context: Context) = Intent(context, HomeActivity::class.java)
    }
}