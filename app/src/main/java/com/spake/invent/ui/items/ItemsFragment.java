package com.spake.invent.ui.items;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProviders;

import com.spake.invent.ui.ItemAdapter;
import com.spake.invent.R;
import com.spake.invent.ScannedBarcodeActivity;
import com.spake.invent.database.entity.Item;
import com.spake.invent.ui.RVFragment;

public class ItemsFragment extends RVFragment {
    private ItemsViewModel viewModel;
    private ItemAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this).get(ItemsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_items, container, false);
        return root;
    }

    @Override
    public void addNewItem() {
        Intent intent = new Intent(getActivity(), ScannedBarcodeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.rv_items);
        adapter = new ItemAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        viewModel.getAll().observe(getViewLifecycleOwner(), items -> adapter.setData(items));

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void leftSwipe(int position) {
        Item item = adapter.getData().get(position);

        new AlertDialog.Builder(getActivity())
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.removeItem(position);
                        viewModel.remove(item);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


}
