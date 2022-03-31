package com.hieuduc18ct1.appbongda.Database;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.hieuduc18ct1.appbongda.Adapter.PlayerAdapter;
import com.hieuduc18ct1.appbongda.PlayerFootball.Player;

import java.io.ByteArrayOutputStream;

public class DataPlayer extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "data_player";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_TABLE = "player";
    public static final String KEY_ID = "id";
    public static final String KEY_IMAGE = "avatar";
    public static final String KEY_NAME = "name";
    public static final String KEY_BIRTH = "birth";
    public static final String KEY_POS = "pos";
    public static final String KEY_COUNTRY = "contry";
    public static final String KEY_VALUES = "values1";
    public static final String KEY_TROPHY = "trophy";
    public static final String KEY_LIKE = "like1";


    public DataPlayer(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_USER = "CREATE TABLE " + DATABASE_TABLE + " (" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_IMAGE + " BLOB, " +
                KEY_NAME + " TEXT, " +
                KEY_BIRTH + " TEXT, " +
                KEY_POS + " TEXT, " +
                KEY_COUNTRY + " TEXT, " +
                KEY_VALUES + " TEXT, " +
                KEY_TROPHY + " TEXT, " +
                KEY_LIKE + " INTEGER );";

        sqLiteDatabase.execSQL(SQL_CREATE_USER);
        putPlayer();
    }

    private void putPlayer() {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        return outputStream.toByteArray();
    }

    public void addPlayer(SQLiteDatabase db, Player player) {

        ContentValues values = new ContentValues();
        if (player.getAvatar() != null)
            values.put(KEY_IMAGE, getBitmapAsByteArray(player.getAvatar()));
        values.put(KEY_NAME, player.getName());
        values.put(KEY_BIRTH, player.getBirth());
        values.put(KEY_POS, player.getPos());
        values.put(KEY_COUNTRY, player.getContry());
        values.put(KEY_VALUES, player.getValues());
        values.put(KEY_TROPHY, player.getTrophy());
        int check = 0;
        if (player.isLike()) check = 1;
        values.put(KEY_LIKE, check);
        db.insert(DATABASE_TABLE, null, values);
        db.close();

    }

    public void updatePlayer(SQLiteDatabase db, Player player) {

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, player.getName());
        values.put(KEY_BIRTH, player.getBirth());
        values.put(KEY_POS, player.getPos());
        if (player.getAvatar() != null)
            values.put(KEY_IMAGE, getBitmapAsByteArray(player.getAvatar()));
        values.put(KEY_COUNTRY, player.getContry());
        values.put(KEY_VALUES, player.getValues());
        values.put(KEY_TROPHY, player.getTrophy());
        int check = 0;
        if (player.isLike()) check = 1;
        values.put(KEY_LIKE, check);
        db.update(DATABASE_TABLE, values, KEY_ID + "=?", new String[]{String.valueOf(player.getId())});
        db.close();

    }

    public void delPlayer(int id, SQLiteDatabase db) {
        db.delete(DATABASE_TABLE, KEY_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public Player showPlayerById(int id, DataPlayer dataPlayer) {

        try {

            SQLiteDatabase db = dataPlayer.getReadableDatabase();
            Cursor cursor = db.query(DATABASE_TABLE, null, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);

            cursor.moveToFirst();
            byte[] avatar = cursor.getBlob(cursor.getColumnIndexOrThrow(DataPlayer.KEY_IMAGE));
            Bitmap bitmap = null;
            if (avatar != null) bitmap = BitmapFactory.decodeByteArray(avatar, 0, avatar.length);
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DataPlayer.KEY_NAME));
            String birth = cursor.getString(cursor.getColumnIndexOrThrow(DataPlayer.KEY_BIRTH));
            String pos = cursor.getString(cursor.getColumnIndexOrThrow(DataPlayer.KEY_POS));
            String values = cursor.getString(cursor.getColumnIndexOrThrow(DataPlayer.KEY_VALUES));
            String contry = cursor.getString(cursor.getColumnIndexOrThrow(DataPlayer.KEY_COUNTRY));
            String trophy = cursor.getString(cursor.getColumnIndexOrThrow(DataPlayer.KEY_TROPHY));
            int like = cursor.getInt(cursor.getColumnIndexOrThrow(DataPlayer.KEY_LIKE));

            Player player = new Player(id, bitmap, name, birth, pos, contry, values, trophy, (like == 1 ? true : false));

            db.close();
            cursor.close();
            return player;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void updatePlayerLike(SQLiteDatabase db, Player player) {
        ContentValues values = new ContentValues();
        int check = 0;
        if (player.isLike()) check = 1;
        values.put(KEY_LIKE, check);
        db.update(DATABASE_TABLE, values, KEY_ID + "=?", new String[]{String.valueOf(player.getId())});

    }




}
