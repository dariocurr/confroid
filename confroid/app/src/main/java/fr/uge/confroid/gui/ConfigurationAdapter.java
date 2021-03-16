package fr.uge.confroid.gui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import fr.uge.confroid.R;

import java.util.List;

public class ConfigurationAdapter extends RecyclerView.Adapter<ConfigurationAdapter.ViewHolder> {

    private List<String> Data;
    private LayoutInflater Inflater;
    private ItemClickListener clickListener;

    /**
     * stores and recycles views as they are scrolled off screen
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textView;

        /**
         * @param itemView
         */
        ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.configuration_name);
            itemView.setOnClickListener(this);
        }

        /**
         * @param view
         */
        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }
    }

    /**
     * @param context
     * @param data
     * data is passed into the constructor
     */
    public ConfigurationAdapter(Context context, List<String> data) {
        this.Inflater = LayoutInflater.from(context);
        this.Data = data;
    }

    /**
     * @param data
     */
    public void setData(List<String> data) {
        Data = data;
    }

    /**
     * @param parent
     * @param viewType
     * @return ViewHolder(view)
     * inflates the row layout from xml when needed
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = Inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * @param holder
     * @param position
     * binds the data to the TextView in each row
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String row = Data.get(position);
        holder.textView.setText(row);
    }

    /**
     * @return ItemCount (Size)
     *  total number of rows
     */
    @Override
    public int getItemCount() {
        return Data.size();
    }

    /**
     * @param id
     * @return item id
     * convenience method for getting data at click position
     */
    public String getItem(int id) {
        return Data.get(id);
    }

    /**
     * @param itemClickListener
     * allows clicks events to be caught
     */
    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    /**
     * parent activity will implement this method to respond to click events
     */
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
