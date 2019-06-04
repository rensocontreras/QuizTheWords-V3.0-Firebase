package com.contreras.myquizapplication.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.contreras.myquizapplication.Entity.Compite;
import com.contreras.myquizapplication.Entity.Nivel;
import com.contreras.myquizapplication.Entity.Pregunta;
import com.contreras.myquizapplication.Interfaces.IQuiz;
import com.contreras.myquizapplication.Presenter.QuizPresenter;
import com.contreras.myquizapplication.R;
import com.contreras.myquizapplication.Resultados.BadActivity;
import com.contreras.myquizapplication.Resultados.GoodActivity;
import com.contreras.myquizapplication.Resultados.TimeOutBadActivity;
import com.contreras.myquizapplication.Resultados.TimeOutGoodActivity;
import com.contreras.myquizapplication.Util.Constantes;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class QuizActivity extends AppCompatActivity implements IQuiz.IQuizView {

    @BindView(R.id.tv_numDePregunta)
    TextView tv_numDePregunta;

    @BindView(R.id.tv_numeroNivel)
    TextView tv_numeroNivel;

    @BindView(R.id.tv_time)
    TextView tv_time;

    @BindView(R.id.tv_contenidoPregunta)
    TextView tv_contenidoPregunta;

    @BindView(R.id.btn_option1)
    Button btn_option1;

    @BindView(R.id.btn_option2)
    Button btn_option2;

    @BindView(R.id.btn_option3)
    Button btn_option3;

    @BindView(R.id.btn_option4)
    Button btn_option4;


    QuizPresenter presenter;
    SweetAlertDialog pd;
    LinearLayout mLayout;
    MediaPlayer sound_right;
    MediaPlayer sound_error;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mostrarDialogo();
        setContentView(R.layout.activity_quiz);
        mLayout = (LinearLayout) findViewById(R.id.contenedor_animado);
        mLayout.setVisibility(View.GONE);

        ButterKnife.bind(this);


        sound_right = MediaPlayer.create(this, R.raw.sound_right);
        sound_error = MediaPlayer.create(this, R.raw.sound_error);

        presenter = new QuizPresenter(this);

        enviarParametrosQuiz();

        //SET LAYOUT
        obtenerQuiz();

    }


    @Override
    public void enviarParametrosQuiz() {
        Bundle bundle = getIntent().getExtras();

        Nivel nivelActual = (Nivel) bundle.getSerializable(Constantes.OBJ_NIVEL_ACTUAL);
        Compite competicionActual = (Compite) bundle.getSerializable(Constantes.OBJ_COMPETICION_ACTUAL);

        SharedPreferences pref = getSharedPreferences(Constantes.PREFERENCIA_USUARIO,0);
        String codigo = pref.getString("codigo_usuario","-1");

        presenter.solicitaParametrosQuiz(nivelActual,competicionActual,codigo);
    }


    @Override
    public void obtenerQuiz() {
        presenter.solicitarQuiz();
    }

    @Override
    public void mostrarQuiz(Pregunta pregunta) {
        tv_numDePregunta.setText(pregunta.getNumero_pregunta()+"");
        tv_numeroNivel.setText(pregunta.getNumero_nivel()+"");
        tv_contenidoPregunta.setText(pregunta.getContenido_pregunta());
        btn_option1.setText(pregunta.getOpcion1());
        btn_option2.setText(pregunta.getOpcion2());
        btn_option3.setText(pregunta.getOpcion3());
        btn_option4.setText(pregunta.getOpcion4());
    }

    @Override
    public void obtenerResultado(String opcionSeleccionada) {
        presenter.solicitarRespuesta(opcionSeleccionada);
    }

    @Override
    public void mostrarRespuestaCorrecta() {
        sound_right.start();
    }

    @Override
    public void mostrarRespuestaIncorrecta() {
        sound_error.start();
    }

    @Override
    public void mostrarRespuestaDefinifiva() {
        Toast.makeText(getApplicationContext(), "Todos los niveles han sido completados", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void mostrarGodActivity() {
        Intent i = new Intent(QuizActivity.this, GoodActivity.class);
        startActivity(i);
    }

    @Override
    public void mostrarBadActivity() {
        Intent i = new Intent(QuizActivity.this, BadActivity.class);
        startActivity(i);
    }

    @Override
    public void mostrarTimeOutGoodActivity() {
        Intent i = new Intent(QuizActivity.this, TimeOutGoodActivity.class);
        startActivity(i);
    }

    @Override
    public void mostrarTimeOutBadActivity() {
        Intent i = new Intent(QuizActivity.this, TimeOutBadActivity.class);
        startActivity(i);
    }

    @Override
    public void mostrarTimer(final int numeroSecond) {
        runOnUiThread(new Runnable() {
            public void run() {
                tv_time.setText(numeroSecond + "");
            }
        });
    }

    @Override
    public void salir() {
        presenter.solicitarCancelarTimer();
    }


    @OnClick({R.id.btn_option1,R.id.btn_option2,R.id.btn_option3,R.id.btn_option4})
    public void onButtonClick(View v) {
        String myRespuesta = "";
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.myanimation);
        tv_contenidoPregunta.startAnimation(animation);

        switch (v.getId()) {
            case R.id.btn_option1:  myRespuesta = btn_option1.getText().toString();
                                    break;
            case R.id.btn_option2:  myRespuesta = btn_option2.getText().toString();
                                    break;
            case R.id.btn_option3:  myRespuesta = btn_option3.getText().toString();
                                    break;
            case R.id.btn_option4:  myRespuesta = btn_option4.getText().toString();
                                    break;
        }
        obtenerResultado(myRespuesta);
    }


    @Override
    public void onBackPressed() {
        salir();
        Intent intent = new Intent(QuizActivity.this, JugarActivity.class); //TODO, CREAR UN PROPIO ACTIVITY
        finish();
        startActivity(intent);
    }

    @Override
    public void mostrarDialogo() {
        pd = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pd.getProgressHelper().setBarColor(Color.parseColor("#303F9F"));
        pd.setContentText("Comenzando...");
        pd.setCancelable(false);
        pd.show();
    }

    @Override
    public void ocultarDialog() {
        mLayout.setVisibility(View.VISIBLE);
        pd.dismiss();
    }

}