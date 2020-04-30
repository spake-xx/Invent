
package com.spake.invent;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.spake.invent.database.entity.Item;
import com.spake.invent.ui.ItemsViewModel;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;

public class ScanBarcodeActivity extends AppCompatActivity {
    SurfaceView surfaceView;
    BarcodeDetector barcodeDetector;
    TextView txtBarcodeValue;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    Button btnAction;
    String scannedBarcode = "";
    Bundle bundle;
    ItemsViewModel itemsViewModel;
    Item scannedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);
        bundle = getIntent().getExtras();
        itemsViewModel = ViewModelProviders.of(this).get(ItemsViewModel.class);

        initViews();
    }

    private void initViews() {
        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        surfaceView = findViewById(R.id.surfaceView);
        btnAction = findViewById(R.id.btnAction);

        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (scannedBarcode.length() > 0) {
                    if(bundle != null) {
                        addNewItem();
                    }else{
                        showItemInfo();
                    }
                }
            }
        });
    }

    private void addNewItem(){
        Intent newIntent = new Intent(ScanBarcodeActivity.this, NewItemActivity.class);
        newIntent.putExtra("barcode", scannedBarcode);
        newIntent.putExtra("storage_place_id", bundle.getInt("storage_place_id"));
        startActivity(newIntent);
        finish();
    }

    private void showItemInfo(){
        itemsViewModel.getSingleByBarcode(scannedBarcode).observe(this, item -> {
            scannedItem = item;
        });
        Intent newIntent = new Intent(ScanBarcodeActivity.this, ShowItemInfo.class);

        Bundle newBundle = new Bundle();
        newBundle.putInt("item_id", scannedItem.getId());
        startActivity(newIntent);
        finish();
    }

    private void initialiseDetectorsAndSources() {
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(ScanBarcodeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(ScanBarcodeActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
//                Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    txtBarcodeValue.post(new Runnable() {
                        @Override
                        public void run() {
                                btnAction.setVisibility(View.VISIBLE);
                                scannedBarcode = barcodes.valueAt(0).displayValue;
                                txtBarcodeValue.setText(scannedBarcode);
                                Log.i("BARCODE", scannedBarcode);
                        }
                    });

                }
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }
}