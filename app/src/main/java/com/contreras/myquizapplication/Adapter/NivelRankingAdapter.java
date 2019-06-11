package com.contreras.myquizapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.contreras.myquizapplication.Entity.ItemEntity.NivelCompetidores;
import com.contreras.myquizapplication.Entity.Nivel;
import com.contreras.myquizapplication.R;
import com.contreras.myquizapplication.Util.Constantes;
import com.contreras.myquizapplication.View.RankingActivity;

import java.util.ArrayList;

public class NivelRankingAdapter extends RecyclerView.Adapter<NivelRankingAdapter.NivelRankingAdapterViewHolder> {

    Context context;
    ArrayList<NivelCompetidores> nivelCompetidores;
    int item_nivel_ranking;

    public NivelRankingAdapter(Context context, ArrayList<NivelCompetidores> nivelCompetidores, int item_nivel_ranking) {
        this.context = context;
        this.nivelCompetidores = nivelCompetidores;
        this.item_nivel_ranking = item_nivel_ranking;
    }

    @Override
    public NivelRankingAdapterViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(item_nivel_ranking, parent, false);
        return new NivelRankingAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NivelRankingAdapterViewHolder holder, final int i) {
        final NivelCompetidores nivel = nivelCompetidores.get(i);

        holder.tv_numero_nivel_ranking.setText(nivel.getNumero_nivel()+"");
        holder.tv_numero_competidores.setText(nivel.getTotal()+"");

        holder.contenedor_nivel_ranking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constantes.OBJ_NIVEL_COMPETICION_ACTUAL,nivel);
                Intent i = new Intent(context, RankingActivity.class);
                i.putExtras(bundle);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return nivelCompetidores.size();
    }

    public class NivelRankingAdapterViewHolder extends RecyclerView.ViewHolder{

        TextView tv_numero_nivel_ranking;
        TextView tv_numero_competidores;
        LinearLayout contenedor_nivel_ranking;

        public NivelRankingAdapterViewHolder(View itemView) {
            super(itemView);
            tv_numero_nivel_ranking = itemView.findViewById(R.id.tv_numero_nivel_ranking);
            tv_numero_competidores = itemView.findViewById(R.id.tv_numero_competidores);
            contenedor_nivel_ranking = itemView.findViewById(R.id.contenedor_nivel_ranking);
        }
    }
}
