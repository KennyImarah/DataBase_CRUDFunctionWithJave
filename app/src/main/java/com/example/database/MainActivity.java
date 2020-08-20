package com.example.database;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.database.data.DatabaseHandler;
import com.example.database.model.Contact;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseHandler db = new DatabaseHandler(MainActivity.this);// DatabaseHandler object assigned MainActivity

        //Create contact object
        Contact kenny = new Contact();  // contact object
        kenny.setName("Kenny");          // set
        kenny.setPhoneNumber("613 6000 315");  // set

        // create a second contact
        Contact jason = new Contact();  // contact object
        jason.setName("Jason");  // set
        jason.setPhoneNumber("95760987");  // set

        Contact c = db.getContact(2);  // contact object assigned DatabaseHandler getContact method

        c.setName("New Name");
        c.setPhoneNumber("986858");

        int updateRow = db.updateContact(c);


        Log.d("Main", "onCreate: " + c.getName() + c.getPhoneNumber());

        Log.d("RowId", "onCreate: " + updateRow);

        // db.addContact(jason);

        List<Contact> contactList = db.getAllContacts();  // Contact List object

        for (Contact contact: contactList){  // add contactList to contact object
            Log.d("MainActivity", "onCreate: " + contact.getName());
        }

    }
}