package sandeep.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_log_in.*

class LogIn : AppCompatActivity() {

//    private lateinit var editEmail: EditText
//    private lateinit var editPassword: EditText
//    private lateinit var LogInBtn: Button
//    private lateinit var SignUpBtn: Button
    private val TAG = "LogIn"

    private lateinit var mAuth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()

        SignUpBtn.setOnClickListener {
            val intent = Intent(this,SignUp::class.java)
            startActivity(intent)
        }

        LogInBtn.setOnClickListener {
            val email=EditEmail.text.toString()
            val passwod=EditPassword.text.toString()

            login(email,passwod)
        }
    }

    private fun login(email:String,password:String){

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val intent = Intent(this@LogIn,MainActivity::class.java)
                    //finish()
                    startActivity(intent)

                } else {
                   Toast.makeText(this@LogIn,"User does not exist",Toast.LENGTH_SHORT).show()
                }
            }

    }

}