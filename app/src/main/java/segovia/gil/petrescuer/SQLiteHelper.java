package segovia.gil.petrescuer;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

/**
 * Clase helper, que ayudará a realizar las operaciones de inserción en la base de datos
 * se instanciará un objeto de ella en otras clases del proyecto para efectuar las operaciones
 * de inserción, actualización y borrado.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    /**
     * Constructor básico para instanciar un objeto de nuestro clase, recibirá el contexto actual,
     * el nombre de la base de datos, factory y la versión.
     *
     * @param context
     * @param name
     * @param factory
     * @param version
     */
    public SQLiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * Método que ejecutará cualquier tipo de sentencia SQL en formato String.
     * @param sql
     */
    public void queryData(String sql) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    /**
     * Método que insertará una mascota en la base de datos. Tabla: MASCOTAS
     * @param name
     * @param tipoMascota
     * @param edad
     * @param peso
     * @param color
     * @param raza
     * @param lugarDesaparicion
     * @param fechaDesaparicion
     * @param telefono
     * @param imagenMascota
     */
    public void insertPetData(String name, String tipoMascota, int edad, int peso, String color, String raza, String lugarDesaparicion, String fechaDesaparicion, int telefono, byte[] imagenMascota) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO MASCOTAS VALUES (NULL, ?, ? ,? ,? ,? ,? ,? ,?, ? ,?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, name);
        statement.bindString(2, tipoMascota);
        statement.bindString(3, (String.valueOf(edad)));
        statement.bindString(4, (String.valueOf(peso)));
        statement.bindString(5, color);
        statement.bindString(6, raza);
        statement.bindString(7, lugarDesaparicion);
        statement.bindString(8, fechaDesaparicion);
        statement.bindString(9, (String.valueOf(telefono)));
        statement.bindBlob(10, imagenMascota);

        statement.executeInsert();

    }

    /**
     * Método que insertará a un usuario en la tabla PROTECTORA si indicó que pertenecía a una.
     * @param numeroRescatador
     * @param telefono
     * @param email
     */
    public void insertProtectora(int numeroRescatador, int telefono, String email) {

        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO PROTECTORA VALUES (?, ? ,?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, (String.valueOf(numeroRescatador)));
        statement.bindString(2, (String.valueOf(telefono)));
        statement.bindString(3, email);

        statement.executeInsert();
    }

    /**
     * Método que insertará a un usuario en la tabla RESCATISTA si indicó que pertenecía a una.
     * @param numeroRescatador
     * @param telefono
     * @param email
     */

    public void insertRescatista(int numeroRescatador, int telefono, String email) {

        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO RESCATISTA VALUES (?, ? ,?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, (String.valueOf(numeroRescatador)));
        statement.bindString(2, (String.valueOf(telefono)));
        statement.bindString(3, email);

        statement.executeInsert();
    }


    /**
     * Método utilizado para encontrar una mascota por su ID y actualizarla sus información de la base de datos
     * @param name
     * @param tipoMascota
     * @param edad
     * @param peso
     * @param color
     * @param raza
     * @param lugarDesaparicion
     * @param fechaDesaparicion
     * @param telefono
     * @param imagenMascota
     * @param id
     */
    public void updateData(String name, String tipoMascota, int edad, int peso, String color, String raza, String lugarDesaparicion, String fechaDesaparicion, int telefono, byte[] imagenMascota, int id) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "UPDATE MASCOTAS SET Nombre = ?, Tipo = ?, Edad = ?, Peso = ?, Color = ?, Raza = ?, Lugar_desaparicion = ?, Fecha_desaparicion = ?, TEL_USUARIO = ?, Imagen = ? WHERE ID_MASCOTA = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, name);
        statement.bindString(2, tipoMascota);
        statement.bindString(3, (String.valueOf(edad)));
        statement.bindString(4, (String.valueOf(peso)));
        statement.bindString(5, color);
        statement.bindString(6, raza);
        statement.bindString(7, lugarDesaparicion);
        statement.bindString(8, fechaDesaparicion);
        statement.bindString(9, (String.valueOf(telefono)));
        statement.bindBlob(10, imagenMascota);
        statement.bindString(11, String.valueOf(id));

        statement.execute();

    }

    /**
     * Método que insertará una registro en la tabla HISTORIAL si se añade una mascota o se modifica.
     * @param idMascota
     * @param telefono
     * @param nombreUsuario
     * @param nombreMascota
     * @param fechaDesaparicion
     * @param fechaAparicion
     * @param lugarDesaparicion
     * @param lugarAparicion
     */

    public void insertHistorial(int idMascota, int telefono, String nombreUsuario, String nombreMascota,
                                String fechaDesaparicion, String fechaAparicion, String lugarDesaparicion, String lugarAparicion) {

        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO HISTORIAL VALUES (NULL, ?, ? ,? ,? ,? ,?, ?, ?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, (String.valueOf(idMascota)));
        statement.bindString(2, (String.valueOf(telefono)));
        statement.bindString(3, nombreUsuario);
        statement.bindString(4, nombreMascota);
        statement.bindString(5, fechaDesaparicion);
        statement.bindString(6, lugarDesaparicion);
        // Optionals in form
        statement.bindString(7, fechaAparicion);
        statement.bindString(8, lugarAparicion);
        statement.executeInsert();
    }

    /**
     * Método que genera un nuevo registro en la tabla AVISO tras añadirse una mascota a través del formulario registro.
     * @param fechaAviso
     * @param idMascota
     * @param telefono
     */

    public void insertAvisos(String fechaAviso, int idMascota, int telefono) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO AVISOS VALUES (?, ? ,?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, fechaAviso);
        statement.bindString(2, (String.valueOf(idMascota)));
        statement.bindString(3, (String.valueOf(telefono)));

        statement.executeInsert();
    }

    /**
     * Método que borra una mascota en concreto pasándole su ID.
     * @param id
     */

    public void deleteData(int id) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "DELETE FROM MASCOTAS WHERE ID_MASCOTA = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, id);
        statement.execute();
        database.close();
    }

    /**
     * Inserta un registro en la tabla BORRADAS cuando el administrador o un usuario con privilegios borra una mascota.
     * @param fechaBorrado
     * @param id
     * @param telefono
     * @param motivo
     */

    public void insertDeletePets(String fechaBorrado, int id, int telefono, String motivo) {

        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO BORRADAS VALUES (?, ? ,? , ?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, fechaBorrado);
        statement.bindDouble(2, id);
        statement.bindString(3, (String.valueOf(telefono)));
        statement.bindString(4, motivo);
        statement.execute();
        database.close();

    }

    /**
     * Método encargado de añadir un usuario nuevo en la tabla USUARIOS.
     * @param telefono
     * @param nombre
     * @param email
     * @param ciudad
     * @param password
     */

    public void insertUserData(int telefono, String nombre, String email, String ciudad, String password) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO USUARIOS VALUES (?, ?, ? ,?, ?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, (String.valueOf(telefono)));
        statement.bindString(2, nombre);
        statement.bindString(3, email);
        statement.bindString(4, ciudad);
        statement.bindString(5, password);

        statement.executeInsert();

    }

    /**
     * Método que ejecuta una sentencia SQL y devuelve un cursor de objetos.
     * @param sql
     * @return
     */
    public Cursor getData(String sql) {
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    /*
    Métodos opcionales para usar en determinados casos.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
