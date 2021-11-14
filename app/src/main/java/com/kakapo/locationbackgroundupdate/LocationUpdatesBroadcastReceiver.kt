package com.kakapo.locationbackgroundupdate

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

private const val TAG = "LUBroadcastReceiver"

class LocationUpdatesBroadcastReceiver: BroadcastReceiver() {

    companion object{
        const val ACTION_PROCESS_UPDATES = "com.kakapo.locationbackgroundupdate.action" + "PROCESS_UPDATES"
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "onReceive() context:$context, intent:$intent")

        if (intent.action == ACTION_PROCESS_UPDATES){

        }
    }
}