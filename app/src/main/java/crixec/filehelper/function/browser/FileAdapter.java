package crixec.filehelper.function.browser;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.List;

import crixec.filehelper.function.browser.FileViewHolder;
import crixec.filehelper.listener.OnFileItemClickListener;
import crixec.filehelper.R;
import crixec.filehelper.Utils;

/**
 * Created by crixec on 17-2-11.
 */

public class FileAdapter extends RecyclerView.Adapter<FileViewHolder> {
    private List<File> files;
    private OnFileItemClickListener onFileItemClickListener;

    public FileAdapter(List<File> files) {
        this.files = files;
    }

    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_file_item, parent, false);
        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FileViewHolder holder, int position) {
        if (holder != null) {
            File file = files.get(position);
            if (file != null) {
                holder.setFileName(file.getName());
                holder.setFileSize(Utils.computeReadableFileSize(file));
                holder.setFileDate(Utils.computeReadableTime(file.lastModified()));
                if (file.isDirectory())
                    holder.setFileImage(R.drawable.ic_folder);
                else
                    holder.setFileImage(R.drawable.ic_file);
                if (onFileItemClickListener != null) {
                    holder.itemVew.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onFileItemClickListener.onFileItemClick(holder.getLayoutPosition());
                        }
                    });
                }
            }
        }
    }

    public void setOnFileItemClickListener(OnFileItemClickListener onFileItemClickListener) {
        this.onFileItemClickListener = onFileItemClickListener;
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

}
