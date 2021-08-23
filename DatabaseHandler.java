package com.tlama.databasetest.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.tlama.databasetest.R;
import com.tlama.databasetest.model.Contact;
import com.tlama.databasetest.util.Util;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    public DatabaseHandler(Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //SQL - Structured Query Language
        String CREATE_CONTACT_TABLE = "CREATE TABLE " + Util.TABLE_NAME + "(" +
                Util.KEY_ID + " INTEGER PRIMARY KEY," + Util.KEY_NAME + " TEXT,"
                + Util.KEY_PHONE_NUMBER + " TEXT" + ")";

        db.execSQL(CREATE_CONTACT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = String.valueOf(R.string.DROP_TABLE_STRING);
        db.execSQL(DROP_TABLE, new String[]{Util.DATABASE_NAME});

        //Create table again
        onCreate(db);
    }

    /*First
        CRUD = Create, Read, Update, Delete
     */
    //Add Contact
    public void addContact(Contact contact){
        //Get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        //Specify values to insert
        ContentValues values = new ContentValues();
        values.put(Util.KEY_NAME, contact.getName());
        values.put(Util.KEY_PHONE_NUMBER, contact.getPhoneNumber());

        //Insert to row
        db.insert(Util.TABLE_NAME, null, values);

        //Close database connection
        db.close();
    }

    //Get a contact

    public Contact getContact(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Util.TABLE_NAME,
                new String[]{ Util.KEY_ID, Util.KEY_NAME, Util.KEY_PHONE_NUMBER},
                Util.KEY_ID +"=?", new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor != null){
            cursor.moveToFirst();
        }

        Contact contact = new Contact();
        contact.setId(Integer.parseInt(cursor.getString(0)));
        contact.setName(cursor.getString(1));
        contact.setPhoneNumber(cursor.getString(2));

        return contact;
    }

    //Get all contacts
    public List<Contact> getAllContacts(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Contact> contactList = new ArrayList<>();

        //Select all contacts from database table
        String selectAll = "SELECT * FROM " + Util.TABLE_NAME;
        Cursor cursor = db.rawQuery(selectAll, null);

        //Loop through data
        if (cursor.moveToFirst()) {

            do {
                Contact contact = new Contact();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setPhoneNumber(cursor.getString(2));

                //Add contact object to contact list
                contactList.add(contact);
            }while (cursor.moveToNext());
        }
        return contactList;
    }

    //Update contact
    public int updateContact(Contact contact){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Util.KEY_NAME, contact.getName());
        values.put(Util.KEY_PHONE_NUMBER, contact.getPhoneNumber());

        //update the row
        //update(tableName, values, where id =
        return db.update(Util.TABLE_NAME, values, Util.KEY_ID + "=?", new String[]{String.valueOf(contact.getId())});
    }

    public void deleteSingleContact(Contact contact){

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(Util.TABLE_NAME, Util.KEY_ID + "=?", new String[]{String.valueOf(contact.getId())});

        db.close();
    }

    //Gets the total number of contacts in a table
    public int getContactsCount(){
        String countQuery = "SELECT * FROM " + Util.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        return cursor.getCount();
    }
}
