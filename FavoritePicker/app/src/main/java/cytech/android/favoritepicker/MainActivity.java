package cytech.android.favoritepicker;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    static final int PICK_CONTACT=1, PICK_COLOR=2;
    String BFF, color;
    Button btnGoogle, btnBFF, btnColor;
    TextView txtBFF, txtColor;
    LinearLayout background;

    @Override
    protected void onStart(){
        super.onStart();

        Intent intent = new Intent(MainActivity.this, BackgroundSoundService.class);
        startService(intent);
    }

    protected void onStop(){
        super.onStop();

        Intent intent = new Intent(MainActivity.this, BackgroundSoundService.class);
        stopService(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGoogle = findViewById(R.id.btnGoogle);
        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                startActivity(intent);
            }
        });

        txtBFF = findViewById(R.id.txtBFF);
        btnBFF = findViewById(R.id.btnBFF);
        btnBFF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });

        txtColor = findViewById(R.id.txtColor);
        btnColor = findViewById(R.id.btnColor);
        background = findViewById(R.id.background);
        btnColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PickColor.class);
                startActivityForResult(intent, PICK_COLOR);
            }
        });
    }

    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c =  managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        BFF = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        txtBFF.setText(BFF);
                    }
                }
                break;
            case (PICK_COLOR):
                if (resultCode == Activity.RESULT_OK)    {
                    color = data.getStringExtra("color");
                    txtColor.setText(color);
                    background.setBackgroundColor(Color.parseColor(color));
                }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putString("BFF", BFF);
        savedInstanceState.putString("color", color);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        BFF = savedInstanceState.getString("BFF");
        color = savedInstanceState.getString("color");
        txtBFF.setText(BFF);
        txtColor.setText(color);
        background.setBackgroundColor(Color.parseColor(color));
    }
}