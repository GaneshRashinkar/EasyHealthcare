package com.ganeshrashinkar.easyhealthcare.adapters
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ganeshrashinkar.easyhealthcare.R
import com.ganeshrashinkar.easyhealthcare.dataclass.Message

class ChatAdapter(val context:Context, msgList:MutableList<Message>): RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {
    var messageList =msgList
    var checkYesNo=false
    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var tvMessageReceive=itemView.findViewById<TextView>(R.id.tvMsgReceive)
            var tvMsgSend=itemView.findViewById<TextView>(R.id.tvMsgSend)
            var llYesNo=itemView.findViewById<LinearLayout>(R.id.ll_yes_no)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        var view=LayoutInflater.from(context).inflate(R.layout.layout_msg,null)
        val lp = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        view.setLayoutParams(lp)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message= messageList[position].message
        val isReceived=messageList[position].isReceived
//        if(checkYesNo){
//            holder.llYesNo.visibility = View.VISIBLE
//            holder.llYesNo.findViewById<Button>(R.id.btn_yes).setOnClickListener {
//                messageList.add(Message("Yes",isReceived))
//                notifyDataSetChanged()
//                it.isEnabled=false
//            }
//            holder.llYesNo.findViewById<Button>(R.id.btn_no).setOnClickListener {
//                messageList.add(Message("No",isReceived))
//                notifyDataSetChanged()
//                it.isEnabled=false
//            }
//        }
        if(isReceived){
            holder.tvMessageReceive.visibility=View.VISIBLE
            holder.tvMsgSend.visibility=View.GONE
            holder.tvMessageReceive.text=message
        }
        else{
            holder.tvMessageReceive.visibility=View.GONE
            holder.tvMsgSend.visibility=View.VISIBLE
            holder.tvMsgSend.text=message
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }
    fun showYesNoButtons( flag:Boolean){
        checkYesNo=flag
    }
}