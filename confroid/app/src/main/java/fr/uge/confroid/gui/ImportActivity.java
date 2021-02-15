package fr.uge.confroid.gui;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fr.uge.confroid.R;

import java.util.Arrays;

public class ImportActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ImportAdapter importAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);

        recyclerView = findViewById(R.id.import_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        String[] test = {"test1", "test2", "test3"};
        importAdapter = new ImportAdapter(this, Arrays.asList(test));
        recyclerView.setAdapter(importAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.import_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(importAdapter.getSelectedItemCount() > 0) {
            menu.findItem(R.id.import_action).setEnabled(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.import_action:
                //openFile();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}