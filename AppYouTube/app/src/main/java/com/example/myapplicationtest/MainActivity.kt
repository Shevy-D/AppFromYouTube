package com.example.myapplicationtest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.ims.RegistrationManager
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private val signInLauncher = registerForActivityResult(  // создали объект авторизации экрана
        FirebaseAuthUIActivityResultContract()
    ) { result ->
        onSignInResult(result)  // запуск самого экрана
    }

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intentToAnotherScreen = Intent(this, MoviesActivity::class.java)
        startActivity(intentToAnotherScreen)

        Log.d("testlog", "in onCreate")

        /*database = Firebase.database.reference  //инициализация базы данных

        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build())  //список регистрации, кt мы используем

        // Create and launch sign-in intent
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()  // создали интент для экрана firebase auth
        signInLauncher.launch(signInIntent)*/

    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse // результат с экрана Firebase auth
        if (result.resultCode == RESULT_OK) { // если рехультат ОК
            Log.d("testLogs", "RegistrationActivity registration success ${response?.email}")
            val authUser = FirebaseAuth.getInstance().currentUser // создаем объект текущего пользоателя Firebase auth
            authUser?.let {
                val firebaseUser = User(it.email.toString(), it.uid)
                database.child("users").child(firebaseUser.uid).setValue(firebaseUser)
                val intentToAnotherScreen = Intent(this, MoviesActivity::class.java)
                startActivity(intentToAnotherScreen)
                //Toast.makeText(this@MainActivity, "You clicked me.", Toast.LENGTH_SHORT).show()
            }

            // ...
        } else { // если результат не ОК, должны обработать ошибку
            Log.d("testLogs", "RegistrationActivity registration failure")
            Toast.makeText(this@MainActivity, "Something wrong happend", Toast.LENGTH_SHORT).show()
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
    }
}