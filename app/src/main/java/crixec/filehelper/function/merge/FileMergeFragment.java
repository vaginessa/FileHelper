package crixec.filehelper.function.merge;

import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.view.FilePickerDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import crixec.filehelper.BaseFragment;
import crixec.filehelper.R;
import crixec.filehelper.SettingHelper;
import crixec.filehelper.ShellUtils;
import crixec.filehelper.Utils;

/**
 * Created by crixec on 17-2-12.
 */

public class FileMergeFragment extends BaseFragment implements TextWatcher, AdapterView.OnItemClickListener {

    private TextInputLayout outputFilenameLayout;
    private AppCompatButton startButton;
    private AppCompatButton stopButton;
    private ArrayAdapter adapter;
    private boolean isMergeable = false;
    private ContentLoadingProgressBar progressBar;
    private List<String> targets = new ArrayList<>();

    public static FileMergeFragment newInstance(AppCompatActivity activity, int titleRes, int contentViewRes) {
        FileMergeFragment fragment = new FileMergeFragment();
        fragment.setActivity(activity);
        fragment.setContentViewRes(contentViewRes);
        fragment.setTitleRes(titleRes);
        fragment.setShowFab(true);
        return fragment;
    }

    @Override
    public void onInitView(View view) {
        super.onInitView(view);
        outputFilenameLayout = (TextInputLayout) findViewById(R.id.outputFilename);
        startButton = (AppCompatButton) findViewById(R.id.startMergeButton);
        stopButton = (AppCompatButton) findViewById(R.id.stopMergeButton);
        ListView listView = (ListView) findViewById(R.id.targetFileList);
        progressBar = (ContentLoadingProgressBar) findViewById(R.id.progressBar);
        outputFilenameLayout.getEditText().addTextChangedListener(this);
        outputFilenameLayout.getEditText().setText(SettingHelper.getDefaultStartStorage().getPath());
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
        String path = outputFilenameLayout.getEditText().getText().toString();
        if (Utils.isTextEmpty(path) || new File(path).isDirectory()) {
            outputFilenameLayout.setErrorEnabled(true);
            outputFilenameLayout.setError(getString(R.string.output_filename_cannot_be_empty));
        } else {
            outputFilenameLayout.setErrorEnabled(false);
            outputFilenameLayout.setError(null);
        }
        refreshButtons();
    }

    private void refreshButtons() {
        startButton.setEnabled(targets.size() > 0 && !(outputFilenameLayout.isErrorEnabled()));
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.fab) {
            FilePickerDialog dialog = getMainActivity().createMultiOnlyFilePickerDialog();
            dialog.setTitle(R.string.add_files);
            dialog.setDialogSelectionListener(new DialogSelectionListener() {
                @Override
                public void onSelectedFilePaths(String[] files) {
                    Collections.addAll(targets, files);
                    adapter.notifyDataSetChanged();
                    refreshButtons();
                }
            });
            dialog.show();
        } else if (v.getId() == R.id.stopMergeButton) {
            synchronized (this) {
                isMergeable = false;
                this.notifyAll();
            }
        } else if (v.getId() == R.id.startMergeButton) {
            if (!isMergeable) {
                new Merger(outputFilenameLayout.getEditText().getText().toString()).execute();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    class Merger extends AsyncTask<Void, Void, Void> {

        String output;
        int p = 0;

        public Merger(String output) {
            this.output = output;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
            isMergeable = true;
            progressBar.setIndeterminate(true);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
            isMergeable = false;
            progressBar.setIndeterminate(false);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            isMergeable = true;
        }

        private boolean mergeFile(String file) {
            return runCmd(String.format("/system/bin/cat '%s' >> '%s'", file, output));
        }

        private boolean runCmd(String cmd) {
            return ShellUtils.exec(cmd, null, false) == 0;
        }

        @Override
        protected Void doInBackground(Void... params) {
            runCmd(String.format("echo > '%s'", output));
            for (String path : targets) {
                if (!isMergeable) return null;
                if (!mergeFile(path)) {
                    getMainActivity().makeSnackBar(getString(R.string.merge_failed), Snackbar.LENGTH_SHORT);
                    return null;
                }
            }
            return null;
        }
    }
}
