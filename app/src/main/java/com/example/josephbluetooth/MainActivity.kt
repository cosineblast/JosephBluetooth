package com.example.josephbluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.json.JSONObject
import java.io.DataOutputStream
import java.io.OutputStream
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    var bluetoothSocket: BluetoothSocket? = null

    @SuppressLint("MissingPermission")
    fun connectBluetooth(adapter: BluetoothAdapter) {

        val pairedDevices: Set<BluetoothDevice>? = adapter.bondedDevices

        if (pairedDevices == null) {
            Log.d("seila", "Não consegui arranjar dispositivos :(")
            return
        }

        Log.d("seila", "Arranjei os ${pairedDevices.size} dispositivos")

        // TODO: mostrar em interface gráfica lista de dispositivos para usuário selecionar e parear
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

            socket.use {
                Log.d("seila", "consegui o socket")

                socket.connect()

                Log.d("seila", "conectado no socket")

                this.bluetoothSocket = socket

                onConnect()
            }
        }
    }

    data class Message(
        val x: Double,
        val y: Double,
        val z: Double
    )

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

    fun onConnect() {
        sendMessage(Message(1.0, 1.0, 1.0))
    }

    override fun onDestroy() {
        super.onDestroy()
        this.bluetoothSocket?.close()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val helper = BluetoothBoilerPlate(this)

        helper.callback = { adapter ->
            connectBluetooth(adapter)
        }

        helper.setup()

    }
}