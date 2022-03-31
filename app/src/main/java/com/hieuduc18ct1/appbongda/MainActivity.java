package com.hieuduc18ct1.appbongda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.SearchManager;
import android.content.Intent;

import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.CursorWindow;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.hieuduc18ct1.appbongda.Adapter.PlayerAdapter;
import com.hieuduc18ct1.appbongda.Database.DataPlayer;
import com.hieuduc18ct1.appbongda.PlayerFootball.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "MainActivity";

    ListView lvPlayer;

    public static PlayerAdapter playerAdapter;
    private DrawerLayout drawerLayout;
    private FloatingActionButton floatingActionButton;
    public static List<Player> lPlayer = new ArrayList<>();
    public static boolean checkItem = true;
    private Cursor cursor;
    private int lenCusor = 0;

    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.mnSearch);

        SearchManager searchManager = (SearchManager)
                MainActivity.this.getSystemService(MainActivity.SEARCH_SERVICE);
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {

            searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.e("onQueryTextChange", newText);
                    playerAdapter.getFilter().filter(newText);
                    return true;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {
                    playerAdapter.getFilter().filter(query);
                    Log.e("onQueryTextSubmit", query);
                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnSearch:
                return false;
            default:
                break;
        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (item.isChecked()) return true;

        if (id == R.id.mnLike) {
            checkItem = false;
            getDatabaseLike();
            floatingActionButton.setVisibility(View.GONE);
        } else if (id == R.id.mnList) {
            checkItem = true;
            getDatabase();
            floatingActionButton.setVisibility(View.VISIBLE);
            floatingActionButton.setEnabled(true);
        }else if(id == R.id.mnInfo){
            startActivity(new Intent(MainActivity.this,InfoClubActivity.class));
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e(TAG, "onCreate: ");
        addControls();
        addEvents();
    }


    private void addEvents() {


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddPlayerActivity.class);

                startActivity(intent);
            }
        });

    }


    private void addControls() {

        lvPlayer = findViewById(R.id.lvPlayer);
        playerAdapter = new PlayerAdapter(MainActivity.this, R.layout.item_player, lPlayer);
        lvPlayer.setAdapter(playerAdapter);
        getDatabase();
        Toolbar toolbar = findViewById(R.id.tb_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Bayern Munich FC");
        drawerLayout = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_main);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().findItem(R.id.mnList).setChecked(true);
        floatingActionButton = findViewById(R.id.fbtnAddPlayer);

    }

    private void getDatabase() {

        try {
            lPlayer.clear();
            playerAdapter.notifyDataSetChanged();
            DataPlayer dataPlayer = new DataPlayer(MainActivity.this);
            SQLiteDatabase db = dataPlayer.getReadableDatabase();
            cursor = db.query(DataPlayer.DATABASE_TABLE, null, null, null, null, null, null);
            lenCusor = cursor.getCount();

            while (cursor.moveToNext()) {

                Player player = getCursor(cursor);
                lPlayer.add(player);
                playerAdapter.notifyDataSetChanged();
            }
            cursor.close();
        } catch (SQLiteException ex) {
            ex.printStackTrace();
        }
    }

    private Player getCursor(Cursor cursor) {
        byte[] avatar = cursor.getBlob(cursor.getColumnIndexOrThrow(DataPlayer.KEY_IMAGE));
        Bitmap bitmap = null;
        if (avatar != null)
            bitmap = BitmapFactory.decodeByteArray(avatar, 0, avatar.length);
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(DataPlayer.KEY_ID));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(DataPlayer.KEY_NAME));
        String birth = cursor.getString(cursor.getColumnIndexOrThrow(DataPlayer.KEY_BIRTH));
        String pos = cursor.getString(cursor.getColumnIndexOrThrow(DataPlayer.KEY_POS));
        String values = cursor.getString(cursor.getColumnIndexOrThrow(DataPlayer.KEY_VALUES));
        String contry = cursor.getString(cursor.getColumnIndexOrThrow(DataPlayer.KEY_COUNTRY));
        String trophy = cursor.getString(cursor.getColumnIndexOrThrow(DataPlayer.KEY_TROPHY));
        int like = cursor.getInt(cursor.getColumnIndexOrThrow(DataPlayer.KEY_LIKE));

        return new Player(id, bitmap, name, birth, pos, contry, values, trophy, (like == 1 ? true : false));
    }

    private void getDatabaseLike() {
        try {
            lPlayer.clear();
            playerAdapter.notifyDataSetChanged();
            DataPlayer dataPlayer = new DataPlayer(MainActivity.this);
            SQLiteDatabase db = dataPlayer.getReadableDatabase();
            Cursor cursor1 = db.query(DataPlayer.DATABASE_TABLE, null, DataPlayer.KEY_LIKE + "=?", new String[]{String.valueOf(0)}, null, null, null);

            while (cursor1.moveToNext()) {
                Player player = getCursor(cursor1);
                lPlayer.add(player);
                playerAdapter.notifyDataSetChanged();
            }

            cursor1.close();
        } catch (SQLiteException ex) {
            ex.printStackTrace();
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        try {
            DataPlayer dataPlayer = new DataPlayer(MainActivity.this);
            SQLiteDatabase db = dataPlayer.getReadableDatabase();
            cursor = db.query(DataPlayer.DATABASE_TABLE, null, null, null, null, null, null);
            if (lenCusor < cursor.getCount()) {
                if (cursor.moveToLast()) {
                    lPlayer.add(getCursor(cursor));
                }
            }
            cursor.close();
            lenCusor = cursor.getCount();
        } catch (SQLiteException ex) {
            ex.printStackTrace();
        }
        playerAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause:");
    }


}