package crixec.filehelper.function.browser;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import crixec.filehelper.R;

/**
 * Created by crixec on 17-2-11.
 */

public class FileViewHolder extends RecyclerView.ViewHolder{
    private AppCompatTextView fileName;
    private AppCompatTextView fileSize;
    private AppCompatTextView fileDate;
    private AppCompatImageView fileImage;
    public View itemVew;
    public FileViewHolder(View itemView) {
        super(itemView);
        this.itemVew = itemView;
        fileName = (AppCompatTextView) itemView.findViewById(R.id.fileName);
        fileSize = (AppCompatTextView) itemView.findViewById(R.id.fileSize);
        fileDate = (AppCompatTextView) itemView.findViewById(R.id.fileDate);
        fileImage = (AppCompatImageView) itemView.findViewById(R.id.fileImage);
    }

    public void setFileImage(int res) {
        this.fileImage.setImageResource(res);
    }

    public void setFileName(String fileName) {
        this.fileName.setText(fileName);
    }

    public void setFileSize(String fileSize) {
        this.fileSize.setText(fileSize);
    }

    public void setFileDate(String fileDate) {
        this.fileDate.setText(fileDate);
    }
}
