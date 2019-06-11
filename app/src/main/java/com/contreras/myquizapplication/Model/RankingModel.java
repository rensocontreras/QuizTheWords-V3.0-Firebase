package com.contreras.myquizapplication.Model;

import android.util.Log;

import com.contreras.myquizapplication.Entity.ItemEntity.ItemCompetidorTop;
import com.contreras.myquizapplication.Entity.Top;
import com.contreras.myquizapplication.Entity.Usuario;
import com.contreras.myquizapplication.Interfaces.IRanking;
import com.contreras.myquizapplication.Presenter.RankingPresenter;
import com.contreras.myquizapplication.View.RankingActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RankingModel implements IRanking.IRankingmodel {

    RankingPresenter presenter;
    RankingActivity view;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    ValueEventListener postListener;
    int num_nivel;

    public RankingModel(RankingPresenter presenter, RankingActivity view){
        this.presenter = presenter;
        this.view = view;

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("BD_QUIZ");
    }


    @Override
    public void consultaListaCompetidoresTop(int num_nivel) {
        this.num_nivel = num_nivel;
        obtenerItemCompetidoresTop(num_nivel);
    }


    public void obtenerItemCompetidoresTop(int num_nivel){

        postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Top> list_top = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){ //El recorrido de todos los nodos
                    Top myTop = snapshot.getValue(Top.class);
                    //Log.i("contrerasKey",snapshot.getKey());
                    list_top.add(myTop);
                }
                obtenerUsuariosTop(list_top);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        databaseReference.child("TOP").child("TOP"+num_nivel).addValueEventListener(postListener);

    }

    private void obtenerUsuariosTop(final ArrayList<Top> list_top) {
        databaseReference.child("USUARIO").addListenerForSingleValueEvent(new ValueEventListener() { //TODO Ascolta solo una volta
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<ItemCompetidorTop> list_competidores = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){ //El recorrido de todos los nodos
                    Usuario myUsuario = snapshot.getValue(Usuario.class);
                    for (Top myTop : list_top) {
                        if(myUsuario.getCodigoUsuario().equals(myTop.getCodigo_usuario())){
                            ItemCompetidorTop myCompetidor = new ItemCompetidorTop();
                            myCompetidor.setNumero_nivel(myTop.getNumero_nivel());
                            myCompetidor.setCodigo_usuario(myTop.getCodigo_usuario());
                            myCompetidor.setNombres(myUsuario.getNombres());
                            myCompetidor.setSexo(myUsuario.getSexo());
                            myCompetidor.setImagen(myUsuario.getImagen());
                            myCompetidor.setMy_timer(myTop.getMy_timer());
                            myCompetidor.setPreguntas_resueltas(myTop.getPreguntas_resueltas());
                            list_competidores.add(myCompetidor);
                        }

                    }
                }

                Collections.sort(list_competidores, new Comparator<ItemCompetidorTop>() {
                    @Override public int compare(ItemCompetidorTop p1, ItemCompetidorTop p2) {
                        if(p1.getPreguntas_resueltas()<p2.getPreguntas_resueltas()) //Decrescente
                            return 1;
                        else if(p1.getPreguntas_resueltas()==p2.getPreguntas_resueltas())
                                  if(p1.getMy_timer()>=p2.getMy_timer())
                                    return 1;
                                  else return -1;
                             else return -1;
                    }

                });

                presenter.obtenerListaCompetidoresTop(list_competidores);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    @Override
    public void eliminarListeners() {
        databaseReference.child("TOP").child("TOP"+num_nivel).removeEventListener(postListener);
        Log.i("Desconectado","listeners"+num_nivel);
    }

}
