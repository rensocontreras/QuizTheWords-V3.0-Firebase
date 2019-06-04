package com.contreras.myquizapplication.Interfaces;

public class IHome {

    public interface IHomeView {
        void salir();
        void mostrarLogout();
        void obtenerNombreUsuario();
        void mostrarNameUser(String name);
    }

    public interface IHomePresenter{
        void solicitarLogout();
        void obtenerRespuesta();
        void solicitarNameUser();
        void recibeNameUser(String name);
    }

    public interface IHomeModel{
        void executeLogout();
        void buscaNombreUsuario();
    }

}
