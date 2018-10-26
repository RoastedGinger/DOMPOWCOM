package com.example.tulsi.dompowcom;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import static android.content.Context.MODE_PRIVATE;


public class Secondsignup extends Fragment {

    Spinner spinner1,spinner2,spinner3,spinner4;
    ArrayAdapter<CharSequence> adapter;
    String supp,met,conn,phas;
    Button submit;
    String Server_url = "http://beholden-effects.000webhostapp.com/DomPowCom/Odd_info.php",name,phone,address,email,ps;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        submit = getActivity().findViewById(R.id.submit);
        spinner1 = getActivity().findViewById(R.id.supply);
        SharedPreferences prefs = getActivity().getSharedPreferences("details",MODE_PRIVATE);
         name = prefs.getString("name", null);
         phone = prefs.getString("Phone",null);
        address = prefs.getString("address",null);
        email = prefs.getString("Email",null);
        ps = prefs.getString("password",null);
        adapter = ArrayAdapter.createFromResource(getActivity(),R.array.supply_type, android.R.layout.simple_spinner_item);
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                supp = spinner1.getSelectedItem().toString();
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner2 = getActivity().findViewById(R.id.phase);
        adapter = ArrayAdapter.createFromResource(getActivity(),R.array.phase, android.R.layout.simple_spinner_item);
        spinner2.setAdapter(adapter);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                phas = spinner2.getSelectedItem().toString();
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner3 = getActivity().findViewById(R.id.load);
        adapter = ArrayAdapter.createFromResource(getActivity(),R.array.connected_load, android.R.layout.simple_spinner_item);
        spinner3.setAdapter(adapter);
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                conn = spinner3.getSelectedItem().toString();
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinner4 = getActivity().findViewById(R.id.meter);
        adapter = ArrayAdapter.createFromResource(getActivity(),R.array.meter_type, android.R.layout.simple_spinner_item);
        spinner4.setAdapter(adapter);
        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                met = spinner4.getSelectedItem().toString();
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        post();
                    }
                });
        }

    private void post() {

        StringRequest request = new StringRequest(Request.Method.POST, Server_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity(),response,Toast.LENGTH_LONG).show();

                        SharedPreferences preferences = getActivity().getSharedPreferences("unique", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("id",response);
                        editor.apply();
                        Intent intent = new Intent(getActivity(),Homepage.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String voll = error.toString();
                Toast.makeText(getActivity(),voll,Toast.LENGTH_LONG).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("supply",supp);
                params.put("meter",met);
                params.put("conn_load",conn);
                params.put("phase_type",phas);
                params.put("name",name);
                params.put("phone",phone);
                params.put("address",address);
                params.put("Email",email);
                params.put("password",ps);
                return params;
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQue(request);
    }
}
