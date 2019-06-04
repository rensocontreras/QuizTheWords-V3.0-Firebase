package com.contreras.myquizapplication.Presenter;

import com.contreras.myquizapplication.Entity.Usuario;
import com.contreras.myquizapplication.Interfaces.IRegistrar;
import com.contreras.myquizapplication.Model.RegistrarModel;
import com.contreras.myquizapplication.View.RegistrarActivity;

public class RegistrarPresenter implements IRegistrar.IRegistrarPresenter {

    private RegistrarActivity view;
    private RegistrarModel model;

    public RegistrarPresenter(RegistrarActivity view){
        this.view = view;
        model = new RegistrarModel(this,view);
    }

    @Override
    public void solicitarUsuario(Usuario usuario) {
        view.mostrarDialogo();
        model.insertarUsuario(usuario);
    }

    @Override
    public void obtenerResultado(String mensaje, int tipo) {
        if(tipo== 1) {
            view.ocultarDialog();
            view.mostrarResultadoCorrecto(mensaje);
        }else {
            view.ocultarDialog();
            view.mostrarResultadoIncorrecto(mensaje);
        }
    }

    @Override
    public void solicitarLogout() {
        model.logoutNow();
    }
}
