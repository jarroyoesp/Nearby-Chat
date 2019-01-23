package es.jarroyo.nearbychat.conversation.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import es.jarroyo.nearbychat.R
import es.jarroyo.nearbychat.model.Message
import kotlinx.android.synthetic.main.item_rv_end_point.view.*

class MessagesRVAdapter(private var mMessagesList: MutableList<Message> = mutableListOf<Message>()) : RecyclerView.Adapter<MessagesRVAdapter.ViewHolder>() {

    val TYPE_SEND = 0
    val TYPE_RECEIVED = 1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        if (viewType == TYPE_SEND) {
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_rv_message_send, parent, false))
        } else {
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_rv_message_received, parent, false))
        }

    override fun onBindViewHolder(holder:  ViewHolder, position: Int) {
        holder.bind(position, mMessagesList[position])
    }

    override fun getItemViewType(position: Int): Int {
        if (mMessagesList.get(position).type == Message.TYPE_SEND) {
            return TYPE_SEND
        } else {
            return TYPE_RECEIVED
        }
    }

    override fun getItemCount() = mMessagesList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int, message: Message) = with(itemView) {
            item_rv_end_point_tv_name.text = message.message
        }
    }

    fun addMessage(message: Message) {
        mMessagesList.add(message)
    }
}