package com.example.moonphase.moonphase;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new LongOperation().execute();

    }

    private class LongOperation extends AsyncTask<Void, Void, String> {

        ProgressDialog progressDialog;
        @Override
        protected String doInBackground(Void... params) {
            try {
                OkHttpClient client = new OkHttpClient();
                Request req = new Request.Builder().method("GET", null).url("http://www.moongiant.com/phase/today/").build();
                Response response = client.newCall(req).execute();
                return response.body().string();

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String data) {
            TextView moonPhaseTV = (TextView) findViewById(R.id.moonphase);
            progressDialog.dismiss();
            if (data != null) {
                String placeholder = "Phase:";
                int pos = data.indexOf(placeholder);
                String phase = data.substring(pos + placeholder.length(), data.indexOf("\"", pos));
                phase = phase.replaceAll("<", "").replaceAll(">", "").replaceAll("span", "").replace("\\", "").replace("/", "");
                moonPhaseTV.setText(phase);
            } else {
                moonPhaseTV.setText("Error occurred. Check your internet connection.");
            }
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Loading phase...");
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}
