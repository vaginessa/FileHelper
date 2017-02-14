package crixec.filehelper.function;

import android.support.v7.app.AppCompatActivity;

import crixec.filehelper.BaseFragment;

/**
 * Created by crixec on 17-2-12.
 */

public class FileCompareFragment extends BaseFragment {
    public static FileCompareFragment newInstance(AppCompatActivity activity, int titleRes, int contentViewRes) {
        FileCompareFragment fragment = new FileCompareFragment();
        fragment.setActivity(activity);
        fragment.setContentViewRes(contentViewRes);
        fragment.setTitleRes(titleRes);
        return fragment;
    }
}
