package duodev.take.eazy.utils

import android.preference.PreferenceManager
import duodev.take.eazy.TakeEasyApp
import duodev.take.eazy.pojo.Users

object PreferenceUtils {

    private val pm = PreferenceManager.getDefaultSharedPreferences(TakeEasyApp.instance)

    private const val PHONE = "phone"
    private const val HASH = "hash"
    private const val ACCOUNT = "account"
    private const val ADDRESS = "address"
    private const val PRESCRIPTION = "prescription"

    var phone: String
        get() = pm.getString(PHONE, "") ?: ""
        set(value) {
            pm.edit().putString(PHONE, value).apply()
        }

    var hash: String
        get() = pm.getString(HASH, "") ?: ""
        set(value) {
            pm.edit().putString(HASH, value).apply()
        }

    var account: Boolean
        get() = pm.getBoolean(ACCOUNT, false)
        set(value) {
            pm.edit().putBoolean(ACCOUNT, value).apply()
        }

    var address: String
        get() = pm.getString(ADDRESS, "")!!
        set(value) {
            pm.edit().putString(ADDRESS, value).apply()
        }

    var prescription: String
        get() = pm.getString(PRESCRIPTION, "")!!
        set(value) {
            pm.edit().putString(PRESCRIPTION, value).apply()
        }

    fun getUser(): Users {
        return Users()
    }

    fun setUser(users: Users) {
        val pm = PreferenceUtils
        pm.account = true
        pm.phone = users.userPhone.trimString()
        pm.hash = users.userPassword.trimString()
    }

    fun resetUser() {
        val pm = PreferenceUtils
        pm.phone = ""
        pm.hash = ""
        pm.account = false
    }
}