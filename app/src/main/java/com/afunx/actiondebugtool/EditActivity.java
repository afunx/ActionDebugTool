package com.afunx.actiondebugtool;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_item_enter_read_mode:
                Toast.makeText(this, R.string.enter_read_mode , Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_item_read:
                Toast.makeText(this, R.string.read , Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_item_copy:
                Toast.makeText(this, R.string.copy , Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_item_paste:
                Toast.makeText(this, R.string.paste , Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_item_delete:
                Toast.makeText(this, R.string.delete , Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_item_save:
                Toast.makeText(this, R.string.save , Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_item_save_as:
                Toast.makeText(this, R.string.save_as , Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initView() {
        // actionbar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}