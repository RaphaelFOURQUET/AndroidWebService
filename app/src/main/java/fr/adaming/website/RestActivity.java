package fr.adaming.website;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.adaming.website.bean.CityBean;
import fr.adaming.website.bean.CityBeanAdapter;
import fr.adaming.website.bean.ResultBean;
import fr.adaming.website.data.Constante;

public class RestActivity extends AppCompatActivity implements View.OnClickListener {

    private MonAT atTextView;

    private Button buttonSend;
    private TextView textViewRestResult;
    private EditText editTextCP;
    private ListView listViewRest;

    //Création
    private ListView lv;
    private List<CityBean> cityList;
    private CityBeanAdapter cityAdapter;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest);

        this.initComponents();
        this.initListeners();

        this.setTitle("REST");

        cityList = new ArrayList<CityBean>();
        cityAdapter = new CityBeanAdapter(this, cityList);
        lv = (ListView) findViewById(R.id.listViewRest);
        lv.setAdapter(cityAdapter);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initComponents() {
        this.buttonSend = (Button) this.findViewById(R.id.buttonSend);
        this.textViewRestResult = (TextView) this.findViewById(R.id.textViewRestResult);
        this.editTextCP = (EditText) this.findViewById(R.id.editTextCP);
        this.listViewRest = (ListView) this.findViewById(R.id.listViewRest);

    }

    private void initListeners() {
        this.buttonSend.setOnClickListener(this);
    }

    private void send() {
        //Recuperer le texte
        String cp = editTextCP.getText().toString();
        if (Constante.LOG_DEV_MODE) Log.w(Constante.TAG, "code postal : " + cp);

        //Construire l'url
        String url = "http://www.citysearch-api.com/fr/city?" + Constante.API_LOGIN + "&" + Constante.API_KEY + "&cp=" + cp;
        if (Constante.LOG_DEV_MODE) Log.w(Constante.TAG, "url : " + url);
        //envoyer requete GET
        if (atTextView == null || atTextView.getStatus() == AsyncTask.Status.FINISHED) {
            atTextView = new MonAT(url);
        }
        if (atTextView.getStatus() == AsyncTask.Status.PENDING) {
            atTextView.execute();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.buttonSend):
                send();
                break;
            default:
                Log.w(Constante.TAG, "Not supposed to happen : default case switch.");
        }
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

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Rest Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://fr.adaming.website/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Rest Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://fr.adaming.website/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    private class MonAT extends AsyncTask<Void, Void, ResultBean> {

        private String urlRequest;
        private HttpURLConnection conn;

        //Création de l'objet
        private Gson gson = new Gson();

        public MonAT(String url) {
            this.urlRequest = url;
        }

        @Override
        protected ResultBean doInBackground(Void... params) {
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

                InputStreamReader isr = new InputStreamReader(is);
                //parsingdu flux ou du String contenant du JSON
                ResultBean result = gson.fromJson(isr, ResultBean.class);

                return result;

                //return readIt(is);

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
        protected void onPostExecute(ResultBean r) {
            super.onPostExecute(r);

//            CityBean[] t = r.getResults();
//            CityBean test = t[0];

//            textViewRestResult.setText("Nombre de resultats : " + r.getNbr() + "\nresults : " + Arrays.toString(r.getResults()));
            textViewRestResult.setText("Nombre de resultats : " + r.getNbr());
            Log.w("TAG", Arrays.toString(r.getResults()));

            if (r.getResults() != null) {
                //listViewRest
                for (CityBean c : r.getResults()) {
                    cityList.add(c);
                }
                //cityList.addAll(Arrays.asList(r.getResults()));
                cityAdapter.notifyDataSetChanged();
            }


        }
    }
}
