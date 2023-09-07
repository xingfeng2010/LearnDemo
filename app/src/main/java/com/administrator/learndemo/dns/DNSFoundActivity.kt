package com.administrator.learndemo.dns

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.administrator.learndemo.R


class DNSFoundActivity : AppCompatActivity() {
    companion object {
        const val TAG = "DNSFound"
    }

    lateinit var nsdManager: NsdManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dnsfound)

        val context: Context = applicationContext
        nsdManager = (context.getSystemService(Context.NSD_SERVICE)) as NsdManager
    }


    override fun onResume() {
        super.onResume()

        testServiceFound()
    }

    private fun testServiceFound() {
       var discoveryListener = object:NsdManager.DiscoveryListener {
           override fun onStartDiscoveryFailed(p0: String?, p1: Int) {
               Log.i(TAG,"onStartDiscoveryFailed $p0")
           }

           override fun onStopDiscoveryFailed(p0: String?, p1: Int) {
               Log.i(TAG,"onStopDiscoveryFailed $p0")
           }

           override fun onDiscoveryStarted(p0: String?) {
               Log.i(TAG,"onDiscoveryStarted $p0")
               Toast.makeText(getApplicationContext(), "onDiscoveryStarted $p0", Toast.LENGTH_SHORT).show()
           }

           override fun onDiscoveryStopped(p0: String?) {
               Log.i(TAG,"onDiscoveryStopped $p0")
           }

           override fun onServiceFound(serviceInfo: NsdServiceInfo?) {
               Log.i(TAG,"onServiceFound !!!!")

               Toast.makeText(getApplicationContext(), "onServiceFound", Toast.LENGTH_SHORT).show()
               resolveServiceInfo(serviceInfo)
           }

           override fun onServiceLost(serviceInfo: NsdServiceInfo?) {
               Log.i(TAG,"onServiceLost !!!!")
           }
       }

        //nsdManager.discoverServices("_http._tcp.", NsdManager.PROTOCOL_DNS_SD, discoveryListener)
        nsdManager.discoverServices("_test._udp.", NsdManager.PROTOCOL_DNS_SD, discoveryListener)

    }

    private fun resolveServiceInfo(serviceInfo: NsdServiceInfo?) {
        var resolveListener = object : NsdManager.ResolveListener {
            override fun onResolveFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
                Log.i(TAG,"onResolveFailed !!!!")
            }

            override fun onServiceResolved(serviceInfo: NsdServiceInfo) {
                Log.i(TAG,"onServiceResolved port:${serviceInfo.port}   host:${serviceInfo.host}")
                Toast.makeText(getApplicationContext(), "port:${serviceInfo.port}   host:${serviceInfo.host}", Toast.LENGTH_SHORT).show()
            }
        }

        nsdManager.resolveService(serviceInfo, resolveListener)
    }
}