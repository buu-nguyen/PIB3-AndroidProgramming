package cytech.android.fibonacci;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

import cytech.android.fibonacci.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'fibonacci' library on application startup.
    static {
        System.loadLibrary("fibonacci");
    }

    private ActivityMainBinding binding;
    Button buttonJava, buttonKotlin, buttonJNI, buttonHTTP;
    EditText editTextNumber;
    TextView textViewResult, textViewRuntime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        textViewResult = binding.textViewResult;
        textViewRuntime = binding.textViewRuntime;
        editTextNumber = binding.editTextNumber;
        buttonJava = binding.buttonJava;
        buttonKotlin = binding.buttonKotlin;
        buttonJNI = binding.buttonJNI;
        buttonHTTP = binding.buttonHTTP;

        // JAVA
        buttonJava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long begin = System.nanoTime();
                int n = Java.Fibonacci(Integer.parseInt(editTextNumber.getText().toString()));
                String result = "Result: " + n;
                String time = "Runtime: " + (System.nanoTime() - begin) * 1.0 / 1000000000 + " s";
                textViewResult.setText(result);
                textViewRuntime.setText(time);
            }
        });

        // KOTLIN
        buttonKotlin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long begin = System.nanoTime();
                int n = KotlinKt.Fibonacci(Integer.parseInt(editTextNumber.getText().toString()));
                String result = "Result: " + n;
                String time = "Runtime: " + (System.nanoTime() - begin) * 1.0 / 1000000000 + " s";
                textViewResult.setText(result);
                textViewRuntime.setText(time);
            }
        });

        // JNI C
        buttonJNI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long begin = System.nanoTime();
                int n = Fibonacci(Integer.parseInt(editTextNumber.getText().toString()));
                String result = "Result: " + n;
                String time = "Runtime: " + (System.nanoTime() - begin) * 1.0 / 1000000000 + " s";
                textViewResult.setText(result);
                textViewRuntime.setText(time);
            }
        });

        // WEB SERVICE
        buttonHTTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long begin = System.nanoTime();
                String n = null;
                try {
                    n = new HttpGetRequest().execute("https://visual.options.eisti.fr/webservices/fibonacci/"+editTextNumber.getText().toString()).get();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
                String result = "Result: " + n;
                String time = "Runtime: " + (System.nanoTime() - begin) * 1.0 / 1000000000 + " s";
                textViewResult.setText(result);
                textViewRuntime.setText(time);
            }
        });
    }

    // JNI function
    public native int Fibonacci(int i);
}