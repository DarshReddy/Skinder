package com.jedischool.skinder.ui.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.jedischool.skinder.R
import com.jedischool.skinder.data.api.RetrofitBuilder
import com.jedischool.skinder.data.model.AuthResponse
import com.jedischool.skinder.ui.base.ViewModelFactory
import com.jedischool.skinder.ui.viewmodel.MainViewModel
import com.jedischool.skinder.utils.Status
import com.royrodriguez.transitionbutton.TransitionButton


class LoginActivity : AppCompatActivity() {

    private lateinit var transitionButton: TransitionButton
    private lateinit var viewModel : MainViewModel
    private lateinit var sharedPreferences : SharedPreferences
    private lateinit var editor : SharedPreferences.Editor

    companion object {
        var TOKEN:String ? = null
        private val loginActivity : LoginActivity ?= null
        fun refresh() = loginActivity?.refreshToken()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel = ViewModelProvider(
                this,
                ViewModelFactory(RetrofitBuilder.apiService)
        ).get(MainViewModel::class.java)

        sharedPreferences = getSharedPreferences("AUTH", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("538224454275-00gviuea25t7t987jha3hpqiuf0indan.apps.googleusercontent.com")
            .requestEmail()
            .build()
        val googleClient = GoogleSignIn.getClient(this, gso)
        if (sharedPreferences.getString("token", null)!=null) {
            TOKEN = sharedPreferences.getString("token",null)
            refreshToken()
            Log.d("TOKEN",TOKEN!!)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        transitionButton = findViewById(R.id.login_button)
        transitionButton.setOnClickListener { btnClicked(googleClient) }
    }

    fun refreshToken() {
        viewModel.refreshToken().observe(this,  {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        TOKEN = resource.data?.accessToken
                        editor.putString("token", resource.data?.accessToken)
                        editor.commit()
                    }
                    Status.ERROR -> {
                        Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
                        Log.e("ERR", resource.message.toString())
                    }
                    Status.LOADING -> {
                    }
                }
            }
        })
    }

    private fun btnClicked(googleClient: GoogleSignInClient) {
        transitionButton.startAnimation()
        val signInIntent = googleClient.signInIntent
        startActivityForResult(signInIntent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1) {
            val task : Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignIn(task)
        }
    }

    private fun handleSignIn(task: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            transitionButton.stopAnimation(TransitionButton.StopAnimationStyle.EXPAND) { loggedIn(account?.idToken.toString()) }
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Sign In", "signInResult:failed code=" + e.statusCode)
        }
    }

    private fun loggedIn(token: String) {
        val params: MutableMap<String, String> = HashMap()
        params["idtoken"] = token
        viewModel.getToken(params).observe(this,  {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { response -> setUser(response) }
//                        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                    Status.ERROR -> {
                        Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
                        Log.e("ERR", resource.message.toString())
                    }
                    Status.LOADING -> {
                    }
                }
            }
        })
    }

    private fun setUser(response: AuthResponse) {
        editor.putString("token", response.accessToken)
        editor.commit()
        TOKEN = response.accessToken
        Log.d("TOKEN NEW",response.accessToken)
        Toast.makeText(this,"Welcome " + response.name, Toast.LENGTH_SHORT).show()
    }
}