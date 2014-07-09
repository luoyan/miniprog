package com.luoyan.androidsyntax;

import java.io.FileWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.SimpleAdapter;

public class PhoneContactsActivity extends ListActivity {
	private static String TAG = "PhoneContactsActivity";
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contacts);

		List<HashMap<String, String>> items = fillMaps(500);
		SimpleAdapter adapter = new SimpleAdapter(this, items,
				R.layout.list_item, new String[] { "name", "key" }, new int[] {
						R.id.item, R.id.item2 });
		this.setListAdapter(adapter);

	}

	private List<HashMap<String, String>> fillMaps(int maxLimit) {
		List<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();

		Cursor cur = null;
		String fileName = "phone_contacts.txt";
		FileWriter out = null;
		try {
			// Query using ContentResolver.query or Activity.managedQuery
			cur = getContentResolver().query(
					ContactsContract.Contacts.CONTENT_URI, null, null, null,
					null);
			int recordNum = 0;
			//out = new FileWriter(new File(this.getFilesDir(), fileName));
			out = new FileWriter(new File(Environment.getExternalStorageDirectory(), fileName));
			if (cur.moveToFirst()) {
				int idColumn = cur
						.getColumnIndex(ContactsContract.Contacts._ID);
				int displayNameColumn = cur
						.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
				// Iterate all users
				do {
					String contactId;
					String displayName;
					String phoneNumber = "";
					// Get the field values
					contactId = cur.getString(idColumn);
					displayName = cur.getString(displayNameColumn);
					// Get number of user's phoneNumbers
					int numberCount = cur
							.getInt(cur
									.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
					if (numberCount > 0) {
						Cursor phones = getContentResolver()
								.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
										null,
										ContactsContract.CommonDataKinds.Phone.CONTACT_ID
												+ " = " + contactId
										/*
										 * + " and " +
										 * ContactsContract.CommonDataKinds
										 * .Phone.TYPE + "=" +
										 * ContactsContract.CommonDataKinds
										 * .Phone.TYPE_MOBILE
										 */, null, null);
						if (phones.moveToFirst()) {
							int numberColumn = phones
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
							// Iterate all numbers
							do {
								phoneNumber += phones.getString(numberColumn)
										+ ",";
							} while (phones.moveToNext());
						}
						phones.close();
					}
					// Add values to items
					HashMap<String, String> i = new HashMap<String, String>();
					i.put("name", displayName);
					i.put("key", phoneNumber);
					out.write(displayName + ":" + phoneNumber + "\n");
					Log.d(TAG, "count = " + recordNum + " name : " + displayName + " key " + phoneNumber);
					items.add(i);
					recordNum += 1;
					if (recordNum > 0 && recordNum >= maxLimit)
						break;
				} while (cur.moveToNext());
				Log.d(TAG, "end while");
			} else {
				HashMap<String, String> i = new HashMap<String, String>();
				i.put("name", "Your Phone");
				i.put("key", "Have No Contacts.");
				items.add(i);
			}
			if (out != null)
				out.close();
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		} finally {
			Log.d(TAG, "end finally");
			if (cur != null)
				cur.close();
		}
		Log.d(TAG, "end fillMaps");
		return items;
	}
}
