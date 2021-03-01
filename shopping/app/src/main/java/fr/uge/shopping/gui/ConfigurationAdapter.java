package fr.uge.shopping.gui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import fr.uge.shopping.R;

import java.util.List;

public class ConfigurationAdapter extends RecyclerView.Adapter<ConfigurationAdapter.ViewHolder> {

    private List<String> Data;
    private LayoutInflater Inflater;
    private ItemClickListener clickListener;

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textView;

        ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.entry_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // data is passed into the constructor
    public ConfigurationAdapter(Context context, List<String> data) {
        this.Inflater = LayoutInflater.from(context);
        this.Data = data;
    }

    public void setData(List<String> data) {
        Data = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = Inflater.inflate(R.layout.list_entries, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String row = Data.get(position);
        holder.textView.setText(row);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return Data.size();
    }

    // convenience method for getting data at click position
    public String getItem(int id) {
        return Data.get(id);
    }

    //allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}