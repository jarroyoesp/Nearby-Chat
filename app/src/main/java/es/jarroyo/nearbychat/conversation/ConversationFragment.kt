package es.jarroyo.nearbychat.conversation


import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import es.jarroyo.nearbychat.R
import es.jarroyo.nearbychat.base.BaseFragment
import es.jarroyo.nearbychat.conversation.adapter.MessagesRVAdapter
import es.jarroyo.nearbychat.model.Message
import kotlinx.android.synthetic.main.fragment_conversation.*

class ConversationFragment : BaseFragment() {
    override var layoutId = R.layout.fragment_conversation

    var mListener: ConversationListener? = null

    //DATA
    lateinit var mEndPointId: String

    // RV Adapter
    private var mLayoutManager: LinearLayoutManager? = null
    private var mRvAdapter: MessagesRVAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getExtras()
        return inflateView(inflater, container)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is ConversationListener) {
            mListener = context
        } else {
            throw IllegalArgumentException("Context mus implemenment ConversationListener")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        configRecyclerView()
        fragment_conversation_button_send_message.setOnClickListener{
            onClickSendMessage(it)
        }
    }

    private fun getExtras() {
        mEndPointId = arguments?.getString(ARG_EXTRA_ENPOINT_ID) ?: ""
    }

    /**
     * CONFIG RECYCLER VIEW
     */
    fun configRecyclerView() {
        mLayoutManager = LinearLayoutManager(context,
            LinearLayoutManager.VERTICAL, false)
        fragment_conversation_rv.layoutManager = mLayoutManager

        mRvAdapter = MessagesRVAdapter()

        fragment_conversation_rv.adapter = mRvAdapter
    }

    fun onClickSendMessage(view: View) {
        var message = fragment_conversation_et_message.text.toString()
        if (message.isNotEmpty()) {
            addMessageToRV(Message(message, Message.TYPE_SEND))
            mListener?.onSendMessage(mEndPointId, message)
            fragment_conversation_et_message.setText("")
        }
    }

    interface ConversationListener {
        fun onSendMessage(endPointId: String, message: String)
    }

    fun onMessageReceived(message:String) {
        addMessageToRV(Message(message, Message.TYPE_RECEIVED))
    }

    fun addMessageToRV(message: Message) {
        mRvAdapter?.addMessage(message)
        mRvAdapter?.notifyDataSetChanged()
        fragment_conversation_rv.smoothScrollToPosition(mRvAdapter?.itemCount!!)
    }


    companion object {
        val ARG_EXTRA_ENPOINT_ID = "ARG_EXTRA_ENPOINT_ID"

        fun newInstance(endPointId: String): ConversationFragment {
            return ConversationFragment().apply {
                arguments = Bundle().apply {
                    this.putString(ARG_EXTRA_ENPOINT_ID, endPointId)
                }
            }
        }
    }

}
