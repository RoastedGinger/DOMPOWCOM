package com.example.tulsi.dompowcom;

import android.support.v4.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



public class Signup extends Fragment {

    String Server_url = "";
    Button button1;
    TextView textView;
    ImageView imageView,plock1,plock0,gone;
    EditText username,password,repassword,phno,adder,eid;
    String un,ps,repas,phn,email,add;
    android.support.v4.app.FragmentTransaction fragmentTransaction;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.signup,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        username = getActivity().findViewById(R.id.username1);
        password = getActivity().findViewById(R.id.password1);
        repassword = getActivity().findViewById(R.id.rpassword1);
        phno = getActivity().findViewById(R.id.phno1);
        adder = getActivity().findViewById(R.id.caddress);
        eid = getActivity().findViewById(R.id.cemailid);
        button1 = getActivity().findViewById(R.id.signup1);
        imageView = getActivity().findViewById(R.id.visible);
        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                un = username.getText().toString();
                ps = password.getText().toString();
                repas = repassword.getText().toString();
                phn = phno.getText().toString();
                add = adder.getText().toString();
                email = eid.getText().toString();

                if(TextUtils.isEmpty(un))
                {
                    username.setError("Cannot be Empty");
                }
                else if(TextUtils.isEmpty(phn))
                {
                    phno.setError("cannot be empty");
                }
                else if(phn.length()!=10){
                    phno.setError("Invalid number");
                }
                else if(TextUtils.isEmpty(ps) && TextUtils.isEmpty(repas)){
                    password.setError("password cannot be empty");
                    repassword.setError("password cannot be empty");
                }

                else if(!ps.equals(repas))
                {
                    Toast.makeText(getActivity(),"password dosent match", Toast.LENGTH_LONG).show();
                }
                else {
                    open();
                }

            }
        });

    }

    public void open(){
        SharedPreferences preferences = getActivity().getSharedPreferences("details", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("name",un);
        editor.putString("Phone", phn);
        editor.putString("Email",email);
        editor.putString("password",ps);
        editor.putString("address",add);
        editor.apply();
        android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
        Secondsignup secondsignup = new Secondsignup();
        Signup signin_form = (Signup) fragmentManager.findFragmentByTag("signin");
        //  LoginActivity login_page = (LoginActivity) fragmentManager.findFragmentByTag("login666");
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(signin_form);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.add(R.id.cre,secondsignup,"secondsign");
        // getFragmentManager().popBackStackImmediate();
        fragmentTransaction.commit();
    }
}
