package fr.adaming.website;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import fr.adaming.website.data.Constante;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MonAT atTextView;

    //Declaration de mes composants visuels
    private EditText editTextRequest;
    private Button buttonRequest;
    private TextView textViewRequestResult;
    private WebView webViewRequestResult;

    private Button buttonRest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.initcomponents();
        this.initListeners();

        //Accepter le javascript
        WebSettings webviewSettings = webViewRequestResult.getSettings();
        webviewSettings.setJavaScriptEnabled(true);
        webViewRequestResult.setWebViewClient(new WebViewClient());

        //Test internet Connexion
        if (!MainActivity.isInternetConnexion(MainActivity.this)) {
            Log.e(Constante.TAG, "Pas de connection internet disponible");
        }
    }

    private void initcomponents() {
        this.editTextRequest = (EditText) this.findViewById(R.id.editTextRequest);
        this.buttonRequest = (Button) this.findViewById(R.id.buttonRequest);
        this.textViewRequestResult = (TextView) this.findViewById(R.id.textViewRequestResult);
        this.webViewRequestResult = (WebView) this.findViewById(R.id.webViewRequestResult);
        this.buttonRest = (Button) this.findViewById(R.id.buttonRest);
    }

    private void initListeners() {
        buttonRequest.setOnClickListener(this);
        buttonRest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.buttonRequest):
                request();
                break;
            case (R.id.buttonRest):
                startActivity(new Intent(this, RestActivity.class));
                break;
            default:
                Log.w(Constante.TAG, "Not supposed to happen : default case switch.");
        }

    }

    private void request() {
        //TODO
        //Recuperer la valeur
        String urlRequest = editTextRequest.getText().toString();
        if (Constante.LOG_DEV_MODE) Log.w(Constante.TAG, urlRequest);

        //TODO TextView
        textView(urlRequest);

        //WebView
        webViewRequest(urlRequest);

    }

    private void webViewRequest(String url) {
        webViewRequestResult.loadUrl(url);
    }

    private void textView(String url) {
        if (atTextView == null || atTextView.getStatus() == AsyncTask.Status.FINISHED) {
            atTextView = new MonAT(url);
        }
        if (atTextView.getStatus() == AsyncTask.Status.PENDING) {
            atTextView.execute();
        }
    }

    public static boolean isInternetConnexion(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm != null && cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private static String readIt(InputStream stream) throws IOException, UnsupportedEncodingException {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line + "\n");
        }
        return builder.toString();
    }

    private class MonAT extends AsyncTask<Void, Void, String> {

        private String urlRequest;
        private HttpURLConnection conn;

        public MonAT(String url) {
            this.urlRequest = url;
        }

        @Override
        protected String doInBackground(Void... params) {
            //preparer la requete
            URL url = null;
            InputStream is = null;
            try {
                url = new URL(this.urlRequest);
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds*/);
                conn.setConnectTimeout(15000 /* milliseconds*/);
                conn.setRequestMethod("GET");

                //Lancer la requete
                conn.connect();

                int response = conn.getResponseCode();
                Log.w(Constante.TAG, "Status response : " + response);
                is = conn.getInputStream();

                return readIt(is);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (conn != null) {
                    conn.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            textViewRequestResult.setText(s);


        }
    }

}
