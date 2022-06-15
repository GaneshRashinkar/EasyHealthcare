package com.ganeshrashinkar.easyhealthcare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.ganeshrashinkar.easyhealthcare.databinding.ActivityMainBinding
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    lateinit var mBinding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mBinding.cvProfile.setOnClickListener {
            val intent=Intent(this,PatientRegistration::class.java)
            startActivity(intent)
        }
        val signInLauncher=registerForActivityResult(
            FirebaseAuthUIActivityResultContract()
        ){
                res->
            this.onSignInResult(res)
        }
        val providers=arrayListOf(

            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        val signInIntent=AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(signInIntent)

        mBinding.cvChatBot.setOnClickListener {
            val intent= Intent(this, ChatActivity::class.java)
            startActivity(intent)
        }
        mBinding.cvMedicalTest.setOnClickListener {
            val intent= Intent(this, TemporatyActivity::class.java)
            startActivity(intent)
        }
        mBinding.cvAppointments.setOnClickListener {
            val intent= Intent(this, AppointmentConfirmationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            // ...
        } else {
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
//        AuthUI.getInstance()
//            .signOut(this)
//            .addOnCompleteListener {
//
//            }

    }
}