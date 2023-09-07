package com.administrator.learndemo.dns

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.administrator.learndemo.R
import java.lang.Exception
import java.net.ServerSocket

class DNSServerActivity : AppCompatActivity() {
    companion object {
        const val TAG = "DNSServer"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dnsfound)
    }

    override fun onResume() {
        super.onResume()

        registerDNS()
    }

    fun registerDNS() {
        var port: Int = 0

        try {
            var sock: ServerSocket = ServerSocket(0)
            port = sock.getLocalPort()

            Toast.makeText(getApplicationContext(), "port: $port", Toast.LENGTH_SHORT).show()
            Log.i(TAG,"getLocalPort $port")
            sock.close()
        } catch (e: Exception) {
            Toast.makeText(getApplicationContext(), "can not set port", Toast.LENGTH_SHORT)
        }


        var serviceInfo = NsdServiceInfo()
        serviceInfo.serviceName = "wanbiao's android platform"
        //serviceInfo.serviceType = "_http._tcp."
        serviceInfo.serviceType = "_test._udp."
        //serviceInfo.port = port
        serviceInfo.port = port
        serviceInfo.setAttribute("hostname","wanbiao")

        var nsdManager = (getSystemService(Context.NSD_SERVICE) as NsdManager)
        nsdManager.registerService(
            serviceInfo,
            NsdManager.PROTOCOL_DNS_SD,
            object: NsdManager.RegistrationListener {
                override fun onRegistrationFailed(p0: NsdServiceInfo?, p1: Int) {
                    Log.i(TAG,"onRegistrationFailed $p0")
                }

                override fun onUnregistrationFailed(p0: NsdServiceInfo?, p1: Int) {
                    Log.i(TAG,"onUnregistrationFailed")
                }

                override fun onServiceRegistered(p0: NsdServiceInfo?) {
                    Log.i(TAG,"onServiceRegistered")
                    Toast.makeText(getApplicationContext(), "onServiceRegistered", Toast.LENGTH_SHORT).show()
                }

                override fun onServiceUnregistered(p0: NsdServiceInfo?) {
                    Toast.makeText(getApplicationContext(), "onServiceUnregistered", Toast.LENGTH_SHORT).show()
                }

            }
        )
    }
}