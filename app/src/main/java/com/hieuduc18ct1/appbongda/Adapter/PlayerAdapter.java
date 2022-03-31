package com.hieuduc18ct1.appbongda.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.hieuduc18ct1.appbongda.AddPlayerActivity;
import com.hieuduc18ct1.appbongda.Database.DataPlayer;
import com.hieuduc18ct1.appbongda.MainActivity;
import com.hieuduc18ct1.appbongda.PlayerFootball.Player;
import com.hieuduc18ct1.appbongda.R;
import com.hieuduc18ct1.appbongda.ShowPlayerActivity;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class PlayerAdapter extends ArrayAdapter<Player> {

    public final static String TAG = "PlayerAdapter";
    Activity context;
    List<Player> objects;
    int resource;
    ImageButton btnUp;
    ImageView imgAvatar;
    CardView cardView;
    private List<Player> lSearch;

    public PlayerAdapter(@NonNull Activity context, int resource, @NonNull List<Player> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.context = context;
        this.lSearch = objects;
        this.objects = objects;
    }

    @Override
    public int getCount() {
        if(this.objects == null) return 0;
        return this.objects.size();
    }
    @Override
    public Player getItem(int position) {
        return this.objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults filterResults = new FilterResults();
                if(constraint == null || constraint.length() == 0){
                    filterResults.count = lSearch.size();
                    filterResults.values = lSearch;

                }else{
                    List<Player> results = new ArrayList<>();
                    String searchStr = constraint.toString().toLowerCase();

                    for(Player pl:lSearch){
                        String name = pl.getName().toLowerCase();
                        String pos = pl.getPos().toLowerCase();
                        if(name.contains(searchStr)||pos.contains(searchStr)){
                            results.add(pl);
                        }
                        filterResults.count = results.size();
                        filterResults.values = results;
                    }

                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                objects = (List<Player>) results.values;
                notifyDataSetChanged();

            }
        };
        return filter;
    }


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View item = inflater.inflate(this.resource, null);
        if (this.objects.get(position) != null) {
            Player player = this.objects.get(position);
            addControls(item, player);
            addEvents(player, position);

        }
        Log.e(TAG, "getView: ");
        return item;
    }


    private void addControls(View item, Player player) {
        cardView = item.findViewById(R.id.cwItem);
        btnUp = item.findViewById(R.id.btnUp);
        TextView txtName = item.findViewById(R.id.txtName);
        TextView txtLocation = item.findViewById(R.id.txtLocation);
        imgAvatar = item.findViewById(R.id.imgAvatar1);

        txtName.setText(player.getName());
        txtLocation.setText(player.getPos());

        if (player.getName().length() == 0) txtName.setText("Chưa đặt tên");
        if (player.getPos().length() == 0) txtLocation.setText("Chưa đặt");

        if (player.getAvatar() != null) imgAvatar.setImageBitmap(player.getAvatar());
    }

    private void addEvents(Player player, int postion) {

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ShowPlayerActivity.class);
                intent.putExtra("player", player.getId());
                intent.putExtra("vitri", postion);
                context.startActivity(intent);
            }
        });
        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, view);

                popupMenu.getMenuInflater().inflate(R.menu.menu_lvplayer, popupMenu.getMenu());

                if (player.isLike()) {
                    popupMenu.getMenu().findItem(R.id.likePlayer).setTitle("Yêu Thích");
                    popupMenu.getMenu().findItem(R.id.likePlayer).setIcon(R.drawable.ic_player_nolike);
                } else {
                    popupMenu.getMenu().findItem(R.id.likePlayer).setTitle("Đã Thích");
                    popupMenu.getMenu().findItem(R.id.likePlayer).setIcon(R.drawable.ic_player_like);
                }

                try {

                    Method method = popupMenu.getMenu().getClass().getDeclaredMethod("setOptionalIconsVisible", boolean.class);
                    method.setAccessible(true);
                    method.invoke(popupMenu.getMenu(), true);


                } catch (Exception e) {
                    e.printStackTrace();
                }

                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int i = menuItem.getItemId();

                        if (i == R.id.delPlayer) {

                            alertSuccess(player);


                        } else if (i == R.id.upPlayer) {
                            Intent intent = new Intent(context, AddPlayerActivity.class);
                            intent.putExtra("id", player.getId());
                            intent.putExtra("postion", postion);
                            context.startActivity(intent);

                        } else if (i == R.id.likePlayer) {
                            try {
                                DataPlayer dataPlayer = new DataPlayer(context);
                                SQLiteDatabase db = dataPlayer.getWritableDatabase();
                                if (player.isLike()) player.setLike(false);
                                else player.setLike(true);
                                objects.set(postion, player);
                                dataPlayer.updatePlayerLike(db, player);
                                notifyDataSetChanged();
                                if(MainActivity.checkItem == false){
                                    objects.remove(postion);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        return true;
                }
            });
        }
    });

}



    private void alertSuccess(Player player) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setTitle("Thông báo")
                .setMessage("Bạn có chắc muốn xóa cầu thủ này?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            DataPlayer dataPlayer = new DataPlayer(context);
                            SQLiteDatabase db = dataPlayer.getWritableDatabase();
                            objects.remove(player);
                            dataPlayer.delPlayer(player.getId(), db);
                            notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        builder.create().show();
    }
}
