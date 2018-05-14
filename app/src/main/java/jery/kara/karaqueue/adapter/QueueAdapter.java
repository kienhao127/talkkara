package jery.kara.karaqueue.adapter;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import jery.kara.R;
import jery.kara.karaqueue.manager.KaraQueueManager;
import jery.kara.karaqueue.model.User;

/**
 * Created by CPU11341-local on 09-May-18.
 */

public class QueueAdapter extends RecyclerView.Adapter {

    private List<User> data;
    private AppCompatActivity context;
    private User currentUser = new User();

    public QueueAdapter(AppCompatActivity context, List<User> data) {
        this.data = data;
        this.context = context;
        currentUser = KaraQueueManager.getInstance().getCurrentUser();
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case User.TYPE_SINGER:
                return new SingerHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.queue_item_singer, parent, false));
            case User.TYPE_WAITTING:
                return new WaittingHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.queue_item_waitting, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        switch (type) {
            case User.TYPE_SINGER:
                bindSinger((SingerHolder) holder);
                break;
            case User.TYPE_WAITTING:
                bindWaitting((WaittingHolder) holder, position);
                break;
        }
    }

    private void bindSinger(SingerHolder holder) {
        holder.txtSongName.setText(data.get(0).beatInfo.title);
        holder.txtUserName.setText(data.get(0).name);
        Glide.with(context)
                .load(data.get(0).avatarURL)
                .apply(RequestOptions.circleCropTransform())
                .apply(RequestOptions.placeholderOf(R.drawable.user_avatar).error(R.drawable.user_avatar))
                .into(holder.userAvatar);
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KaraQueueManager.getInstance().removeFromQueue(data.get(0).id);
            }
        });

        if (currentUser.role == User.ROLE_MANAGER){
            holder.remove.setVisibility(View.VISIBLE);
        }
    }

    private void bindWaitting(WaittingHolder holder, final int position) {
        holder.txtQueueNum.setText(String.valueOf(position));
        holder.txtSongName.setText(data.get(position).beatInfo.title);
        holder.txtUserName.setText(data.get(position).name);
        Glide.with(context)
                .load(data.get(position).avatarURL)
                .apply(RequestOptions.circleCropTransform())
                .apply(RequestOptions.placeholderOf(R.drawable.user_avatar).error(R.drawable.user_avatar))
                .into(holder.userAvatar);
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KaraQueueManager.getInstance().removeFromQueue(data.get(position).id);
            }
        });
        if (currentUser.role == User.ROLE_MANAGER){
            holder.remove.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    class SingerHolder extends RecyclerView.ViewHolder {

        TextView txtUserName;
        TextView txtSongName;
        ImageView userAvatar;
        ImageView remove;

        public SingerHolder(View itemView) {
            super(itemView);
            txtUserName = itemView.findViewById(R.id.txt_user_name);
            txtSongName = itemView.findViewById(R.id.txt_song_name);
            userAvatar = itemView.findViewById(R.id.user_avatar);
            remove = itemView.findViewById(R.id.remove);
        }
    }

    class WaittingHolder extends RecyclerView.ViewHolder {

        TextView txtQueueNum;
        TextView txtUserName;
        TextView txtSongName;
        ImageView userAvatar;
        ImageView remove;
        public WaittingHolder(View itemView) {
            super(itemView);
            txtQueueNum = itemView.findViewById(R.id.txt_queue_num);
            txtUserName = itemView.findViewById(R.id.txt_user_name);
            txtSongName = itemView.findViewById(R.id.txt_song_name);
            userAvatar = itemView.findViewById(R.id.user_avatar);
            remove = itemView.findViewById(R.id.remove);
        }
    }
}
