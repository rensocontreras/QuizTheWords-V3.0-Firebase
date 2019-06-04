package com.contreras.myquizapplication.Interfaces;

public class ILogin {

    public interface ILoginView{
         void enviarInformacion(String correo, String password);
         void mostrarResultadoCorrecto(String codigoUsuario);
         void mostrarResultadoIncorrecto();
    }

    public interface ILoginPresenter{
         void solicitarValidacion(String correo, String password);
         void obtenerValidacion(String codigoUsuario);
    }

    public interface ILoginModel{
         void validarUsuario(String correo, String password);
    }

}
