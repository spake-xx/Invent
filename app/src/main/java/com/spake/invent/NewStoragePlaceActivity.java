package com.spake.invent;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.spake.invent.database.AppDatabase;
import com.spake.invent.database.entity.Item;
import com.spake.invent.database.entity.StoragePlace;

import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;

public class NewStoragePlaceActivity extends AppCompatActivity {
    EditText txtName, txtDesc;
    Button btnSave;
    Intent i;
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);
        i = getIntent();

        initViews();

        mDb = AppDatabase.getInstance(getApplicationContext());
    }

    public void onSaveButtonClicked(){
        Date date = new Date();
        String name = txtName.getText().toString();
        String desc = txtDesc.getText().toString();

        StoragePlace newStoragePlace = new StoragePlace(name, StoragePlace.Type.BAG, desc, date);
        try {
            mDb.storagePlaceDao().insertStoragePlace(newStoragePlace);
            Log.i("Database", "Added new storage place");
        }catch(SQLiteConstraintException e){
            Log.e("SQLITE:",e.getLocalizedMessage());
        }
        finish();
    }

    protected void initViews(){
        txtName = findViewById(R.id.txtName);
        txtDesc = findViewById(R.id.txtDescription);
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener((view)->{ onSaveButtonClicked(); });
    }
}
