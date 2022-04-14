package segovia.gil.petrescuer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;


/**
 * Pantalla donde se muestra los detalles de la mascota tras hacer click desde la pantalla de lista de mascotas (RecyclerView)
 */
public class ActivityDetalles extends AppCompatActivity {

    TextView tvNombreMascota, tvFechaMascota, tvTelefonoDueño, tvEdad, tvPeso, tvLugar, tvIdMascota, tvColor;
    ImageView imageViewMascota;
    Button btnLlamar, btnEdit;
    ImageButton btnMapa;
    String nombreMascota, fechaMascota, telefonoDueño, edad, peso, lugar, idMascota, tipoMascota, raza, color, usuario, numeroUsuarioMascota;
    Bitmap _bitmap;

    /**
     * Método que convierte y descodifica un array de byte a Bitmap para trabajar más cómodamente con imágenes.
     *
     * @param image
     * @return
     */

    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    /**
     * Método onCreate, enlaza e inicializa los widgets del Activity (pantalla), se programa el comportamiento y función de los distintos botones que reaccionarán a los clicks-
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);
        tvNombreMascota = findViewById(R.id.tvNombreMascota);
        tvFechaMascota = findViewById(R.id.tvFechaMascota);
        tvEdad = findViewById(R.id.tvEdad);
        tvPeso = findViewById(R.id.tvPeso);
        tvLugar = findViewById(R.id.tvLugar);
        tvColor = findViewById(R.id.tvColor);
        tvTelefonoDueño = findViewById(R.id.tvTelefonoDueño);
        tvIdMascota = findViewById(R.id.tvIdm);
        btnLlamar = findViewById(R.id.btnLlamar);
        btnMapa = findViewById(R.id.btnMapa);
        btnEdit = findViewById(R.id.btEditPet);
        imageViewMascota = findViewById(R.id.imageViewMascota);

        getAndSetIntentData();

        btnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String map = "http://maps.google.com/maps?q=" + tvLugar.getText().toString();
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
                startActivity(i);
            }
        });

        btnLlamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(Intent.ACTION_CALL);
//                i.setData(Uri.parse("tel:"+tvTelefonoDueño.getText().toString()));
//                startActivity(i);
                String tel = tvTelefonoDueño.getText().toString();
                startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:" + "+34" + tel)));
            }
        });

        /**
         * Botón editar mascota, recoge los datos de la mascota actual y los manda a la siguiente pantalla / activity "ActivityModificarMascota desde donde podrá editar la información.
         */
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (numeroUsuarioMascota.trim().equals(telefonoDueño) || usuario.trim().equals("Admin")) {
                    Intent intent = new Intent(ActivityDetalles.this, ActivityModificarMascota.class);

                    intent.putExtra("idm", String.valueOf(idMascota));
                    intent.putExtra("nombre", nombreMascota);
                    intent.putExtra("tipoMascota", tipoMascota);
                    intent.putExtra("edad", String.valueOf(edad));
                    intent.putExtra("peso", String.valueOf(peso));
                    intent.putExtra("color", color);
                    intent.putExtra("raza", raza);
                    intent.putExtra("lugarDesaparicion", lugar);
                    intent.putExtra("fechaDesaparicion", fechaMascota);
                    intent.putExtra("telefono", String.valueOf(telefonoDueño));
                    intent.putExtra("nombreUsuario", usuario);

                    // Se comprime la la imagen en el formato adecuado para trabajar con ella y mostrarla en un imageview.
                    ByteArrayOutputStream _bs = new ByteArrayOutputStream();
                    _bitmap.compress(Bitmap.CompressFormat.JPEG, 50, _bs);

                    intent.putExtra("imagenMascota", _bs.toByteArray());
                    startActivity(intent);
                    finish();
                }

                //Condición para comprobar si la mascota que desea modificar el usuario le corresponde y la ha subido desde su cuenta.
                //El administrador siempre puede modificar la mascota que desee.

                if (!numeroUsuarioMascota.trim().equals(telefonoDueño.trim())) {
                    Toast.makeText(ActivityDetalles.this, "No eres Administrador ni dueño de esta mascota, no puedes modificarla.", Toast.LENGTH_SHORT).show();
                    return;
                }


            }
        });


    }

    /**
     * Método que recoge los datos de la pantalla anterior para mostrarlo en la pantalla actual
     */
    void getAndSetIntentData() {

        if (getIntent().hasExtra("nombre")) {

            Bundle extras = getIntent().getExtras();
            nombreMascota = extras.getString("nombre");
            tvNombreMascota.setText(nombreMascota);
        }

        if (getIntent().hasExtra("fechaDesaparicion")) {
            fechaMascota = getIntent().getStringExtra("fechaDesaparicion");
            tvFechaMascota.setText(fechaMascota);
        }

        if (getIntent().hasExtra("telefono")) {
            telefonoDueño = getIntent().getStringExtra("telefono");
            tvTelefonoDueño.setText(telefonoDueño);
        }

        if (getIntent().hasExtra("edad")) {

            Bundle extras = getIntent().getExtras();
            edad = extras.getString("edad");
            tvEdad.setText(edad);
        }
        if (getIntent().hasExtra("peso")) {

            Bundle extras = getIntent().getExtras();
            peso = extras.getString("peso");
            tvPeso.setText(peso);
        }
        if (getIntent().hasExtra("lugarDesaparicion")) {

            Bundle extras = getIntent().getExtras();
            lugar = extras.getString("lugarDesaparicion");
            tvLugar.setText(lugar);
        }

        if (getIntent().hasExtra("tipoMascota")) {

            Bundle extras = getIntent().getExtras();
            tipoMascota = extras.getString("tipoMascota");

        }

        if (getIntent().hasExtra("raza")) {

            Bundle extras = getIntent().getExtras();
            raza = extras.getString("raza");

        }

        if (getIntent().hasExtra("color")) {

            Bundle extras = getIntent().getExtras();
            color = extras.getString("color");
            tvColor.setText(color);
        }

        if (getIntent().hasExtra("imagenMascota")) {

            _bitmap = BitmapFactory.decodeByteArray(
                    getIntent().getByteArrayExtra("imagenMascota"), 0, getIntent().getByteArrayExtra("imagenMascota").length);
            imageViewMascota.setImageBitmap(_bitmap);

        }

        if (getIntent().hasExtra("idm")) {

            Bundle extras = getIntent().getExtras();
            idMascota = extras.getString("idm");
            tvIdMascota.setText(":" + idMascota);
        }

        if (getIntent().hasExtra("nombreUsuario")) {

            Bundle extras = getIntent().getExtras();
            usuario = extras.getString("nombreUsuario");

        }

        if (getIntent().hasExtra("numeroUsuarioMascota")) {

            Bundle extras = getIntent().getExtras();
            numeroUsuarioMascota = extras.getString("numeroUsuarioMascota");
            //Toast.makeText(getApplicationContext(), numeroUsuarioMascota, Toast.LENGTH_LONG).show();
        }

    }
}