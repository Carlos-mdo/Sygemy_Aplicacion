package Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import Ventana.Admin.Fragment_DetalleProfesores;
import Ventana.Admin.Fragment_DetalleUsuarios;

public class TabAdapter extends FragmentStateAdapter {

    public TabAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {

            case 0:
                return new Fragment_DetalleUsuarios();

            case 1:
                return new Fragment_DetalleProfesores();

            default:
                return new Fragment_DetalleUsuarios();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}