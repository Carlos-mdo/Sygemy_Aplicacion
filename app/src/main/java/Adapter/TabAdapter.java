package Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import Ventana.Admin.Fragment_Alumnos;
import Ventana.Admin.Fragment_Detalles;
import Ventana.Admin.Fragment_Profesores;

public class TabAdapter extends FragmentStateAdapter {

    public TabAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position){
            case 0:
                return new Fragment_Profesores();
            case 1:
                return new Fragment_Alumnos();
            case 2:
                return new Fragment_Detalles();
        }

        return null;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
