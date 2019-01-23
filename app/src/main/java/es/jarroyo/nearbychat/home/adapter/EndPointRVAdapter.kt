package es.jarroyo.nearbychat.home.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import es.jarroyo.nearbychat.R
import es.jarroyo.nearbychat.model.EndPointInfo
import kotlinx.android.synthetic.main.item_rv_end_point.view.*

class EndPointRVAdapter(private var mEndpointInfoList: MutableList<EndPointInfo> = mutableListOf<EndPointInfo>(),
                        private val mListenerEndPointClicked: (ItemEndPoint) -> Unit) : RecyclerView.Adapter<EndPointRVAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_rv_end_point, parent, false))

    override fun onBindViewHolder(holder:  ViewHolder, position: Int) {
        holder.bind(position, mEndpointInfoList[position], mListenerEndPointClicked)
    }

    override fun getItemCount() = mEndpointInfoList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int, endPointInfo: EndPointInfo, listener: (ItemEndPoint) -> Unit) = with(itemView) {
            item_rv_end_point_tv_name.text = endPointInfo.discoveredEndpointInfo.endpointName
            setOnClickListener {
                listener(ItemEndPoint(position, endPointInfo))
            }
        }
    }

    fun addEndPoint(endPointInfo: EndPointInfo) {
        mEndpointInfoList.add(endPointInfo)
    }

    fun removeEndPoint(endPointId: String) {
        val discoveredEndpointInfo = mEndpointInfoList.find { endPoint -> endPoint.endPointId == endPointId }
        if (discoveredEndpointInfo != null) {
            mEndpointInfoList.remove(discoveredEndpointInfo)
        }
    }

    fun getEndPoint(endPointId: String): EndPointInfo? {
        val endPointInfo = mEndpointInfoList.find { endPointInfo -> endPointInfo.endPointId == endPointId }
        return endPointInfo
    }

    data class ItemEndPoint(val position: Int, val endPoint: EndPointInfo)
}