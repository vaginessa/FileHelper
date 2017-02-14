package crixec.filehelper.function;

import android.support.v7.app.AppCompatActivity;

import crixec.filehelper.BaseFragment;

/**
 * Created by crixec on 17-2-12.
 */

public class FileRenameFragment extends BaseFragment {
    public static FileRenameFragment newInstance(AppCompatActivity activity, int titleRes, int contentViewRes) {
        FileRenameFragment fragment = new FileRenameFragment();
        fragment.setActivity(activity);
        fragment.setContentViewRes(contentViewRes);
        fragment.setTitleRes(titleRes);
        return fragment;
    }

}
