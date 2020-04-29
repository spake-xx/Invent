package com.spake.invent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.widget.TextView;

import com.spake.invent.database.entity.Item;
import com.spake.invent.ui.StoragePlaceViewModel;
import com.spake.invent.ui.ItemsViewModel;

public class ShowItemInfo extends AppCompatActivity {
    StoragePlaceViewModel storagePlaceViewModel;
    ItemsViewModel itemViewModel;
    Item item;
    TextView txtItemName, txtItemDescription, txtStoragePlace;
    int itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_item_info);

        storagePlaceViewModel = ViewModelProviders.of(this).get(StoragePlaceViewModel.class);
        itemViewModel = ViewModelProviders.of(this).get(ItemsViewModel.class);
        Bundle b = getIntent().getExtras();
        assert b!=null;
        itemId = b.getInt("item_id");
        initViews();
    }

    public void initViews(){
//        assert getSupportActionBar()!=null;
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtItemName = findViewById(R.id.itemName);
        txtItemDescription = findViewById(R.id.itemDescription);
        txtStoragePlace = findViewById(R.id.storagePlace);

        itemViewModel.getSingle(itemId).observe(this, item -> {
            txtItemName.setText(item.getName());
            txtItemDescription.setText(item.getDescription());
            storagePlaceViewModel.getSingle(item.getStoragePlaceId()).observe(this, storagePlace ->{
                txtStoragePlace.setText(storagePlace.getName());
            });
        });
    }
}
