package crixec.filehelper;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

/**
 * Created by crixec on 17-2-12.
 */

public class SecondActivity extends AppCompatActivity {
    public void applyToolbar() {
        if (getSupportActionBar() == null) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (getSupportActionBar() != null) {
            if (item.getItemId() == android.R.id.home) {
                this.finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
