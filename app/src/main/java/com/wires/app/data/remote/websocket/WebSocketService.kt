package com.wires.app.data.remote.websocket

import com.google.gson.Gson
import com.neovisionaries.ws.client.WebSocket
import com.wires.app.data.remote.params.MessageSendParams
import com.wires.app.data.remote.response.MessageResponse
import com.wires.app.extensions.toSocketEventFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketService @Inject constructor(
    private val chatWebSocketFactory: ChatWebSocketFactory,
    private val gson: Gson
) {
    private var currentWebSocket: WebSocket? = null

    fun listenChatSocket(channelId: Int): Flow<SocketEvent<MessageResponse>> =
        getOrCreateSocket(channelId)?.toSocketEventFlow(gson) ?: emptyFlow()

    fun sendChatMessage(channelId: Int, messageParams: MessageSendParams) {
        getOrCreateSocket(channelId)?.sendText(gson.toJson(messageParams))
    }

    private fun getOrCreateSocket(channelId: Int): WebSocket? {
        if (currentWebSocket == null) {
            currentWebSocket = chatWebSocketFactory.create(channelId)
            CoroutineScope(Dispatchers.IO).launch { currentWebSocket?.connect() }
        }
        return currentWebSocket
    }
}
