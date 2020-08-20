package com.example.database.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.database.R;
import com.example.database.model.Contact;
import com.example.database.util.Util;

import java.util.ArrayList;
import java.util.List;

 public class DatabaseHandler extends SQLiteOpenHelper {
    /**
     * Create a helper object to create, open, and/or manage a database.
     * This method always returns very quickly.  The database is not actually
     * created or opened until one of {@link #getWritableDatabase} or
     * {@link #getReadableDatabase} is called.
     *
     * @param context to use for locating paths to the the database
     *
     *
     *                {@link #onUpgrade} will be used to upgrade the database; if the database is
     *                newer, {@link #onDowngrade} will be used to downgrade the database
     */
    public DatabaseHandler(@Nullable Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //SQL - Structured Query Language

        String CREATE_CONTACT_TABLE = R.string.create_table + Util.KEY_NAME + "(" + Util.KEY_ID + " " +
                "INTEGER PRIMARY KEY," + Util.KEY_NAME + " TEXT,"
                + Util.KEY_PHONE_NUMBER + " TEXT" + ")";
        db.execSQL(CREATE_CONTACT_TABLE); // execute create table


    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     *
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = String.valueOf(R.string.db_droptable);
        db.execSQL(DROP_TABLE, new String[]{Util.DATABASE_NAME} );

        //Create a table again
        onCreate(db);
    }

    /**
     * Create a helper object to create, open, and/or manage a database.
     * The database is not actually created or opened until one of
     * {@link #getWritableDatabase} or {@link #getReadableDatabase} is called.
     *
     * <p>Accepts input param: a concrete instance of {@link DatabaseErrorHandler} to be
     * used to handle corruption when sqlite reports database corruption.</p>
     *
     * @param context      to use for locating paths to the the database
     * @param name         of the database file, or null for an in-memory database
     * @param factory      to use for creating cursor objects, or null for the default
     * @param version      number of the database (starting at 1); if the database is older,
     *                     {@link #onUpgrade} will be used to upgrade the database; if the database is
     *                     newer, {@link #onDowngrade} will be used to downgrade the database
     * @param errorHandler the {@link DatabaseErrorHandler} to be used when sqlite reports database
     */



    public DatabaseHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    /**
     * Create a helper object to create, open, and/or manage a database.
     * This method always returns very quickly.  The database is not actually
     * created or opened until one of {@link #getWritableDatabase} or
     * {@link #getReadableDatabase} is called.
     *
     * @param context    to use for locating paths to the the database
     * @param name       of the database file, or null for an in-memory database
     * @param version    number of the database (starting at 1); if the database is older,
     *                   {@link #onUpgrade} will be used to upgrade the database; if the database is
     *                   newer, {@link #onDowngrade} will be used to downgrade the database
     * @param openParams configuration parameters that are used for opening {@link SQLiteDatabase}.
     *                   Please note that {@link SQLiteDatabase#CREATE_IF_NECESSARY} flag will always be
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    public DatabaseHandler(@Nullable Context context, @Nullable String name, int version, @NonNull SQLiteDatabase.OpenParams openParams) {
        super(context, name, version, openParams);
    }

    // CRUD operations

    public void addContact(Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();     // use ContentValues

        values.put(Util.KEY_NAME, contact.getName());
        values.put(Util.KEY_PHONE_NUMBER, contact.getPhoneNumber());

        //Insert to row
        db.insert(Util.TABLE_NAME, null, values);

        Log.d("DBHandler", "addContact: " + "item added");

        db.close(); // close db connection

    }

    // Get a contact

    public Contact getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Util.TABLE_NAME, new String[]{Util.KEY_ID, Util.KEY_NAME, Util.KEY_PHONE_NUMBER},  // add Uyil TABLE_NAME
                Util.KEY_ID + "=?", new String[]{String.valueOf(id)},  // add util Key_ID
                null, null, null);

        if (cursor !=null)
            cursor.moveToFirst();


        Contact contact = new Contact();

        contact.setId(Integer.parseInt(cursor.getString(0)));
        contact.setName(cursor.getString(1));
        contact.setPhoneNumber(cursor.getString(2));

        return contact;

    }

    // getAllContact method

    public List<Contact> getAllContacts(){
        List<Contact> contactList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        // Select all contacts

        String selectAll = "SELECT * FROM " + Util.TABLE_NAME;
        Cursor cursor = db.rawQuery(selectAll, null);

        // Loop through data

        if (cursor.moveToFirst()) {  // check cursor state if items exist
            do {
                Contact contact = new Contact();   // contact object
                contact.setId(Integer.parseInt(cursor.getString(0)));  // get index
                contact.setName(cursor.getString(1));
                contact.setPhoneNumber(cursor.getString(2));

                // add contact objects to our list
                contactList.add(contact);
            }while (cursor.moveToNext());
        }

        return contactList;
    }

    // update contact

     public int updateContact(Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Util.KEY_NAME, contact.getName());
        values.put(Util.KEY_PHONE_NUMBER, contact.getPhoneNumber());

        // update the row

         // update (tablename, values, where id )
         return db.update(Util.TABLE_NAME, values, Util.KEY_ID + "=?",
                 new String[]{
                         String.valueOf(contact.getId())});
     }
}
