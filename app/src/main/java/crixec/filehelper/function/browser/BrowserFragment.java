package crixec.filehelper.function.browser;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import crixec.filehelper.BaseFragment;
import crixec.filehelper.function.fileinfo.FileInfoActivity;
import crixec.filehelper.listener.OnFileItemClickListener;
import crixec.filehelper.R;
import crixec.filehelper.SettingHelper;
import crixec.filehelper.Utils;

/**
 * Created by crixec on 17-2-11.
 */

public class BrowserFragment extends BaseFragment implements OnFileItemClickListener, View.OnClickListener {
    private FileAdapter recycleAdapter;
    private RecyclerView recyclerView;
    private List<File> fileList = new ArrayList<>();
    private File parentFile;


    public static BrowserFragment newInstance(AppCompatActivity activity, int titleRes, int contentViewRes) {
        BrowserFragment fragment = new BrowserFragment();
        fragment.setActivity(activity);
        fragment.setContentViewRes(contentViewRes);
        fragment.setTitleRes(titleRes);
        return fragment;
    }

    @Override
    public void onInitView(View view) {
        super.onInitView(view);
        recyclerView = (RecyclerView) findViewById(R.id.fileRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        recycleAdapter = new FileAdapter(fileList);
        recycleAdapter.setOnFileItemClickListener(this);
        recyclerView.setAdapter(recycleAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        parentFile = SettingHelper.getDefautlStartStorage();
        new FileListUpdater().execute(parentFile);
    }

    public File getParentFile() {
        return parentFile;
    }

    public void setParentFile(File parentFile) {
        this.parentFile = parentFile;
    }


    @Override
    public void onFileItemClick(int position) {
        final File clickedFile = fileList.get(position);
        if (clickedFile != null) {
            if (clickedFile.isDirectory()) {
                parentFile = clickedFile;
                new FileListUpdater().execute(parentFile);
            } else {
                new AlertDialog.Builder(getContext())
                        .setTitle(clickedFile.getName())
                        .setItems(R.array.single_click_operations, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    // open
                                } else if (which == 1) {
                                    // delete
                                    int msgRes = R.string.delete_succeed;
                                    if (!Utils.delete(clickedFile)) {
                                        msgRes = R.string.delete_failed;
                                    } else {
                                        new FileListUpdater().execute(parentFile);
                                    }
                                    getMainActivity().makeSnackBar(getString(msgRes), Snackbar.LENGTH_SHORT);
                                } else if (which == 2) {
                                    // view
                                    Intent intent = new Intent(getMainActivity(), FileInfoActivity.class);
                                    intent.putExtra("FILE", clickedFile.getPath());
                                    startActivity(intent);
                                }
                            }
                        })
                        .show();
            }
        }
    }

    @Override
    public boolean onBack() {
        if (parentFile.toString().isEmpty() || "/".equals(parentFile.getPath())) {
            return super.onBack();
        } else {
            parentFile = parentFile.getParentFile();
            new FileListUpdater().execute(parentFile);
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        final AppCompatEditText editText = new AppCompatEditText(getContext());
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String str = editText.getText().toString();
                if (Utils.isTextEmpty(str)) {
                    getMainActivity().makeSnackBar(getString(R.string.name_cannot_be_empty), Snackbar.LENGTH_SHORT);
                    return;
                }
                boolean result = false;
                if (which == DialogInterface.BUTTON_NEGATIVE) {
                    result = Utils.createFile(new File(getParentFile(), str));
                } else if (which == DialogInterface.BUTTON_POSITIVE) {
                    result = Utils.createFolder(new File(getParentFile(), str));
                }
                if (result) {
                    getMainActivity().makeSnackBar(getString(R.string.create_successed), Snackbar.LENGTH_SHORT);
                    new FileListUpdater().execute(parentFile);
                } else {
                    getMainActivity().makeSnackBar(getString(R.string.create_failed), Snackbar.LENGTH_SHORT);
                }
            }
        };
        new AlertDialog.Builder(getContext())
                .setView(editText)
                .setTitle(R.string.create)
                .setNegativeButton(R.string.file, onClickListener)
                .setPositiveButton(R.string.folder, onClickListener)
                .setCancelable(true)
                .show();
    }

    class FileListUpdater extends AsyncTask<File, Void, File> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            fileList.clear();
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            getMainActivity().setSubTitle(file.getPath());
            recycleAdapter.notifyDataSetChanged();
        }

        @Override
        protected File doInBackground(File... params) {
            File[] files = Utils.listFiles(params[0]);
            for (File f : files) {
                fileList.add(f);
            }
            Collections.sort(fileList, new Comparator<Object>() {
                @Override
                public int compare(Object obj, Object obj2) {
                    return (!((File) obj).isDirectory() ||
                            ((File) obj2).isDirectory()) ?
                            (((File) obj).isDirectory() ||
                                    !((File) obj2).isDirectory()) ?
                                    ((File) obj).getName().compareToIgnoreCase(((File) obj2).getName())
                                            < 0 ? -1 : 1 : 1 : -1;
                }
            });
            return params[0];
        }

    }

}
