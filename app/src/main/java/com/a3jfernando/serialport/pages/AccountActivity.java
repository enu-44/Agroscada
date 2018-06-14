package com.a3jfernando.serialport.pages;

import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.a3jfernando.serialport.R;
import com.a3jfernando.serialport.controllers.LoginController;
import com.a3jfernando.serialport.controllers.UserController;
import com.a3jfernando.serialport.models.Usuario;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountActivity extends AppCompatActivity {

    @BindView(R.id.txtEditNombre)
    TextView txtEditNombre;

    @BindView(R.id.txtEditApellido)
    TextView txtEditApellido;

    @BindView(R.id.txtEditEmail)
    TextView txtEditEmail;

    @BindView(R.id.txtEditPaasword)
    TextView txtEditPaasword;

    ///Conections
    UserController dbconeccion_user;
    long User_Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        setToolbar();
        ButterKnife.bind(this);
        loadInstances();
        accountDates();
    }

    private void loadInstances() {
        this.dbconeccion_user= UserController.getInstance(this);
    }

    //Toolbar
    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)// Habilitar Up Button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    ///Get detail Accoiunt
    private void accountDates() {
        dbconeccion_user.abrirBaseDeDatos();
        Usuario user= dbconeccion_user.getFirstUser();
        if(user!=null){
            User_Id= user.getIdusuario();
            txtEditNombre.setText(user.getNombre_Usuario());
            txtEditApellido.setText(user.getApellido_Usuario());
            txtEditEmail.setText(user.getEmail());
            txtEditPaasword.setText(user.getClave());
        }
        dbconeccion_user.cerrar();
    }


    @OnClick(R.id.btnUpdateAccount)
    public void handleClickSubmit() {
        dbconeccion_user.abrirBaseDeDatos();
        dbconeccion_user.updateUser(User_Id,txtEditNombre.getText().toString(),
                                      txtEditApellido.getText().toString(),
                                        "1",
                                        txtEditPaasword.getText().toString(),
                                        txtEditEmail.getText().toString());
        dbconeccion_user.cerrar();
        finish();
    }



    ///Metodo que permite no recargar la pagina al devolverse
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
            case android.R.id.home:
                // Obtener intent de la actividad padre
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                // Comprobar si DetailActivity no se creó desde CourseActivity
                if (NavUtils.shouldUpRecreateTask(this, upIntent)
                        || this.isTaskRoot()) {

                    // Construir de nuevo la tarea para ligar ambas actividades
                    TaskStackBuilder.create(this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    // Terminar con el método correspondiente para Android 5.x
                    this.finishAfterTransition();
                    return true;
                }

                //Para versiones anterios a 5.x
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    // Terminar con el método correspondiente para Android 5.x
                    onBackPressed();
                    return true;
                }

                // Dejar que el sistema maneje el comportamiento del up button
                return false;
        }
    }
}
