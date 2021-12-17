package sandeep.example.chatapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class msgAdapter(val context:Context,val msgList:ArrayList<message>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    val ITEM_SENT = 1
    val ITEM_RCV = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

       if(viewType == 1) {
           val view:View = LayoutInflater.from(context).inflate(R.layout.msg_sent,parent,false)
           return sentViewHolder(view)
       }
        else{

           val view:View = LayoutInflater.from(context).inflate(R.layout.msg_rcvd,parent,false)
           return ReceiveViewHolder(view)
       }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val currMsg = msgList[position]

        if(holder.javaClass == sentViewHolder::class.java){

            val viewHolder = holder as sentViewHolder
            holder.sentMsg.text = currMsg.message
        }
        else{

            val viewHolder = holder as ReceiveViewHolder
            holder.rcvMsg.text = currMsg.message
        }
    }

    override fun getItemViewType(position: Int): Int {

        val currMsg = msgList[position]

        if(FirebaseAuth.getInstance().currentUser?.uid.equals(currMsg.senderId))
        {
            return ITEM_SENT
        }
        else{
            return ITEM_RCV
        }
    }

    override fun getItemCount(): Int {

       return  msgList.size
    }

    class sentViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        val sentMsg = itemView.findViewById<TextView>(R.id.sent_msg)
    }

    class ReceiveViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        val rcvMsg = itemView.findViewById<TextView>(R.id.rcv_msg)

    }
}