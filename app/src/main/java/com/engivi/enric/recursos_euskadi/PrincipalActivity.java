package com.engivi.enric.recursos_euskadi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class PrincipalActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_principal);

        // Acceso a los ImageButtons por el id
        ImageButton imgbtn_restaurant = (ImageButton) findViewById(R.id.imgbtn_restaurant);
        ImageButton imgbtn_bed = (ImageButton) findViewById(R.id.imgbtn_bed);
        ImageButton imgbtn_compras = (ImageButton) findViewById(R.id.imgbtn_compras);
        ImageButton imgbtn_information = (ImageButton) findViewById(R.id.imgbtn_information);
        ImageButton imgbtn_culture= (ImageButton) findViewById(R.id.imgbtn_culture);
        ImageButton imgbtn_ocio= (ImageButton) findViewById(R.id.imgbtn_ocio);
        ImageButton imgbtn_desturisticos= (ImageButton) findViewById(R.id.imgbtn_dest_turisticos);

        imgbtn_restaurant.setOnClickListener(this);
        imgbtn_bed.setOnClickListener(this);
        imgbtn_compras.setOnClickListener(this);
        imgbtn_information.setOnClickListener(this);
        imgbtn_culture.setOnClickListener(this);
        imgbtn_ocio.setOnClickListener(this);
        imgbtn_desturisticos.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.imgbtn_restaurant:

                Intent intent_restaurantes = new Intent(this,RestaurantesActivity.class);
                startActivity(intent_restaurantes);

                break;

            case R.id.imgbtn_bed:

                Intent intent_alojamientos = new Intent(this,AlojamientosActivity.class);
                startActivity(intent_alojamientos);

                break;

            case R.id.imgbtn_compras:

                Intent intent_compras = new Intent(this,ComprasActivity.class);
                startActivity(intent_compras);

                break;


            case R.id.imgbtn_information:

                Intent intent_ofiTurismo = new Intent(this,OficinaTurismoActivity.class);
                startActivity(intent_ofiTurismo);

                break;

            case R.id.imgbtn_culture:

                Intent intent_patrimonio = new Intent(this,PatrimonioActivity.class);
                startActivity(intent_patrimonio);

                break;

            case R.id.imgbtn_ocio:
                Intent intent_ocio = new Intent(this,OcioActivity.class);
                startActivity(intent_ocio);

                break;

            case R.id.imgbtn_dest_turisticos:
                Intent intent_destTuris = new Intent(this,TurismoActivity.class);
                startActivity(intent_destTuris);


                break;

        }

    }
}
