package com.contreras.myquizapplication.Interfaces;

import com.contreras.myquizapplication.Entity.Compite;
import com.contreras.myquizapplication.Entity.Nivel;

import java.util.ArrayList;

public class IJugar {

    public interface IJugarView{
        void obtenerNiveles(String codigoUsuario);
        void mostrarListaNiveles(ArrayList<Nivel> niveles, ArrayList<Compite> competiciones);
        void ocultarDialog();
        void mostrarDialogo();
    }

    public interface IJugarPresenter{
        void solicitarNiveles(String codigoUsuario);
        void obtenerListaNiveles(ArrayList<Nivel> niveles, ArrayList<Compite> competiciones);
    }

    public interface IJugarModel{
        void consultaListaNiveles(String codigoUsuario);
    }
}
