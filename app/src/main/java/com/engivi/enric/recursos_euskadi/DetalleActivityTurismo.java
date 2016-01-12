package com.engivi.enric.recursos_euskadi;


import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class DetalleActivityTurismo extends Activity {

    TextView txtNombre,  txtDescripcion, txtWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalle_elemento_sinmapa);

        txtNombre = (TextView) findViewById(R.id.txtViewNombre);
        txtDescripcion = (TextView) findViewById(R.id.txtViewDescripcion);
        txtWeb = (TextView) findViewById(R.id.txtViewWeb);

    }

    @Override
    protected void onStart() {
        super.onStart();

        try {
            // Obtenemos el id del lugar que hay que mostrar
            Bundle extras = getIntent().getExtras();
            String stringNombre = extras.getString("nombre");
            txtNombre.setText(stringNombre);
            String stringDescripcion = extras.getString("descripcion");
            txtDescripcion.setText(stringDescripcion);
            String stringWeb = extras.getString("web");
            txtWeb.setText(stringWeb);


        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}