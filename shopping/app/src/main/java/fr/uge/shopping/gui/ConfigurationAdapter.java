package fr.uge.shopping.gui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import fr.uge.shopping.R;

import java.util.ArrayList;
import java.util.List;

public class ConfigurationAdapter extends RecyclerView.Adapter<ConfigurationAdapter.ViewHolder> {

    private List<ConfigurationItem> Data;
    private LayoutInflater Inflater;

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.entry_name);
        }

        private void update(ConfigurationItem item) {
            textView.setText(item.getName());
        }

    }

    public ConfigurationAdapter(Context context, List<ConfigurationItem> data) {
        this.Inflater = LayoutInflater.from(context);
        this.Data = new ArrayList<>();
        this.Data.addAll(data);
    }

    public void setData(List<ConfigurationItem> data) {
        Data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_entries, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.update(Data.get(position));
        Log.i("load123", "setted text as "+holder.textView.getText());
    }

    @Override
    public int getItemCount() {
        return Data.size();
    }

    public ConfigurationItem getItem(int id) {
        return Data.get(id);
    }

}