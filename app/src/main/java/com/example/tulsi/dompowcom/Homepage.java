package com.example.tulsi.dompowcom;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
public class Homepage extends AppCompatActivity {


    private TextView txtViewLatNetwork;
    private TextView txtViewLongNetwork;
    private LocationManager mLocationManagerNetwork;
    private LocationListener mLocationListenerNetwork;


    String id;
    TextView textView,idno,text;
    EditText editText;
    Button submit;
    Spinner spinner2;
    String formattedDate;
    ArrayAdapter<CharSequence> adapter;
    String details, c_type,Server_url="https://beholden-effects.000webhostapp.com/DomPowCom/complaint.php",trigger ="1";


    Double lat,lon;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        SharedPreferences prefs =getSharedPreferences("unique",MODE_PRIVATE);

        txtViewLatNetwork = findViewById(R.id.txtViewLatNetwork);
        txtViewLongNetwork = findViewById(R.id.txtViewLonNetwork);

        id  = prefs.getString("id", null);
        submit = findViewById(R.id.submitcom);
        text = findViewById(R.id.location);
        idno = findViewById(R.id.idno);
        editText = findViewById(R.id.details);
        textView = findViewById(R.id.textfield);
        textView.setText("your customer id is "+id);
        spinner2 = findViewById(R.id.c_type);
        adapter = ArrayAdapter.createFromResource(Homepage.this,R.array.complaint_type, android.R.layout.simple_spinner_item);
        spinner2.setAdapter(adapter);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                c_type = spinner2.getSelectedItem().toString();
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPositionNetwork();
                post();
            }
        });
    }

    private void getPositionNetwork() {
        mLocationManagerNetwork = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        mLocationListenerNetwork = new LocationListener() {
            public void onLocationChanged(Location location) {
                txtViewLatNetwork.setText(Double.toString(location.getLatitude()));
                txtViewLongNetwork.setText(Double.toString(location.getLongitude()));

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
                showAlert(R.string.Network_disabled);
            }
        };

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestLocationPermission();
            } else {
                submit.setEnabled(false);
                mLocationManagerNetwork.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5, 0, mLocationListenerNetwork);
            }
        }
    }


    private void showAlert(int messageId) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(messageId).setCancelable(false).setPositiveButton(R.string.btn_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton(R.string.btn_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void requestLocationPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.GPS_permissions).setCancelable(false).setPositiveButton(R.string.btn_yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    ActivityCompat.requestPermissions(Homepage.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
            }).show();
        } else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.GPS_permissions).setCancelable(false).setPositiveButton(R.string.btn_watch_permissions, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName())));
                }
            }).setNegativeButton(R.string.btn_close, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }).show();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();


        if (mLocationManagerNetwork != null) {
            mLocationManagerNetwork.removeUpdates(mLocationListenerNetwork);
        }


        txtViewLatNetwork.setText(null);
        txtViewLongNetwork.setText(null);
    }

    @Override
    protected void onResume() {
        super.onResume();



        if (!submit.isEnabled()) {
            submit.setEnabled(true);
        }

    }

    void post()
    {
        StringRequest request = new StringRequest(Request.Method.POST, Server_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String res=response.toString().trim();
                        if(!res.equals("0"))
                        {
                           /* SharedPreferences sharedPreferences=getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString("status",res);
                            editor.apply();*/
                            idno.setText(res);
                            Toast.makeText(Homepage.this,res,Toast.LENGTH_LONG).show();

//                            fragmentTransaction.remove(Login.this).commit();
                        }
                        else
                        {
                            Toast.makeText(Homepage.this, "invalid user name or password",Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String voll = error.toString();
                Toast.makeText(Homepage.this,voll,Toast.LENGTH_LONG).show();
                //     avi.hide();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                details = editText.getText().toString();
                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                formattedDate = df.format(c);
                Log.v("ADebugTag",id);
                Log.v("ADebugTag",formattedDate);
                Log.v("ADebugTag", "Value: " + c_type);
                Log.v("ADebugTag", "Value: " + details);
                params.put("id",id);
                params.put("date",formattedDate);
                params.put("c_type",c_type);
                params.put("details",details);


                return params;
            }
        };
        MySingleton.getInstance(Homepage.this).addToRequestQue(request);
    }
}
