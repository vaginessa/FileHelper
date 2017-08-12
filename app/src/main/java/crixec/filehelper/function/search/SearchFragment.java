package crixec.filehelper.function.search;

import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import crixec.filehelper.BaseFragment;
import crixec.filehelper.R;
import crixec.filehelper.SettingHelper;
import crixec.filehelper.Utils;
import crixec.filehelper.function.browser.BrowserFragment;

/**
 * Created by crixec on 17-2-11.
 */

public class SearchFragment extends BaseFragment implements TextWatcher, View.OnClickListener, AdapterView.OnItemClickListener {

    private TextInputLayout startPathLayout;
    private TextInputLayout fileNameLayout;
    private AppCompatButton startButton;
    private AppCompatButton stopButton;
    private RadioGroup radioGroup;
    private ArrayAdapter adapter;
    private AppCompatCheckBox checkBox;
    private ContentLoadingProgressBar progressBar;
    private boolean isSearchable = false;
    private List<String> results = new ArrayList<>();

    public static SearchFragment newInstance(AppCompatActivity activity, int titleRes, int contentViewRes) {
        SearchFragment fragment = new SearchFragment();
        fragment.setActivity(activity);
        fragment.setContentViewRes(contentViewRes);
        fragment.setTitleRes(titleRes);
        return fragment;
    }

    @Override
    public void onInitView(View view) {
        super.onInitView(view);
        startPathLayout = (TextInputLayout) findViewById(R.id.startPath);
        fileNameLayout = (TextInputLayout) findViewById(R.id.searchContent);
        startButton = (AppCompatButton) findViewById(R.id.startSearchButton);
        stopButton = (AppCompatButton) findViewById(R.id.stopSearchButton);
        radioGroup = (RadioGroup) findViewById(R.id.rgSearchType);
        ListView listView = (ListView) findViewById(R.id.searchResultList);
        checkBox = (AppCompatCheckBox) findViewById(R.id.cbWildcardMatch);
        progressBar = (ContentLoadingProgressBar) findViewById(R.id.progressBar);
        fileNameLayout.getEditText().addTextChangedListener(this);
        startPathLayout.getEditText().addTextChangedListener(this);
        startButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);
        adapter = new ArrayAdapter(getActivity(), R.layout.layout_simple_result, results);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        startPathLayout.getEditText().setText(SettingHelper.getDefaultStartStorage().getPath());
        progressBar.setIndeterminate(false);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String path = startPathLayout.getEditText().getText().toString();
        String fileName = fileNameLayout.getEditText().getText().toString();
        if (Utils.isTextEmpty(path)) {
            startPathLayout.setErrorEnabled(true);
            startPathLayout.setError(getString(R.string.path_cannot_be_empty));
        } else {
            startPathLayout.setErrorEnabled(false);
            startPathLayout.setError(null);
        }
        if (Utils.isTextEmpty(fileName)) {
            fileNameLayout.setErrorEnabled(true);
            fileNameLayout.setError(getString(R.string.search_content_cannot_be_empty));
        } else {
            fileNameLayout.setErrorEnabled(false);
            fileNameLayout.setError(null);
        }
        startButton.setEnabled(!(startPathLayout.isErrorEnabled() || fileNameLayout.isErrorEnabled()));
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.stopSearchButton) {
            synchronized (this) {
                isSearchable = false;
                this.notifyAll();
            }
        } else if (v.getId() == R.id.startSearchButton) {
            if (!isSearchable) {
                new Searcher(startPathLayout.getEditText().getText().toString()).execute(getSearchFilter());
            }
        }
    }

    private AbsSearchFilter getSearchFilter() {
        if (radioGroup != null) {
            int id = radioGroup.getCheckedRadioButtonId();
            if (id == R.id.rg_file) {
                return new SearchFileFilter(fileNameLayout.getEditText().getText().toString());
            } else if (id == R.id.rg_folder) {
                return new SearchFolderFilter(fileNameLayout.getEditText().getText().toString());
            } else if (id == R.id.rg_file_content) {
                return new SearchContentFilter(fileNameLayout.getEditText().getText().toString());
            }
        }
        return new AbsSearchFilter() {
            @Override
            public boolean onFilter(File file) {
                return false;
            }
        };
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        File newFile = new File(results.get(position));
        ((BrowserFragment) getMainActivity().getFragment(0)).setParentFile(newFile.isFile() ? newFile.getParentFile() : newFile);
        ((BrowserFragment) getMainActivity().getFragment(0)).refresh();
        getMainActivity().switchFragment(0);
    }

    class Searcher extends AsyncTask<AbsSearchFilter, String, Void> {
        private String startPath;
        private boolean isWildcardable = false;


        public Searcher(String startPath) {
            this.startPath = startPath;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            results.clear();
            adapter.notifyDataSetChanged();
            isSearchable = true;
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
            isWildcardable = checkBox.isChecked();
            progressBar.setIndeterminate(true);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            isSearchable = false;
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
            progressBar.setIndeterminate(false);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            isSearchable = false;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            results.add(values[0]);
            adapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(AbsSearchFilter... params) {
            params[0].setWildcardable(isWildcardable);
            searchInPath(startPath, params[0]);
            return null;
        }

        public void searchInPath(String path, AbsSearchFilter filter) {
            File dir = new File(path);
            File[] files = dir.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    File file = files[i];
                    if (!isSearchable) return;
                    boolean b = filter.onFilter(file);
                    if (b) {
                        publishProgress(file.getAbsolutePath());
                    }
                    if (file.isDirectory()) {
                        searchInPath(file.getAbsolutePath(), filter);
                    }
                }
            }
        }
    }
}
