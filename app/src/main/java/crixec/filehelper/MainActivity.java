package crixec.filehelper;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import crixec.filehelper.function.FileCompareFragment;
import crixec.filehelper.function.FileContentReplaceFragment;
import crixec.filehelper.function.FileMergeFragment;
import crixec.filehelper.function.FileRenameFragment;
import crixec.filehelper.function.FileSplitFragment;
import crixec.filehelper.function.FileSuffixReplaceFragment;
import crixec.filehelper.function.browser.BrowserFragment;
import crixec.filehelper.function.search.FileSearchFragment;
import crixec.filehelper.listener.AboutFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private List<BaseFragment> fragments = new ArrayList<>();
    private int CURRENT_INDEX;
    private AppCompatTextView subTitle;
    private FloatingActionButton fab;
    private Toolbar toolbar;

    static class Section {
        public static int FRAGMENT_BROWSER = 0;
        public static int FRAGMENT_SEARCH = 1;
        public static int FRAGMENT_CONTENT_REPLACE = 2;
        public static int FRAGMENT_SUFFIX_REPLACE = 3;
        public static int FRAGMENT_COMPARE = 4;
        public static int FRAGMENT_MERGE = 5;
        public static int FRAGMENT_SPLIT = 6;
        public static int FRAGMENT_RENAME = 7;
        public static int FRAGMENT_ABOUT = 8;
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
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        // add fragments
        fragments.add(BrowserFragment.newInstance(this, R.string.nav_file_browser, R.layout.fragment_browser));
        fragments.add(FileSearchFragment.newInstance(this, R.string.nav_file_search, R.layout.fragment_file_search));
        fragments.add(FileContentReplaceFragment.newInstance(this, R.string.nav_file_content_replace, R.layout.fragment_content_replace));
        fragments.add(FileSuffixReplaceFragment.newInstance(this, R.string.nav_file_suffix_replace, R.layout.fragment_suffix_replace));
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
        fab.setOnClickListener((BrowserFragment) fragments.get(0));
    }

    public FloatingActionButton getFAB() {
        return fab;
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
            getFAB().setVisibility(View.GONE);
        } else {
            showSubTitle();
            getFAB().setVisibility(View.VISIBLE);
        }
        setTitle(fragments.get(index).getTitle());
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
            case R.id.nav_file_suffix_replace: {
                switchFragment(Section.FRAGMENT_SUFFIX_REPLACE);
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
        Snackbar.make(toolbar.getRootView(), text, duration).show();
    }
}