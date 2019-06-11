package com.contreras.myquizapplication.Interfaces;

import com.contreras.myquizapplication.Entity.Compite;
import com.contreras.myquizapplication.Entity.ItemEntity.NivelCompetidores;
import com.contreras.myquizapplication.Entity.Nivel;

import java.util.ArrayList;

public class INivelRanking {

    public interface INivelRankingView{
        void obtenerCompetidores();
        void mostrarListaCompetidores(ArrayList<NivelCompetidores> nivelCompetidores);
        void mostrarDialogo();
        void ocultarDialog();

        void cerrarListeners();
    }

    public interface INivelRankingPresenter{
        void solicitarListaCompetidores();
        void obtenerListaCompetidores(ArrayList<NivelCompetidores> nivelCompetidores);

        void solicitarEliminacionListeners();
    }

    public interface INivelRankingmodel{
        void consultaListaCompetidores();

        void eliminarListeners();
    }
}
