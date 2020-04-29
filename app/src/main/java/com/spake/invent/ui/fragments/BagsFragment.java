package com.spake.invent.ui.fragments;

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

import com.spake.invent.ScanBarcodeActivity;
import com.spake.invent.ShowItemInfo;
import com.spake.invent.database.entity.Item;
import com.spake.invent.database.entity.StoragePlace;
import com.spake.invent.ui.ItemAdapter;
import com.spake.invent.NewStoragePlaceActivity;
import com.spake.invent.R;
import com.spake.invent.ui.RVFragment;
import com.spake.invent.ui.StoragePlaceAdapter;
import com.spake.invent.ui.StoragePlaceViewModel;
import com.spake.invent.ui.ItemsViewModel;

public class BagsFragment extends RVFragment {
    private StoragePlaceViewModel viewModel;
    private StoragePlaceAdapter adapter;
    private boolean isOnItemList = false;
    ItemAdapter itemAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this).get(StoragePlaceViewModel.class);
        View root = inflater.inflate(R.layout.fragment_items, container, false);

        return root;
    }

        @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.rv_items);
        adapter = new StoragePlaceAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        viewModel.getAll(StoragePlace.Type.BAG).observe(getViewLifecycleOwner(), items -> adapter.setData(items));

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onRowClick(int position) {
        super.onRowClick(position);

        if(isOnItemList){
            Log.i("CLICKED", "ON ITEM"+Integer.toString(position));
            Item clickedItem = itemAdapter.getItemByPosition(position);
            Intent intent = new Intent(getActivity(), ShowItemInfo.class);
            Bundle bundle = new Bundle();
            bundle.putInt("item_id", clickedItem.getId());
            intent.putExtras(bundle);
            startActivity(intent);
            return;
        }

        StoragePlace sp = adapter.getStoragePlaceByPosition(position);
        itemAdapter = new ItemAdapter(getActivity());
        recyclerView.setAdapter(itemAdapter);
        ItemsViewModel viewModel = ViewModelProviders.of(this).get(ItemsViewModel.class);
        viewModel.getByStoragePlace(sp.getId()).observe(getViewLifecycleOwner(), items -> itemAdapter.setData(items));
        isOnItemList = true;
//        getActivity().getActionBar().setTitle(sp.getName());
    }

    @Override
    public void addNewItem() {
        if(isOnItemList){
            Intent intent = new Intent(getActivity(), ScanBarcodeActivity.class);
            startActivity(intent);
            return;
        };

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