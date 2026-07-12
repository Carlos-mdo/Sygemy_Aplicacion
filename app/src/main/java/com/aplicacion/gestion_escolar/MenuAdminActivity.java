package com.aplicacion.gestion_escolar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import Ventana.Admin.Fragment_Alumnos;
import Ventana.Admin.Fragment_DetalleUsuarios;
import Ventana.Admin.Fragment_MenuDetalles;
import Ventana.Admin.Fragment_Profesores;
import com.google.android.material.navigation.NavigationView;

public class MenuAdminActivity extends AppCompatActivity implements ControllerDrawerMenu{

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageButton btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_admin);

        ViewCompat.setOnApplyWindowInsetsListener( findViewById(R.id.main),
                (v, insets) -> {

                    Insets systemBars = insets.getInsets( WindowInsetsCompat.Type.systemBars());
                    v.setPadding( systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

                    return insets;
                });

        drawerLayout = findViewById(R.id.layoutAdministracion);
        navigationView = findViewById(R.id.navigationView);
        btnMenu = findViewById(R.id.iBtnMenuAdmin);

        btnMenu.setOnClickListener(v -> desplegarMenu());

        seleccionMenu();
        roles();
    }

    private void seleccionMenu() {
        SharedPreferences spref = getSharedPreferences("Roles", Context.MODE_PRIVATE);
        String rol = spref.getString("rol","");
        navigationView.setNavigationItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_profesores) {
                cargarFragment(new Fragment_Profesores());
                return true;
            }
            if (id == R.id.nav_alumnos) {
                cargarFragment(new Fragment_Alumnos());
                return true;
            }
            if (id == R.id.nav_detalles) {
                cargarFragment(new Fragment_MenuDetalles());
                return true;
            }
            if (id == R.id.nav_cerrar) {
                Intent intent = new Intent(
                        MenuAdminActivity.this,
                        MainActivity.class
                );

                startActivity(intent);
                finish();
                return true;
            }
            return false;
        });
    }
    public void roles(){
        SharedPreferences spref = getSharedPreferences("Roles", Context.MODE_PRIVATE);
        String rol = spref.getString("rol","sin_rol");

        activarRol(rol);
    }
    public void activarRol(String rol){
        Menu menu = navigationView.getMenu();

        menu.setGroupVisible(R.id.grupo_profesores, rol.equals("profe"));
        menu.setGroupVisible(R.id.grupo_alumnos, rol.equals("alumn"));
        menu.setGroupVisible(R.id.grupo_admin, rol.equals("admin"));

    }

    private void cargarFragment(Fragment fragment) {

        View scrollBienvenida = findViewById(R.id.scrollBienvenAdmin);
        View contenedorFragment = findViewById(R.id.contenFragmentAdmin);

        if (scrollBienvenida != null) {
            scrollBienvenida.setVisibility(View.GONE);
        }

        if (contenedorFragment != null) {
            contenedorFragment.setVisibility(View.VISIBLE);
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contenFragmentAdmin, fragment)
                .commit();

        cerrarNav();
    }

    public void abrirNav() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void cerrarNav() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void desplegarMenu() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            cerrarNav();
        } else {
            abrirNav();
        }
    }
}

