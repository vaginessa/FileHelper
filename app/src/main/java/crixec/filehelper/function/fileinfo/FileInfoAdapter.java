package crixec.filehelper.function.fileinfo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import crixec.filehelper.R;
import crixec.filehelper.listener.OnFileItemClickListener;

/**
 * Created by crixec on 17-2-11.
 */

public class FileInfoAdapter extends RecyclerView.Adapter<FileInfoViewHolder> {
    private List<InfoBean> infos;
    private OnFileItemClickListener onFileItemClickListener;

    public FileInfoAdapter(List<InfoBean> infos) {
        this.infos = infos;
    }

    @Override
    public FileInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_file_info_item, parent, false);
        return new FileInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FileInfoViewHolder holder, int position) {
        if (holder != null) {
            InfoBean bean = infos.get(position);
            if (bean != null) {
                holder.setText1(bean.getText1());
                holder.setText2(bean.getText2());
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
        return infos.size();
    }

}
