package com.example.mobileprogramming

import android.app.Activity
import android.content.Intent
import android.content.SyncRequest
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import android.provider.ContactsContract.Contacts.Photo
import android.view.inputmethod.InputBinding
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.Login
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : AppCompatActivity() {
    private lateinit var firebase: FirebaseAuth
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var callbackManager: CallbackManager
    private lateinit var loginManager: LoginManager
    private var oneTapClint: SignInClient? =
        null//google tarafından yapılan one tap clientin tanımlanmasını sağlar.Bunun üzerinden oturum açma ve kullanıcı doğrulama işlemleri gerçekleştirilir.


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        firebase = FirebaseAuth.getInstance()
        var callbackManager = CallbackManager.Factory.create();// facebook için maneger
        val fcbkbutton=findViewById<Button>(R.id.fcbklogin)//facebook buton
        val accessToken=AccessToken.getCurrentAccessToken()// facebook token

        LoginManager.getInstance().registerCallback(callbackManager,
            object :FacebookCallback<LoginResult>{
                override fun onCancel() {
                }

                override fun onError(error: FacebookException) {
                }

                override fun onSuccess(result: LoginResult) {
                    startActivity(Intent(this@MainActivity,Giris::class.java))
                    finish()
                }


            })
        fcbkbutton.setOnClickListener(){
            LoginManager.getInstance().logInWithReadPermissions(this, listOf("public_profile,email"));
        }












        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }
        val kayit = findViewById<Button>(R.id.kayit)
        kayit.setOnClickListener {
            val intent = Intent(this, KaydolmaEkran::class.java)
            startActivity(intent)
        }
        val giris = findViewById<Button>(R.id.giris)
        val mailgir = findViewById<TextInputLayout>(R.id.mailLayout)
        val sifregir = findViewById<TextInputLayout>(R.id.sifrelayout)

        giris.setOnClickListener {
            val email = mailgir.editText?.text.toString()
            val sifre = sifregir.editText?.text.toString()
            if (email.isNotEmpty() && sifre.isNotEmpty()) {
                firebase.signInWithEmailAndPassword(email, sifre).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Snackbar.make(
                            findViewById(android.R.id.content),
                            "Giris Başarılı!",
                            Snackbar.LENGTH_SHORT
                        ).show();

                        val intent = Intent(this, Giris::class.java)
                        startActivity(intent)
                    } else {
                        Snackbar.make(
                            findViewById(android.R.id.content),
                            it.exception.toString(),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }

                }

            } else {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "İlgili alanları doldurunuz",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
        // giriş için yapılandırma ayarları.
        val googlesign = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.baglantiID))
            .requestEmail()

            .build()

        googleSignInClient = GoogleSignIn.getClient(this, googlesign)

        findViewById<Button>(R.id.googleGiris).setOnClickListener {
            googlegirisfonk()
        }



        if(accessToken!=null&&!accessToken.isExpired){
            startActivity(Intent(this,giris::class.java))
            finish()





        }
         fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            callbackManager.onActivityResult(requestCode,resultCode,data    )
             super.onActivityResult(requestCode, resultCode, data)
        }























    }
  //  oturum açma için gerekli bir intent .
    private fun googlegirisfonk() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)//launcher fonksiyonuna yapıştır
    }

    //oturum açma işleminin sonucunu yakalamak için kullanılır.
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                sonucfonksiyon(task)
            }
        }

    private fun sonucfonksiyon(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result//Başarılı Oturum Açma Durumu: Kullanıcının Google hesabı alınır
            if (account != null) {
                uiguncelleme(account)
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Giris Başarılı!",
                    Snackbar.LENGTH_SHORT
                ).show();
            }
        } else {
            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }
//Kullanıcı Bilgileriyle Firebase'e Oturum Açma

    private fun uiguncelleme(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebase.signInWithCredential(credential).addOnCompleteListener { //Firebase üzerinde oturum açılır.
            if (it.isSuccessful) {
                val intent: Intent = Intent(this, Giris::class.java)
                intent.putExtra("email", account.email)//verilerigönder
                intent.putExtra("name", account.displayName)//verilerigönder
                startActivity(intent)
            } else {
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()

            }
        }
    }





}
