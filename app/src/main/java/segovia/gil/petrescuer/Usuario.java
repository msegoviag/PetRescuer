package segovia.gil.petrescuer;


/**
 * Clase Usuario, el esqueleto a partir del cual creamos usuarios nuevos y lo añadimos a la base de datos.
 */
public class Usuario {

    private int telefono;
    private String nombre;
    private String email;
    private String ciudad;
    private String password;

    /**
     * Constructor Clase usuario, recoge por parámetro un teléfono, nombre, email, ciudad y contraseña.
     * @param telefono
     * @param nombre
     * @param email
     * @param ciudad
     * @param password
     */

    public Usuario(int telefono, String nombre, String email, String ciudad, String password) {
        this.telefono = telefono;
        this.nombre = nombre;
        this.email = email;
        this.ciudad = ciudad;
        this.password = password;
    }

    /**
     * Getters y setters clase Usuario
     * @return
     */
    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
