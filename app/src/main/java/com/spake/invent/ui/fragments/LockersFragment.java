package com.spake.invent.ui.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import com.spake.invent.database.entity.StoragePlace;
import com.spake.invent.ui.ItemAdapter;
import com.spake.invent.EditStoragePlaceActivity;
import com.spake.invent.ui.RVFragment;

public class LockersFragment extends RVFragment{
    public LockersFragment(){
        super(StoragePlace.Type.LOCKER);
    }

    @Override
    public void onRowClick(int position) {
        super.onRowClick(position);

        if(!isOnItemList) {
            selectedStoragePlace = storagePlaceAdapter.getStoragePlaceByPosition(position);
            itemAdapter = new ItemAdapter(getActivity());
            recyclerView.setAdapter(itemAdapter);
            itemsViewModel.getByStoragePlace(selectedStoragePlace.getId()).observe(getViewLifecycleOwner(), items -> itemAdapter.setData(items));
            isOnItemList = true;
        }
    }

    @Override
    public void addNewItem() {
        if(!isOnItemList) {
            Intent intent = new Intent(getActivity(), EditStoragePlaceActivity.class);
            intent.putExtra("storage_place_type", storagePlaceType);
            startActivity(intent);
        }

        super.addNewItem();
    }

    @Override
    public void leftSwipe(int position) {
//        BagsViewModel item = adapter.getData().get(position);
//
        if(!isOnItemList) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Usunąć torbę?")
                    .setMessage("Wszystkie przedmioty znajdujące się w torbie także zostaną usunięte.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Log.i("Deleted: ", storagePlaceAdapter.getStoragePlaceByPosition(position).getName());
                            storagePlaceViewModel.remove(storagePlaceAdapter.getStoragePlaceByPosition(position));
                            storagePlaceAdapter.remove(position);
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        super.leftSwipe(position);
    }

    @Override
    public void rightSwipe(int position) {
        if(!isOnItemList){
            StoragePlace sp = storagePlaceAdapter.getStoragePlaceByPosition(position);
            Intent intent = new Intent(getActivity(), EditStoragePlaceActivity.class);
            intent.putExtra("storage_place_id", sp.getId());
            startActivity(intent);
            storagePlaceAdapter.notifyItemChanged(position);
        }
        super.rightSwipe(position);
    }
}