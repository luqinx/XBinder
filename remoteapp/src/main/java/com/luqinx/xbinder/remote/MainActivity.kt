package com.luqinx.xbinder.remote

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.luqinx.codecrafts.ipcapis.InitializationIPCService
import com.luqinx.xbinder.XBinder
import com.luqinx.xbinder.contextService
import com.luqinx.xbinder.debuggable

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        XBinder.xbinderReady = true
        debuggable = true
        contextService = RemoteContextService(applicationContext)
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            XBinder.getService(InitializationIPCService::class.java,
                "com.luqinx.devkits.codecrafts").initIpc()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}