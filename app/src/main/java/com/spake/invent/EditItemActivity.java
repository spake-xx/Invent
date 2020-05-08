package com.spake.invent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.spake.invent.database.entity.Item;
import com.spake.invent.ui.ItemsViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditItemActivity extends AppCompatActivity {
    final Calendar myCalendar = Calendar.getInstance();
    EditText txtBarcodeValue, txtName, txtDesc, txtExpireDate;
    Button btnSave;
    Intent i;
    ItemsViewModel itemViewModel;
    Item editingItem;
    String intentBarcode;
    final String dateFormat = "dd/MM/yyyy";
    SimpleDateFormat sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        i = getIntent();
        itemViewModel = ViewModelProviders.of(this).get(ItemsViewModel.class);
        intentBarcode = i.getStringExtra("barcode");
        sdf = new SimpleDateFormat(dateFormat, Locale.UK);
        initViews();

        if(intentBarcode ==null)
            loadItemInfo();
    }

    /**
     * Save Button Click-Callback.
     */
    protected void onSaveButtonClicked(){
        String barcode = txtBarcodeValue.getText().toString();
        String name = txtName.getText().toString();
        String desc = txtDesc.getText().toString();
        Date expireDate = null;
        try {
            expireDate = sdf.parse(txtExpireDate.getText().toString());
        }catch(ParseException e){};

        if(intentBarcode!=null) {
            int storagePlaceId = i.getIntExtra("storage_place_id", 0);
            Item newItem = new Item(barcode, name, desc, storagePlaceId, expireDate);
            itemViewModel.insert(newItem);
        }else{
            Item editedItem = new Item(editingItem.getId(), barcode, name, desc, editingItem.getStoragePlaceId(), expireDate);
            itemViewModel.update(editedItem);
        }
        finish();
    }

    /**
     * Inits all buttons, textviews and other from layout to variables
     */
    protected void initViews(){
        txtBarcodeValue = findViewById(R.id.txtBarcode);
        txtName = findViewById(R.id.txtName);
        txtDesc = findViewById(R.id.txtDescription);
        txtExpireDate = findViewById(R.id.txtExpireDate);
        if(intentBarcode !=null) txtBarcodeValue.setText(i.getStringExtra("barcode"));
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener((view)->{ onSaveButtonClicked(); });

        txtExpireDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(EditItemActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    /**
     * Loads item from database and pass it to the view
     */
    protected void loadItemInfo(){
        int itemId = i.getIntExtra("item_id", 0);
        itemViewModel.getSingle(itemId).observe(this, item -> {
            editingItem = item;
            txtBarcodeValue.setText(item.getBarcode());
            txtName.setText(item.getName());
            txtDesc.setText(item.getDescription());
            txtExpireDate.setText(sdf.format(item.getExpireAt()));
        });
    }

    /**
     * Listens DatePicker for date set
     */
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            txtExpireDate.setText(sdf.format(myCalendar.getTime()));
        }

    };
}
