package com.hieuduc18ct1.appbongda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.hieuduc18ct1.appbongda.Database.DataPlayer;
import com.hieuduc18ct1.appbongda.PlayerFootball.Player;

public class ShowPlayerActivity extends AppCompatActivity {
    private Player player;
    private int pos = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_player);
        addControls();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.updatePlayer)
        {
            Intent intent = new Intent(ShowPlayerActivity.this,AddPlayerActivity.class);
            intent.putExtra("id",player.getId());
            intent.putExtra("postion",pos);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void addControls() {
        Toolbar tb = findViewById(R.id.tb_show);
        tb.setTitle("");
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView txtName = findViewById(R.id.showName);
        TextView txtBirth = findViewById(R.id.txtDate);
        TextView txtSalery = findViewById(R.id.txtSalery);
        TextView txtPos = findViewById(R.id.txtPos);
        TextView txtTrophy = findViewById(R.id.txtTropy);
        TextView txtContry = findViewById(R.id.txtCountry);

        ImageView imgAvatarShow = findViewById(R.id.imgAvatarShow);
        Intent intent = getIntent();
        DataPlayer dataPlayer = new DataPlayer(this);
        int id = intent.getExtras().getInt("player");
        pos = intent.getExtras().getInt("vitri");
        player = dataPlayer.showPlayerById(id,dataPlayer);
        String v="";

        if(player.getValues().isEmpty())  v = " ??? ";
        else {
            try{
                v = String.format("%,d",Integer.parseInt(player.getValues()));
            }catch (Exception ex){
                v = player.getValues();
            }

        }
        txtName.setText(player.getName().isEmpty() ?" ??? ": player.getName());
        txtBirth.setText(player.getBirth().isEmpty() ?" ??? ": player.getBirth());
        txtSalery.setText(v);
        txtPos.setText(player.getPos().isEmpty() ?" ??? ": player.getPos());
        txtTrophy.setText(player.getTrophy().isEmpty()?" ??? ": player.getTrophy());
        txtContry.setText(player.getContry().isEmpty() ?" ??? ": player.getContry());
        if(player.getAvatar()!=null)imgAvatarShow.setImageBitmap(player.getAvatar());
    }




}