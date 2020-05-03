package com.spake.invent.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.spake.invent.EditItemActivity;
import com.spake.invent.R;
import com.spake.invent.ScanBarcodeActivity;
import com.spake.invent.ShowItemInfo;
import com.spake.invent.database.entity.Item;
import com.spake.invent.database.entity.StoragePlace;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Abstract class for all recyclerviews in the application.
 * Has handling for swiping left/right and clicking single row.
 */
public abstract class RVFragment extends Fragment {
    protected StoragePlace.Type storagePlaceType;
    private FloatingActionButton btnAdd;

    protected RecyclerView recyclerView;
    protected StoragePlaceViewModel storagePlaceViewModel;
    protected ItemsViewModel itemsViewModel;

    protected StoragePlaceAdapter storagePlaceAdapter;
    protected ItemAdapter itemAdapter;

    protected boolean isOnItemList = false;
    protected StoragePlace selectedStoragePlace;

    /**
     * @param type for storage place handled
     */
    public RVFragment(StoragePlace.Type type){
        storagePlaceType = type;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        storagePlaceViewModel = ViewModelProviders.of(this).get(StoragePlaceViewModel.class);
        itemsViewModel = ViewModelProviders.of(this).get(ItemsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_items, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.rv_items);
        storagePlaceAdapter = new StoragePlaceAdapter(getActivity());
        recyclerView.setAdapter(storagePlaceAdapter);
        storagePlaceViewModel.getAll(storagePlaceType).observe(getViewLifecycleOwner(), items -> storagePlaceAdapter.setData(items));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                onRowClick(position);
            }
        }));
        btnAdd = view.findViewById(R.id.btnNewItem);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewItem();
            }
        });

        initSwipe();
    }

    /**
     * Method called when user clicks on recyclerview single item.
     * In default has itemAdapter handling - ShowItemInfo activity starts when user is on itemview adapter.
     * @param position position in the recyclerview adapter has been clicked
     */
    public void onRowClick(int position){
        Log.i("Clicked row", Integer.toString(position));
        if(isOnItemList){
            Item clickedItem = itemAdapter.getItemByPosition(position);
            Intent intent = new Intent(getActivity(), ShowItemInfo.class);
            Bundle bundle = new Bundle();
            bundle.putInt("item_id", clickedItem.getId());
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    /**
     * Method called when user clicks on add new item button.
     * In default has itemAdapter handling - ScanBarcodeActivity starts when user is on itemview adapter.
     */
    public void addNewItem(){
        if(isOnItemList){
            Intent intent = new Intent(getActivity(), ScanBarcodeActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("storage_place_id", selectedStoragePlace.getId());
            intent.putExtras(bundle);
            startActivity(intent);
        };
    };

    /**
     * Implements all swipe posibilities.
     * Implements also canvas - drawing colored background and icon on the user swipe.
     */
    private void initSwipe() {
        Paint p = new Paint();

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT) {
                    Log.i("SWAJP:", "Swajpnieto w lewo");
                    leftSwipe(position);
                } else {
                    Log.i("SWAJP:", "Swajpnieto w prawo");
                    rightSwipe(position);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;
                    if (dX > 0) {
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, p);
                        Drawable d = getResources().getDrawable(R.drawable.ic_edit_black);
                        icon = drawableToBitmap(d);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        Drawable d = getResources().getDrawable(R.drawable.ic_delete_black);
                        icon = drawableToBitmap(d);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    /**
     * Helper method used to convert Drawable to Bitmap
     * @param drawable drawable object to convert
     * @return bitmap result
     */
    private static Bitmap drawableToBitmap (Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }


    /**
     * Method called by swiping left that you can bind to your own logic
     * In default has itemAdapter handling.
     * @param position Recyclerview swiped row position number
     */
    public void leftSwipe(int position){
        if(isOnItemList){
            Item item = itemAdapter.getItemByPosition(position);

            Log.i("Deleted: ", item.getName());
            itemsViewModel.remove(item);
            itemAdapter.remove(position);
            itemAdapter.notifyItemChanged(position);
        };

    }
    /**
     * Method called by swiping right that you can bind to your own logic
     * In default has itemAdapter handling.
     * @param position Recyclerview swiped row position number
     */
    public void rightSwipe(int position){
        if(isOnItemList){
            Item item = itemAdapter.getItemByPosition(position);
            Intent intent = new Intent(getActivity(), EditItemActivity.class);
            intent.putExtra("item_id", item.getId());
            startActivity(intent);
            itemAdapter.notifyItemChanged(position);
        };
    }
}
