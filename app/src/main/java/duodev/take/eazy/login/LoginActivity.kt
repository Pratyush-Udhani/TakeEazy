package duodev.take.eazy.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import duodev.take.eazy.R
import duodev.take.eazy.base.BaseActivity

class LoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()
    }

    private fun init() {
        setUpFragment()
    }

    private fun setUpFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.loginContainer, LoginFragment.newInstance())
        fragmentTransaction.commit()
        fragmentTransaction.addToBackStack(null)
    }

    companion object {
        fun newInstance(context: Context) = Intent(context, LoginActivity::class.java)
    }
}