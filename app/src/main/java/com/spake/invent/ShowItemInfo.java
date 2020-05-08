package com.spake.invent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.widget.TextView;

import com.spake.invent.database.entity.Item;
import com.spake.invent.ui.StoragePlaceViewModel;
import com.spake.invent.ui.ItemsViewModel;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class ShowItemInfo extends AppCompatActivity {
    StoragePlaceViewModel storagePlaceViewModel;
    ItemsViewModel itemViewModel;
    Item item;
    TextView txtItemName, txtItemDescription, txtStoragePlace, txtExpireDate;
    int itemId;
    final Format formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);

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
        getItemInfo();
    }

    /**
     * Inits all buttons, textviews and other from layout to variables
     */
    public void initViews(){
        txtItemName = findViewById(R.id.itemName);
        txtItemDescription = findViewById(R.id.itemDescription);
        txtStoragePlace = findViewById(R.id.storagePlace);
        txtExpireDate = findViewById(R.id.txtExpireDate);
    }

    /**
     * Loads item info from database and pass it to view
     */
    private void getItemInfo(){
        itemViewModel.getSingle(itemId).observe(this, loadedItem -> {
            item = loadedItem;
            txtItemName.setText(item.getName());
            txtItemDescription.setText(item.getDescription());
            if(item.getExpireAt()!=null) txtExpireDate.setText("Trwałość do "+formatter.format(item.getExpireAt()));
            storagePlaceViewModel.getSingle(item.getStoragePlaceId()).observe(this, storagePlace ->{
                txtStoragePlace.setText(storagePlace.getName());
            });
        });
    }
}
