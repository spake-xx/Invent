package com.spake.invent.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.spake.invent.R;
import com.spake.invent.database.entity.Item;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * RecyclerView adapter for Item entity
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {
    Context context;
    List<Item> data;

    public ItemAdapter(Context ct){
        context = ct;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_list_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.myText1.setText(data.get(position).getName());
        holder.myText2.setText(data.get(position).getDescription());
    }

    /**
     * Get item by position from adapter data set.
     * @param position position in RecyclerView adapter.
     * @return Item object
     */
    public Item getItemByPosition(int position){
        return data.get(position);
    }

    @Override
    public int getItemCount() {
        if(data==null) return 0;
        return data.size();
    }

    /**
     * Sets adapter data.
     * @param newData new data set.
     */
    public void setData(List<Item> newData) {
        this.data = newData;
        notifyDataSetChanged();
    }

    /**
     * Get adapter data set.
     * @return List of Items in data set.
     */
    public List<Item> getData() {
        return data;
    }

    /**
     * Removes item from data set and notify adapter about remove.
     * @param position Item position in data set to remove.
     */
    public void remove(int position){
        data.remove(position);
        notifyItemRemoved(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView myText1, myText2;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myText1 = itemView.findViewById(R.id.item_name);
            myText2 = itemView.findViewById(R.id.item_description);

        }
    }
}
