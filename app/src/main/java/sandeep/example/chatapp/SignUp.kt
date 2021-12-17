package sandeep.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Log.*
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_log_in.*
import kotlinx.android.synthetic.main.activity_log_in.EditEmail
import kotlinx.android.synthetic.main.activity_log_in.EditPassword
import kotlinx.android.synthetic.main.activity_log_in.SignUpBtn
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUp : AppCompatActivity() {


    private val TAG = "SignUp"
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()

        SignUpBtn.setOnClickListener {
            val name = EditName.text.toString()
            val email=EditEmail.text.toString()
            val password=EditPassword.text.toString()

            signUp(name,email,password)
        }

    }
    private fun signUp(name:String,email:String,password:String){

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG,"successful")
                    addUserToDatabase(name,email,mAuth.currentUser?.uid!!)
                    val intent = Intent(this@SignUp,MainActivity::class.java)
                    finish()
                    startActivity(intent)

                } else {
                    Log.d(TAG,"failed")
                   Toast.makeText(this@SignUp,"Some error occured",Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addUserToDatabase(name:String,email:String,uid:String){
        mDbRef = FirebaseDatabase.getInstance().getReference()

        mDbRef.child("user").child(uid).setValue(User(name,email,uid))

    }
}