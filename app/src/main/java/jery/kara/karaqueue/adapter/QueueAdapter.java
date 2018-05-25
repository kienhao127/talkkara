package jery.kara.karaqueue.adapter;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import jery.kara.R;
import jery.kara.karaqueue.manager.KaraQueueManager;
import jery.kara.karaqueue.model.QueueItem;
import jery.kara.karaqueue.model.User;

/**
 * Created by CPU11341-local on 09-May-18.
 */

public class QueueAdapter extends RecyclerView.Adapter {

    private List<QueueItem> data;
    private AppCompatActivity context;
    private User currentUser = new User();

    public QueueAdapter(AppCompatActivity context, List<QueueItem> data) {
        this.data = data;
        this.context = context;
        currentUser = KaraQueueManager.getInstance().getCurrentUser();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new SingerHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.queue_item_singer, parent, false));
            default:
                return new WaittingHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.queue_item_waitting, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        switch (type) {
            case 0:
                bindSinger((SingerHolder) holder);
                break;
            default:
                bindWaitting((WaittingHolder) holder, position);
                break;
        }
    }

    private void bindSinger(SingerHolder holder) {
        holder.txtSongName.setText(data.get(0).songName);
        holder.txtUserName.setText(data.get(0).username);
        Glide.with(context)
                .load(data.get(0).avatarURL)
                .apply(RequestOptions.circleCropTransform())
                .apply(RequestOptions.placeholderOf(R.drawable.user_avatar).error(R.drawable.user_avatar))
                .into(holder.userAvatar);
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, view);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.singer_popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().equals("Xóa")){
                            Toast.makeText(context, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        }
                        if (item.getTitle().equals("Chặn")){
                            Toast.makeText(context, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        }

                        return true;
                    }
                });
                popup.show();
            }
        });

        if (currentUser.role == User.ROLE_MANAGER){
            holder.remove.setVisibility(View.VISIBLE);
        }
    }

    private void bindWaitting(final WaittingHolder holder, final int position) {
        holder.txtQueueNum.setText(String.valueOf(position));
        holder.txtSongName.setText(data.get(position).songName);
        holder.txtUserName.setText(data.get(position).username);
        Glide.with(context)
                .load(data.get(position).avatarURL)
                .apply(RequestOptions.circleCropTransform())
                .apply(RequestOptions.placeholderOf(R.drawable.user_avatar).error(R.drawable.user_avatar))
                .into(holder.userAvatar);
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
