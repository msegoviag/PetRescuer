package segovia.gil.petrescuer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Pantalla que contiene un formulario donde el usuario podrá registrarse introduciendo su nombre, email, ciudad, número de teléfono
 * y contraseña, los datos serán almacenados en la tabla USUARIOS y si corresponde también en la tabla RESCATISTA o PROTECTORA
 */

public class ActivityRegistro extends AppCompatActivity {

    public static SQLiteHelper sqLiteHelper;
    final int REQUEST_CODE_GALLERY = 999;
    EditText etNombre, etEmail, etTel, etCiudad, etPassUser, etIdentificacion;
    CheckBox cbUser;
    Button btRegister;
    RadioButton rbRescatista, rbProtectora;

    /**
     * Se inicializa la pantalla que contiene el formulario de registro y se inicializan sus componentes, se crea un objeto
     * de la clase SQLiteHelper para trabajar con la base de datos que guardará el registro del nuevo usuario.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);
        init();
        sqLiteHelper = new SQLiteHelper(this, "Mascotas.sqlite", null, 1);


        /*
        Se comprueban que los campos sean correctos, se verifican los tipos de datos y que no estén vacíos.
         */
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (etNombre.getText().toString().isEmpty() || etNombre.getText().toString().equals("Admin") || etEmail.getText().toString().isEmpty() || etTel.getText().toString().isEmpty() ||
                            etCiudad.getText().toString().isEmpty() || etPassUser.getText().toString().isEmpty() ||
                            etIdentificacion.getVisibility() == View.VISIBLE && etIdentificacion.getText().toString().isEmpty()) {
                        Toast.makeText(ActivityRegistro.this, "Hay campos con datos erróneos o incompletos", Toast.LENGTH_SHORT).show();

                    } else {
                        /*
                        Se verifica que el usuario ha aceptado los términos y condiciones, también si pertenece a una protectora o es un rescatista independiente, en ese caso
                        deberá proporcionar un número de identificación.
                         */
                        if (cbUser.isChecked()) {

                            if (rbProtectora.isChecked()) {

                                sqLiteHelper.insertProtectora(Integer.parseInt(etIdentificacion.getText().toString().trim()), Integer.parseInt(etTel.getText().toString().trim()), etEmail.getText().toString().trim());

                            }

                            if (rbRescatista.isChecked()) {
                                sqLiteHelper.insertRescatista(Integer.parseInt(etIdentificacion.getText().toString().trim()), Integer.parseInt(etTel.getText().toString().trim()), etEmail.getText().toString().trim());

                            }

                            sqLiteHelper.insertUserData(Integer.parseInt(etTel.getText().toString().trim()), etNombre.getText().toString().trim(), etEmail.getText().toString().trim(),
                                    etCiudad.getText().toString().trim(),
                                    etPassUser.getText().toString().trim());


                            /*
                            Si el proceso de registro ha sido correcto se añadirá el nuevo usuario a la base de datos y se mostrará una alerta
                            indicando que el proceso fue exitoso.
                             */
                            Toast.makeText(getApplicationContext(), "Usuario registrado", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ActivityRegistro.this, ActivityInicioSesion.class);
                            intent.putExtra("nombreUsuario", etNombre.getText().toString());
                            intent.putExtra("passwordUsuario", etPassUser.getText().toString());
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(ActivityRegistro.this, "No has aceptado los términos y condiciones de servicio", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(ActivityRegistro.this, "¡Error al guardar datos! El número de teléfono " + etTel.getText().toString() + " o email " + etEmail.getText().toString() + " ya está registrado.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        rbProtectora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etIdentificacion.setVisibility(View.VISIBLE);
            }
        });

        rbRescatista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etIdentificacion.setVisibility(View.VISIBLE);
            }
        });

    }

    /**
     * Método que se encarga de inicializar los distintos elementos del Activity para que sea posible trabajar con ellos.
     */
    private void init() {
        etNombre = findViewById(R.id.etNombre);
        etEmail = findViewById(R.id.etEmail);
        etTel = findViewById(R.id.etTel);
        etCiudad = findViewById(R.id.etCiudad);
        etPassUser = findViewById(R.id.etPassUser);
        cbUser = findViewById(R.id.cbUser);
        btRegister = findViewById(R.id.btRegister);
        etIdentificacion = findViewById(R.id.etIdentificacion);
        rbProtectora = findViewById(R.id.rbProtectora);
        rbRescatista = findViewById(R.id.rbRescatista);
    }

}
