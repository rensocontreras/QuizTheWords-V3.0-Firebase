package com.contreras.myquizapplication.View;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.contreras.myquizapplication.Entity.Usuario;
import com.contreras.myquizapplication.Interfaces.IRegistrar;
import com.contreras.myquizapplication.MainActivity;
import com.contreras.myquizapplication.Presenter.RegistrarPresenter;
import com.contreras.myquizapplication.R;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Required;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class RegistrarActivity extends AppCompatActivity implements Validator.ValidationListener, IRegistrar.IRegistrarView {

    @BindView(R.id.edt_nombre)
    @Required(order = 1, message = "Debe ingresar su nombre")
    EditText edt_nombre;

    @BindView(R.id.edt_apellido)
    @Required(order = 2, message = "Debe ingresar su apellido")
    EditText edt_apellido;

    @BindView(R.id.edt_correo)
    @Required(order=3,message = "Debe ingresar correo")
    @Email(order = 4,message = "Correo incorrecto")
    EditText edt_correo;

    @BindView(R.id.edt_password)
    @Password(order = 5,message = "Debe ingresar su contraseña")
    EditText edt_password;

    @BindView(R.id.btn_foto)
    Button btn_foto;

    @BindView(R.id.rb_masculino)
    RadioButton rb_masculino;

    @BindView(R.id.rb_femenino)
    RadioButton rb_femenino;

    @BindView(R.id.tv_foto)
    TextView tv_foto;

    RegistrarPresenter presenter;
    Validator validator;
    SweetAlertDialog pd;
    Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        ButterKnife.bind(this);

        presenter = new RegistrarPresenter(this);

        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @OnClick(R.id.btn_registrar)
    public void registrar(){
      validator.validate();
    }

    @Override
    public void onValidationSucceeded() {

        String sexo;

        Usuario usuario = new Usuario();
        usuario.setNombres(edt_nombre.getText().toString());
        usuario.setApellidos(edt_apellido.getText().toString());
        usuario.setCorreo(edt_correo.getText().toString());
        usuario.setPassword(edt_password.getText().toString());

        if(rb_masculino.isChecked())
            sexo="masculino";
        else
            sexo="femenino";

        if(filePath!=null)
            usuario.setImagen(filePath.toString());
        else
            usuario.setImagen("default");

        usuario.setSexo(sexo);

        enviarUsuario(usuario);

    }

    @Override
    public void onValidationFailed(View failedView, Rule<?> failedRule) {
        final String failureMessage = failedRule.getFailureMessage(); //Para capturar el error del saripaar
        if (failedView instanceof EditText) {
            EditText failed = (EditText) failedView;
            failed.requestFocus(); //Para que il touch si posizioni subito nella cassella in cui c'è stato l'errore
            failed.setError(failureMessage); //Para q aparezca el error
        } else {
            Toast.makeText(this, failureMessage, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void enviarUsuario(Usuario usuario) {
        presenter.solicitarUsuario(usuario);
    }

    @Override
    public void mostrarDialogo() {
        pd = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pd.getProgressHelper().setBarColor(Color.parseColor("#303F9F"));
        pd.setContentText("Creando...");
        pd.show();
    }

    @Override
    public void mostrarResultadoCorrecto(String mensaje) {
        pd = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
        pd.setContentText(mensaje);
        pd.getProgressHelper().setBarColor(Color.parseColor("#303F9F"));
        pd.setCancelable(false);
        pd.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                logout();
                sDialog.dismissWithAnimation();
                Intent i = new Intent(RegistrarActivity.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });
        pd.show();

        Button btn = (Button) pd.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(ContextCompat.getColor(RegistrarActivity.this,R.color.colorAlertSuccess));
    }

    @Override
    public void logout() {
        presenter.solicitarLogout();
    }

    @Override
    public void mostrarResultadoIncorrecto(String mensaje) {
        pd = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
        pd.getProgressHelper().setBarColor(Color.parseColor("#303F9F"));
        pd.setContentText(mensaje);
        pd.show();
    }

    @Override
    public void ocultarDialog() {
        pd.dismiss();
    }

    @OnClick(R.id.btn_foto)
    public void elegirFoto(){
        Intent intent = new Intent();
        intent.setType("image/*");  //Para filtrar solo cosas de tipo image, cioè immagini

        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Seleccionar Imagen"),1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1000 && resultCode == RESULT_OK && data.getData() != null){
            filePath = data.getData();
            tv_foto.setText("Foto seleccionada!");
        }

    }

}
