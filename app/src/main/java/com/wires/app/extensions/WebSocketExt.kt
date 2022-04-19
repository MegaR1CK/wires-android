package com.wires.app.extensions

import com.google.gson.Gson
import com.neovisionaries.ws.client.WebSocket
import com.neovisionaries.ws.client.WebSocketException
import com.wires.app.data.remote.response.ObjectResponse
import com.wires.app.data.remote.websocket.SocketEvent
import com.wires.app.data.remote.websocket.WiresWebSocketListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow

inline fun <reified T, R> WebSocket.toSocketEventFlow(gson: Gson, crossinline map: (R) -> T) = callbackFlow<SocketEvent<T>> {
    addListener(object : WiresWebSocketListener {
        override fun onConnected(websocket: WebSocket?, headers: MutableMap<String, MutableList<String>>?) {
            trySendBlocking(SocketEvent.OpenEvent())
        }
        override fun onTextMessage(websocket: WebSocket?, text: String?) {
            trySendBlocking(SocketEvent.Message(map(gson.fromJson<ObjectResponse<R>>(text.orEmpty()).data)))
        }
        override fun onError(websocket: WebSocket?, cause: WebSocketException?) {
            cause?.cause?.let { trySendBlocking(SocketEvent.Error(it)) }
        }
    })
    awaitClose { this@toSocketEventFlow.disconnect() }
}
