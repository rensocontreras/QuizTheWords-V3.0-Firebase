package com.contreras.myquizapplication.Model;

import com.contreras.myquizapplication.Interfaces.IHome;
import com.contreras.myquizapplication.Presenter.HomePresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeModel implements IHome.IHomeModel {

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    HomePresenter presenter;

    public HomeModel(HomePresenter presenter) {
        this.presenter = presenter;
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
    }

    @Override
    public void executeLogout() {
        //Log.i("LOGOUT_NO",mAuth.getUid()+"");
        mAuth.signOut();
        //Log.i("LOGOUT_SI",mAuth.getUid()+"");
        presenter.obtenerRespuesta();
    }

    @Override
    public void buscaNombreUsuario() {
        //String[] parts = currentUser.getDisplayName().split("@");

        presenter.recibeNameUser(currentUser.getDisplayName());
    }
}
