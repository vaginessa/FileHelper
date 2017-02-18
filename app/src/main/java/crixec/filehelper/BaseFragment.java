package crixec.filehelper;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import crixec.filehelper.listener.OnBackPress;

/**
 * Created by crixec on 17-2-11.
 */

public class BaseFragment extends Fragment implements OnBackPress, View.OnClickListener {
    private AppCompatActivity activity;
    private View contentView;
    private int contentViewRes;
    private int titleRes;
    private boolean showFab = false;

    public static BaseFragment newInstance(AppCompatActivity activity, int titleRes, int contentViewRes) {
        BaseFragment fragment = new BaseFragment();
        fragment.setActivity(activity);
        fragment.setContentViewRes(contentViewRes);
        fragment.setTitleRes(titleRes);
        return fragment;
    }
    public CharSequence getTitle(){
        return getMainActivity().getString(titleRes);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(getContentView() == null){
            View view = inflater.inflate(contentViewRes, container, false);
            setContentView(view);
            onInitView(view);
        }
        return getContentView();
    }

    public void setContentViewRes(int contentViewRes) {
        this.contentViewRes = contentViewRes;
    }

    public void setTitleRes(int titleRes) {
        this.titleRes = titleRes;
    }

    public void onInitView(View view) {

    }

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    public View findViewById(int id) {
        return contentView == null ? null : contentView.findViewById(id);
    }

    public View getContentView() {
        return contentView;
    }

    public void setContentView(View contentView) {
        this.contentView = contentView;
    }
    public Context getContext() {
        return activity;
    }

    public MainActivity getMainActivity(){
        return (MainActivity) activity;
    }

    @Override
    public boolean onBack() {
        return false;
    }

    public boolean isShowFab() {
        return showFab;
    }

    public void setShowFab(boolean showFab) {
        this.showFab = showFab;
    }

    @Override
    public void onClick(View v) {

    }
}
