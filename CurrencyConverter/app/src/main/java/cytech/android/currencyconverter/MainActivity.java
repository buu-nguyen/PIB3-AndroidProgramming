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
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    String[] CurrencyCodes = {"USD", "EUR", "RMB", "CHF", "AUD"};
    Double[] FromUSDRatios = {1.0, 1.0, 1.0, 1.0, 1.0};
    Double[] ToUSDRatios = {1.0, 1.0, 1.0, 1.0, 1.0};
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

        // Get exchange rates from online API
        getExchangeRates();

        addListenerOnButton();
        addListenerOnSpinner();
    }

    private void getExchangeRates() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://api.exchangeratesapi.io/v1/latest?access_key=fd2f82626077aab9466e82d4742a6e85&symbols=USD,EUR,CNY,CHF,AUD";

        // Request a JSON response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject rates = response.getJSONObject("rates");
                            JSONArray key = rates.names();
                            for (int i = 0; i < key.length(); ++i) {
                                String keys = key.getString(i);
                                double value = Double.parseDouble(rates.getString(keys));
                                FromUSDRatios[i] = value;
                                ToUSDRatios[i] = 1 / value;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
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