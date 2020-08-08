package duodev.take.eazy.Utils

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
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

fun checkKeyboardState(context: Context, currentFocus: View?): Int {
    return if (currentFocus != null)
        0
    else
        -1
}

fun showLog(tag:String, message:String){
    Log.d(tag,message)
}

fun log(message: String) {
    Log.d("TAG!!!!", message)
}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun String.capitaliseFirst() = this[0].toUpperCase().toString() + this.substring(1)

fun TextView.trimString(): String = text.toString().trim()

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