package com.contreras.myquizapplication.View;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.contreras.myquizapplication.Adapter.CompetidorAdapter;
import com.contreras.myquizapplication.Entity.ItemEntity.ItemCompetidorTop;
import com.contreras.myquizapplication.Entity.ItemEntity.NivelCompetidores;
import com.contreras.myquizapplication.Entity.Nivel;
import com.contreras.myquizapplication.Interfaces.IRanking;
import com.contreras.myquizapplication.Presenter.RankingPresenter;
import com.contreras.myquizapplication.R;
import com.contreras.myquizapplication.Util.Constantes;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class RankingActivity extends AppCompatActivity implements IRanking.IRankingView {

    @BindView(R.id.recycler_competidoresTop)
    RecyclerView recycler_competidoresTop;

    RankingPresenter presenter;
    CompetidorAdapter competidorAdapter;
    SweetAlertDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ButterKnife.bind(this);
        presenter = new RankingPresenter(this);
        Bundle bundle = getIntent().getExtras();
        NivelCompetidores nivelActual = (NivelCompetidores) bundle.getSerializable(Constantes.OBJ_NIVEL_COMPETICION_ACTUAL);
        obtenerCompetidoresTop(nivelActual.getNumero_nivel());
    }

    @Override
    protected void onStop() {
        super.onStop();
        cerrarListeners();
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(RankingActivity.this, NivelRankingActivity.class);
        startActivity(intent);
    }

    @Override
    public void obtenerCompetidoresTop(int num_nivel) {
        presenter.solicitarListaCompetidoresTop(num_nivel);
    }

    @Override
    public void mostrarListaCompetidoresTop(ArrayList<ItemCompetidorTop> competidoresTop) {
        competidorAdapter = new CompetidorAdapter(this,competidoresTop,R.layout.item_competidor_top);
        recycler_competidoresTop.setAdapter(competidorAdapter);
        recycler_competidoresTop.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void mostrarDialogo() {
        pd = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pd.getProgressHelper().setBarColor(Color.parseColor("#303F9F"));
        pd.setCancelable(false);
        pd.setContentText("Cargando...");
        pd.show();
    }

    @Override
    public void ocultarDialog() {
        pd.dismiss();
    }

    @Override
    public void cerrarListeners() {
        presenter.solicitarEliminacionListeners();
    }

}
