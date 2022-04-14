package segovia.gil.petrescuer;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityInicioSesion extends AppCompatActivity {

    public static SQLiteHelper sqLiteHelper;
    final int REQUEST_CODE_GALLERY = 999;
    Button btLogin;
    EditText etUser;
    EditText etPass;
    TextView tvRegistro, tvAyuda;
    ImageView imEmail;
    SQLiteHelper helper;
    Cursor cursor;
    String numeroUsuarioMascota;

    /**
     * Método onCreate, enlaza e inicializa los widgets del Activity (pantalla), se programa el comportamiento y función de los distintos botones que reaccionarán
     * a los clicks, se recuperan datos de la anterior pantalla (Registro) y que se utilizarán en los campos de login.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);
        btLogin = findViewById(R.id.btnLogin);
        etUser = findViewById(R.id.etUser);
        etPass = findViewById(R.id.etPass);
        tvRegistro = findViewById(R.id.tvRegistro);
        tvAyuda = findViewById(R.id.tvAyuda);
        imEmail = findViewById(R.id.imEmail);
        getAndSetIntentData();
        helper = new SQLiteHelper(this, "Mascotas.sqlite", null, 1);

        // Se abrirá la pantalla de registro si el usuario desea registrarse.

        tvRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ActivityInicioSesion.this, ActivityRegistro.class);
                startActivity(intent);
            }
        });


        // Se comprueba que el usuario y la contraseña existen y coinciden en la base de datos.
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    cursor = helper.getData("SELECT * FROM USUARIOS WHERE Nombre = " + "'" + etUser.getText().toString() + "'" + " and Password = " + "'" + etPass.getText().toString() + "'");

                    if (cursor != null && cursor.getCount() > 0) {
                        int telefono = 0;

                        //Toast.makeText(ActivityInicioSesion.this, "Inicio sesión Correcto", Toast.LENGTH_SHORT).show();

                        if (cursor.moveToFirst()) {
                            do {
                                telefono = cursor.getInt(0);

                            } while (cursor.moveToNext());
                            cursor.close();
                        }

                        // Se envía a la siguiente pantalla el nombre de usuario y su teléfono para realizar operaciones.
                        Intent intent = new Intent(ActivityInicioSesion.this, ActivityPetList.class);
                        intent.putExtra("nombreUsuario", etUser.getText().toString());
                        intent.putExtra("numeroUsuarioMascota", String.valueOf(telefono));
                        Toast.makeText(ActivityInicioSesion.this, "Acceso correcto.", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(ActivityInicioSesion.this, "Usuario o contraseñas incorrectos.", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Toast.makeText(ActivityInicioSesion.this, "Error: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                }
            }
        });

        /*
         * Se abre una pantalla donde el usuario puede enviar desde su correo un email solicitando ayuda.
         */
        tvAyuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "miguelsegovia21@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "[TICKET PETRESCUER]");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });

        imEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "miguelsegovia21@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "[TICKET PETRESCUER]");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });


    }

    /*
    Método que obtiene datos de la pantalla anterior y se los asigna a los componentes de la pantalla.
     */

    void getAndSetIntentData() {

        if (getIntent().hasExtra("nombreUsuario")) {

            Bundle extras = getIntent().getExtras();
            etUser.setText(extras.getString("nombreUsuario"));
        }

        if (getIntent().hasExtra("passwordUsuario")) {

            Bundle extras = getIntent().getExtras();
            etPass.setText(extras.getString("passwordUsuario"));
        }


    }

}
