package com.contreras.myquizapplication;

import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.contreras.myquizapplication.View.HomeActivity;
import com.contreras.myquizapplication.View.LoginActivity;
import com.contreras.myquizapplication.View.RegistrarActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    SweetAlertDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        //IF PER VEDERE SE LA CONNESSIONE È ATTIVA
        if(isNetworkConnected()) {
            controlAuthtentication();
        }else {
            mostrarErrorConection();
        }
    }

    private void controlAuthtentication() {
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getUid() == null){
            Log.d("admin","No iniciado");
        }else{
            FirebaseUser currentUser = mAuth.getCurrentUser();
            Log.d("admin",currentUser.getUid());
            Log.d("admin",currentUser.getEmail());
            Intent i = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(i);
        }
    }

    @OnClick(R.id.btn_login)
    public void login(){
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.btn_registrar)
    public void registrar(){
        Intent i = new Intent(MainActivity.this, RegistrarActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.btn_salir)
    public void salir(){
        finish();
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null;
    }

    public void mostrarErrorConection() {
        pd = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
        pd.getProgressHelper().setBarColor(Color.parseColor("#303F9F"));
        pd.setCancelable(false);
        pd.setContentText("Sin conexion");
        pd.setConfirmText("Intenta otra vez!");

        pd.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.dismissWithAnimation();
                onStart();
            }
        });
        pd.show();

        Button btn = (Button) pd.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.colorAlertError));
    }



}
