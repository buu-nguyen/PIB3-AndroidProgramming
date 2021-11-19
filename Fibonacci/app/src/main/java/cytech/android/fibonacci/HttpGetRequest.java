package cytech.android.fibonacci;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpGetRequest extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... uri) {
        String responseString = null;
        try {
            URL url = new URL(uri[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            StringBuilder builder = new StringBuilder();
            String inputString;
            while ((inputString = bufferedReader.readLine()) != null) { builder.append(inputString); }
            responseString = builder.toString();

            urlConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseString;
    }
}
