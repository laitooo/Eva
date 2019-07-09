package zxc.laitooo.eva.data;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Laitooo San on 30/06/2019.
 */

public class GetContacts extends AsyncTask<Void,Void,ArrayList<Contact>> {

    private Context context;

    public GetContacts(Context context) {
        this.context = context;
    }

    @Override
    protected ArrayList<Contact> doInBackground(Void... params) {
        ArrayList<Contact> contacts = new ArrayList<>();

        Cursor cursor = null;
        ContentResolver resolver = context.getContentResolver();
        cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        assert cursor != null;
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Contact a = new Contact(null, null);

                String _id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                a.setName(name);

                int phone = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (phone > 0) {
                    Cursor cursor2 = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", new String[]{_id}, null);


                    while (cursor2.moveToNext()) {
                        a.setPhone(cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                    }

                    cursor2.close();
                }

                contacts.add(a);
            }
            cursor.close();
        }

        return contacts;
    }

    @Override
    protected void onPostExecute(ArrayList<Contact> contacts) {
        super.onPostExecute(contacts);
        Log.e("GetContacts","contacts loaded");
    }
}
