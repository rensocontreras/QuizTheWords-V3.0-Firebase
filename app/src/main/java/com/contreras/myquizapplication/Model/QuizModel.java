package com.contreras.myquizapplication.Model;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.contreras.myquizapplication.Entity.Compite;
import com.contreras.myquizapplication.Entity.Nivel;
import com.contreras.myquizapplication.Entity.Pregunta;
import com.contreras.myquizapplication.Entity.Top;
import com.contreras.myquizapplication.Interfaces.IQuiz;
import com.contreras.myquizapplication.Presenter.QuizPresenter;

import com.contreras.myquizapplication.View.QuizActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class QuizModel implements IQuiz.IQuizModel {

    private QuizPresenter presenter;

    Nivel nivelActual;
    Compite competicionActual;
    String codigo;


    ArrayList<Pregunta> list_preguntas;
    int posicionCorriente;
    int preguntasResueltas;


    int numSecond=60;
    TimerTask task;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public QuizModel(QuizPresenter presenter, QuizActivity view){
        this.presenter = presenter;
        posicionCorriente = 0;
        preguntasResueltas = 0;

        list_preguntas = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("BD_QUIZ"); //Primer nodo
    }


    @Override
    public void actualizarConfiguracionQuiz(Nivel nivelActual, Compite competicionActual, String codigo) {
        this.nivelActual = nivelActual;
        this.competicionActual = competicionActual;
        this.codigo = codigo;

        //Start timer
        myTimerStart();

    }

    @Override
    public void elaborarQuiz() {
        obtenerPreguntas(nivelActual.getNumero_nivel(), new MyCallbackPregunta() {
            @Override
            public void onCallback(ArrayList<Pregunta> list_preguntas) {
                Pregunta myPregunta = (Pregunta) list_preguntas.get(posicionCorriente);
                myPregunta.setNumero_pregunta(posicionCorriente+1); //TODO, nuevo update
                presenter.obtenerResultadoQuiz(mesclarOpciones(myPregunta));
            }
        });

    }

    public Pregunta mesclarOpciones(Pregunta myPregunta){
        ArrayList<String> list_opciones = new ArrayList<>();
        list_opciones.add(myPregunta.getOpcion1());
        list_opciones.add(myPregunta.getOpcion2());
        list_opciones.add(myPregunta.getOpcion3());
        list_opciones.add(myPregunta.getOpcion4());

        Collections.shuffle(list_opciones);
        myPregunta.setOpcion1(list_opciones.get(0));
        myPregunta.setOpcion2(list_opciones.get(1));
        myPregunta.setOpcion3(list_opciones.get(2));
        myPregunta.setOpcion4(list_opciones.get(3));

        return myPregunta;
    }

    @Override
    public void analisaRespuesta(String opcionSeleccionada) {
        if(opcionSeleccionada.equals(list_preguntas.get(posicionCorriente).getRespuesta())){
            //Actualizar el numero de Preguntas del objeto nivel actual
            preguntasResueltas++;
            presenter.obtenerRespuesta(1);
        }else{
            presenter.obtenerRespuesta(2);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if((posicionCorriente+1)<list_preguntas.size()){
                    //Actualizo position dell'arrayPreguntas y seteo il Quiz
                    posicionCorriente++;
                    Pregunta myPregunta = (Pregunta) list_preguntas.get(posicionCorriente);
                    myPregunta.setNumero_pregunta(posicionCorriente+1);//TODO, nuevo update
                    presenter.obtenerResultadoQuiz(mesclarOpciones(myPregunta));
                }else {
                    controlFinalRespuesta();
                }
            }
        },600);
    }




    public void controlFinalRespuesta(){
        if (preguntasResueltas >= nivelActual.getLimite_preguntas()) {
            //Toast.makeText(this, "Nivel completado", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    presenter.obtenerCalificacion(1);
                }
            }, 700);


            existenciaNivelSiguiente(nivelActual.getNumero_nivel() + 1,new MyCallbackExiste() {
                @Override
                public void onCallback(boolean existe) {
                    if(existe) {
                        //TODO, ARREGLAR NIVEL SIGUIENTE
                        actualizarCompeticionSiguiente(competicionActual.getCodigo_usuario(), nivelActual.getNumero_nivel() + 1, 1);//Nivel siguiente unlock
                    } else {
                        presenter.obtenerRespuesta(3);
                    }
                }
            });

        } else {
            //Toast.makeText(this, "Intentalo otra vez!", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    presenter.obtenerCalificacion(2);
                }
            }, 700);
        }

        Log.i("AQUIRC","aqui");
        if (preguntasResueltas >= competicionActual.getPreguntas_resueltas()) {
            competicionActual.setPreguntas_resueltas(preguntasResueltas);
            if (competicionActual.getMy_timer() >= (60 - numSecond)) {
                competicionActual.setMy_timer(60 - numSecond);
                actualizarTimer(competicionActual.getCodigo_usuario(), nivelActual.getNumero_nivel(), competicionActual.getMy_timer());

            }
        }

        actualizarTop();

        actualizarCompeticionActual(competicionActual.getCodigo_usuario(), nivelActual.getNumero_nivel(), competicionActual.getPreguntas_resueltas());

        anularTimer();
    }



    public void controlFinalRespuestaTimeOut(){
        if (preguntasResueltas >= nivelActual.getLimite_preguntas()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    presenter.obtenerCalificacion(3);

                }
            }, 700);




            existenciaNivelSiguiente(nivelActual.getNumero_nivel() + 1,new MyCallbackExiste() {
                @Override
                public void onCallback(boolean existe) {
                    if(existe) {
                        //TODO, ARREGLAR NIVEL SIGUIENTE
                        actualizarCompeticionSiguiente(competicionActual.getCodigo_usuario(), nivelActual.getNumero_nivel() + 1, 1);//Nivel siguiente unlock
                    }
                }
            });

        } else {
            try {
                Thread.sleep(700);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
                    presenter.obtenerCalificacion(4);

        }

        if (preguntasResueltas >= competicionActual.getPreguntas_resueltas()) {
            competicionActual.setPreguntas_resueltas(preguntasResueltas);
            if (competicionActual.getMy_timer() >= (60 - numSecond)) {
                competicionActual.setMy_timer(60 - numSecond);
                Log.i("COMPITETIME1",competicionActual.getMy_timer() +"");
                actualizarTimer(competicionActual.getCodigo_usuario(), nivelActual.getNumero_nivel(), competicionActual.getMy_timer());
            }
        }

        actualizarTop();

        actualizarCompeticionActual(competicionActual.getCodigo_usuario(), nivelActual.getNumero_nivel(), competicionActual.getPreguntas_resueltas());

        anularTimer();
    }


    private void myTimerStart() {
        final Timer timer = new Timer();
        task = new TimerTask() {
            public void run() {
                        presenter.obtenerNumeroSegundos(numSecond);
                        numSecond--;
                        if (numSecond == -1) {

                            try {
                                Thread.sleep(700);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            numSecond=0;
                            controlFinalRespuestaTimeOut();


                            timer.cancel();
                        }
            }

        };
        timer.scheduleAtFixedRate(task, 0, 1000);
    }


    @Override
    public void anularTimer() {
        task.cancel();
    }



    private void actualizarTop() {

        final Top nuevoCompetidorTop = new Top();
        nuevoCompetidorTop.setNumero_nivel(nivelActual.getNumero_nivel());
        nuevoCompetidorTop.setCodigo_usuario(codigo+"");
        nuevoCompetidorTop.setMy_timer(competicionActual.getMy_timer());
        nuevoCompetidorTop.setPreguntas_resueltas(competicionActual.getPreguntas_resueltas());

        obtenerCompetidorTop(nivelActual.getNumero_nivel(), new MyCallbackExisteTop() {
            @Override
            public void onCallback(Top mycompetidorTop) {

                if(mycompetidorTop!=null){
                    if(mycompetidorTop.getPreguntas_resueltas()<competicionActual.getPreguntas_resueltas()) {
                            actualizarCompetidorTop(nuevoCompetidorTop);
                    }else if(mycompetidorTop.getPreguntas_resueltas() == competicionActual.getPreguntas_resueltas()){
                                if (mycompetidorTop.getMy_timer() >= competicionActual.getMy_timer()) {
                                    actualizarCompetidorTop(nuevoCompetidorTop);
                                }
                    }
                }else{
                    insertarCompetidor(nuevoCompetidorTop);
                }
            }
        });
    }



    /**************************************FIREBASE********************************************************/

    public interface MyCallbackPregunta {
        void onCallback(ArrayList<Pregunta> preguntas);
    }

    public void obtenerPreguntas(int nivelActual, final MyCallbackPregunta myCallbackPregunta){
        databaseReference.child("PREGUNTA").child(nivelActual+"").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){ //El recorrido de todos los nodos
                    Pregunta myPregunta = snapshot.getValue(Pregunta.class);
                    list_preguntas.add(myPregunta);
                }
                Collections.shuffle(list_preguntas);
                myCallbackPregunta.onCallback(list_preguntas);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    public interface MyCallbackExiste {
        void onCallback(boolean existe);
    }

    public void existenciaNivelSiguiente(final int nivelActual, final MyCallbackExiste myCallbackExiste) {
        databaseReference.child("NIVEL").child(nivelActual+"").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                    myCallbackExiste.onCallback(true);
                else
                    myCallbackExiste.onCallback(false);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }




    public void actualizarCompeticionSiguiente(String codigoUsuario, int numeroNivel, int stadoCandado){
        Map<String,Object> actualizacion = new HashMap<>();
        actualizacion.put("/estado_candado",stadoCandado);

        databaseReference.child("COMPITE").child(codigoUsuario).child(numeroNivel+"").updateChildren(actualizacion);
    }


    public void actualizarTimer(String codigoUsuario,int numeroNivel,int numTimer){
        Map<String,Object> actualizacion = new HashMap<>();
        actualizacion.put("/my_timer",numTimer);
        Log.i("COMPITETIME",numTimer+"");
        databaseReference.child("COMPITE").child(codigoUsuario).child(numeroNivel+"").updateChildren(actualizacion);
    }

    public void actualizarCompeticionActual(String codigoUsuario,int numeroNivel, int misPreguntasResueltas){

        Map<String,Object> actualizacion = new HashMap<>();
        actualizacion.put("/preguntas_resueltas",misPreguntasResueltas);

        databaseReference.child("COMPITE").child(codigoUsuario).child(numeroNivel+"").updateChildren(actualizacion);
    }



    public void insertarCompetidor(Top competidorTop){

        //Nuevo child, el push() me mette la chiave unica
        databaseReference.child("TOP").child(competidorTop.getNumero_nivel()+"").setValue(competidorTop).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Toast.makeText(DatabaseActivity.this, "Ok", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
               // Toast.makeText(DatabaseActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public interface MyCallbackExisteTop {
        void onCallback(Top mycompetidorTop);
    }


    public void obtenerCompetidorTop(int myNumeroNivel,final MyCallbackExisteTop myCallbackExisteTop ){
        databaseReference.child("TOP").child(myNumeroNivel+"").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                    myCallbackExisteTop.onCallback(dataSnapshot.getValue(Top.class));
                else
                    myCallbackExisteTop.onCallback(null);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void actualizarCompetidorTop(Top competidorTop){
        Map<String,Object> actualizacion = new HashMap<>();
        actualizacion.put("/codigo_usuario",competidorTop.getCodigo_usuario());
        actualizacion.put("/my_timer",competidorTop.getMy_timer());
        actualizacion.put("/preguntas_resueltas",competidorTop.getPreguntas_resueltas());

        databaseReference.child("TOP").child(competidorTop.getNumero_nivel()+"").updateChildren(actualizacion);
    }

}
