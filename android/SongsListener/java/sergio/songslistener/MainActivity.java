package sergio.lab7thirdtry;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
public class MainActivity extends AppCompatActivity {
    private static Context context;
    ListView listView;
    DBHelper dbhelper;
    private Button btnDel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbhelper = new DBHelper(this);
        MainActivity.context = getApplicationContext();
        if (! isOnline()) {
            toastMessage("Here is no Internet connection!");
        }
        else {
            callTask();
        }
        listView = (ListView) findViewById(R.id.listView);
        btnDel = (Button) findViewById(R.id.btnDel);
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbhelper.deleteAll();
                populateListView();
                toastMessage("Cleared!");
            }
        });
        populateListView();
    }
    public static Context getAppContext() {
        return MainActivity.context;
    }
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    private void toastMessage(String message){
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
    }
    private void populateListView () {
        Cursor data =dbhelper.getData();
        ArrayList<String> listData = new ArrayList<>();
        ListAdapter adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, listData);
        listView.setAdapter(adapter);
        while(data.moveToNext()){
            listData.add(data.getString(1) + "\n" + data.getString(2));
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                listData);
        listView.setAdapter(adapter);
    }
    public void callTask() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            MyAsyncTask performBackgroundTask = new MyAsyncTask();
                            performBackgroundTask.execute();
                        } catch (Exception e) { }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 30000);
    }
    public class MyAsyncTask extends AsyncTask<String, String, String> {
        public MyAsyncTask() { }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... params) {
            String response = "";
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost http = new
                        HttpPost("http://media.ifmo.ru/api_get_current_song.php");
                List nameValuePairs = new ArrayList(2);
                nameValuePairs.add(new BasicNameValuePair("login", "4707login"));
                nameValuePairs.add(new BasicNameValuePair("password", "4707pass"));
                http.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                response = (String) httpclient.execute(http, new
                        BasicResponseHandler());
            }
            catch (Exception e) { }
            return response;
        }
        protected void onPostExecute(String result) {
            result = result.replace("{\"result\" : \"success\", \"info\" : \"", "");
            result = result.replace("\"}", "");
            int i = result.indexOf('-');
            String artist = result.substring(0, i-1);
            String song = result.substring(i+2, result.length());
            dbhelper.addData(artist, song);
            populateListView();
        }
    }
}