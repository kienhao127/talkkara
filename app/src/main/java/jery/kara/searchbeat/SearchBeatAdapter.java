package jery.kara.searchbeat;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import jery.kara.R;

/**
 * Created by CPU11296-local on 1/4/2018.
 */

public class SearchBeatAdapter extends RecyclerView.Adapter {

    private List<BeatInfo> data;
    private AppCompatActivity context;
    public SearchBeatAdapter(AppCompatActivity context, List<BeatInfo> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case BeatInfo.TYPE_RECENT:
            case BeatInfo.TYPE_RESULT:
                return new TitleHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item_title, parent, false));
            case BeatInfo.TYPE_BEAT:
                return new BeatHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.seach_item_result, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        switch (type) {
            case BeatInfo.TYPE_RECENT:
            case BeatInfo.TYPE_RESULT:
                bindTitle((TitleHolder) holder, position);
                break;
            case BeatInfo.TYPE_BEAT:
                bindItem((BeatHolder) holder, position);
                break;
        }
    }

    private void bindItem(BeatHolder holder, final int position) {
        holder.txtTitle.setText(data.get(position).title);
        holder.txtAuthor.setText(data.get(position).author);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClickListener(view, data.get(position));
                }
//                PopupDownload.show(context.getSupportFragmentManager());
            }
        });
    }

    private void bindTitle(TitleHolder holder, int position) {
        holder.txtTitle.setText(data.get(position).title);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    class BeatHolder extends RecyclerView.ViewHolder {

        TextView txtTitle;
        TextView txtAuthor;

        public BeatHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_title);
            txtAuthor = itemView.findViewById(R.id.txt_author);
        }
    }

    class TitleHolder extends RecyclerView.ViewHolder {

        TextView txtTitle;
        public TitleHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_title);
        }
    }

    private onItemClickListener mItemClickListener;

    public void setOnItemClickListener(onItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface onItemClickListener {
        void onItemClickListener(View view, BeatInfo beatInfo);
    }
}
