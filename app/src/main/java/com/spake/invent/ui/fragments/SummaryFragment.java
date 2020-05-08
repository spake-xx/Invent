package com.spake.invent.ui.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.spake.invent.AlarmReceiver;
import com.spake.invent.ui.ItemAdapter;
import com.spake.invent.R;
import com.spake.invent.ScanBarcodeActivity;
import com.spake.invent.ui.ItemsViewModel;
import com.spake.invent.ui.StoragePlaceViewModel;

public class SummaryFragment extends Fragment {
    private ItemsViewModel itemsViewModel;
    private StoragePlaceViewModel storagePlaceViewModel;
    private ItemAdapter adapter;
    private TextView txtItemCount, txtStoragePlaceCount;
    private Button btnScan;
    private Switch swNotifications;
    private Intent notificationActionIntent;
    private AlarmManager alarmManager;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        itemsViewModel = ViewModelProviders.of(this).get(ItemsViewModel.class);
        storagePlaceViewModel = ViewModelProviders.of(this).get(StoragePlaceViewModel.class);

        View root = inflater.inflate(R.layout.fragment_summary, container, false);
        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        notificationActionIntent = new Intent(getActivity(), AlarmReceiver.class);
        notificationActionIntent.setAction(AlarmReceiver.ACTION_ALARM_RECEIVER);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        txtItemCount = view.findViewById(R.id.txtItemCount);
        txtStoragePlaceCount = view.findViewById(R.id.txtStoragePlaceCount);

        btnScan = view.findViewById(R.id.btnScan);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ScanBarcodeActivity.class);
                startActivity(i);
            }
        });

        swNotifications = view.findViewById(R.id.swNotifications);
        boolean alarmUp = (PendingIntent.getBroadcast(getContext(), 1001,notificationActionIntent,
                PendingIntent.FLAG_NO_CREATE) != null);
        swNotifications.setChecked(alarmUp);
        swNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    setNotificationAlarm();
                else
                    cancelNotificationAlarm();
            }
        });

        itemsViewModel.getCount().observe(getViewLifecycleOwner(), count -> {
            txtItemCount.setText(count.toString());
        });
        storagePlaceViewModel.getCount().observe(getViewLifecycleOwner(), count -> {
            txtStoragePlaceCount.setText(count.toString());
        });
    }
    private void setNotificationAlarm(){
//        final int interval = 24 * 60 * 60 * 1000;
        final int interval = 60000;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 1001,
                notificationActionIntent, PendingIntent.FLAG_CANCEL_CURRENT);//used unique ID as 1001
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime()+1000, interval, pendingIntent);//first start will start asap

        Log.i("Alarm", "Started alarm "+1001);
    }

    private void cancelNotificationAlarm() {
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getContext(), 1001, notificationActionIntent,0);

        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
        Log.i("Alarm", "Cancelled alarm "+1001);
    }
}