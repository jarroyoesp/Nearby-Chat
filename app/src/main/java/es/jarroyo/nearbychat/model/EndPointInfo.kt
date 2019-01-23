package es.jarroyo.nearbychat.model

import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo

data class EndPointInfo(
    val endPointId: String,
    val discoveredEndpointInfo: DiscoveredEndpointInfo
) {
}