package com.example.josephbluetooth


import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import com.example.josephbluetooth.MainActivity.Message
import org.json.JSONObject
import java.io.DataOutputStream
import java.io.OutputStream
import kotlin.concurrent.thread

class BluetoothStuff : AutoCloseable {

    var bluetoothSocket: BluetoothSocket? = null

    fun serializeMessage(message: Message, stream: OutputStream) {
        val result = JSONObject()
        result.put("x", message.x)
        result.put("y", message.y)
        result.put("z", message.z)

        val bytes = result.toString().toByteArray(Charsets.UTF_8)

        val newStream = DataOutputStream(stream)

        newStream.writeInt(bytes.size)
        newStream.write(bytes)
        newStream.flush()
    }

    fun sendMessage(message: Message) {
        val socket = bluetoothSocket!!

        serializeMessage(message, socket.outputStream)
    }


    @SuppressLint("MissingPermission")
    fun connectBluetooth(adapter: BluetoothAdapter, onConnect: (BluetoothSocket) -> Unit) {

        val pairedDevices: Set<BluetoothDevice>? = adapter.bondedDevices

        if (pairedDevices == null) {
            Log.d("seila", "Não consegui arranjar dispositivos :(")
            return
        }

        Log.d("seila", "Arranjei os ${pairedDevices.size} dispositivos")

        // TODO: mostrar em interface gráfica lista de dispositivos para usuário selecionar e parear
        // TODO: ou colocar o MAC address do seu PC
        val device = pairedDevices.find { it.address == "88:78:73:13:3A:C0" }

        if (device == null) {
            Log.d("seila", "Não encontrei o pc >-<")
            return
        }

        thread {
            adapter.cancelDiscovery()

            // que horror
            val socket = device.javaClass.getMethod("createRfcommSocket", *arrayOf(Int::class.java))
                .invoke(device, 1) as BluetoothSocket

            Log.d("seila", "consegui o socket")

            socket.connect()

            Log.d("seila", "conectado no socket")

            bluetoothSocket = socket

            onConnect(socket)
        }
    }

    override fun close() {
        bluetoothSocket?.close()
    }
}
