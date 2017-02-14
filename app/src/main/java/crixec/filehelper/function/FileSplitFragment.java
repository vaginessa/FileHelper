package crixec.filehelper.function;

import android.support.v7.app.AppCompatActivity;

import crixec.filehelper.BaseFragment;

/**
 * Created by crixec on 17-2-12.
 */

public class FileSplitFragment extends BaseFragment {
    public static FileSplitFragment newInstance(AppCompatActivity activity, int titleRes, int contentViewRes) {
        FileSplitFragment fragment = new FileSplitFragment();
        fragment.setActivity(activity);
        fragment.setContentViewRes(contentViewRes);
        fragment.setTitleRes(titleRes);
        return fragment;
    }
}
