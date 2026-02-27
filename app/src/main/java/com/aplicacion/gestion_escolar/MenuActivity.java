package com.aplicacion.gestion_escolar;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import Datos.AdminSQLiteOpenHelper;

public class MenuActivity extends AppCompatActivity {

    private TextView txtBienvenida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MainActivity main = new MainActivity();

        txtBienvenida = findViewById(R.id.Bienvenida);
        txtBienvenida.setText("Hola");
    }

    public void menu(View view){

        txtBienvenida.setText("hola admin");

    }

}