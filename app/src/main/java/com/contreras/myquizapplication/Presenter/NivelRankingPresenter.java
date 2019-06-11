package com.contreras.myquizapplication.Presenter;

import com.contreras.myquizapplication.Entity.Compite;
import com.contreras.myquizapplication.Entity.ItemEntity.NivelCompetidores;
import com.contreras.myquizapplication.Entity.Nivel;
import com.contreras.myquizapplication.Interfaces.INivelRanking;
import com.contreras.myquizapplication.Model.NivelRankingModel;
import com.contreras.myquizapplication.View.NivelRankingActivity;

import java.util.ArrayList;

public class NivelRankingPresenter implements INivelRanking.INivelRankingPresenter {

    NivelRankingModel model;
    NivelRankingActivity view;

    public NivelRankingPresenter(NivelRankingActivity view) {
        model = new NivelRankingModel(this);
        this.view = view;
    }

    @Override
    public void solicitarListaCompetidores() {
        view.mostrarDialogo();
        model.consultaListaCompetidores();
    }

    @Override
    public void obtenerListaCompetidores(ArrayList<NivelCompetidores> nivelCompetidores) {
        view.mostrarListaCompetidores(nivelCompetidores);
        view.ocultarDialog();
    }

    @Override
    public void solicitarEliminacionListeners() {
        model.eliminarListeners();
    }
}
