package com.android.tlb

import android.app.Application
import android.content.IntentFilter
import android.os.Build
import android.widget.Toast
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.android.tlb.network.NetworkMonitor

class Tlb: Application(), NetworkMonitor.ConnectivityReceiverListener {

    private var isConnected: Boolean = false

    init {
        mInstance = this
    }

    companion object{
        private lateinit var mInstance: Tlb

        @Synchronized
        fun getInstance(): Tlb {
            return mInstance as Tlb
        }
    }

    override fun onCreate() {
        super.onCreate()
        FacebookSdk.sdkInitialize(mInstance)
        AppEventsLogger.activateApp(mInstance)
        registerConnectionReceiver()//1
        setConnectivityListener(this)//2
    }

    private fun registerConnectionReceiver() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            val receiver = NetworkMonitor()
            val intentFilter = IntentFilter()
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
            registerReceiver(receiver,intentFilter)
        }
    }

    private fun setConnectivityListener(listener: NetworkMonitor.ConnectivityReceiverListener) {
        NetworkMonitor.connectivityReceiverListener = listener
    }

    fun isConnected():Boolean{
        return isConnected
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        this.isConnected = isConnected
        val networkStatus: String?
        networkStatus = if(isConnected){
            "Network Connected"
        }else{
            "Network disconnected"
        }
        Toast.makeText(mInstance,networkStatus,Toast.LENGTH_SHORT).show()
    }

}
