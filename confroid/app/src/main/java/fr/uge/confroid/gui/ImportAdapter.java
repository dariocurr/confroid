package fr.uge.confroid.gui;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import fr.uge.confroid.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ImportAdapter extends RecyclerView.Adapter<ImportAdapter.ViewHolder> {
    private List<ImportItem> configurations;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private TextView textView;
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            textView = itemView.findViewById(R.id.configuration_name);
            imageView = itemView.findViewById(R.id.logo);
        }

        private void update(ImportItem item) {
            imageView.setImageBitmap(item.getBitmap(imageView.getContext()));
            textView.setText(item.getName());
        }
    }

    public ImportAdapter(Context context, List<String> configurations) {
        super();
        this.context = context;
        this.configurations = new ArrayList<>();
        for(String s : configurations) {
            this.configurations.add(new ImportItem(s));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_configurations, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImportItem i = configurations.get(position);
        holder.update(i);
        holder.view.setBackgroundColor(i.isSelected() ? Color.CYAN : Color.TRANSPARENT);
        holder.view.setOnClickListener(v -> {
            ((ImportActivity)context).invalidateOptionsMenu();
            i.setSelected(!i.isSelected());
            holder.view.setBackgroundColor(i.isSelected() ? Color.argb(127, 52, 152, 219) : Color.TRANSPARENT);
        });
    }

    @Override
    public int getItemCount() {
        return configurations.size();
    }

    public int getSelectedItemCount() {
        int sum = 0;
        for(ImportItem i : configurations) {
            if (i.isSelected()) {
                sum++;
            }
        }
        return sum;
    }


}
