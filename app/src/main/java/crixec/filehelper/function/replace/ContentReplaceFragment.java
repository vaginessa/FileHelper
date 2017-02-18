package crixec.filehelper.function.replace;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import crixec.filehelper.BaseFragment;
import crixec.filehelper.BuildConfig;
import crixec.filehelper.Logger;
import crixec.filehelper.R;
import crixec.filehelper.SettingHelper;
import crixec.filehelper.Utils;
import crixec.filehelper.function.browser.BrowserFragment;

/**
 * Created by crixec on 17-2-12.
 */

public class ContentReplaceFragment extends BaseFragment implements TextWatcher, AdapterView.OnItemClickListener {

    private TextInputLayout searchContentLayout;
    private TextInputLayout replaceContentLayout;
    private AppCompatButton startButton;
    private AppCompatButton stopButton;
    private ArrayAdapter adapter;
    private boolean isReplaceable = false;
    private ContentLoadingProgressBar progressBar;
    private List<String> targets = new ArrayList<>();

    public static ContentReplaceFragment newInstance(AppCompatActivity activity, int titleRes, int contentViewRes) {
        ContentReplaceFragment fragment = new ContentReplaceFragment();
        fragment.setActivity(activity);
        fragment.setContentViewRes(contentViewRes);
        fragment.setTitleRes(titleRes);
        fragment.setShowFab(true);
        return fragment;
    }

    @Override
    public void onInitView(View view) {
        super.onInitView(view);
        searchContentLayout = (TextInputLayout) findViewById(R.id.searchContent);
        replaceContentLayout = (TextInputLayout) findViewById(R.id.replaceContent);
        startButton = (AppCompatButton) findViewById(R.id.startReplaceButton);
        stopButton = (AppCompatButton) findViewById(R.id.stopReplaceButton);
        ListView listView = (ListView) findViewById(R.id.targetFileList);
        progressBar = (ContentLoadingProgressBar) findViewById(R.id.progressBar);
        replaceContentLayout.getEditText().addTextChangedListener(this);
        searchContentLayout.getEditText().addTextChangedListener(this);
        startButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);
        adapter = new ArrayAdapter(getActivity(), R.layout.layout_simple_result, targets);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        progressBar.setIndeterminate(false);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String path = searchContentLayout.getEditText().getText().toString();
        if (Utils.isTextEmpty(path)) {
            searchContentLayout.setErrorEnabled(true);
            searchContentLayout.setError(getString(R.string.search_content_cannot_be_empty));
        } else {
            searchContentLayout.setErrorEnabled(false);
            searchContentLayout.setError(null);
        }
        refreshButtons();
    }
    private void refreshButtons(){
        startButton.setEnabled(targets.size() > 0 && !(searchContentLayout.isErrorEnabled() || replaceContentLayout.isErrorEnabled()));
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        File newFile = new File(targets.get(position));
        ((BrowserFragment) getMainActivity().getFragment(0)).setParentFile(newFile.isFile() ? newFile.getParentFile() : newFile);
        ((BrowserFragment) getMainActivity().getFragment(0)).refresh();
        getMainActivity().switchFragment(0);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.fab) {
            final AppCompatEditText editText = new AppCompatEditText(getContext());
            editText.setHint(R.string.add_new_paths_or_files);
            DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String str = editText.getText().toString();
                    if (Utils.isTextEmpty(str)) {
                        getMainActivity().makeSnackBar(getString(R.string.content_cannot_be_empty), Snackbar.LENGTH_SHORT);
                        return;
                    }
                    boolean result = new File(str).exists();
                    if (!result) {
                        getMainActivity().makeSnackBar(getString(R.string.add_failed), Snackbar.LENGTH_SHORT);
                    }else {
                        targets.add(str);
                        adapter.notifyDataSetChanged();
                        refreshButtons();
                    }
                }
            };
            editText.setText(SettingHelper.getDefautlStartStorage().getPath());
            new AlertDialog.Builder(getContext())
                    .setView(editText)
                    .setTitle(R.string.add)
                    .setNegativeButton(R.string.add, onClickListener)
                    .setCancelable(true)
                    .show();
        } else if (v.getId() == R.id.stopReplaceButton) {
            synchronized (this) {
                isReplaceable = false;
                this.notifyAll();
            }
        } else if (v.getId() == R.id.startReplaceButton) {
            if (!isReplaceable) {
                new Replacer(new ReplaceBean(searchContentLayout.getEditText().getText().toString(), replaceContentLayout.getEditText().getText().toString())).execute();
            }
        }
    }

    class Replacer extends AsyncTask<Void, Void, Void> {
        private ReplaceBean replaceBean;

        public Replacer(ReplaceBean replaceBean) {
            this.replaceBean = replaceBean;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
            isReplaceable = true;
            progressBar.setIndeterminate(true);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
            isReplaceable = false;
            progressBar.setIndeterminate(false);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            isReplaceable = true;
        }

        public void replaceInPath(String path) {
            File dir = new File(path);
            if (dir.isFile()) {
                processFile(dir);
                return;
            }
            File[] files = dir.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    File file = files[i];
                    if (!isReplaceable) return;
                    if (file.isDirectory()) {
                        replaceInPath(file.getAbsolutePath());
                    } else {
                        processFile(file);
                    }
                }
            }
        }

        private void processFile(File file) {
            Logger.i(getClass(), "Replace file : " + file.getPath());
            StringBuilder fileContent = Utils.readFile(file);
            Utils.writeFile(file, fileContent.toString().replace(replaceBean.getOrginContent(), replaceBean.getNewContent()));
        }

        @Override
        protected Void doInBackground(Void... params) {
            for (String path : targets) {
                replaceInPath(path);
            }
            return null;
        }
    }
}
