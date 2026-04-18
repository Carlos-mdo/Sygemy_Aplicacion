package com.aplicacion.gestion_escolar;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import Adapter.TabAdapter;

public class MenuAdminActivity extends AppCompatActivity {

    public TabLayout tabLay;
    public ViewPager2 view2;

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

        tabLay = findViewById(R.id.tabLayout);
        view2 = findViewById(R.id.tabView2);

        TabAdapter adapterTab = new TabAdapter(this);

        view2.setAdapter(adapterTab);

        final String[] titles = new String[]{"Profesores", "Alumnos", "Detalles"};

        new TabLayoutMediator(tabLay, view2,
                (tab, position) -> tab.setText(titles[position])
        ).attach();

    }

}