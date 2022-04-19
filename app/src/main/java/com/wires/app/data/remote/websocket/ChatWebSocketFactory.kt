package com.wires.app.data.remote.websocket

import com.neovisionaries.ws.client.WebSocket
import com.neovisionaries.ws.client.WebSocketFactory
import com.wires.app.domain.repository.TokenRepository
import javax.inject.Inject

class ChatWebSocketFactory @Inject constructor(
    private val webSocketFactory: WebSocketFactory,
    private val tokenRepository: TokenRepository
) {
    companion object {
        private const val CHAT_WEBSOCKET_URL = "wss://wires-api.herokuapp.com/v1/channels/%d/listen"
        private const val AUTH_HEADER_NAME = "Authorization"
        private const val AUTH_TOKEN_PREFIX = "Bearer"
    }

    fun create(channelId: Int): WebSocket =
        webSocketFactory.createSocket(CHAT_WEBSOCKET_URL.format(channelId)).apply {
            addHeader(AUTH_HEADER_NAME, "$AUTH_TOKEN_PREFIX ${tokenRepository.getAccessToken()}")
        }
}
