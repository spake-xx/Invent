package com.spake.invent.ui.bags;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.spake.invent.database.entity.StoragePlace;
import com.spake.invent.ui.ItemAdapter;
import com.spake.invent.NewStoragePlaceActivity;
import com.spake.invent.R;
import com.spake.invent.ui.RVFragment;
import com.spake.invent.ui.StoragePlaceAdapter;
import com.spake.invent.ui.items.ItemsViewModel;

public class BagsFragment extends RVFragment {
    private BagsViewModel viewModel;
    private StoragePlaceAdapter adapter;
    private boolean isOnItemList = false;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this).get(BagsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_items, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.rv_items);
        adapter = new StoragePlaceAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        viewModel.getAll().observe(getViewLifecycleOwner(), items -> adapter.setData(items));

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onRowClick(int position) {
        super.onRowClick(position);

        if(isOnItemList) return;


        StoragePlace sp = adapter.getStoragePlaceByPosition(position);
        ItemAdapter adapter = new ItemAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        ItemsViewModel viewModel = ViewModelProviders.of(this).get(ItemsViewModel.class);
        viewModel.getByStoragePlace(sp.getId()).observe(getViewLifecycleOwner(), items -> adapter.setData(items));
    }

    @Override
    public void addNewItem() {
        if(isOnItemList) return;

        Intent intent = new Intent(getActivity(), NewStoragePlaceActivity.class);
        startActivity(intent);
    }

    @Override
    public void leftSwipe(int position) {
//        BagsViewModel item = adapter.getData().get(position);
//
        if(isOnItemList) return;
        new AlertDialog.Builder(getActivity())
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("Usunieto: ", adapter.getStoragePlaceByPosition(position).getName());
                        viewModel.remove(adapter.getStoragePlaceByPosition(position));
                        adapter.remove(position);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void rightSwipe(int position) {

    }
}
