package com.aplicacion.gestion_escolar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import kotlin.time.Instant;

public class ProfesorActivity extends AppCompatActivity {

    private ImageButton btnDesplegar;
    private DrawerLayout drawLayout;
    private NavigationView naView;


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
                Intent intent = new Intent(getBaseContext(), ProfesorActivity.class);
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

}