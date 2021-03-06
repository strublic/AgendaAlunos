package br.com.gledsonweb.agenda;

import android.*;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import br.com.gledsonweb.agenda.dao.AlunoDAO;
import br.com.gledsonweb.agenda.modelo.Aluno;

public class MapaFragment extends SupportMapFragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if(ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

            if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                ActivityCompat.requestPermissions(this.getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        111);

            }
//            else{
//                LatLng posicaoDaEscola = getAddress("Avenida Paulista, 1234, SAo Paulo");
//                if(posicaoDaEscola != null) {
//                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(posicaoDaEscola, 17);
//                    googleMap.moveCamera(update);
//                    if (posicaoDaEscola != null) {
//                        MarkerOptions marcador = new MarkerOptions();
//                        marcador.position(posicaoDaEscola);
//                        marcador.title("Escola da Agenda APP");
//                        googleMap.addMarker(marcador);
//                    }
//                }
//
//                new Localizador(getContext(), googleMap);
//            }

        }


        LatLng posicaoDaEscola = new LatLng(-23.564108, -46.652409);
        if (posicaoDaEscola != null) {
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(posicaoDaEscola, 17);
            googleMap.moveCamera(update);
            if (posicaoDaEscola != null) {
                MarkerOptions marcador = new MarkerOptions();
                marcador.position(posicaoDaEscola);
                marcador.title("Escola da Agenda APP");
                googleMap.addMarker(marcador);
            }
        }

        new Localizador(getContext(), googleMap);
//        AlunoDAO alunoDAO = new AlunoDAO(getContext());
//        for(Aluno aluno : alunoDAO.buscaAlunos()){
//            LatLng coordenada = getAddress(aluno.getEndereco());
//            if(coordenada != null){
//                MarkerOptions marcador = new MarkerOptions();
//                marcador.position(coordenada);
//                marcador.title(aluno.getNome());
//                marcador.snippet(String.valueOf(aluno.getNota()));
//                googleMap.addMarker(marcador);
//            }
//        }

//        alunoDAO.close();
    }

    private LatLng getAddress(String endereco){
        try {
            Geocoder geocoder = new Geocoder(getContext());
            List<Address> resultados = geocoder.getFromLocationName(endereco, 1);

            if(!resultados.isEmpty()){
                LatLng posicao = new LatLng(resultados.get(0).getLatitude(), resultados.get(0).getLongitude());
                return posicao;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
