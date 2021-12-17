package sandeep.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {

    private lateinit var msgAdapter: msgAdapter
    private lateinit var msgList: ArrayList<message>
    private lateinit var mDbRef:DatabaseReference

    var rcvrRoom:String? = null
    var senderRoom:String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)


        val name = intent.getStringExtra("name")
        val rcvrUid = intent.getStringExtra("uid")

         val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        mDbRef = FirebaseDatabase.getInstance().getReference()

         senderRoom = rcvrUid + senderUid
        rcvrRoom = senderUid + rcvrUid

        supportActionBar?.title = name

        msgList = ArrayList()
        msgAdapter = msgAdapter(this,msgList)

        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = msgAdapter

        mDbRef.child("chats").child(senderRoom!!).child("message")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    msgList.clear()
                    for(i in snapshot.children){

                        val msg = i.getValue(message::class.java)
                        msgList.add(msg!!)
                    }
                    msgAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        sendBtn.setOnClickListener{
            val msg = messageBox.text.toString()
            val msgObj = message(msg,senderUid)
            mDbRef.child("chats").child(senderRoom!!).child("message").push()
                .setValue(msgObj).addOnSuccessListener {
                    mDbRef.child("chats").child(rcvrRoom!!).child("message").push()
                        .setValue(msgObj)
                }
            messageBox.setText("")

        }

    }
}