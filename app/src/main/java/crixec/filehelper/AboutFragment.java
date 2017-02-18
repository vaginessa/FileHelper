package crixec.filehelper;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mukesh.MarkdownView;

/**
 * Created by crixec on 17-2-12.
 */

public class AboutFragment extends BaseFragment{
    public static AboutFragment newInstance(AppCompatActivity activity, int titleRes, int contentViewRes) {
        AboutFragment fragment = new AboutFragment();
        fragment.setActivity(activity);
        fragment.setContentViewRes(contentViewRes);
        fragment.setTitleRes(titleRes);
        return fragment;
    }

    @Override
    public void onInitView(View view) {
        super.onInitView(view);
        MarkdownView markdownView = (MarkdownView) findViewById(R.id.markdown_view);
        markdownView.loadMarkdownFromAssets("about.md");
        markdownView.setOpenUrlInBrowser(true);
    }

}
