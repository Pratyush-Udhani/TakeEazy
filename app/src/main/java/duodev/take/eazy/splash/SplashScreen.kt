package duodev.take.eazy.splash

import android.os.Bundle
import android.os.Handler
import duodev.take.eazy.R
import duodev.take.eazy.base.BaseActivity
import duodev.take.eazy.home.HomeActivity
import duodev.take.eazy.login.LoginActivity
import duodev.take.eazy.pojo.Users
import duodev.take.eazy.utils.USERS
import duodev.take.eazy.utils.convertToPojo
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreen : BaseActivity() {

    private val SPLASH_SCREEN_TIMEOUT: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        setUpUsers()
        animateViews()
    }

    private fun animateViews() {
        splashBackground.animate().alpha(1f).duration = SPLASH_SCREEN_TIMEOUT
    }

    private fun setUpUsers() {
        if (pm.phone.isNotEmpty()) {
            firebaseFirestore.collection(USERS).document(pm.phone).get().addOnSuccessListener {
                if (it.exists())
                    pm.setUser(convertToPojo(it.data!!, Users::class.java))
                handleLogin()
            }
        } else {
            handleLogin()
        }
    }

    private fun handleLogin() {

        Handler().postDelayed({
            if (pm.account) {
                startActivity(HomeActivity.newInstance(this))
            } else {
                startActivity(LoginActivity.newInstance(this))
            }
        }, SPLASH_SCREEN_TIMEOUT)
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}