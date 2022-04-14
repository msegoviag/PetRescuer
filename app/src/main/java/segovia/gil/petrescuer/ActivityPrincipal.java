package segovia.gil.petrescuer;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


/**
 * Activity auxiliar donde se muestra el gridview que se usará a modo de lista dinámica (recyclerview)
 * mostrará las mascotas, también se encarga de inicar la barra de menú y sus opciones.
 */
public class ActivityPrincipal extends AppCompatActivity {

    String usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pet_list_activity);
    }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_add_pet) {
            Intent intent = new Intent(this, ActivityFormulario.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}