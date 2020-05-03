package com.spake.invent;

import android.content.Intent;
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
    StoragePlace editingStoragePlace;
    int storagePlaceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);
        i = getIntent();
        storagePlaceType = (StoragePlace.Type) i.getSerializableExtra("storage_place_type");
        storagePlaceId = i.getIntExtra("storage_place_id", 0);
        storagePlaceViewModel = ViewModelProviders.of(this).get(StoragePlaceViewModel.class);

        initViews();
        if(storagePlaceId>0) loadStoragePlaceInfo();

    }

    /**
     * Save Button Click-Callback.
     */
    protected void onSaveButtonClicked(){
        Date date = new Date();
        String name = txtName.getText().toString();
        String desc = txtDesc.getText().toString();

        if(storagePlaceId>0){
            StoragePlace editedStoragePlace = new StoragePlace(editingStoragePlace.getId(),  name, editingStoragePlace.getType(), desc, editingStoragePlace.getCreatedAt());
            storagePlaceViewModel.update(editedStoragePlace);
        }else {
            StoragePlace newStoragePlace = new StoragePlace(name, storagePlaceType, desc, date);
            storagePlaceViewModel.insert(newStoragePlace);
            Log.i("Database", "Added new storage place");
        }

        finish();
    }

    /**
     * Inits all buttons, textviews and other from layout to variables
     */
    protected void initViews(){
        txtName = findViewById(R.id.txtName);
        txtDesc = findViewById(R.id.txtDescription);
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener((view)->{ onSaveButtonClicked(); });
    }

    /**
     * Loads StoragePlace from database and pass it to the view
     */
    protected void loadStoragePlaceInfo(){
        storagePlaceViewModel.getSingle(storagePlaceId).observe(this, storagePlace -> {
            editingStoragePlace = storagePlace;
            txtName.setText(storagePlace.getName());
            txtDesc.setText(storagePlace.getDescription());
        });
    }
}
