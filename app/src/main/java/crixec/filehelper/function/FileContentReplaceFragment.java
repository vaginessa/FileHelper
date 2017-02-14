package crixec.filehelper.function;

import android.support.v7.app.AppCompatActivity;

import crixec.filehelper.BaseFragment;

/**
 * Created by crixec on 17-2-12.
 */

public class FileContentReplaceFragment extends BaseFragment {
    public static FileContentReplaceFragment newInstance(AppCompatActivity activity, int titleRes, int contentViewRes) {
        FileContentReplaceFragment fragment = new FileContentReplaceFragment();
        fragment.setActivity(activity);
        fragment.setContentViewRes(contentViewRes);
        fragment.setTitleRes(titleRes);
        return fragment;
    }

}
