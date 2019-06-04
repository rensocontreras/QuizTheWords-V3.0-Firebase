package com.contreras.myquizapplication.Model;

import com.contreras.myquizapplication.Entity.Compite;
import com.contreras.myquizapplication.Entity.Nivel;
import com.contreras.myquizapplication.Interfaces.IJugar;
import com.contreras.myquizapplication.Presenter.JugarPresenter;
import com.contreras.myquizapplication.View.JugarActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class JugarModel implements IJugar.IJugarModel {

    JugarPresenter presenter;
    ArrayList<Nivel> list_niveles;
    ArrayList<Compite> list_competiciones;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public JugarModel(JugarPresenter presenter, JugarActivity view) {
        this.presenter = presenter;

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("BD_QUIZ"); //Primer nodo

        list_niveles = new ArrayList<>();
        list_competiciones = new ArrayList<>();
    }



    @Override
    public void consultaListaNiveles(String codigoUsuario) {
        obtenerNiveles(codigoUsuario);
    }


    public void obtenerNiveles(final String codigoUsuario){

        databaseReference.child("NIVEL").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){ //El recorrido de todos los nodos
                    Nivel nivel = snapshot.getValue(Nivel.class);
                    list_niveles.add(nivel);
                }

                    databaseReference.child("COMPITE").child(codigoUsuario).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()){ //El recorrido de todos los nodos
                                Compite miCompeticion = snapshot.getValue(Compite.class);

                                if(miCompeticion.getCodigo_usuario().equals(codigoUsuario))
                                    list_competiciones.add(miCompeticion);
                            }
                            presenter.obtenerListaNiveles(list_niveles,list_competiciones);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }









}
