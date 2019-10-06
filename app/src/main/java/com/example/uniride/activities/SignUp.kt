package com.example.uniride.activities

import android.app.PendingIntent.getActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.uniride.R
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.util.*

class SignUp: AppCompatActivity(){

     //private var callbackManager: CallbackManager? = null
     //private var accessToken: AccessToken? = null

     override fun onCreate(savedInstanceState: Bundle?) {

         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_sign_up)

         //FacebookSdk.sdkInitialize(applicationContext)
         //var loginButton = findViewById<Button>(R.id.login_button)

         /*login_button.setOnClickListener(View.OnClickListener {
             callbackManager = CallbackManager.Factory.create()
             LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))
             LoginManager.getInstance().registerCallback(callbackManager,
                 object : FacebookCallback<LoginResult> {
                     override fun onSuccess(loginResult: LoginResult) {
                         userID = loginResult.accessToken.userId
                         Log.d("LoginResult", userID+" "+Profile.getCurrentProfile().id)
                     }

                     override fun onCancel() {
                         Log.d("LoginResult", "Cancel")
                     }

                     override fun onError(error: FacebookException) {
                         Log.d("LoginResult", "Error")
                     }
                 })
             }
         )*/

         //sign up
         login.setOnClickListener {
             val email = username.text.toString()
             val password = password_signup.text.toString()
             val userFirstName = firstName.text.toString()
             val userLastName = lastName.text.toString()
             val address = address.text.toString()
             val fbUserID = userid.text.toString()

             if (email.isEmpty() || password.isEmpty()){
                 Toast.makeText(this, "Please enter text in the email/password", Toast.LENGTH_SHORT).show()
                 return@setOnClickListener
             }
             when{
//                 email.isEmpty() -> Toast.makeText(this, "Please enter text in the email", Toast.LENGTH_SHORT).show()
//                 password.isEmpty() -> Toast.makeText(this, "Please enter text in the password", Toast.LENGTH_SHORT).show()
                 userFirstName.isEmpty() -> Toast.makeText(this, "Please enter your first name!", Toast.LENGTH_SHORT).show()
                 userLastName.isEmpty() -> Toast.makeText(this, "Please enter your last name!", Toast.LENGTH_SHORT).show()
                 address.isEmpty() -> Toast.makeText(this, "Please enter your address!", Toast.LENGTH_SHORT).show()
                 fbUserID.isEmpty() -> Toast.makeText(this, "Please enter Facebook UserID, if you are unsure, click the button below", Toast.LENGTH_SHORT).show()

                 else ->{
                     FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                         .addOnCompleteListener {
                             if (!it.isSuccessful) return@addOnCompleteListener

                             // else if successful
                             Log.d("Main","Successfully created user with uid: ${it.result?.user?.uid}")
                             //GRAB INFORMATION
                             val userAccount = com.example.uniride.classes.UserAccount(userFirstName, userLastName, address, email, fbUserID)
                             userAccount.saveUserToDatabase()
                             val intent = Intent(this, PassengerDriverSelector::class.java)
                             startActivity(intent)
                         }
                         .addOnFailureListener(){
                             Log.d("Main", "Failed to create user: ${it.message}")
                             Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_SHORT)
                                 .show()
                         }
                 }

             }

                     Log.d("SignUp.kt", "Email is: " +email)
                     Log.d("SignUp.kt", "Password: $password")



                 }
         loginherebutton.setOnClickListener {
             Log.d("SignUp.kt", "User has been directed to login activity")
             val intent = Intent(this, LogIn::class.java)
             startActivity(intent)
         }
     }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }*/
}
