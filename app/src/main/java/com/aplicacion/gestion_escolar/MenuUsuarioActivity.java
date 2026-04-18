package com.aplicacion.gestion_escolar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MenuUsuarioActivity extends AppCompatActivity {

    private ImageButton btnDesplegar;
    private DrawerLayout drawLayout;
    private NavigationView naView;
    private String rolProf,rolAlum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profesor);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        naView = findViewById(R.id.navigationView);
        drawLayout = findViewById(R.id.layoutEscuela);
        btnDesplegar = findViewById(R.id.iBtnMenu);

        btnDesplegar.setOnClickListener(view -> desplegarMenu());

        seleccionMenu();
        roles();
    }

    public void desplegarMenu() {
        if (drawLayout.isDrawerOpen(GravityCompat.START)) {
            drawLayout.closeDrawer(GravityCompat.START);
        } else {
            drawLayout.openDrawer(GravityCompat.START);
        }
    }

    public void seleccionMenu(){
        naView.setNavigationItemSelectedListener(item -> {

            if(item.getItemId() == R.id.nav_inicio){

                Intent intent = new Intent(getBaseContext(), MenuUsuarioActivity.class);
                startActivity(intent);

                finish();
                return true;
            }

            if(item.getItemId() == R.id.nav_cerrar){

                Intent intent = new Intent(getBaseContext(), MainActivity.class);
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
        Menu menu = naView.getMenu();

        menu.setGroupVisible(R.id.grupo_profesores, rol.equals("profe"));
        menu.setGroupVisible(R.id.grupo_alumnos, rol.equals("alumn"));

    }
}