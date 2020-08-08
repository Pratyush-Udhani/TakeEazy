package duodev.take.eazy.base

import androidx.lifecycle.ViewModel
import duodev.take.eazy.utils.PreferenceUtils

abstract class BaseViewModel: ViewModel() {

    protected val pm = PreferenceUtils
}