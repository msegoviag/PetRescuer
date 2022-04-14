package segovia.gil.petrescuer;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Pantalla que muestra por pantalla la lista (RecyclerView) de mascotas actuales en la base de datos
 */

public class ActivityPetList extends AppCompatActivity {

    GridView gridView;
    ArrayList<Mascota> list;
    ArrayList<Mascota> listSeleccion;
    PetListAdapter adapter = null;
    SQLiteHelper helper;
    Cursor cursor;
    String usuario;
    String numeroUsuarioMascota;

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    /**
     * Se inicializa la pantalla y se crean las variables necesarias para almacenar en tiempo de ejecución las mascotas de la base de datos.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pet_list_activity);
        gridView = (GridView) findViewById(R.id.gridView);
        list = new ArrayList<>();
        listSeleccion = new ArrayList<>();
        adapter = new PetListAdapter(this, R.layout.mascota_item, list);
        gridView.setAdapter(adapter);

        Bundle extras = getIntent().getExtras();

        if (getIntent().hasExtra("nombreUsuario")) {

            extras = getIntent().getExtras();
            usuario = extras.getString("nombreUsuario");

        }

        if (getIntent().hasExtra("numeroUsuarioMascota")) {

            numeroUsuarioMascota = extras.getString("numeroUsuarioMascota");
        }

        // Obtener datos de la base datos.
        helper = new SQLiteHelper(this, "Mascotas.sqlite", null, 1);
        cursor = helper.getData("SELECT * FROM MASCOTAS");
        list.clear();

        // Si no hay mascotas se lanzará un mensaje avisando al usuario.
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No hay mascotas en la lista actualmente", Toast.LENGTH_LONG).show();
        }

        /*
        Cursor que se encarga de recorrer todas las mascotas que se encuentran actualmente en la base de datos y guardarlas en una lista.
         */
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String nombre = cursor.getString(1);
                    String tipoMascota = cursor.getString(2);
                    int edad = cursor.getInt(3);
                    int peso = cursor.getInt(4);
                    String color = cursor.getString(5);
                    String raza = cursor.getString(6);
                    String lugarDesaparicion = cursor.getString(7);
                    String fechaDesaparicion = cursor.getString(8);
                    int telefono = cursor.getInt(9);
                    byte[] imagenMascota = cursor.getBlob(10);
                    list.add(new Mascota(id, nombre, tipoMascota, edad, peso, color, raza, lugarDesaparicion, fechaDesaparicion, telefono, imagenMascota));
                } while (cursor.moveToNext());
                cursor.close();
            }
        }

        // Se refresca el adaptador que se encarga de pasar los datos al RecyclerView que mostrará las mascotas por pantalla.
        adapter.notifyDataSetChanged();

        /*
        Si se detecta una pulsación larga sobre una mascota ésta se borrará, ésta operación rápida está
        reservada para administradores o usuarios con privilegios.
         */

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Bundle extras = getIntent().getExtras();

                if (getIntent().hasExtra("nombreUsuario")) {

                    extras = getIntent().getExtras();
                    usuario = extras.getString("nombreUsuario");

                }

                if (extras == null) {
                    return false;
                } else {
                    usuario = extras.getString("nombreUsuario");
                }

                if (usuario.equals("Admin")) {
                    int idMascota = 0;
                    int telefono = 0;
                    Cursor c = helper.getData("SELECT * FROM MASCOTAS");
                    ArrayList<Integer> arrId = new ArrayList<Integer>();

                    while (c.moveToNext()) {
                        idMascota = c.getInt(0);
                        telefono = c.getInt(9);
                        arrId.add(idMascota);
                    }

                    // Borrar mascota
                    helper.deleteData(arrId.get(position));

                    // Mandar mascota a historial borrado.
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    String fechaActual = dtf.format(LocalDateTime.now());

                    helper.insertDeletePets(fechaActual, idMascota, telefono, "Motivo a especificar por el administrador");

                    Toast.makeText(ActivityPetList.this, "Mascota borrada", Toast.LENGTH_SHORT).show();
                    updatePetList();
                    return true;
                } else {
                    Toast.makeText(ActivityPetList.this, "No tienes permisos para borrar mascotas, contacta con el administrador", Toast.LENGTH_LONG).show();
                    return false;
                }


            }
        });

        /**
         * Al pulsar sobre una mascota se recoge desde la base de dato su información y se inicia la pantalla de detalles,
         * donde se expande la información sobre la mascota con los datos recuperados
         */
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                //Operaciones de recorrido en la base de datos y almacenamiento en variables que se
                // utilizarán en la pantalla de detalles.

                Intent intent = new Intent(ActivityPetList.this, ActivityDetalles.class);
                Cursor c = ActivityInicial.sqLiteHelper.getData("SELECT ID_MASCOTA from MASCOTAS");
                ArrayList<Integer> arrId = new ArrayList<Integer>();
                while (c.moveToNext()) {
                    arrId.add(c.getInt(0));
                }
                c.close();

                Cursor c1 = ActivityInicial.sqLiteHelper.getData("SELECT * from MASCOTAS where ID_MASCOTA = " + arrId.get(position));
                listSeleccion.clear();

                while (c1.moveToNext()) {
                    int idm = c1.getInt(0);
                    String nombre = c1.getString(1);
                    String tipoMascota = c1.getString(2);
                    int edad = c1.getInt(3);
                    int peso = c1.getInt(4);
                    String color = c1.getString(5);
                    String raza = c1.getString(6);
                    String lugarDesaparicion = c1.getString(7);
                    String fechaDesaparicion = c1.getString(8);
                    int telefono = c1.getInt(9);
                    byte[] imagenMascota = c1.getBlob(10);
                    listSeleccion.add(new Mascota(idm, nombre, tipoMascota, edad, peso, color, raza, lugarDesaparicion, fechaDesaparicion, telefono, imagenMascota));

                    Bitmap image;
                    image = getImage(imagenMascota);
                    Bitmap _bitmap = image; // your bitmap
                    ByteArrayOutputStream _bs = new ByteArrayOutputStream();
                    _bitmap.compress(Bitmap.CompressFormat.JPEG, 50, _bs);

                    intent.putExtra("imagenMascota", _bs.toByteArray());

                    intent.putExtra("idm", String.valueOf(idm));
                    intent.putExtra("nombre", nombre);
                    intent.putExtra("tipoMascota", tipoMascota);
                    intent.putExtra("edad", String.valueOf(edad));
                    intent.putExtra("peso", String.valueOf(peso));
                    intent.putExtra("color", color);
                    intent.putExtra("raza", raza);
                    intent.putExtra("lugarDesaparicion", lugarDesaparicion);
                    intent.putExtra("fechaDesaparicion", fechaDesaparicion);
                    intent.putExtra("telefono", String.valueOf(telefono));
                    intent.putExtra("nombreUsuario", usuario);
                    intent.putExtra("numeroUsuarioMascota", numeroUsuarioMascota);

                    startActivity(intent);

                    Toast.makeText(ActivityPetList.this, nombre, Toast.LENGTH_SHORT).show();
                }
                c1.close();


            }
        });

    }

    /**
     * Método que reanuda la pantalla tras volver de segundo plano y actualiza el recyclerview donde
     * se reflejan las mascotas
     */

    @Override
    protected void onResume() {
        super.onResume();
        updatePetList();
    }

    /**
     * Método que se encarga comprobar las mascotas actuales en la base de datos y actualizarlas
     * en pantalla, imitando un refresco.
     */
    private void updatePetList() {
        cursor = helper.getData("SELECT * FROM MASCOTAS");
        list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String nombre = cursor.getString(1);
            String tipoMascota = cursor.getString(2);
            int edad = cursor.getInt(3);
            int peso = cursor.getInt(4);
            String color = cursor.getString(5);
            String raza = cursor.getString(6);
            String lugarDesaparicion = cursor.getString(7);
            String fechaDesaparicion = cursor.getString(8);
            int telefono = cursor.getInt(9);
            byte[] imagenMascota = cursor.getBlob(10);
            list.add(new Mascota(id, nombre, tipoMascota, edad, peso, color, raza, lugarDesaparicion, fechaDesaparicion, telefono, imagenMascota));
        }
        cursor.close();

        adapter.notifyDataSetChanged();

    }

    /**
     * Se configura las opciones del menú, si se detecta que el usuario es administrador
     * se le habilitará la opción de acceder al panel de administración.
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //MenuItem addPet = menu.findItem(R.id.action_add_pet);
        MenuItem logoutUser = menu.findItem(R.id.action_logout_user);
        MenuItem adminAccess = menu.findItem(R.id.action_admin);
        MenuItem helpUser = menu.findItem(R.id.action_help);

        //adminAccess.setVisible(true);
        Bundle extras = getIntent().getExtras();

        if (extras == null) {
            return false;
        } else {
            usuario = extras.getString("nombreUsuario");
        }

        if (usuario.equals("Admin")) {
            adminAccess.setVisible(true);
        }

        logoutUser.setTitle("Salir: " + usuario);

        return super.onPrepareOptionsMenu(menu);

    }

    /**
     * Se crea el menú al arrancar el Activity
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Se ejecutarán determinadas acciones dependiendo de la opción de menú seleccionada.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        // La opción añadir mascota abrirá el formulario (ActivityFormulario)

        if (id == R.id.action_add_pet) {
            Intent intent = new Intent(this, ActivityFormulario.class);
            intent.putExtra("nombreUsuario", usuario);
            intent.putExtra("numeroUsuarioMascota", String.valueOf(numeroUsuarioMascota));
            startActivity(intent);
        }

        // La opción salir redirigirá al usuario a la página de inicio de sesión.

        if (id == R.id.action_logout_user) {
            Intent intent = new Intent(this, ActivityInicioSesion.class);
            Toast.makeText(ActivityPetList.this, "Te has desconectado.", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }

        // La opción que abrirá el panel de administrador si el usuario tiene privilegios (es Admin)

        if (id == R.id.action_admin) {
            Intent intent = new Intent(this, ActivityPanelAdmin.class);
            startActivity(intent);

        }

        // Si el usuario necesita ayuda ejecutará esta opción que abrirá un popup con su cliente de mensajería predeterminado
        if (id == R.id.action_help) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "miguelsegovia21@gmail.com", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "[TICKET PETRESCUER]");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "");
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        }

        return super.onOptionsItemSelected(item);
    }
}

