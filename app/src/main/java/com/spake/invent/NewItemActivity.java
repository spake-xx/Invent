package com.spake.invent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.spake.invent.database.AppDatabase;
import com.spake.invent.database.entity.Item;

import java.util.Date;

public class NewItemActivity extends AppCompatActivity {

    EditText txtBarcodeValue, txtName, txtDesc;
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

        Item newItem = new Item(name, desc, date);
        mDb.itemDao().insertItem(newItem);
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
