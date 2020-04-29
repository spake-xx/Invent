package com.spake.invent.ui.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.spake.invent.ui.ItemAdapter;
import com.spake.invent.R;
import com.spake.invent.ScanBarcodeActivity;
import com.spake.invent.database.entity.Item;
import com.spake.invent.ui.ItemsViewModel;
import com.spake.invent.ui.RVFragment;

public class SummaryFragment extends Fragment {
    private ItemsViewModel viewModel;
    private ItemAdapter adapter;
    TextView txtItemCount;
    Button btnScan;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this).get(ItemsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_summary, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        txtItemCount = view.findViewById(R.id.txtItemCount);
        btnScan = view.findViewById(R.id.btnScan);

        viewModel.getCount().observe(getViewLifecycleOwner(), count -> {
            txtItemCount.setText(count.toString());
        });
    }


}
