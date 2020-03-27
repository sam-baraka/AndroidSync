package com.vercetti.allyandroidsync

import android.accounts.Account
import android.accounts.AccountManager
import android.content.AbstractThreadedSyncAdapter
import android.content.ContentProviderClient
import android.content.Context
import android.content.SyncResult
import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import java.lang.Exception


public class AllySyncAdapter(context: Context?, autoInitialize: Boolean) :
    AbstractThreadedSyncAdapter(context, autoInitialize) {
    private val mAccountManager: AccountManager? = null
    override fun onPerformSync(
        account: Account?,
        extras: Bundle?,
        authority: String?,
        provider: ContentProviderClient?,
        syncResult: SyncResult?
    ) {
       TODO("Do the actuall sync")
        try{
            Toast.makeText(context,"Synced",LENGTH_LONG).show()
        }
        catch(e:Exception)
        {
            Toast.makeText(context,e.message,LENGTH_LONG).show()
        }



    }
}