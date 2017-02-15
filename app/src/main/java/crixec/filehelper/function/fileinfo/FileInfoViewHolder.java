package crixec.filehelper.function.fileinfo;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import crixec.filehelper.R;

/**
 * Created by crixec on 17-2-11.
 */

public class FileInfoViewHolder extends RecyclerView.ViewHolder {
    private AppCompatTextView text1;
    private AppCompatTextView text2;
    public View itemVew;

    public FileInfoViewHolder(View itemView) {
        super(itemView);
        this.itemVew = itemView;
        text1 = (AppCompatTextView) itemView.findViewById(R.id.text1);
        text2 = (AppCompatTextView) itemView.findViewById(R.id.text2);

    }

    public CharSequence getText1() {
        return text1.getText().toString();
    }

    public void setText1(CharSequence text) {
        text1.setText(text);
    }

    public void setText2(CharSequence text) {
        text2.setText(text);
    }
}
