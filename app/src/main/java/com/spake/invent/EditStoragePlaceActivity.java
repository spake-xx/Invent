package com.spake.invent;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.spake.invent.database.entity.StoragePlace;
import com.spake.invent.ui.StoragePlaceViewModel;

import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

public class EditStoragePlaceActivity extends AppCompatActivity {
    EditText txtName, txtDesc;
    Button btnSave;
    Intent i;
    StoragePlaceViewModel storagePlaceViewModel;
    StoragePlace.Type storagePlaceType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);
        i = getIntent();
        storagePlaceType = (StoragePlace.Type) i.getSerializableExtra("storage_place_type");

        initViews();

        storagePlaceViewModel = ViewModelProviders.of(this).get(StoragePlaceViewModel.class);
    }

    public void onSaveButtonClicked(){
        Date date = new Date();
        String name = txtName.getText().toString();
        String desc = txtDesc.getText().toString();

        StoragePlace newStoragePlace = new StoragePlace(name, storagePlaceType, desc, date);
        try {
            storagePlaceViewModel.insert(newStoragePlace);
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
