package com.contreras.myquizapplication.Model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.contreras.myquizapplication.Interfaces.ILogin;
import com.contreras.myquizapplication.Presenter.LoginPresenter;
import com.contreras.myquizapplication.View.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginModel implements ILogin.ILoginModel {

    FirebaseAuth mAuth;

    LoginPresenter presenter;
    LoginActivity view;

    public LoginModel(LoginPresenter presenter, LoginActivity view) {
        this.presenter = presenter;
        mAuth = FirebaseAuth.getInstance();
        this.view = view;
    }

    @Override
    public void validarUsuario(String correo, String password) {
        mAuth.signInWithEmailAndPassword(correo, password)
                .addOnCompleteListener(view, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("rc_login", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            presenter.obtenerValidacion(user.getUid());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("rc_login", "signInWithEmail:failure", task.getException());
                            presenter.obtenerValidacion(null);

                        }
                    }
                });
    }
}
