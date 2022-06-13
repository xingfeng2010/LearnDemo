package com.administrator.learndemo.wifi

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiNetworkSpecifier
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.administrator.learndemo.R


class TestNetworkActivity : AppCompatActivity() {
    private lateinit var connectivityManager:ConnectivityManager

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_network)

        connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        connectWifi("OnePlus 9", "lsx123456")
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun connectWifi(ssid: String, pass: String) {
        val wifiNetworkSpecifier: WifiNetworkSpecifier = WifiNetworkSpecifier.Builder().setSsid(ssid)
                .setWpa2Passphrase(pass).build()
        val build: NetworkRequest = NetworkRequest.Builder().addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .removeCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .setNetworkSpecifier(wifiNetworkSpecifier).build()

        val callBack: ConnectivityManager.NetworkCallback = object:ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)

                Log.i("TestNetwor","onAvailable ${network.toString()}")
                connectivityManager.bindProcessToNetwork(network)
            }

            override fun onUnavailable() {
                super.onUnavailable()
            }

            override fun onLost(network: Network) {
                super.onLost(network)
            }
        }

        connectivityManager.requestNetwork(build, callBack)
    }
}