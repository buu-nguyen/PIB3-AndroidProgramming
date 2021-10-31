package cytech.android.favoritepicker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;

public class PickColor extends AppCompatActivity {
    RadioGroup radioGroupColor;
    String color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickcolor);

        radioGroupColor = findViewById(R.id.radioGroupColor);
        radioGroupColor.clearCheck();

        radioGroupColor.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case(R.id.radioBtnRed):
                        color = "RED";
                        break;
                    case (R.id.radioBtnGreen):
                        color = "GREEN";
                        break;
                    case (R.id.radioBtnBlue):
                        color = "BLUE";
                }
                // create return intent
                Intent r = new Intent();
                r.putExtra("color", color);
                setResult(RESULT_OK, r);
                finish();
            }
        });
    }
}