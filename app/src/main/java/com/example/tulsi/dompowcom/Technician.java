package com.example.tulsi.dompowcom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Technician extends AppCompatActivity {

    Spinner spinner1,spinner2;
    ArrayAdapter<CharSequence> adapter;
    String tech,place,Server_url = "";
    Button assign;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technician);
        assign = findViewById(R.id.assign);
        spinner1 = findViewById(R.id.phase);
        adapter = ArrayAdapter.createFromResource(Technician.this,R.array.phase, android.R.layout.simple_spinner_item);
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                tech = spinner2.getSelectedItem().toString();
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner2 = findViewById(R.id.phase);
        adapter = ArrayAdapter.createFromResource(Technician.this,R.array.phase, android.R.layout.simple_spinner_item);
        spinner2.setAdapter(adapter);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                place = spinner2.getSelectedItem().toString();
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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


                            Toast.makeText(Technician.this,res,Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Technician.this,Homepage.class);
                            startActivity(intent);
//                            fragmentTransaction.remove(Login.this).commit();
                        }
                        else
                        {
                           // avi.hide();
                            Toast.makeText(getApplicationContext(), "invalid user name or password",Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String voll = error.toString();
                Toast.makeText(getApplicationContext(),voll,Toast.LENGTH_LONG).show();
                //avi.hide();
            }
        });
        /*{
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                idofcus = .getText().toString();
                ps = password.getText().toString();
                params.put("cus",idofcus);
                params.put("ps",ps);
                return params;
            }
        };*/
        MySingleton.getInstance(Technician.this).addToRequestQue(request);
    }
}
