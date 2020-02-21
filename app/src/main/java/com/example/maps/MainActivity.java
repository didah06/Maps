package com.example.maps;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    Button button;
    String url;

    TextView mFajrTv;
    TextView mDhuhrTv;
    TextView mAsrTv;
    TextView mMagrbTv;
    TextView mIsyaTv;
    TextView mLocationTv, mDateTv;
    ProgressDialog pDialog;

    String tag_json_obj = "json_obj_req";
    private static final String TAG ="tag";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle extras = getIntent().getExtras();

        mFajrTv = findViewById(R.id.fajrTv);
        mDhuhrTv = findViewById(R.id.dhuhrTv);
        mAsrTv = findViewById(R.id.asrTv);
        mMagrbTv = findViewById(R.id.MagrbTv);
        mIsyaTv = findViewById(R.id.IsyaTv);
        mLocationTv = findViewById(R.id.locationTv);
        mDateTv = findViewById(R.id.dateTv);

        if (extras != null){
            textView = findViewById(R.id.text_location);
            textView.setText(extras.getString("city"));
            String lokasi = String.valueOf(extras.getString("city"));
            url = "https://muslimsalat.com/" + lokasi + ".json?key=5fe91155ab8498eb43bd3aae6755c72c";
            searchLocation();
        }
        
        button = findViewById(R.id.button_location);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });



    }

    private void searchLocation() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading");
        pDialog.show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //get location
                            String country = response.get("country").toString();
                            String location =country+" ";
                            //get date
                            String date = response.getJSONArray("items").getJSONObject(0).get("date_for").toString();

                            //get timming
                            String txtSubuh = response.getJSONArray("items").getJSONObject(0).get("fajr").toString();
                            String txtDzuhur = response.getJSONArray("items").getJSONObject(0).get("dhuhr").toString();
                            String txtAshar = response.getJSONArray("items").getJSONObject(0).get("asr").toString();
                            String txtMaghrib = response.getJSONArray("items").getJSONObject(0).get("maghrib").toString();
                            String txtIsya = response.getJSONArray("items").getJSONObject(0).get("isha").toString();
                            mFajrTv.setText(txtSubuh);
                            mDhuhrTv.setText(txtDzuhur);
                            mAsrTv.setText(txtAshar);
                            mMagrbTv.setText(txtMaghrib);
                            mIsyaTv.setText(txtIsya);
                            mLocationTv.setText(location);
                            mDateTv.setText(date);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: "+error.getMessage());
                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                pDialog.hide();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);

    }

    }


