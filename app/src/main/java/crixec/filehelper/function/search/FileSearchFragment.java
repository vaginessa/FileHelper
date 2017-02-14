package crixec.filehelper.function.search;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import crixec.filehelper.BaseFragment;
import crixec.filehelper.R;
import crixec.filehelper.Utils;

/**
 * Created by crixec on 17-2-11.
 */

public class FileSearchFragment extends BaseFragment implements TextWatcher {

    private TextInputLayout startPathLayout;
    private TextInputLayout fileNameLayout;
    private AppCompatButton startButton;
    private AppCompatButton stopButton;

    public static FileSearchFragment newInstance(AppCompatActivity activity, int titleRes, int contentViewRes) {
        FileSearchFragment fragment = new FileSearchFragment();
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
        fileNameLayout.getEditText().addTextChangedListener(this);
        startPathLayout.getEditText().addTextChangedListener(this);
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String path = startPathLayout.getEditText().getText().toString();
        String fileName = fileNameLayout.getEditText().getText().toString();
        if(Utils.isTextEmpty(path)){
            startPathLayout.setErrorEnabled(true);
            startPathLayout.setError(getString(R.string.path_cannot_be_empty));
        }else {
            startPathLayout.setErrorEnabled(false);
            startPathLayout.setError(null);
        }
        if(Utils.isTextEmpty(fileName)){
            fileNameLayout.setErrorEnabled(true);
            fileNameLayout.setError(getString(R.string.file_name_cannot_be_empty));
        }else {
            fileNameLayout.setErrorEnabled(false);
            fileNameLayout.setError(null);
        }
        startButton.setEnabled(!(startPathLayout.isErrorEnabled() || fileNameLayout.isErrorEnabled()));
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
