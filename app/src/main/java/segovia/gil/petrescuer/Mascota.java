package segovia.gil.petrescuer;

/**
 * Esqueleto de la clase mascota que se usará para crear un objeto Mascota en otras clases del proyecto.
 */
public class Mascota {

    private int id;
    private String name;
    private String tipoMascota;
    private int edad;
    private int peso;
    private String color;
    private String raza;
    private String lugarDesaparicion;
    private String fechaDesaparicion;
    private int telefono;
    private byte[] imagenMascota;

    /**
     * Se construye pasando como parámetro el id de la mascota, el nombre, el tipo, la edad, peso, color, raza
     * lugar de desaparicción, fecha de desaparición, número de teléfono del propiertario o persona que le añadió y una imagen si corresponde.
     *
     * @param id
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

    public Mascota(int id, String name, String tipoMascota, int edad, int peso, String color, String raza, String lugarDesaparicion, String fechaDesaparicion, int telefono, byte[] imagenMascota) {
        this.id = id;
        this.name = name;
        this.tipoMascota = tipoMascota;
        this.edad = edad;
        this.peso = peso;
        this.color = color;
        this.raza = raza;
        this.lugarDesaparicion = lugarDesaparicion;
        this.fechaDesaparicion = fechaDesaparicion;
        this.telefono = telefono;
        this.imagenMascota = imagenMascota;
    }


    /**
     * Getters y setters.
     * @return
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTipoMascota() {
        return tipoMascota;
    }

    public void setTipoMascota(String tipoMascota) {
        this.tipoMascota = tipoMascota;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public String getLugarDesaparicion() {
        return lugarDesaparicion;
    }

    public void setLugarDesaparicion(String lugarDesaparicion) {
        this.lugarDesaparicion = lugarDesaparicion;
    }

    public String getFechaDesaparicion() {
        return fechaDesaparicion;
    }

    public void setFechaDesaparicion(String fechaDesaparicion) {
        this.fechaDesaparicion = fechaDesaparicion;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public byte[] getImagenMascota() {
        return imagenMascota;
    }

    public void setImagenMascota(byte[] imagenMascota) {
        this.imagenMascota = imagenMascota;
    }
}
