package duodev.take.eazy.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import duodev.take.eazy.TakeEasyApp
import duodev.take.eazy.utils.PreferenceUtils
import javax.inject.Inject

abstract class BaseActivity: AppCompatActivity() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var firestore: FirebaseFirestore

    @Inject
    lateinit var firebaseFirestore: FirebaseFirestore

//    @Inject
//    lateinit var viewModelFactory: ViewModelFactory


    val pm = PreferenceUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TakeEasyApp.components.inject(this)
    }
}