package segovia.gil.petrescuer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;


/**
 * Pantalla que se inicia al abrir la aplicación, se encargará de crear la base de datos y las tablas necesarias para el funcionamiento
 * de la aplicación, se inserta un usuario Admin que puede efectuar operaciones de inserción, borrado y modificación sobre la base de datos desde la app
 */

public class ActivityInicial extends AppCompatActivity {

    public static SQLiteHelper sqLiteHelper;
    final int REQUEST_CODE_GALLERY = 999;
    ImageButton btInicio;
    SharedPreferences prefs = null;


    /**
     * Al ejecutarse el método onCreate se verifica si es la primera ejecución y si existen las tablas, si no existen se crean.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences("com.PetRescuer", MODE_PRIVATE);
        setContentView(R.layout.activity_inicial);
        btInicio = findViewById(R.id.btnInicio);

        sqLiteHelper = new SQLiteHelper(this, "Mascotas.sqlite", null, 1);

        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS \"USUARIOS\" (\n" +
                "\t\"Telefono\"\tNUMERIC NOT NULL UNIQUE,\n" +
                "\t\"Nombre\"\tTEXT NOT NULL,\n" +
                "\t\"Email\"\tTEXT NOT NULL UNIQUE,\n" +
                "\t\"Ciudad\"\tTEXT NOT NULL,\n" +
                "\t\"Password\" TEXT NOT NULL,\n" +
                "\tPRIMARY KEY(\"Telefono\")\n" +
                ");");

        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS \"RESCATISTA\" (\n" +
                "\t\"numero_rescatador\"\tINTEGER NOT NULL,\n" +
                "\t\"Telefono\"\tNUMERIC NOT NULL,\n" +
                "\t\"Email\"\tTEXT NOT NULL UNIQUE,\n" +
                "\tPRIMARY KEY(\"numero_rescatador\"),\n" +
                "\tFOREIGN KEY(\"Telefono\") REFERENCES \"Usuarios\"(\"Telefono\")\n" +
                ");");

        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS \"PROTECTORA\" (\n" +
                "\t\"numero_protectora\"\tINTEGER NOT NULL,\n" +
                "\t\"Telefono\"\tNUMERIC NOT NULL,\n" +
                "\t\"Email\"\tTEXT NOT NULL UNIQUE,\n" +
                "\tPRIMARY KEY(\"numero_protectora\"),\n" +
                "\tFOREIGN KEY(\"Telefono\") REFERENCES \"Usuarios\"(\"Telefono\")\n" +
                ");");

        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS \"MASCOTAS\" (\n" +
                "\t\"ID_MASCOTA\"\tINTEGER UNIQUE,\n" +
                "\t\"Nombre\"\tTEXT NOT NULL,\n" +
                "\t\"Tipo\"\tTEXT NOT NULL,\n" +
                "\t\"Edad\"\tINTEGER NOT NULL,\n" +
                "\t\"Peso\"\tINTEGER NOT NULL,\n" +
                "\t\"Color\"\tTEXT NOT NULL,\n" +
                "\t\"Raza\"\tTEXT NOT NULL,\n" +
                "\t\"Lugar_desaparicion\"\tTEXT NOT NULL,\n" +
                "\t\"Fecha_desaparicion\"\tTEXT NOT NULL,\n" +
                "\t\"TEL_USUARIO\"\tNUMERIC NOT NULL,\n" +
                "\t\"Imagen\"\tBLOB,\n" +
                "\tPRIMARY KEY(\"ID_MASCOTA\" AUTOINCREMENT),\n" +
                "\tFOREIGN KEY(\"TEL_USUARIO\") REFERENCES \"Usuarios\"(\"Telefono\")\n" +
                ");");

        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS \"HISTORIAL\" (\n" +
                "\t\"ID_HISTORIAL\"\tINTEGER,\n" +
                "\t\"ID_MASCOTA\"\tINTEGER NOT NULL,\n" +
                "\t\"TEL_USUARIO\"\tNUMERIC NOT NULL,\n" +
                "\t\"NOMBRE_USUARIO\"\tTEXT NOT NULL,\n" +
                "\t\"NOMBRE_MASCOTA\"\tTEXT NOT NULL,\n" +
                "\t\"FECHA_DESAPARICION\"\tTEXT NOT NULL,\n" +
                "\t\"LUGAR_DESAPARICION\"\tTEXT,\n" +
                "\t\"FECHA_APARICION\"\tTEXT,\n" +
                "\t\"LUGAR_APARICION\"\tTEXT,\n" +
                "\tPRIMARY KEY(\"ID_HISTORIAL\" AUTOINCREMENT),\n" +
                "\tFOREIGN KEY(\"TEL_USUARIO\") REFERENCES \"Usuarios\"(\"Telefono\") ON DELETE CASCADE ON UPDATE NO ACTION,\n" +
                "\tFOREIGN KEY(\"ID_MASCOTA\") REFERENCES \"Mascotas\"(\"ID_MASCOTA\") ON DELETE CASCADE ON UPDATE NO ACTION\n" +
                ");");

        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS \"AVISOS\" (\n" +
                "\t\"FECHA_AVISO\"\tTEXT,\n" +
                "\t\"ID_MASCOTA\"\tINTEGER,\n" +
                "\t\"TEL_USUARIO\"\tNUMERIC,\n" +
                "\tPRIMARY KEY(\"FECHA_AVISO\",\"ID_MASCOTA\"),\n" +
                "\tFOREIGN KEY(\"TEL_USUARIO\") REFERENCES \"Usuarios\"(\"Telefono\") ON DELETE CASCADE ON UPDATE NO ACTION,\n" +
                "\tFOREIGN KEY(\"ID_MASCOTA\") REFERENCES \"Mascotas\"(\"ID_MASCOTA\") ON DELETE CASCADE ON UPDATE NO ACTION\n" +
                ");");

        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS \"BORRADAS\" (\n" +
                "\t\"FECHA_BORRADO\"\tTEXT NOT NULL,\n" +
                "\t\"ID_MASCOTA\"\tINTEGER NOT NULL,\n" +
                "\t\"TEL_USUARIO\"\tNUMERIC NOT NULL,\n" +
                "\t\"MOTIVO\"\tTEXT,\n" +
                "\tPRIMARY KEY(\"FECHA_BORRADO\",\"ID_MASCOTA\"),\n" +
                "\tFOREIGN KEY(\"ID_MASCOTA\") REFERENCES \"Mascotas\"(\"ID_MASCOTA\") ON DELETE CASCADE ON UPDATE NO ACTION,\n" +
                "\tFOREIGN KEY(\"TEL_USUARIO\") REFERENCES \"Usuarios\"(\"Telefono\") ON DELETE CASCADE ON UPDATE NO ACTION\n" +
                ");");


        /**
         * Método para crear un efecto de una splash screen.
         */
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(ActivityInicial.this, ActivityInicioSesion.class);
                startActivity(intent);
                finish();
            }
        }, 2500);
    }


    /**
     * Se creará el usuario Admin si no existe para administrar la aplicación desde la base de datos, también se insertarán registros
     * de usuarios aleatorios y algunas mascotas.
     */
    @Override
    protected void onResume() {
        super.onResume();

        if (prefs.getBoolean("firstrun", true)) {
            sqLiteHelper.queryData("INSERT INTO \"USUARIOS\"\n" +
                    "(\"Telefono\", \"Nombre\", \"Email\", \"Ciudad\", \"Password\")\n" +
                    "VALUES (617852130, 'Admin', 'Admin@Petrescuer.com', 'Málaga', 'securePassword');");
            sqLiteHelper.queryData("INSERT INTO \"USUARIOS\"\n" +
                    "(\"Telefono\", \"Nombre\", \"Email\", \"Ciudad\", \"Password\")\n" +
                    "VALUES (654321919, 'María Luisa', 'marialuisa@gmail.com', 'Almería', '123123aaa');");

            sqLiteHelper.queryData("INSERT INTO \"USUARIOS\"\n" +
                    "(\"Telefono\", \"Nombre\", \"Email\", \"Ciudad\", \"Password\")\n" +
                    "VALUES (622332909, 'Manuel Rivas', 'ManuelRivas@Petrescuer.com', 'Madrid', 'passwordSecure');");

            sqLiteHelper.queryData("INSERT INTO \"USUARIOS\"\n" +
                    "(\"Telefono\", \"Nombre\", \"Email\", \"Ciudad\", \"Password\")\n" +
                    "VALUES (777777777, 'Profesor', 'Profesor@Aguadulce.com', 'Almería, Aguadulce', 'profesor');");


            byte[] imagePredeterminada = imageToByte();
            sqLiteHelper.insertPetData("Coco", "Perro", 4, 18, "Marrón", "Podenco", "Aguadulce", "07/04/22", 638739046, imagePredeterminada);
            sqLiteHelper.insertPetData("Shiro", "Gato", 2, 3, "Negro", "Común", "Málaga", "06/03/22", 622332909, imagePredeterminada);


            prefs.edit().putBoolean("firstrun", false).commit();
        }
    }



    public byte[] imageToByte() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.pred);
        ByteArrayOutputStream opstream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, opstream);
        byte[] bytArray = opstream.toByteArray();
        return bytArray;
    }



    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    // convert from bitmap to byte array
    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }
}