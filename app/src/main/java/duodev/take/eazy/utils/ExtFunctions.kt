package duodev.take.eazy.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.lang.StringBuilder
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*

fun View.makeGone(){
    visibility = View.GONE
}

fun View.makeVisible(){
    visibility = View.VISIBLE
}

fun View.makeInvisible(){
    visibility = View.INVISIBLE
}

fun closeKeyboard(context: Context, currentFocus: View?) {
    if (currentFocus != null) {
        val imm: InputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
    }
}

fun openKeyboard(context: Context, currentFocus: View?) {
    val imm: InputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//    imm.showSoftInput(view, InputMethodManager.SHOW_FORCED)
    if (currentFocus != null) {
        imm.toggleSoftInputFromWindow(currentFocus.windowToken, InputMethodManager.SHOW_FORCED, 0)
    }
}

fun checkKeyboardState(context: Context, currentFocus: View?): Int {
    return if (currentFocus != null)
        0
    else
        -1
}

fun <T> log(message: T) {
    Log.d("TAG!!!!", message.toString())
}

fun Activity.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.toast(message: String) {
    Toast.makeText(this.requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun String.capitaliseFirst() = this[0].toUpperCase().toString() + this.substring(1)

fun TextView.trimString(): String = text.toString().trim()

fun EditText.trimString(): String = text.toString().trim()

fun String.trimString(): String = this.trim()

fun String.getCount(): Int = this.trimString().length

fun long2Date(date: Long): String {
    val formatter = SimpleDateFormat("dd-MM-yyyy")
    return formatter.format(Date(date))
}

fun long2time(time: Long): String {
    val formatter = SimpleDateFormat("K:mm a")
    return formatter.format(Date(time))
}

val Int.toPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Int.toDp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Float.toDp: Int
    get() = (this.toInt() / Resources.getSystem().displayMetrics.density).toInt()

fun getDateTime(dateInLong: Long, timeInLong: Long): Long {

    val formattedDate = long2Date(dateInLong)
    val parts = formattedDate.split("-")
    val calendar = Calendar.getInstance()

    calendar.timeInMillis = timeInLong
    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(parts[0]))
    calendar.set(Calendar.MONTH, Integer.parseInt(parts[1]) - 1)
    calendar.set(Calendar.YEAR, Integer.parseInt(parts[2]))

    return calendar.timeInMillis
}

fun getAddedDate(dateInLong: Long, months: Int): Long {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = dateInLong
    calendar.add(Calendar.MONTH, months)
    return calendar.timeInMillis
}

fun String.isInteger(): Boolean {
    try {
        Integer.parseInt(this)
    } catch (e: NumberFormatException) {
        return false
    }
    return true
}

fun generateHash(password: String): String {
    val bytes = password.toByteArray()
    val mb = MessageDigest.getInstance("SHA-256")
    val digest = mb.digest(bytes)
    return digest.fold("", { str, it -> str + "%02x".format(it) })
}

fun getRandomString(size: Int = 20): String {
    val generator = Random()
    val stringBuilder = StringBuilder(size)

    for (i in 0 until size) {
        stringBuilder.append(ALLOWED_CHARS[generator.nextInt(ALLOWED_CHARS.length)])
    }
    return stringBuilder.toString()
}


fun multiplyStrings(string1: String, string2: String): Int {
    return if (string1.isInteger() && string2.isInteger()) {
        val int1 = Integer.parseInt(string1)
        val int2 = Integer.parseInt(string2)
        int1 * int2
    } else {
        0
    }
}

// This is made for when database needs to be
// shifted from Firebase to a custom one

// You can use this call like this
// safeApiCall({suspend fun()}, {networkResult = it},{networkResult = it})
// Where networkResult is a val of type NetworkResult

suspend fun <T> safeApiCall(
    call: suspend () -> T,
    onSuccess: (NetworkResult.Success<T>) -> Unit,
    onFailure: (NetworkResult.Failure) -> Unit
) {
    runCatching {
        val response = call()
        onSuccess.invoke(NetworkResult.Success(response))
    }.onFailure {
        it.printStackTrace()
        onFailure.invoke(NetworkResult.Failure(it.message))
    }
}