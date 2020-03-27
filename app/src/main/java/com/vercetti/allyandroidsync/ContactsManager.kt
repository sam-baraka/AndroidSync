package com.vercetti.allyandroidsync

import android.R
import android.content.ContentProviderOperation
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds
import android.provider.ContactsContract.RawContacts
import android.provider.SyncStateContract
import android.util.Log


object ContactsManager {
    private const val MIMETYPE = "vnd.android.cursor.item/com.example.ajay.contacts_4"
    fun addContact(context: Context, contact: MyContact) {
        val resolver: ContentResolver = context.getContentResolver()
        val mHasAccount = isAlreadyRegistered(resolver, contact.Id)
        if (mHasAccount) {
            Log.d("Log", "Account Exists")
        } else {
            val ops = ArrayList<ContentProviderOperation>()
            // insert account name and account type
            ops.add(
                ContentProviderOperation
                    .newInsert(
                        addCallerIsSyncAdapterParameter(
                            RawContacts.CONTENT_URI,
                            true
                        )
                    )
                    .withValue(RawContacts.ACCOUNT_NAME, SyncStateContract.Constants.ACCOUNT_NAME)
                    .withValue(RawContacts.ACCOUNT_TYPE, SyncStateContract.Constants.ACCOUNT_TYPE)
                    .withValue(
                        RawContacts.AGGREGATION_MODE,
                        RawContacts.AGGREGATION_MODE_DEFAULT
                    )
                    .build()
            )
            // insert contact number
            ops.add(
                ContentProviderOperation
                    .newInsert(
                        addCallerIsSyncAdapterParameter(
                            ContactsContract.Data.CONTENT_URI,
                            true
                        )
                    )
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(
                        ContactsContract.Data.MIMETYPE,
                        CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                    )
                    .withValue(CommonDataKinds.Phone.NUMBER, contact.number)
                    .build()
            )
            // insert contact name
//            ops.add(ContentProviderOperation
//                    .newInsert(addCallerIsSyncAdapterParameter(ContactsContract.Data.CONTENT_URI, true))
//                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
//                    .withValue(ContactsContract.Data.MIMETYPE,
//                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
//                    .withValue(CommonDataKinds.StructuredName.DISPLAY_NAME, contact.name)
//                    .build());
// insert mime-type data
            ops.add(
                ContentProviderOperation
                    .newInsert(
                        addCallerIsSyncAdapterParameter(
                            ContactsContract.Data.CONTENT_URI,
                            true
                        )
                    )
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE, MIMETYPE)
                    .withValue(ContactsContract.Data.DATA1, 12345)
                    .withValue(ContactsContract.Data.DATA2, "user")
                    .withValue(ContactsContract.Data.DATA3, "ContactsDemo")
                    .build()
            )
            try {
                resolver.applyBatch(ContactsContract.AUTHORITY, ops)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Check if contact is already registered with app
     * @param resolver
     * @param id
     * @return
     */
    private fun isAlreadyRegistered(resolver: ContentResolver, id: String): Boolean {
        var isRegistered = false
        val str: MutableList<String> = ArrayList()
        //query raw contact id's from the contact id
        val c: Cursor? = resolver.query(
            RawContacts.CONTENT_URI, arrayOf(RawContacts._ID),
            RawContacts.CONTACT_ID + "=?", arrayOf(id), null
        )
        //fetch all raw contact id's and save them in a list of string
        if (c != null && c.moveToFirst()) {
            do {
                str.add(c.getString(c.getColumnIndexOrThrow(RawContacts._ID)))
            } while (c.moveToNext())
            c.close()
        }
        //query account types and check the account type for each raw contact id
        for (i in str.indices) {
            val c1: Cursor? = resolver.query(
                RawContacts.CONTENT_URI, arrayOf(RawContacts.ACCOUNT_TYPE),
                RawContacts._ID + "=?", arrayOf(str[i]), null
            )
            if (c1 != null) {
                c1.moveToFirst()
                val accType: String =
                    c1.getString(c1.getColumnIndexOrThrow(RawContacts.ACCOUNT_TYPE))
                if (accType != null && accType == "com.example.ajay.contacts_4") {
                    isRegistered = true
                    break
                }
                c1.close()
            }
        }
        return isRegistered
    }

    /**
     * Check for sync call
     * @param uri
     * @param isSyncOperation
     * @return
     */
    private fun addCallerIsSyncAdapterParameter(uri: Uri, isSyncOperation: Boolean): Uri {
        return if (isSyncOperation) {
            uri.buildUpon()
                .appendQueryParameter(ContactsContract.CALLER_IS_SYNCADAPTER, "true")
                .build()
        } else uri
    } //    public static void updateMyContact(Context context,String id){
//
//        Cursor ids = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null,
//                ContactsContract.Contacts._ID + " = ?",
//                new String[]{id}, null);
//
////        int rawContactId = ids.getColumnIndexOrThrow(ContactsContract.RawContacts.);
//
//        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
//        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
//                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,0)
//                .withValue(ContactsContract.Contacts._ID, id)
//                .withValue(ContactsContract.Data.DATA1, "Data1")
//                .withValue(ContactsContract.Data.DATA2, "Data2")
//                .withValue(ContactsContract.Data.DATA3, "MyData")
//                .build());
//
//        ids.close();
//
//        try{
//            context.getContentResolver().applyBatch(ContactsContract.AUTHORITY,ops);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        } catch (OperationApplicationException e) {
//            e.printStackTrace();
//        }
//    }
}