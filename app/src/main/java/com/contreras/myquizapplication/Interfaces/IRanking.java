package com.contreras.myquizapplication.Interfaces;

import com.contreras.myquizapplication.Entity.ItemEntity.ItemCompetidorTop;
import java.util.ArrayList;

public class IRanking {

    public interface IRankingView{
        void obtenerCompetidoresTop();
        void mostrarListaCompetidoresTop(ArrayList<ItemCompetidorTop> competidoresTop);
        void mostrarDialogo();
        void ocultarDialog();

        void cerrarListeners();
    }

    public interface IRankingPresenter{
        void solicitarListaCompetidoresTop();
        void obtenerListaCompetidoresTop(ArrayList<ItemCompetidorTop> competidoresTop);

        void solicitarEliminacionListeners();
    }

    public interface IRankingmodel{
        void consultaListaCompetidoresTop();

        void eliminarListeners();
    }
}
