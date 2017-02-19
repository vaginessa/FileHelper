package crixec.filehelper;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import crixec.filehelper.function.FileCompareFragment;
import crixec.filehelper.function.replace.ContentReplaceFragment;
import crixec.filehelper.function.merge.FileMergeFragment;
import crixec.filehelper.function.FileRenameFragment;
import crixec.filehelper.function.FileSplitFragment;
import crixec.filehelper.function.browser.BrowserFragment;
import crixec.filehelper.function.search.SearchFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private List<BaseFragment> fragments = new ArrayList<>();
    private int CURRENT_INDEX;
    private AppCompatTextView subTitle;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private NavigationView navigationView;

    static class Section {
        private static int i = 0;
        public static int FRAGMENT_BROWSER = i;
        public static int FRAGMENT_SEARCH = ++i;
        public static int FRAGMENT_CONTENT_REPLACE = ++i;
        public static int FRAGMENT_COMPARE = ++i;
        public static int FRAGMENT_MERGE = ++i;
        public static int FRAGMENT_SPLIT = ++i;
        public static int FRAGMENT_RENAME = ++i;
        public static int FRAGMENT_ABOUT = ++i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        // add fragments
        fragments.add(BrowserFragment.newInstance(this, R.string.nav_file_browser, R.layout.fragment_browser));
        fragments.add(SearchFragment.newInstance(this, R.string.nav_file_search, R.layout.fragment_search));
        fragments.add(ContentReplaceFragment.newInstance(this, R.string.nav_file_content_replace, R.layout.fragment_content_replace));
        fragments.add(FileCompareFragment.newInstance(this, R.string.nav_file_compare, R.layout.fragment_file_compare));
        fragments.add(FileMergeFragment.newInstance(this, R.string.nav_file_merge, R.layout.fragment_file_merge));
        fragments.add(FileSplitFragment.newInstance(this, R.string.nav_file_split, R.layout.fragment_file_split));
        fragments.add(FileRenameFragment.newInstance(this, R.string.nav_file_rename, R.layout.fragment_file_rename));
        fragments.add(AboutFragment.newInstance(this, R.string.nav_about, R.layout.fragment_thanks));
        for (BaseFragment fragment :
                fragments) {
            getSupportFragmentManager().beginTransaction().add(R.id.contentLayout, fragment).hide(fragment).commit();
        }
        switchFragment(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }
        }
    }

    public BaseFragment getFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            new AlertDialog.Builder(this).setTitle(R.string.warning)
                    .setMessage(R.string.need_storage_permission)
                    .setCancelable(false)
                    .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
        } else {
            Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public FloatingActionButton getFAB() {
        return fab;
    }

    public FilePickerDialog createFilePickerDialog(DialogProperties dialogProperties) {
        return new FilePickerDialog(this, dialogProperties);
    }

    public FilePickerDialog createMultiOnlyFilePickerDialog() {
        DialogProperties properties = new DialogProperties();
        properties.selection_mode = DialogConfigs.MULTI_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;
        properties.root = new File("/");
        properties.error_dir = new File("/");
        properties.offset = SettingHelper.getDefautlStartStorage();
        properties.extensions = null;
        return createFilePickerDialog(properties);
    }

    public FilePickerDialog createSingleFilePickerDialog() {
        DialogProperties properties = new DialogProperties();
        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.selection_type = DialogConfigs.FILE_AND_DIR_SELECT;
        properties.root = new File("/");
        properties.error_dir = new File("/");
        properties.offset = SettingHelper.getDefautlStartStorage();
        properties.extensions = null;
        return createFilePickerDialog(properties);
    }
    public FilePickerDialog createMultiFilePickerDialog() {
        DialogProperties properties = new DialogProperties();
        properties.selection_mode = DialogConfigs.MULTI_MODE;
        properties.selection_type = DialogConfigs.FILE_AND_DIR_SELECT;
        properties.root = new File("/");
        properties.error_dir = new File("/");
        properties.offset = SettingHelper.getDefautlStartStorage();
        properties.extensions = null;
        return createFilePickerDialog(properties);
    }

    public void switchFragment(int index) {
        if (index < fragments.size()) {
            getSupportFragmentManager().beginTransaction().hide(fragments.get(CURRENT_INDEX)).show(fragments.get(index)).commit();
            CURRENT_INDEX = index;
        } else {
            getSupportFragmentManager().beginTransaction().hide(fragments.get(CURRENT_INDEX)).show(fragments.get(0)).commit();
            CURRENT_INDEX = 0;
        }
        if (CURRENT_INDEX != 0) {
            dismissSubTitle();
        } else {
            showSubTitle();
        }
        if (fragments.get(index).isShowFab()) {
            getFAB().setVisibility(View.VISIBLE);
        } else {
            getFAB().setVisibility(View.GONE);
        }
        fab.setOnClickListener(fragments.get(index));
        setTitle(fragments.get(index).getTitle());
        navigationView.getMenu().getItem(index).setChecked(true);
        invalidateOptionsMenu();
    }

    public void showSubTitle() {
        getSubTitle().setVisibility(View.VISIBLE);
    }

    public void dismissSubTitle() {
        getSubTitle().setVisibility(View.GONE);
    }

    public AppCompatTextView getSubTitle() {
        if (subTitle == null) {
            subTitle = (AppCompatTextView) findViewById(R.id.subtitle);
        }
        return subTitle;
    }

    public void setSubTitle(String str) {
        getSubTitle().setText(str);
    }

    @Override
    public void onBackPressed() {
        if (!fragments.get(CURRENT_INDEX).onBack()) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        if (CURRENT_INDEX == 0) {
            menu.findItem(R.id.action_goto).setVisible(true);
        } else {
            menu.findItem(R.id.action_goto).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_exit) {
            finish();
        } else if (item.getItemId() == R.id.action_goto) {
            final AppCompatEditText editText = new AppCompatEditText(this);
            editText.setHint(R.string.target_path);
            new AlertDialog.Builder(this)
                    .setTitle(R.string.action_goto)
                    .setView(editText)
                    .setPositiveButton(R.string.go, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            File file;
                            String path = editText.getText().toString();
                            if (!TextUtils.isEmpty(path)) {
                                BrowserFragment browserFragment = ((BrowserFragment) fragments.get(0));
                                if (path.toCharArray()[0] == '/') {
                                    file = new File(path);
                                } else {
                                    file = new File(browserFragment.getParentFile(), path);
                                }
                                if (!file.exists() || file.isFile()) {
                                    makeSnackBar(getString(R.string.not_exist) + file.getPath(), Snackbar.LENGTH_SHORT);
                                } else {
                                    browserFragment.setParentFile(file);
                                    browserFragment.refresh();
                                }
                            }
                        }
                    })
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_file_browser: {
                switchFragment(Section.FRAGMENT_BROWSER);
                break;
            }
            case R.id.nav_file_search: {
                switchFragment(Section.FRAGMENT_SEARCH);
                break;
            }
            case R.id.nav_file_content_replace: {
                switchFragment(Section.FRAGMENT_CONTENT_REPLACE);
                break;
            }
            case R.id.nav_file_compare: {
                switchFragment(Section.FRAGMENT_COMPARE);
                break;
            }
            case R.id.nav_file_merge: {
                switchFragment(Section.FRAGMENT_MERGE);
                break;
            }
            case R.id.nav_file_split: {
                switchFragment(Section.FRAGMENT_SPLIT);
                break;
            }
            case R.id.nav_file_rename: {
                switchFragment(Section.FRAGMENT_RENAME);
                break;
            }
            case R.id.nav_about: {
                switchFragment(Section.FRAGMENT_ABOUT);
                break;
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    public void makeSnackBar(CharSequence text, int duration) {
        Snackbar.make(fab, text, duration).show();
    }
}
