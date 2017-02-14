package crixec.filehelper.function;

import android.support.v7.app.AppCompatActivity;

import crixec.filehelper.BaseFragment;

/**
 * Created by crixec on 17-2-12.
 */

public class FileSuffixReplaceFragment extends BaseFragment {
    public static FileSuffixReplaceFragment newInstance(AppCompatActivity activity, int titleRes, int contentViewRes) {
        FileSuffixReplaceFragment fragment = new FileSuffixReplaceFragment();
        fragment.setActivity(activity);
        fragment.setContentViewRes(contentViewRes);
        fragment.setTitleRes(titleRes);
        return fragment;
    }
}
