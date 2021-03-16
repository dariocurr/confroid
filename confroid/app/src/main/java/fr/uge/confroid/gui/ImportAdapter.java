package fr.uge.confroid.gui;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import fr.uge.confroid.ImportActivity;
import fr.uge.confroid.R;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ImportAdapter extends RecyclerView.Adapter<ImportAdapter.ViewHolder> {
    private List<ImportItem> configurations;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private TextView textView;
        private ImageView imageView;

        /**
         * @param itemView
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            textView = itemView.findViewById(R.id.configuration_name);
            imageView = itemView.findViewById(R.id.logo);
        }

        /**
         * @param item
         */
        private void update(ImportItem item) {
            imageView.setImageBitmap(item.getBitmap(imageView.getContext()));
            textView.setText(item.getName());
        }
    }

    /**
     * @param context
     * @param configurations
     */
    public ImportAdapter(Context context, List<ImportItem> configurations) {
        super();
        this.context = context;
        this.configurations = new ArrayList<>();
        this.configurations.addAll(configurations);
    }

    /**
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_configurations, parent, false));
    }

    /**
     * @param holder
     * @param position
     */
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

    /**
     * @return Item size
     */
    @Override
    public int getItemCount() {
        return configurations.size();
    }

    /**
     * @return selected item
     */
    public int getSelectedItemCount() {
        /*int sum = 0;
        for(ImportItem i : configurations) {
            if (i.isSelected()) {
                sum++;
            }
        }
        return sum;*/
        return (int) configurations.stream().filter(ImportItem::isSelected).count();
    }

    /**
     * @return collection of selected items
     */
    public ArrayList<ImportItem> getSelectedItems() {
        return configurations.stream()
                .filter(ImportItem::isSelected)
                .collect(Collectors.toCollection((Supplier<ArrayList<ImportItem>>) ArrayList::new));
    }

}
