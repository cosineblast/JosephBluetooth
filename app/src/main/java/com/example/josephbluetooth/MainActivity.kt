package com.example.josephbluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    lateinit var bluetoothAdapter: BluetoothAdapter

    private fun loadBluetooth() {
        requestBluetooth()

        val bluetoothManager: BluetoothManager? = getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager?.adapter

        if (bluetoothAdapter == null) {
            Log.d("seila", "Não tem adapter :(")
            return
        }

        Log.d("seila", "Tem adapter")

        this.bluetoothAdapter = bluetoothAdapter
    }

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.entries.all { it.value }) {
                Log.d("seila", "deu bom!")
                execute();
            } else {
                Log.d("seila", "deu ruim :(")
            }
        }

    private val requestEnableBluetooth =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                Log.d("seila", "deu bom")

                execute()
            } else {
                Log.d("seila", "deu ruim")
            }
        }

    fun requestBluetooth() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestMultiplePermissions.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                )
            )
        } else {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            requestEnableBluetooth.launch(enableBtIntent)
        }
    }


    fun hasBluetoothPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.BLUETOOTH_CONNECT
        ) != PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    fun execute() {
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter.bondedDevices

        if (pairedDevices == null) {
            Log.d("seila", "Não consegui arranjar dispositivos :(")
            return
        }

        Log.d("seila", "Arranjei os ${pairedDevices.size} dispositivos")


        pairedDevices.forEach { device ->
            val deviceName = device.name
            val deviceHardwareAddress = device.address

            Log.d("seila", "Device name: $deviceName")
            Log.d("seila", "Device MAC address: $deviceHardwareAddress")
        }
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

        loadBluetooth()

        if (hasBluetoothPermission()) {
            execute()
        }
    }
}