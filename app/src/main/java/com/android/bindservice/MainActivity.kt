package com.android.bindservice

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var dataService: DataService
    private var mBound: Boolean = false
    lateinit var textView: TextView
    lateinit var button: Button
    var connection = object : ServiceConnection {
        override fun onServiceDisconnected(p0: ComponentName?) {
            mBound = false
        }

        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            val binder = p1 as DataService.LocalBinder
            dataService = binder.getService()
            mBound = true
        }

    }

    override fun onStart() {
        super.onStart()
        Intent(this, DataService::class.java).also {
            bindService(
                it,
                connection,
                Context.BIND_AUTO_CREATE
            )
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.text)
        button = findViewById(R.id.change_button)

        button.setOnClickListener {
            textView.text = dataService.text
            CoroutineScope(Dispatchers.Main).launch {

              val result =  async { dataService.changeText("Please WORK!!!") }
                Log.d("TAG", "CHANGED")
                textView.text = result.await()
           }

        }



    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection)
    }
}
