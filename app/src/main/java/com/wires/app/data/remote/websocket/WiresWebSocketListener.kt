package com.wires.app.data.remote.websocket

import com.neovisionaries.ws.client.ThreadType
import com.neovisionaries.ws.client.WebSocket
import com.neovisionaries.ws.client.WebSocketException
import com.neovisionaries.ws.client.WebSocketFrame
import com.neovisionaries.ws.client.WebSocketListener
import com.neovisionaries.ws.client.WebSocketState

interface WiresWebSocketListener : WebSocketListener {

    override fun onStateChanged(websocket: WebSocket?, newState: WebSocketState?) = Unit

    override fun onConnected(websocket: WebSocket?, headers: MutableMap<String, MutableList<String>>?) = Unit

    override fun onConnectError(websocket: WebSocket?, cause: WebSocketException?) = Unit

    override fun onDisconnected(
        websocket: WebSocket?,
        serverCloseFrame: WebSocketFrame?,
        clientCloseFrame: WebSocketFrame?,
        closedByServer: Boolean
    ) = Unit

    override fun onFrame(websocket: WebSocket?, frame: WebSocketFrame?) = Unit

    override fun onContinuationFrame(websocket: WebSocket?, frame: WebSocketFrame?) = Unit

    override fun onTextFrame(websocket: WebSocket?, frame: WebSocketFrame?) = Unit

    override fun onBinaryFrame(websocket: WebSocket?, frame: WebSocketFrame?) = Unit

    override fun onCloseFrame(websocket: WebSocket?, frame: WebSocketFrame?) = Unit

    override fun onPingFrame(websocket: WebSocket?, frame: WebSocketFrame?) = Unit

    override fun onPongFrame(websocket: WebSocket?, frame: WebSocketFrame?) = Unit

    override fun onTextMessage(websocket: WebSocket?, text: String?) = Unit

    override fun onTextMessage(websocket: WebSocket?, data: ByteArray?) = Unit

    override fun onBinaryMessage(websocket: WebSocket?, binary: ByteArray?) = Unit

    override fun onSendingFrame(websocket: WebSocket?, frame: WebSocketFrame?) = Unit

    override fun onFrameSent(websocket: WebSocket?, frame: WebSocketFrame?) = Unit

    override fun onFrameUnsent(websocket: WebSocket?, frame: WebSocketFrame?) = Unit

    override fun onThreadCreated(websocket: WebSocket?, threadType: ThreadType?, thread: Thread?) = Unit

    override fun onThreadStarted(websocket: WebSocket?, threadType: ThreadType?, thread: Thread?) = Unit

    override fun onThreadStopping(websocket: WebSocket?, threadType: ThreadType?, thread: Thread?) = Unit

    override fun onError(websocket: WebSocket?, cause: WebSocketException?) = Unit

    override fun onFrameError(websocket: WebSocket?, cause: WebSocketException?, frame: WebSocketFrame?) = Unit

    override fun onMessageError(websocket: WebSocket?, cause: WebSocketException?, frames: MutableList<WebSocketFrame>?) = Unit

    override fun onMessageDecompressionError(websocket: WebSocket?, cause: WebSocketException?, compressed: ByteArray?) = Unit

    override fun onTextMessageError(websocket: WebSocket?, cause: WebSocketException?, data: ByteArray?) = Unit

    override fun onSendError(websocket: WebSocket?, cause: WebSocketException?, frame: WebSocketFrame?) = Unit

    override fun onUnexpectedError(websocket: WebSocket?, cause: WebSocketException?) = Unit

    override fun handleCallbackError(websocket: WebSocket?, cause: Throwable?) = Unit

    override fun onSendingHandshake(websocket: WebSocket?, requestLine: String?, headers: MutableList<Array<String>>?) = Unit
}
