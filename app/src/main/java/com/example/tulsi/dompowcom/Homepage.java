package com.example.tulsi.dompowcom;


import android.content.Intent;
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

public class Homepage extends AppCompatActivity {

    String id;
    TextView textView,idno;
    EditText editText;
    Button submit;
    Spinner spinner2;
    String formattedDate;
    ArrayAdapter<CharSequence> adapter;
    String details, c_type,Server_url="https://beholden-effects.000webhostapp.com/DomPowCom/complaint.php",trigger ="1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        SharedPreferences prefs =getSharedPreferences("unique",MODE_PRIVATE);
        id  = prefs.getString("id", null);
        submit = findViewById(R.id.submitcom);
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
            public void onClick(View view) {
                post();
            }
        });


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
