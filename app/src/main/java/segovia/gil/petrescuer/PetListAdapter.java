package segovia.gil.petrescuer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Clase adapter que se encarga de gestionar los datos que se mostrarán en el componente RecyclerView.
 */

public class PetListAdapter extends BaseAdapter {

    private final Context context;
    private final int layout;
    private final ArrayList<Mascota> petList;

    public PetListAdapter(Context context, int layout, ArrayList<Mascota> petList) {
        this.context = context;
        this.layout = layout;
        this.petList = petList;
    }

    /**
     * Métodos encargados de obtener los objetos del array Mascota y la celdas correspondientes
     * en el RecyclerView
     *
     * @return
     */
    @Override
    public int getCount() {
        return petList.size();
    }

    @Override
    public Object getItem(int position) {
        return petList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        View row = view;
        ViewHolder holder = new ViewHolder();

        // Se inicializan y vinculas los elementos del holder.
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);
            holder.imgAnimal = (ImageView) row.findViewById(R.id.imgAnimal);
            holder.tvNombre = row.findViewById(R.id.tvNombre);
            holder.tvAnimal = row.findViewById(R.id.tvAnimal);
            //holder.tvPeso = row.findViewById(R.id.tvPeso);
            holder.tvColor = row.findViewById(R.id.tvColor);
            holder.tvRaza = row.findViewById(R.id.tvRaza);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        // Se persiste la información al elemento y añaden los elementos a la fila
        Mascota mascota = petList.get(position);
        byte[] petImage = mascota.getImagenMascota();
        Bitmap bitmap = BitmapFactory.decodeByteArray(petImage, 0, petImage.length);
        holder.imgAnimal.setImageBitmap(bitmap);
        holder.tvNombre.setText(mascota.getName());
        holder.tvAnimal.setText(mascota.getTipoMascota());
        //holder.tvPeso.setText(mascota.getPeso());
        holder.tvColor.setText(mascota.getColor());
        holder.tvRaza.setText(mascota.getRaza());

        return row;
    }

    /**
     * Clase ViewHolder, configurada para mostrar elementos como la imagen de mascota, nombre, tipo, color y raza.
     */

    private class ViewHolder {
        ImageView imgAnimal;
        TextView tvNombre, tvAnimal, tvPeso, tvColor, tvRaza;
    }
}
