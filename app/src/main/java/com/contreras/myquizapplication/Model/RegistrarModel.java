package com.contreras.myquizapplication.Model;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.contreras.myquizapplication.Entity.Compite;
import com.contreras.myquizapplication.Entity.Nivel;
import com.contreras.myquizapplication.Entity.Usuario;
import com.contreras.myquizapplication.Interfaces.IRegistrar;
import com.contreras.myquizapplication.Presenter.RegistrarPresenter;
import com.contreras.myquizapplication.View.RegistrarActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class RegistrarModel implements IRegistrar.IRegistrarModel {

    RegistrarPresenter presenter;
    RegistrarActivity view;

    //FIREBASE
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    ArrayList<Nivel> niveles;

    public RegistrarModel(RegistrarPresenter presenter, RegistrarActivity view) {
        this.presenter = presenter;
        this.view = view;

        mAuth = FirebaseAuth.getInstance();

        niveles = new ArrayList<>();

    }

    @Override
    public void insertarUsuario(final Usuario usuario) {

        mAuth.createUserWithEmailAndPassword(usuario.getCorreo(), usuario.getPassword())
                .addOnCompleteListener(view, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("rc_create", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(usuario.getNombres()).build();

                            user.updateProfile(profileUpdates);


                            usuario.setCodigoUsuario(user.getUid());

                            updateStorageUsuario(usuario);

                            Log.i("milogin",mAuth.getCurrentUser()+"");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("rc_create", "createUserWithEmail:failure", task.getException());
                            presenter.obtenerResultado("Error, vuelva a intentarlo",2);

                        }

                    }
                });

    }

    public void updateDatabaseUsuario(final Usuario usuario) {

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("BD_QUIZ");

        databaseReference.child("USUARIO").child(usuario.getCodigoUsuario())
                .setValue(usuario).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                insertCompetir(usuario);
                presenter.obtenerResultado("Usuario creado",1);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                presenter.obtenerResultado("Error, vuelva a intentarlo",2);
            }
        });

    }

    public void insertCompetir(final Usuario usuario){

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("BD_QUIZ");

        databaseReference.child("NIVEL").addListenerForSingleValueEvent(new ValueEventListener() { //Listener una vez
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){ //El recorrido de todos los nodos
                    Nivel nivel = snapshot.getValue(Nivel.class);
                    niveles.add(nivel);
                }

                int estado_candado = 1;

                for (Nivel nivel:niveles) {
                    Compite compite = new Compite();
                    compite.setCodigo_usuario(usuario.getCodigoUsuario());
                    compite.setNumero_nivel(nivel.getNumero_nivel());
                    compite.setEstado_candado(estado_candado);
                    compite.setPreguntas_resueltas(0);
                    compite.setMy_timer(100);

                    estado_candado=0;

                    databaseReference.child("COMPITE").child(compite.getCodigo_usuario()).child(compite.getNumero_nivel()+"").setValue(compite);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    private void updateStorageUsuario(Usuario usuario) {
        if(!usuario.getImagen().equals("default")){
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference("imagenes");

            Uri filePath = Uri.parse(usuario.getImagen());

            final StorageReference ref = storageReference.child(usuario.getCodigoUsuario()).child(filePath.getLastPathSegment());
            ref.putFile(filePath);

            usuario.setImagen(usuario.getCodigoUsuario()+"/"+filePath.getLastPathSegment());
        }

        updateDatabaseUsuario(usuario);
    }

    @Override
    public void logoutNow() {
        mAuth.signOut();
    }
}
