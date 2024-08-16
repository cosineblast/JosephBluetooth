package com.example.josephbluetooth

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    val stuff: BluetoothStuff = BluetoothStuff()

    data class Message(
        val x: Double,
        val y: Double,
        val z: Double
    )

    fun onConnect() {
        // código que será executado quando/se a conexão bluetooth for estabelecida
        stuff.sendMessage(Message(1.0, 2.0, 3.0))
    }

    override fun onDestroy() {
        super.onDestroy()
        this.stuff.close()
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
            stuff.connectBluetooth(adapter) {
                onConnect()
            }
        }

        helper.setup()

    }
}