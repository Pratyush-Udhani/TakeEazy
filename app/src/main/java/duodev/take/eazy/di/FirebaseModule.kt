package duodev.take.eazy.di

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
class FirebaseModule : Activity(){

    @Provides
    fun provideFirebaseFirestore() : FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    fun provideFirebaseAuth() : FirebaseAuth{
        return FirebaseAuth.getInstance()
    }

}