package com.spake.invent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.spake.invent.database.AppDatabase;
import com.spake.invent.database.entity.Item;
import com.spake.invent.ui.ItemsViewModel;
import com.spake.invent.ui.StoragePlaceViewModel;

import java.util.Date;

public class NewItemActivity extends AppCompatActivity {
    EditText txtBarcodeValue, txtName, txtDesc;
    Button btnSave;
    Intent i;
    ItemsViewModel itemViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);
        i = getIntent();
        itemViewModel = ViewModelProviders.of(this).get(ItemsViewModel.class);

        initViews();
    }

    public void onSaveButtonClicked(){
        String barcode = txtBarcodeValue.getText().toString();
        String name = txtName.getText().toString();
        String desc = txtDesc.getText().toString();

        Item newItem = new Item(barcode, name, desc, 5);
        try {
            itemViewModel.insert(newItem);
        }catch(SQLiteConstraintException e){
            Log.e("SQLITE:",e.getLocalizedMessage());
        }
        finish();
    }

    protected void initViews(){
        txtBarcodeValue = findViewById(R.id.txtBarcode);
        txtName = findViewById(R.id.txtName);
        txtDesc = findViewById(R.id.txtDescription);
        txtBarcodeValue.setText(i.getStringExtra("barcode"));
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener((view)->{ onSaveButtonClicked(); });
    }
}
