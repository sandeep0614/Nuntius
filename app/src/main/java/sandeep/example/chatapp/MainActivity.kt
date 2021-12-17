package sandeep.example.chatapp

import android.content.Intent
import android.graphics.Color
import android.media.tv.TvContract
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {


    private val TAG = "MainActivity"
    private lateinit var userList: ArrayList<User>
    private lateinit var tempArrayList : ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().getReference()

        userList = ArrayList()
        tempArrayList = ArrayList()
        adapter = UserAdapter(this,tempArrayList)

        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.adapter = adapter

        mDbRef.child("user").addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                tempArrayList.clear()
                for(i in snapshot.children)
                {
                    val cUser = i.getValue(User::class.java)
                    if(mAuth.currentUser?.uid != cUser?.uid){
                        userList.add(cUser!!)
                    }

                }
                userList.sortBy {
                    it.name
                }

                tempArrayList.addAll(userList)
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.LogOut){

            mAuth.signOut()
            val intent = Intent(this@MainActivity,LogIn::class.java)
            finish()
            startActivity(intent)
            return true
        }
        if(item.itemId == R.id.action_search)
        {
            val searchView = item?.actionView as SearchView
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    TODO()
                }

                override fun onQueryTextChange(newText: String?): Boolean {

                    tempArrayList.clear()
                    val searchText = newText!!.toLowerCase(Locale.getDefault())
                    if(searchText.isNotEmpty()){

                        Log.d(TAG,"searchText is not empty")
                        userList.forEach {
                            if(it.name!!.toLowerCase(Locale.getDefault()).contains(searchText)){
                                tempArrayList.add(it)
                            }
                        }
                        adapter.notifyDataSetChanged()
                    }
                    else{
                        tempArrayList.clear()
                        tempArrayList.addAll(userList)
                        adapter.notifyDataSetChanged()
                    }

                    return false
                }
            })
            return true
        }

        return true
    }
}
