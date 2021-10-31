package cytech.android.currencyconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    String[] CurrencyCodes = {"USD", "EUR", "RMB", "CHF", "AUD"};
    Double[] FromUSDRatios = {1.0, 1.16, 0.16, 1.1, 0.75};
    Double[] ToUSDRatios = {1.0, 0.86, 6.4, 0.91, 1.33};
    EditText editTxtFrom, editTxtFromSpinner, editTxtToSpinner;
    TextView txtTo;
    Button btnConvert;
    RadioGroup radioGroupFrom, radioGroupTo;
    RadioButton radioBtnFrom, radioBtnTo;
    Spinner spinnerFrom, spinnerTo;
    CheckBox checkReverse;
    double ratioFrom, ratioTo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addListenerOnButton();
        addListenerOnSpinner();
    }

    public void addListenerOnButton(){
        editTxtFrom = findViewById(R.id.editTxtFrom);
        txtTo = findViewById(R.id.txtTo);
        btnConvert = findViewById(R.id.btnConvert);
        radioGroupFrom = findViewById(R.id.radioGroupFrom);
        radioGroupTo = findViewById(R.id.radioGroupTo);

        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get selected radio button from radioGroup
                int radioFromId = radioGroupFrom.getCheckedRadioButtonId();
                int radioToId = radioGroupTo.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radioBtnFrom = findViewById(radioFromId);
                radioBtnTo = findViewById(radioToId);

                // get convert ratio
                String CurrencyFrom = radioBtnFrom.getText().toString();
                String CurrencyTo = radioBtnTo.getText().toString();
                int idFrom = Arrays.asList(CurrencyCodes).indexOf(CurrencyFrom);
                int idTo = Arrays.asList(CurrencyCodes).indexOf(CurrencyTo);
                double ratio = FromUSDRatios[idFrom] * ToUSDRatios[idTo];

                // convert
                String converted = (Double.parseDouble(editTxtFrom.getText().toString()) * ratio) + "";

                txtTo.setText(converted);
            }
        });
    }

    public void addListenerOnSpinner(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                CurrencyCodes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinnerFrom = findViewById(R.id.spinnerFrom);
        this.spinnerFrom.setAdapter(adapter);
        this.spinnerTo = findViewById(R.id.spinnerTo);
        this.spinnerTo.setAdapter(adapter);
        editTxtFromSpinner = findViewById(R.id.editTxtFromSpinner);
        editTxtToSpinner = findViewById(R.id.editTxtToSpinner);
        checkReverse = findViewById(R.id.checkReverse);

        // When user select a List-Item
        this.spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ratioFrom = FromUSDRatios[position];

                if (checkReverse.isChecked()) {
                    // convert
                    String converted = (Double.parseDouble(editTxtToSpinner.getText().toString()) * (ratioFrom * ratioTo)) + "";

                    editTxtFromSpinner.setText(converted);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        this.spinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ratioTo = ToUSDRatios[position];

                if (!checkReverse.isChecked()) {
                    // convert
                    String converted = (Double.parseDouble(editTxtFromSpinner.getText().toString()) * (ratioFrom * ratioTo)) + "";

                    editTxtToSpinner.setText(converted);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}