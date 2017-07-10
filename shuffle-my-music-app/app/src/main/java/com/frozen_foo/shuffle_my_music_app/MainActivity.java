package com.frozen_foo.shuffle_my_music_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.frozen_foo.shuffle_my_music_app.R;

import java.security.AccessController;

public class MainActivity extends AppCompatActivity {

    ListView lv;
    RowModel[] modelItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        lv = (ListView) findViewById(R.id.listView1);

        modelItems = new RowModel[5];
        modelItems[0] = new RowModel("pizza", false);
        modelItems[1] = new RowModel("burger", true);
        modelItems[2] = new RowModel("olives", true);
        modelItems[3] = new RowModel("orange", false);
        modelItems[4] = new RowModel("tomato", true);
        RowAdapter adapter = new RowAdapter(this, modelItems);
        lv.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menubar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.settings:
                openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openSettings() {
        Intent intent = new Intent(this, SmbConnectionActivity.class);
        startActivity(intent);
    }
}
