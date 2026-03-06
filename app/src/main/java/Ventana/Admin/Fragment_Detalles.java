package Ventana.Admin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aplicacion.gestion_escolar.R;


public class Fragment_Detalles extends Fragment {

    private String mParam1;
    private String mParam2;

   public Fragment_Detalles() {
   }

   public static Fragment_Detalles newInstance(String param1, String param2) {
        Fragment_Detalles fragment = new Fragment_Detalles();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__detalles, container, false);
    }
}