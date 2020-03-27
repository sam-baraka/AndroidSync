package com.vercetti.allyandroidsync

import android.app.Service
import android.content.Intent
import android.os.IBinder

class SyncService : Service() {
    override fun onCreate() {
        synchronized(
            sSyncAdapterLock
        ) {
            if (sSyncAdapter == null) sSyncAdapter =
                AllySyncAdapter(applicationContext, true)
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return sSyncAdapter?.getSyncAdapterBinder()
    }

    companion object {
        private val sSyncAdapterLock = Any()
        private var sSyncAdapter: AllySyncAdapter? = null
    }
}