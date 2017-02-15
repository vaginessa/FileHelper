package crixec.filehelper.function.fileinfo;

import android.content.ClipboardManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import crixec.filehelper.R;
import crixec.filehelper.Utils;
import crixec.filehelper.listener.OnFileItemClickListener;

public class FileInfoActivity extends AppCompatActivity implements OnFileItemClickListener {

    private FileInfoAdapter adapter;
    private RecyclerView recyclerView;
    private List<InfoBean> info = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.infoRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        File file = new File(getIntent().getStringExtra("FILE"));
        adapter = new FileInfoAdapter(generateInfoBean(file));
        adapter.setOnFileItemClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (getSupportActionBar() != null) {
            if (item.getItemId() == android.R.id.home) {
                this.finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private List<InfoBean> generateInfoBean(File file) {
        info.clear();
        info.add(new InfoBean(getString(R.string.file_name), file.getName()));
        info.add(new InfoBean(getString(R.string.file_path), file.getPath()));
        boolean isSymlink = Utils.isSymlink(file);
        info.add(new InfoBean(getString(R.string.is_symlink), String.valueOf(isSymlink)));
        if (isSymlink) {
            info.add(new InfoBean(getString(R.string.real_path), Utils.realPath(file)));
        }
        info.add(new InfoBean(getString(R.string.last_modified), Utils.computeUseableTime(file.lastModified())));
        info.add(new InfoBean(getString(R.string.file_size), Utils.computeUseableFileSize(file)));
        info.add(new InfoBean(getString(R.string.readable), String.valueOf(file.canRead())));
        info.add(new InfoBean(getString(R.string.writeable), String.valueOf(file.canWrite())));
        info.add(new InfoBean(getString(R.string.executeable), String.valueOf(file.canExecute())));
        info.add(new InfoBean(getString(R.string.file_md5), getString(R.string.computing)));
        new AlgorithmComputer(file, info.size() - 1, "MD5").execute();
        info.add(new InfoBean(getString(R.string.file_sha), getString(R.string.computing)));
        new AlgorithmComputer(file, info.size() - 1, "SHA").execute();
        return info;
    }

    @Override
    public void onFileItemClick(int position) {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboardManager.setText(info.get(position).getText2());
        Toast.makeText(getApplicationContext(), getString(R.string.copied), Toast.LENGTH_SHORT).show();
    }

    class AlgorithmComputer extends AsyncTask<Void, Void, String> {

        private final int index;
        private final File file;
        private final String type;

        public AlgorithmComputer(File file, int index, String type) {
            this.index = index;
            this.file = file;
            this.type = type;
        }

        @Override
        protected String doInBackground(Void... params) {
            return Utils.computeDigest(file, type);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String text1 = info.get(index).getText1();
            info.remove(index);
            InfoBean bean = new InfoBean(text1, s);
            info.add(index, bean);
            adapter.notifyDataSetChanged();
        }
    }
}
