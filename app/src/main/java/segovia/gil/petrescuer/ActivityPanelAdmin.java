package segovia.gil.petrescuer;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * Pantalla exclusivamente para el administrador desde la que podrá de manera rápida efectuar
 * operaciones de limpieza y borrado de la base de datos.
 */
public class ActivityPanelAdmin extends AppCompatActivity {

    public static SQLiteHelper sqLiteHelper;
    Button btEliminarUsuarios, btEliminarMascotas, btEliminarHistorial, btEliminarBorradas, btEliminarProtectoras, btEliminarRescatistas, btEliminarAvisos, btEliminarDB;

    /**
     * Se inicia el layout que corresponde a la Activity, se inicializan los componentes de la pantalla
     * y se instancia un objeto para trabajar con la base de datos.
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_admin);
        initComponents();
        sqLiteHelper = new SQLiteHelper(this, "Mascotas.sqlite", null, 1);


        // Se eliminan todos los usuarios de la tabla USUARIOS de la base de datos al pulsar el botón eliminar.
        btEliminarUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqLiteHelper.queryData("DELETE FROM USUARIOS");
                Toast.makeText(ActivityPanelAdmin.this, "Usuarios borrados", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ActivityPanelAdmin.this, ActivityInicioSesion.class);
                startActivity(intent);
                finish();
            }
        });

        // Se eliminan todos los registros de mascotas de la tabla MASCOTAS de la base de datos al pulsar el botón eliminar,
        // al hacerlo se crea una copia a modo de backup en la tabla BORRADAS.
        btEliminarMascotas.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                int idMascota = 0;
                int telefono = 0;
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                String fechaActual = dtf.format(LocalDateTime.now());

                Cursor c = sqLiteHelper.getData("SELECT * FROM MASCOTAS");

                while (c.moveToNext()) {
                    idMascota = c.getInt(0);
                    telefono = c.getInt(9);
                    sqLiteHelper.insertDeletePets(fechaActual, idMascota, telefono, "Borrado masivo");
                }
                c.close();

                sqLiteHelper.queryData("DELETE FROM MASCOTAS");
                Toast.makeText(ActivityPanelAdmin.this, "Mascotas borradas", Toast.LENGTH_SHORT).show();
                onBackPressed();
                finish();
            }
        });

        // Se elimina el contenido de la tabla HISTORIAL
        btEliminarHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqLiteHelper.queryData("DELETE FROM HISTORIAL");
                Toast.makeText(ActivityPanelAdmin.this, "Historial borrado", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // Se eliminan los registros de la tabla BORRADAS
        btEliminarBorradas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqLiteHelper.queryData("DELETE FROM BORRADAS");
                Toast.makeText(ActivityPanelAdmin.this, "Tabla BORRADAS vaciada", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // Se eliminan los registros de la tabla PROTECTORA
        btEliminarProtectoras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqLiteHelper.queryData("DELETE FROM PROTECTORA");
                Toast.makeText(ActivityPanelAdmin.this, "Tabla PROTECTORA vaciada", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // Se eliminan los registros de la tabla RESCATISTA
        btEliminarRescatistas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqLiteHelper.queryData("DELETE FROM RESCATISTA");
                Toast.makeText(ActivityPanelAdmin.this, "Tabla RESCATISTA vaciada", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // Se eliminan los registros de la tabla AVISOS
        btEliminarAvisos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqLiteHelper.queryData("DELETE FROM AVISOS");
                Toast.makeText(ActivityPanelAdmin.this, "Tabla AVISOS vaciada", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // Se borra la base de datos y se sale de la aplicación.

        btEliminarDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getApplicationContext().deleteDatabase("Mascotas.sqlite");
                Toast.makeText(ActivityPanelAdmin.this, "Base de datos borrada, saliendo de la aplicación...", Toast.LENGTH_SHORT).show();
                finishAffinity();
            }
        });

    }

    /**
     * Método que se encarga de inicializar los distintos elementos del Activity para que sea posible trabajar con ellos.
     */

    public void initComponents() {

        btEliminarUsuarios = findViewById(R.id.btEliminarUsuario);
        btEliminarMascotas = findViewById(R.id.btEliminarMascotas);
        btEliminarHistorial = findViewById(R.id.btEliminarHistorial);
        btEliminarBorradas = findViewById(R.id.btEliminarBorradas);
        btEliminarProtectoras = findViewById(R.id.btEliminarProtectoras);
        btEliminarRescatistas = findViewById(R.id.btEliminarRescatistas);
        btEliminarAvisos = findViewById(R.id.btEliminarAvisos);
        btEliminarDB = findViewById(R.id.btEliminarDB);

    }
}
