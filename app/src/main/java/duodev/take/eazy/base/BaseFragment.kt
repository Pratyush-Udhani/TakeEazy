package duodev.take.eazy.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import duodev.take.eazy.TakeEasyApp
import duodev.take.eazy.di.ViewModelFactory
import duodev.take.eazy.utils.PreferenceUtils
import javax.inject.Inject

abstract class BaseFragment: Fragment() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var firebaseFirestore: FirebaseFirestore

//    @Inject
//    lateinit var viewModelFactory: ViewModelFactory

    protected val pm = PreferenceUtils


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TakeEasyApp.components.inject(this)
    }
}