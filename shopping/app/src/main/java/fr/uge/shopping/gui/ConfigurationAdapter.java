package fr.uge.shopping.gui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import fr.uge.shopping.R;

import java.util.ArrayList;
import java.util.List;

public class ConfigurationAdapter extends RecyclerView.Adapter<ConfigurationAdapter.ViewHolder> {

    private List<ConfigurationItem> data;
    private LayoutInflater inflater;
    private ItemClickListener clickListener;

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textView;

        ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.entry_name);
        }

        private void update(ConfigurationItem item) {
            textView.setText(item.getName());
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }

    }

    public ConfigurationAdapter(Context context, List<ConfigurationItem> data) {
        this.inflater = LayoutInflater.from(context);
        this.data = new ArrayList<>();
        this.data.addAll(data);
    }

    public void setData(List<ConfigurationItem> data) {
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_entries, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.update(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public ConfigurationItem getItem(int id) {
        return data.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}