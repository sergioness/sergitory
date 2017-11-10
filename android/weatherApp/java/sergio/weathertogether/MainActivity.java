package sergio.weathertogether;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    TextView degreeText;
    EditText setCityText;
    TextView cityText;
    TextView dateText;
    TextView descrText;
    final double degreeKelvin = 274.15;
    String url;
    ImageView weatherImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        degreeText = (TextView) findViewById(R.id.degreeText);
        cityText = (TextView) findViewById(R.id.cityText);
        dateText = (TextView) findViewById(R.id.dateText);
        descrText = (TextView) findViewById(R.id.descrText);
        weatherImageView = (ImageView) findViewById(R.id.stateImage);

        connect("Kiev");
    }


    public void setCity(View view)
    {
        if (! isOnline())
            toastMessage("Here is no Internet connection!");
        else {
            setCityText = (EditText) findViewById(R.id.setCityText);
            String city = setCityText.getText().toString();
            connect(city);
        }
    }

    private void connect(String city){
        url = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=9409648cbbaf32efcd4d55e81782a6a1";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject mainJSON = response.getJSONObject("main");
                            JSONArray weatherArray = response.getJSONArray("weather");
                            JSONObject weatherObject = weatherArray.getJSONObject(0);

                            degreeText.setText(Integer.toString((int)Math.round((mainJSON.getDouble("temp")-degreeKelvin))));
                            descrText.setText(weatherObject.getString("description"));
                            cityText.setText(response.getString("name"));
                            dateText.setText(getCurrentDate());

                            int iconResourceId = getResources().getIdentifier("icon_" + weatherObject.getString("description").replace(" ", ""), "drawable", getPackageName());
                            weatherImageView.setImageResource(iconResourceId);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsObjRequest);
    }

    private String getCurrentDate(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Calendar c = Calendar.getInstance();

        return dateFormat.format(c.getTime());//05/09/17 12:08:43
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
