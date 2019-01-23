package es.jarroyo.nearbychat.home

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.*
import es.jarroyo.nearbychat.CodenameGenerator
import es.jarroyo.nearbychat.R
import es.jarroyo.nearbychat.base.NearbyBaseActivity
import es.jarroyo.nearbychat.conversation.ConversationFragment
import es.jarroyo.nearbychat.home.adapter.EndPointRVAdapter
import es.jarroyo.nearbychat.model.EndPointInfo
import kotlinx.android.synthetic.main.activity_home.*
import kotlin.text.Charsets.UTF_8

class HomeActivity : NearbyBaseActivity(), ConversationFragment.ConversationListener {


    override var layoutId = R.layout.activity_home

    private val TAG = "HomeActivity"

    // NEARBY CONNECTIONS
    private var connectionsClient: ConnectionsClient? = null
    private val STRATEGY = Strategy.P2P_STAR

    private val codeName = CodenameGenerator.generate()

    // RV Adapter
    private var mLayoutManager: LinearLayoutManager? = null
    private var mRvAdapter: EndPointRVAdapter? = null

    // Conversation
    private lateinit var conversationFragment: ConversationFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configView()

        connectionsClient = Nearby.getConnectionsClient(this)

        startAdvertising()
        startDiscovery()

        showStatus(getString(R.string.searching_people))
    }

    private fun configView(){
        activity_home_tv_name.text = "You are $codeName"

        configRecyclerView()
    }

    /**
     * CONFIG RECYCLER VIEW
     */
    fun configRecyclerView() {
        mLayoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL, false)
        activity_home_rv_end_point.layoutManager = mLayoutManager

        mRvAdapter = EndPointRVAdapter(mListenerEndPointClicked = {
            connectWith(it.endPoint)
        })

        activity_home_rv_end_point.adapter = mRvAdapter
    }

    /**
     * NEARBY CONNECTIONS
     */
    /** Broadcasts our presence using Nearby Connections so other players can find us.  */
    private fun startAdvertising() {
        // Note: Advertising may fail. To keep this demo simple, we don't handle failures.
        connectionsClient?.startAdvertising(
            codeName, packageName, connectionLifecycleCallback,
            AdvertisingOptions.Builder().setStrategy(STRATEGY).build()
        )
    }

    /** Starts looking for other players using Nearby Connections.  */
    private fun startDiscovery() {
        // Note: Discovery may fail. To keep this demo simple, we don't handle failures.
        connectionsClient?.startDiscovery(
            packageName, endpointDiscoveryCallback,
            DiscoveryOptions.Builder().setStrategy(STRATEGY).build()
        )
    }

    /**
     * CONNECT WITH
     */
    fun connectWith(endpointInfo: EndPointInfo) {
        showStatus("${getString(R.string.connect_with)} ${endpointInfo.discoveredEndpointInfo.endpointName}...")

        connectionsClient?.requestConnection(codeName, endpointInfo.endPointId, connectionLifecycleCallback)
    }

    // Callbacks for connections to other devices
    private val connectionLifecycleCallback = object : ConnectionLifecycleCallback() {
        override fun onConnectionInitiated(endpointId: String, connectionInfo: ConnectionInfo) {
            Log.i(TAG, "onConnectionInitiated: accepting connection")

            activity_home_tv_status.text = "${getString(R.string.connect_with)} ${connectionInfo.endpointName}..."
            connectionsClient?.acceptConnection(endpointId, payloadCallback)
            /*opponentName = connectionInfo.endpointName
            connectWith.setVisibility(View.GONE)*/
        }

        override fun onConnectionResult(endpointId: String, result: ConnectionResolution) {
            if (result.status.isSuccess) {
                Log.i(TAG, "onConnectionResult: connection successful")

                /*connectionsClient.stopDiscovery()
                connectionsClient.stopAdvertising()

                opponentEndpointId = endpointId
                setOpponentName(opponentName)
                setStatusText(getString(R.string.status_connected))
                setButtonState(true)*/
                showConversation(endpointId, result)
            } else {
                Log.i(TAG, "onConnectionResult: connection failed")
            }
        }

        override fun onDisconnected(endpointId: String) {
            Log.i(TAG, "onDisconnected: disconnected from the opponent")
            //resetGame()

            removeEndPointFromRV(endpointId)
        }
    }

    // Callbacks for receiving payloads
    private val payloadCallback = object : PayloadCallback() {
        override fun onPayloadReceived(endpointId: String, payload: Payload) {
            /*val payloadMessage = String(payload.asBytes()!!, UTF_8)
            if (payloadMessage.contains("message_")) {
                runOnUiThread {
                    Toast.makeText(applicationContext, "Message: $payloadMessage", Toast.LENGTH_SHORT).show()
                }

            } else {
                opponentChoice = GameChoice.valueOf(payloadMessage)
            }*/
            val payloadMessage = String(payload.asBytes()!!, UTF_8)
            conversationFragment.onMessageReceived(payloadMessage)
        }

        override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {
            /*if (update.status == PayloadTransferUpdate.Status.SUCCESS && myChoice != null && opponentChoice != null) {
                finishRound()
            }*/

        }
    }

    // Callbacks for finding other devices
    private val endpointDiscoveryCallback = object : EndpointDiscoveryCallback() {
        override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
            Log.i(TAG, "onEndpointFound: endpoint found, connecting")
            /*setStatusText("Connecting with " + info.getEndpointName());
                    connectionsClient.requestConnection(codeName, endpointId, connectionLifecycleCallback);*/
            /*connectWith.setText(info.endpointName)
            connectWith.setVisibility(View.VISIBLE)
            mDiscoveredEndpointInfo = info
            mEndpointId = endpointId*/

            mRvAdapter?.addEndPoint(EndPointInfo(endpointId, info))
            mRvAdapter?.notifyDataSetChanged()

        }

        override fun onEndpointLost(endpointId: String) {
            removeEndPointFromRV(endpointId)
        }
    }

    private fun removeEndPointFromRV(endpointId: String) {
        mRvAdapter?.removeEndPoint(endpointId)
        mRvAdapter?.notifyDataSetChanged()
    }

    /**
     * STATUS
     */
    private fun showStatus(message: String) {
        activity_home_tv_status.text = message
    }

    private fun addConversationFragment() {

    }
    /**
     * CONVERSATION
     */
    fun showConversation(endPointId: String, result: ConnectionResolution) {
        supportActionBar?.title = mRvAdapter?.getEndPoint(endPointId)?.discoveredEndpointInfo?.endpointName
        activity_home_tv_status.text = "${getString(R.string.connected)}"
        addConversationFragment(endPointId)
    }

    fun addConversationFragment(endPointId: String) {
        conversationFragment = ConversationFragment.newInstance(endPointId)
        val ft = supportFragmentManager?.beginTransaction()
        ft?.addToBackStack(ConversationFragment::class.java.simpleName)
        ft?.replace(R.id.activity_home_layout_main, conversationFragment)?.commit()
    }

    override fun onSendMessage(endPointId: String, message: String) {
        connectionsClient?.sendPayload(
            endPointId, Payload.fromBytes(message.toByteArray(UTF_8))
        )
    }

}
