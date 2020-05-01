package com.spake.invent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.spake.invent.database.entity.Item;
import com.spake.invent.ui.ItemsViewModel;

public class EditItemActivity extends AppCompatActivity {
    EditText txtBarcodeValue, txtName, txtDesc;
    Button btnSave;
    Intent i;
    ItemsViewModel itemViewModel;
    Item editingItem;
    String intentBarcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);
        i = getIntent();
        itemViewModel = ViewModelProviders.of(this).get(ItemsViewModel.class);
        intentBarcode = i.getStringExtra("barcode");
        initViews();

        if(intentBarcode ==null)
            initEditForm();
    }

    public void onSaveButtonClicked(){
        String barcode = txtBarcodeValue.getText().toString();
        String name = txtName.getText().toString();
        String desc = txtDesc.getText().toString();

        if(intentBarcode!=null) {
            int storagePlaceId = i.getIntExtra("storage_place_id", 0);
            Item newItem = new Item(barcode, name, desc, storagePlaceId);
            itemViewModel.insert(newItem);
        }else{
            Item editedItem = new Item(editingItem.getId(), barcode, name, desc, editingItem.getStoragePlaceId());
//            itemViewModel.update(newItem);
        }
        finish();
    }

    protected void initViews(){
        txtBarcodeValue = findViewById(R.id.txtBarcode);
        txtName = findViewById(R.id.txtName);
        txtDesc = findViewById(R.id.txtDescription);
        if(intentBarcode !=null) txtBarcodeValue.setText(i.getStringExtra("barcode"));
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener((view)->{ onSaveButtonClicked(); });
    }

    private void initEditForm(){
        int itemId = i.getIntExtra("item_id", 0);
        itemViewModel.getSingle(itemId).observe(this, item -> {
            editingItem = item;
            txtBarcodeValue.setText(item.getBarcode());
            txtName.setText(item.getName());
            txtDesc.setText(item.getDescription());
        });
    }
}
