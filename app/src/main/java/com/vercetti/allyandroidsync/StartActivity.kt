package com.vercetti.allyandroidsync

import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract

class StartActivity : AppCompatActivity() {
    val mNames: ArrayList<String> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        names
        //      Apply changes to phone's contact list
        Thread(Runnable {
            var name: String
            for (i in 0 until mNames.size) {
                name = mNames[i]
                ContactsManager.addContact(this@StartActivity, MyContact(name))
            }
        }).start()
    }

    private val names: Unit
        private get() {
            var hasPhone: Int
            val c: Cursor? = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null
            )
            if (c != null && c.moveToFirst()) {
                while (c.moveToNext()) {
                    hasPhone = c.getString(
                        c.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER)
                    ).toInt()
                    if (hasPhone == 1) mNames.add(
                        c.getString(
                            c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME)
                        )
                    )
                }
                c.close()
            }
        }
}