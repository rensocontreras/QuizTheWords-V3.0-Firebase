package com.contreras.myquizapplication.Model;


import android.util.Log;

import com.contreras.myquizapplication.Entity.Compite;
import com.contreras.myquizapplication.Entity.ItemEntity.NivelCompetidores;
import com.contreras.myquizapplication.Entity.Nivel;
import com.contreras.myquizapplication.Entity.Usuario;
import com.contreras.myquizapplication.Interfaces.INivelRanking;
import com.contreras.myquizapplication.Presenter.NivelRankingPresenter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NivelRankingModel implements INivelRanking.INivelRankingmodel {

    NivelRankingPresenter presenter;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    ValueEventListener postListener;


    public NivelRankingModel(NivelRankingPresenter presenter) {
        this.presenter = presenter;

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("BD_QUIZ");
    }

    @Override
    public void consultaListaCompetidores() {
        obtenerNivelesRanking();
    }

    private void obtenerNivelesRanking() {
        postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<NivelCompetidores> list_nivelesCompetidores = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){ //El recorrido de todos los nodos
                    Nivel myNivel = snapshot.getValue(Nivel.class);

                    NivelCompetidores nivelCompetidores = new NivelCompetidores();
                    nivelCompetidores.setNumero_nivel(myNivel.getNumero_nivel());
                    list_nivelesCompetidores.add(nivelCompetidores);
                }
                obtenerListaUsuarios(list_nivelesCompetidores);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        databaseReference.child("NIVEL").addValueEventListener(postListener);
    }


    private void obtenerListaUsuarios(final ArrayList<NivelCompetidores> list_nivelesCompetidores) {
        databaseReference.child("USUARIO").addListenerForSingleValueEvent(new ValueEventListener() { //TODO Ascolta solo una volta
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> list_codigos_usuarios = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){ //El recorrido de todos los nodos
                    Usuario myUsuario = snapshot.getValue(Usuario.class);
                    list_codigos_usuarios.add(myUsuario.getCodigoUsuario());
                }
                obtenerTotalCompetidoresNivel(list_nivelesCompetidores,list_codigos_usuarios);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }


    private void obtenerTotalCompetidoresNivel(final ArrayList<NivelCompetidores> list_nivelesCompetidores, final ArrayList<String> list_codigos_usuarios) {

        for(final String codigo: list_codigos_usuarios) {
            databaseReference.child("COMPITE").child(codigo).addListenerForSingleValueEvent(new ValueEventListener() { //TODO Ascolta solo una volta
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) { //El recorrido de todos los nodos
                        Compite myCompetidor = snapshot.getValue(Compite.class);

                        for (final NivelCompetidores nivel : list_nivelesCompetidores) {
                            if (myCompetidor.getMy_timer() != 100) {
                                if (myCompetidor.getNumero_nivel() == nivel.getNumero_nivel()) {
                                    nivel.incrementTotal();
                                    break;
                                }
                            }
                        }
                    }

                    if(codigo.equals(list_codigos_usuarios.get(list_codigos_usuarios.size()-1)))
                        presenter.obtenerListaCompetidores(list_nivelesCompetidores);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }


    @Override
    public void eliminarListeners() {
        databaseReference.child("NIVEL").removeEventListener(postListener);
        Log.i("Desconectado","listeners");
    }
}
