package segovia.gil.petrescuer;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.TextView;
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
 * Pantalla desde la que se modifica la mascota seleccionada y se guarda nuevamente en la base de datos.
 */
public class ActivityModificarMascota extends AppCompatActivity {

    public static SQLiteHelper sqLiteHelper;
    final int REQUEST_CODE_GALLERY = 999;

    EditText etModifyName, etModifyTypePet, etModifyEdad, etModifyPeso, etModifyColor, etModifyRaza, etModifyLugarDesaparicion,
            etModifyFechaDesaparicion, etModifyLugarAparicion, etModifyFechaAparicion;
    ImageView imModifyPet;
    Button btModifyImage, btUpdatePet, btRemovePet;
    TextView tvId;
    Bitmap _bitmap;
    String telefono, idm, usuario;
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
     * Método onCreate, enlaza e inicializa los widgets del Activity (pantalla), se instancia un objeto para insertar datos en la base de datos y
     * se programa el comportamiento y función de los distintos botones que reaccionarán
     * a los clicks, se recuperan datos de la anterior pantalla (ActivityDetalles) y que se utilizarán en el formulario.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pet);
        init();
        getAndSetIntentData();

        sqLiteHelper = new SQLiteHelper(this, "Mascotas.sqlite", null, 1);


        // Al pulsar sobre el EditText se abrerá un popup para seleccionar la fecha, deberemos indicar una fecha
        // la cual no debe ser posterior al día actual. Un Listener se encarga de capturar la fecha y añadirla al campo de texto si es correcta.

        etModifyFechaDesaparicion.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                DatePickerDialog dialog = new DatePickerDialog(ActivityModificarMascota.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, yearT, monthT, dayT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

        etModifyFechaAparicion.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                DatePickerDialog dialog = new DatePickerDialog(ActivityModificarMascota.this,
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
                    Toast.makeText(ActivityModificarMascota.this, "La fecha no puede ser posterior a hoy", Toast.LENGTH_SHORT).show();
                } else {

                    if (etModifyFechaAparicion.isFocused()) {
                        etModifyFechaAparicion.setText(date);
                    } else {
                        etModifyFechaDesaparicion.setText(date);
                    }


                }
            }
        };


        // Al hacer click sobre el botón añadir imagen o la miniatura se nos abrirá la galería para seleccionar una imagen de la mascota en cuestión
        // si no añadimos ninguna se añade una imagen por defecto.

        btModifyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        ActivityModificarMascota.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALLERY);
            }
        });

        imModifyPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        ActivityModificarMascota.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALLERY);
            }
        });

        // Al pulsar el botón aceptar se efectua una comprobación de los campos (EditText) si hay alguno incorrecto o vació se le mostrará por
        // pantalla una alerta (Toast) al usuario, en cambio, si los datos introducidos son correctos se guardará la mascota modificada en la DB.
        btUpdatePet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (etModifyName.getText().toString().isEmpty() ||
                            etModifyTypePet.getText().toString().isEmpty() ||
                            etModifyEdad.getText().toString().isEmpty() ||
                            etModifyPeso.getText().toString().isEmpty() ||
                            etModifyColor.getText().toString().isEmpty() ||
                            etModifyRaza.getText().toString().isEmpty() ||
                            etModifyLugarDesaparicion.getText().toString().isEmpty() ||
                            etModifyFechaDesaparicion.getText().toString().isEmpty() ||
                            Integer.parseInt(etModifyEdad.getText().toString()) < 0 ||
                            Integer.parseInt(etModifyEdad.getText().toString()) > 60 ||
                            Integer.parseInt(etModifyPeso.getText().toString()) < 0 ||
                            Integer.parseInt(etModifyPeso.getText().toString()) > 120) {

                        Toast.makeText(ActivityModificarMascota.this, "¡Hay campos con datos erróneos o vacíos! Revísalos", Toast.LENGTH_SHORT).show();

                    } else {

                        sqLiteHelper.updateData(etModifyName.getText().toString().trim(), etModifyTypePet.getText().toString().trim(), Integer.parseInt(etModifyEdad.getText().toString().trim()),
                                Integer.parseInt(etModifyPeso.getText().toString().trim()), etModifyColor.getText().toString().trim(), etModifyRaza.getText().toString().trim(),
                                etModifyLugarDesaparicion.getText().toString().trim(), etModifyFechaDesaparicion.getText().toString().trim(), Integer.parseInt(telefono), imageViewToByte(imModifyPet),
                                Integer.parseInt(idm));

                        sqLiteHelper.insertHistorial(Integer.parseInt(idm), Integer.parseInt(telefono),
                                usuario, etModifyName.getText().toString().trim(), etModifyFechaDesaparicion.getText().toString().trim(),
                                etModifyFechaAparicion.getText().toString().trim(), etModifyLugarDesaparicion.getText().toString().trim(), etModifyLugarAparicion.getText().toString().trim());


                        Toast.makeText(ActivityModificarMascota.this, "Mascota actualizada", Toast.LENGTH_SHORT).show();
                        onBackPressed();

                    }


                } catch (Exception e) {
                    System.out.println(e.getLocalizedMessage());

                }

            }

        });

        // Se identifica el id de la mascota actual y se borra de la base de datos.
        btRemovePet.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                try {
                    sqLiteHelper.deleteData(Integer.parseInt(idm));

                    // Mandar mascota a historial borrado.
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    String fechaActual = dtf.format(LocalDateTime.now());

                    sqLiteHelper.insertDeletePets(fechaActual, Integer.parseInt(idm), Integer.parseInt(telefono), "Motivo a especificar por el administrador");

                    Toast.makeText(ActivityModificarMascota.this, "Mascota borrada", Toast.LENGTH_SHORT).show();
                    onBackPressed();

                } catch (Exception e) {
                    System.out.println(e.getLocalizedMessage());
                }
            }
        });
    }

    /*
    Método que obtiene datos de la pantalla anterior y se los asigna a los componentes de la pantalla
    o se guardan en una variable para su posterior utilización.
     */
    private void getAndSetIntentData() {

        if (getIntent().hasExtra("idm")) {

            Bundle extras = getIntent().getExtras();
            idm = extras.getString("idm");
            tvId.setText("ID:" + idm);
        }

        if (getIntent().hasExtra("nombre")) {

            Bundle extras = getIntent().getExtras();
            String nombre = extras.getString("nombre");
            etModifyName.setText(nombre);
        }

        if (getIntent().hasExtra("tipoMascota")) {

            Bundle extras = getIntent().getExtras();
            String tipoMascota = extras.getString("tipoMascota");
            etModifyTypePet.setText(tipoMascota);
        }

        if (getIntent().hasExtra("edad")) {

            Bundle extras = getIntent().getExtras();
            String edad = extras.getString("edad");
            etModifyEdad.setText(edad);
        }

        if (getIntent().hasExtra("peso")) {

            Bundle extras = getIntent().getExtras();
            String peso = extras.getString("peso");
            etModifyPeso.setText(peso);
        }

        if (getIntent().hasExtra("color")) {

            Bundle extras = getIntent().getExtras();
            String color = extras.getString("color");
            etModifyColor.setText(color);
        }

        if (getIntent().hasExtra("raza")) {

            Bundle extras = getIntent().getExtras();
            String raza = extras.getString("raza");
            etModifyRaza.setText(raza);
        }

        if (getIntent().hasExtra("lugarDesaparicion")) {

            Bundle extras = getIntent().getExtras();
            String lugarDesaparicion = extras.getString("lugarDesaparicion");
            etModifyLugarDesaparicion.setText(lugarDesaparicion);
        }

        if (getIntent().hasExtra("fechaDesaparicion")) {

            Bundle extras = getIntent().getExtras();
            String fechaDesaparicion = extras.getString("fechaDesaparicion");
            etModifyFechaDesaparicion.setText(fechaDesaparicion);
        }

        if (getIntent().hasExtra("telefono")) {

            Bundle extras = getIntent().getExtras();
            telefono = extras.getString("telefono");
            //etModifyFechaDesaparicion.setText(fechaDesaparicion);
        }

        if (getIntent().hasExtra("imagenMascota")) {

            _bitmap = BitmapFactory.decodeByteArray(
                    getIntent().getByteArrayExtra("imagenMascota"), 0, getIntent().getByteArrayExtra("imagenMascota").length);
            imModifyPet.setImageBitmap(_bitmap);

        }
        if (getIntent().hasExtra("nombreUsuario")) {

            Bundle extras = getIntent().getExtras();
            usuario = extras.getString("nombreUsuario");

        }


    }

    /**
     * Método que se encarga de inicializar los distintos elementos del Activity para que sea posible trabajar con ellos.
     */
    private void init() {
        etModifyName = findViewById(R.id.etModifyName);
        etModifyTypePet = findViewById(R.id.etModifyTypePet);
        etModifyEdad = findViewById(R.id.etModifyEdad);
        etModifyPeso = findViewById(R.id.etModifyPeso);
        etModifyColor = findViewById(R.id.etModifyColor);
        etModifyRaza = findViewById(R.id.etModifyRaza);
        etModifyLugarDesaparicion = findViewById(R.id.etModifyLugarDesaparicion);
        etModifyFechaDesaparicion = findViewById(R.id.etModifyFechaDesaparicion);
        etModifyLugarAparicion = findViewById(R.id.etModifyLugarAparicion);
        etModifyFechaAparicion = findViewById(R.id.etModifyFechaAparicion);
        imModifyPet = findViewById(R.id.imModifyPet);
        btModifyImage = findViewById(R.id.btModifyImage);
        btUpdatePet = findViewById(R.id.btUpdatePet);
        btRemovePet = findViewById(R.id.btRemovePet);
        tvId = findViewById(R.id.tvId);

    }

    /**
     * Métodos que permiten gestionar el traspaso de imagen desde la galería del dispositivo hasta nuestra aplicación,
     * se realiza una transformación a formato Bitmap y establecemos una imagen en nuestro elemento ImageView
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imModifyPet.setImageBitmap(bitmap);
                //imModifyPet.setVisibility(View.VISIBLE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
