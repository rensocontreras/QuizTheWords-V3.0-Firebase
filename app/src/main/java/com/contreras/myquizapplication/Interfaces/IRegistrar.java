package com.contreras.myquizapplication.Interfaces;

import com.contreras.myquizapplication.Entity.Usuario;

public class IRegistrar {

    public interface IRegistrarView{
        void enviarUsuario(Usuario usuario);
        void mostrarResultadoCorrecto(String mensaje);
        void mostrarResultadoIncorrecto(String mensaje);
        void mostrarDialogo();
        void ocultarDialog();

        void logout();
    }

    public interface IRegistrarPresenter{
        void solicitarUsuario(Usuario usuario);
        void obtenerResultado(String mensaje,int tipo);

        void solicitarLogout();
    }

    public interface IRegistrarModel{
        void insertarUsuario(Usuario usuario);

        void logoutNow();
    }

}
