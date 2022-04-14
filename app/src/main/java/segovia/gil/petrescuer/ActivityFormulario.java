package segovia.gil.petrescuer;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

/**
 * Pantalla que recoge los datos de la mascota del formulario y la añade a la base de datos si la información es correcta.
 */

public class ActivityFormulario extends AppCompatActivity {

    public static SQLiteHelper sqLiteHelper;
    final int REQUEST_CODE_GALLERY = 999;
    EditText etName, etEdad, etPeso, etColor, etRaza, etLugar, etFecha, etMovil;
    Spinner spTipoMascota;
    Button btnAddImage, btnAceptar, btnCancelar;
    ImageView imageView;
    Calendar cal = Calendar.getInstance();
    int yearT = cal.get(Calendar.YEAR);
    int monthT = cal.get(Calendar.MONTH);
    int dayT = cal.get(Calendar.DAY_OF_MONTH);
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    /**
     * Método para tratar correctamente una imagen y guardarla en la base de datos en el formato adecuado./**
     *
     * @param image
     * @return
     */
    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        Bitmap resized = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * 0.8), (int) (bitmap.getHeight() * 0.8), true);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        resized.compress(Bitmap.CompressFormat.JPEG, 30, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }


    /**
     * Método onCreate, enlaza e inicializa los widgets del Activity (pantalla), se programa el comportamiento y función de los distintos botones que reaccionarán a los clicks
     * se inicializa una instancia de la clase SQLiteHelper para introducir información de la base de datos
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);
        init();

        // Se crea la instancia y se le pasa un método constructor que tiene como parámetro el nombre de la base de datos sobre la que se trabajará.
        sqLiteHelper = new SQLiteHelper(this, "Mascotas.sqlite", null, 1);
        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS MASCOTAS" +
                " (Id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, tipoMascota VARCHAR, edad VARCHAR, peso VARCHAR," +
                "color VARCHAR, raza VARCHAR, lugarDesaparicion VARCHAR, fechaDesaparicion VARCHAR, telefono VARCHAR, image BLOG )");

        // Al pulsar sobre el EditText se abrerá un popup para seleccionar la fecha, deberemos indicar una fecha
        // la cual no debe ser posterior al día actual.
        etFecha.setShowSoftInputOnFocus(false);

        etFecha.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                DatePickerDialog dialog = new DatePickerDialog(ActivityFormulario.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, yearT, monthT, dayT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                String date = month + "/" + day + "/" + year;

                if (year > yearT || day > dayT || month > monthT) {
                    Toast.makeText(ActivityFormulario.this, "La fecha no puede ser posterior a hoy", Toast.LENGTH_SHORT).show();
                } else {
                    etFecha.setText(date);
                }

            }
        };

        // Al hacer click sobre el botón añadir imagen se nos abrirá la galería para seleccionar una imagen de la mascota en cuestión
        // si no añadimos ninguna se añade una imagen por defecto.

        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        ActivityFormulario.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALLERY);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        ActivityFormulario.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALLERY);
            }

        });

        // Al pulsar el botón aceptar se efectua una comprobación de los campos (EditText) si hay alguno incorrecto o vació se le mostrará por
        // pantalla una alerta (Toast) al usuario, en cambio, si está bien se guardará la mascota en la DB.
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                try {
                    if (etName.getText().toString().isEmpty() || etEdad.getText().toString().isEmpty() || etPeso.getText().toString().isEmpty() ||
                            etColor.getText().toString().isEmpty() || etRaza.getText().toString().isEmpty() || etLugar.getText().toString().isEmpty() ||
                            etFecha.getText().toString().isEmpty() || etMovil.getText().toString().isEmpty() || Integer.parseInt(etEdad.getText().toString()) <= 0
                            || Integer.parseInt(etEdad.getText().toString()) > 60 || Integer.parseInt(etPeso.getText().toString()) <= 0
                            || Integer.parseInt(etPeso.getText().toString()) > 120) {
                        Toast.makeText(ActivityFormulario.this, "Hay campos con datos erróneos o incompletos", Toast.LENGTH_SHORT).show();

                    } else {

                        Bundle extras = getIntent().getExtras();
                        String usuario = extras.getString("nombreUsuario");

                        sqLiteHelper.insertPetData(etName.getText().toString().trim(), spTipoMascota.getSelectedItem().toString(),
                                Integer.parseInt(etEdad.getText().toString().trim()), Integer.parseInt(etPeso.getText().toString().trim()), etColor.getText().toString().trim(),
                                etRaza.getText().toString().trim(), etLugar.getText().toString().trim(), etFecha.getText().toString().trim(),
                                Integer.parseInt(etMovil.getText().toString().trim()), imageViewToByte(imageView));


                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                        String fechaActual = dtf.format(LocalDateTime.now());

                        int newIdMascota = 0;
                        Cursor c = sqLiteHelper.getData("SELECT * FROM MASCOTAS ORDER BY ID_MASCOTA DESC LIMIT 1");
                        while (c.moveToNext()) {
                            newIdMascota = c.getInt(0);
                        }

                        /**
                         * Tras insertar una mascota en la tabla MASCOTAS se guarda paralelamente en la tabla AVISOS e HISTORIAL
                         * información relativa a la fecha, usuario y mascota para tener un mayor control.
                         */
                        sqLiteHelper.insertAvisos(fechaActual, newIdMascota, Integer.parseInt(etMovil.getText().toString().trim()));

                        sqLiteHelper.insertHistorial(newIdMascota, Integer.parseInt(etMovil.getText().toString().trim()),
                                usuario, etName.getText().toString().trim(), etFecha.getText().toString().trim(),
                                "Sin definir", etLugar.getText().toString().trim(), "Sin definir");

                        Toast.makeText(getApplicationContext(), "Mascota desaparecida añadida", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                        finish();
                    }
                } catch (Exception e) {
                    Toast.makeText(ActivityFormulario.this, "¡Hay campos con datos erróneos! Revísalos antes de continuar." + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        // Si el usuario no desea añadir ninguna mascota volverá a la pantalla anterior donde se muestra la lista de mascotas.
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * Método que se encarga de inicializar los distintos elementos del Activity para que sea posible trabajar con ellos.
     */
    private void init() {
        etName = findViewById(R.id.etName);
        etEdad = findViewById(R.id.etEdad);
        etPeso = findViewById(R.id.etPeso);
        etColor = findViewById(R.id.etColor);
        etRaza = findViewById(R.id.etRaza);
        etLugar = findViewById(R.id.etLugar);
        etFecha = findViewById(R.id.etFecha);
        etMovil = findViewById(R.id.etMovil);
        btnAddImage = findViewById(R.id.btnAddImage);
        btnAceptar = findViewById(R.id.btnAceptar);
        btnCancelar = findViewById(R.id.btnCancelar);
        spTipoMascota = findViewById(R.id.spTipoMascota);
        imageView = findViewById(R.id.imageView);

        if (getIntent().hasExtra("numeroUsuarioMascota")) {

            Bundle extras = getIntent().getExtras();
            String numeroUsuarioMascota = extras.getString("numeroUsuarioMascota");
            etMovil.setText(numeroUsuarioMascota);
        }

    }

    /**
     * Métodos para acceder comprobar permiso y acceder a la galería para poder seleccionar imágenes desde nuestra aplicación.
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            } else {
                Toast.makeText(getApplicationContext(), "No tienes permisos para acceder al archivo", Toast.LENGTH_LONG).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * Método que permite gestionar el traspaso de imagen desde la galería del dispositivo hasta nuestra aplicación,
     * se realiza una transformación a formato Bitmap y establecemos una imagen en nuestro elemento ImageView
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Método que imita la acción de pulsar el botón para ir atrás
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}