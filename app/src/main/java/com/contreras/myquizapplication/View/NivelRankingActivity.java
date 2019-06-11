package com.contreras.myquizapplication.View;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.contreras.myquizapplication.Adapter.NivelRankingAdapter;
import com.contreras.myquizapplication.Entity.Compite;
import com.contreras.myquizapplication.Entity.ItemEntity.NivelCompetidores;
import com.contreras.myquizapplication.Entity.Nivel;
import com.contreras.myquizapplication.Interfaces.INivelRanking;
import com.contreras.myquizapplication.Presenter.NivelRankingPresenter;
import com.contreras.myquizapplication.Presenter.RankingPresenter;
import com.contreras.myquizapplication.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class NivelRankingActivity extends AppCompatActivity implements INivelRanking.INivelRankingView {

    @BindView(R.id.recyler_niveles_ranking)
    RecyclerView recycler_niveles_ranking;

    NivelRankingPresenter presenter;
    NivelRankingAdapter nivelRankingAdapter;
    SweetAlertDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nivel_ranking);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ButterKnife.bind(this);
        presenter = new NivelRankingPresenter(this);
        obtenerCompetidores();
    }


    @Override
    protected void onStop() {
        super.onStop();
        cerrarListeners();
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(NivelRankingActivity.this, HomeActivity.class);
        startActivity(intent);
    }


    @Override
    public void obtenerCompetidores() {
        presenter.solicitarListaCompetidores();
    }

    @Override
    public void mostrarListaCompetidores(ArrayList<NivelCompetidores> nivelCompetidores) {
        nivelRankingAdapter = new NivelRankingAdapter(this,nivelCompetidores,R.layout.item_nivel_ranking);
        recycler_niveles_ranking.setAdapter(nivelRankingAdapter);
        recycler_niveles_ranking.setLayoutManager(new GridLayoutManager(this,2));
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
