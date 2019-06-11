package com.contreras.myquizapplication.Presenter;

import com.contreras.myquizapplication.Entity.ItemEntity.ItemCompetidorTop;
import com.contreras.myquizapplication.Interfaces.IRanking;
import com.contreras.myquizapplication.Model.RankingModel;
import com.contreras.myquizapplication.View.RankingActivity;
import java.util.ArrayList;


public class RankingPresenter implements IRanking.IRankingPresenter {

    RankingActivity view;
    RankingModel model;

    public RankingPresenter(RankingActivity view) {
        this.view = view;
        model = new RankingModel(this,view);
    }


    @Override
    public void solicitarListaCompetidoresTop(int num_nivel) {
        view.mostrarDialogo();
        model.consultaListaCompetidoresTop(num_nivel);
    }

    @Override
    public void obtenerListaCompetidoresTop(ArrayList<ItemCompetidorTop> competidoresTop) {
        view.mostrarListaCompetidoresTop(competidoresTop);
        view.ocultarDialog();
    }

    @Override
    public void solicitarEliminacionListeners() {
        model.eliminarListeners();
    }
}
