package com.example.josephbluetooth

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.core.app.ActivityCompat
import java.lang.UnsupportedOperationException

class BluetoothBoilerPlate(val activity: AppCompatActivity) {

    var callback: ((BluetoothAdapter) -> Unit)? = null

    fun setup() {
        if (hasBluetoothPermission()) {
            callback?.invoke(getAdapter())
        }
        else {
            requestBluetooth()
        }

    }

    private fun getAdapter(): BluetoothAdapter {
        val bluetoothManager: BluetoothManager? = activity.getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager?.adapter

        if (bluetoothAdapter == null) {
            Log.d("seila", "Não tem adapter :(")
            // TODO: tratar esse erro
            throw UnsupportedOperationException("no bluetoothmnaager available")
        }

        Log.d("seila", "Tem adapter")

        return bluetoothAdapter
    }

    private val requestMultiplePermissions =
        activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.entries.all { it.value }) {
                Log.d("seila", "deu bom 1!")

                callback?.invoke(getAdapter())
            } else {
                Log.d("seila", "deu ruim :(")
            }
        }

    private val requestEnableBluetooth =
        activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                Log.d("seila", "deu bom 2")

                callback?.invoke(getAdapter())
            } else {
                Log.d("seila", "deu ruim")
            }
        }

    private fun requestBluetooth() {
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


    private fun hasBluetoothPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            activity,
            Manifest.permission.BLUETOOTH_CONNECT
        ) != PackageManager.PERMISSION_GRANTED
    }
}