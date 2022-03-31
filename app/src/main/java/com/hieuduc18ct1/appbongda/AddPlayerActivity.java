package com.hieuduc18ct1.appbongda;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hieuduc18ct1.appbongda.Database.DataPlayer;
import com.hieuduc18ct1.appbongda.PlayerFootball.Player;

import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class AddPlayerActivity extends AppCompatActivity {

    private ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    // tra ve Uri
                    if (uri != null) {

                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                            imgAvatar.setImageBitmap(bitmap);

                            dataImage = bitmap;
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }


                    }
                }
            });

    private Bitmap dataImage;
    public static final String TAG = "AddPlayerActivity";
    private AutoCompleteTextView edtCountry;
    private Spinner spPostion;
    private ImageButton btnCalendar;
    private Button btnAvatar;
    private ImageView imgAvatar;
    private EditText edtName, edtBirth, edtSalary,edtTrophy;
    private FloatingActionButton addPlayer;
    private boolean checkUpOrAdd = true;
    private Player player;
    private int postion = 0;
    private boolean like1 = true;
    private Map<String,Integer> hsPos = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player);
        mapPos();
        addControls();
        addGetEntent();
        addEvents();
    }

    private void addGetEntent() {
        Intent intent = getIntent();
        if (intent.getExtras() == null) return;
        getSupportActionBar().setTitle("Cập nhật cầu thủ");
        checkUpOrAdd = false;
        try {
            DataPlayer dataPlayer = new DataPlayer(this);
            player = dataPlayer.showPlayerById(intent.getExtras().getInt("id"), dataPlayer);
            postion = intent.getExtras().getInt("postion");
            spPostion.setSelection(hsPos.get(player.getPos())==null ? 0 :hsPos.get(player.getPos()));
            edtName.setText(player.getName().isEmpty() ?" ??? ": player.getName());
            edtBirth.setText(player.getBirth().isEmpty() ?" ??? ": player.getBirth());
            edtSalary.setText(player.getValues().isEmpty()  ?" ??? ": player.getValues());
            edtTrophy.setText(player.getTrophy().isEmpty()?" ??? ": player.getTrophy());
            like1 = player.isLike();
            edtCountry.setText(player.getContry().isEmpty() ?" ??? ": player.getContry());
            if(player.getAvatar()!=null){
                imgAvatar.setImageBitmap(player.getAvatar());
                dataImage = player.getAvatar();
            }
        } catch (SQLiteException ex) {
            ex.printStackTrace();
        }
    }



    private void mapPos(){
        hsPos.put("Tiền Đạo",0);
        hsPos.put("Tiền Vệ",1);
        hsPos.put("Hậu Vệ",2);
        hsPos.put("Thủ Môn",3);

    }
    private void addControls() {

        Toolbar toolbar = findViewById(R.id.tb_add);
        toolbar.setTitle("Thêm Cầu Thủ");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        edtName = findViewById(R.id.edtName);
        edtBirth = findViewById(R.id.edtDate);
        edtSalary = findViewById(R.id.edtSalery);
        btnCalendar = findViewById(R.id.btnCalendar);
        edtCountry = findViewById(R.id.edtCountry);
        edtTrophy = findViewById(R.id.edtTropy);
        spPostion = findViewById(R.id.spPostion);
        btnAvatar = findViewById(R.id.btnAvatar);
        imgAvatar = findViewById(R.id.imgAvatar);
        addPlayer = findViewById(R.id.addPlayer);
        ArrayAdapter<String> adapterCountry = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.countries_array));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, getResources().getStringArray(R.array.postion_array));
        spPostion.setAdapter(adapter);
        edtCountry.setAdapter(adapterCountry);
    }


    private void addEvents() {

        edtSalary.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDatePicker();
            }
        });

        btnAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetContent.launch("image/*");
            }
        });

        addPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataPlayer dataPlayer = new DataPlayer(AddPlayerActivity.this);
                SQLiteDatabase db = dataPlayer.getWritableDatabase();
                String name = edtName.getText().toString();
                String birth = edtBirth.getText().toString();
                String contry = edtCountry.getText().toString();
                String pos = spPostion.getSelectedItem().toString();
                String values = edtSalary.getText().toString();
                String trophy = edtTrophy.getText().toString();

                Player player = new Player(dataImage, name, birth, pos, contry, values, trophy, like1);

                if(checkUpOrAdd){
                    dataPlayer.addPlayer(db,player);
                    alertSuccess("Thêm thành công.");
                }
                else
                {
                    player.setId(AddPlayerActivity.this.player.getId());
                    dataPlayer.updatePlayer(db,player);
                    MainActivity.lPlayer.set(postion,player);
                    alertSuccess("Cập nhật thành công.");
                }

            }
        });
    }



    private void createDatePicker() {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(AddPlayerActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                edtBirth.setText(String.format("%d - %d - %d", i2, i1 + 1, i));
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void alertSuccess(String tb) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(AddPlayerActivity.this);
        builder.setTitle("Thông báo")
                .setMessage(tb)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
        builder.create().show();
    }


}