package com.engivi.enric.recursos_euskadi;


import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class OcioActivity extends ListActivity {

    ArrayList<HashMap<String, String>> mData = new ArrayList<HashMap<String, String>>();
    private SimpleAdapter mAdapter;

    // Search EditText
    EditText inputSearch;
    TextView txtViewCargando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.elementos);

        inputSearch = (EditText) findViewById(R.id.inputSearch);
        txtViewCargando = (TextView) findViewById(android.R.id.empty);
        inputSearch.setEnabled(false);


        String[] from = {"nombre", "tipo"};
        int[] to = {android.R.id.text1, android.R.id.text2};

        mAdapter = new SimpleAdapter(this, mData,
                android.R.layout.simple_list_item_2, from, to);
        setListAdapter(mAdapter);

        new DownloadTask().execute(10);

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                OcioActivity.this.mAdapter.getFilter().filter(cs);
                txtViewCargando.setText(R.string.noResultados);

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });


    }

    class DownloadTask extends AsyncTask<Integer, Void, String> {

        private static final String BASE_URL = "http://opendata.euskadi.eus/contenidos/ds_recursos_turisticos/recursos_ocio_euskadi/opendata/ocio.json?";

        @Override
        protected String doInBackground(Integer... params) {
            try {
                String jsonString = downloadData(params[0]);
                return jsonString;
            } catch (Exception e) {
                return null;
            }
        }

        private String downloadData(int param) throws ClientProtocolException,
                IOException {
            final HttpClient client = new DefaultHttpClient();
            final HttpGet request = new HttpGet(BASE_URL + param);

            final HttpResponse response = client.execute(request);

            final InputStream contentStream = response.getEntity().getContent();
            final BufferedReader reader = new BufferedReader(
                    new InputStreamReader(contentStream));

            final StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            return result.toString();
        }

        @Override
        protected void onPostExecute(String jsonString) {
            if (jsonString == null) {
                return;
            }

            final ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();

            try {
                JSONArray jsonArr = new JSONArray(jsonString);

                final int numRows = jsonArr.length();
                for (int x = 0; x < numRows; x++) {
                    final JSONObject row = jsonArr.getJSONObject(x);

                    final HashMap<String, String> ocio = new HashMap<String, String>();
                    ocio.put("nombre", row.getString("documentName"));
                    String tipoTienda = row.getString("templateType");
                    ocio.put("tipo", tipoTienda);
                    String descripcionLimpia = Html.fromHtml(row.getString("turismDescription")).toString();
                    ocio.put("descripcion", "" + descripcionLimpia);

                    String dir1 = row.getString("address");
                    String dir2 = row.getString("locality");
                    String direccion;
                    if (dir1.equals("")) {
                        direccion = dir2;
                        ocio.put("direccion", direccion);
                    }else{
                        direccion = dir1;
                        ocio.put("direccion", direccion);
                        }
                    if (dir1.equals("")&& dir2.equals("")){
                        direccion = "La ubicación no tiene una dirección específica";
                        ocio.put("direccion", direccion);
                    }

                    ocio.put("telefono", "" + row.getString("phoneNumber"));
                    ocio.put("web", row.getString("web"));
                    ocio.put("accesibilidad", "" + row.getString("accesibility"));

                    result.add(ocio);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mData.clear();
            mData.addAll(result);

            inputSearch.setEnabled(true);
            mAdapter.notifyDataSetChanged();
        }

    }

    /**
     * Al pulsar sobre un lugar abriremos su detalle
     */
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        //Toast.makeText(this,mData.get(position).get("accesibilidad"), Toast.LENGTH_SHORT).show();
        Intent intent_detalle = new Intent();
        intent_detalle.setClass(OcioActivity.this, DetalleActivityOcio.class);
        intent_detalle.putExtra("nombre", mData.get(position).get("nombre"));
        intent_detalle.putExtra("tipo", mData.get(position).get("tipo"));
        intent_detalle.putExtra("descripcion", mData.get(position).get("descripcion"));
        intent_detalle.putExtra("direccion", mData.get(position).get("direccion"));
        intent_detalle.putExtra("telefono", mData.get(position).get("telefono"));
        intent_detalle.putExtra("web", mData.get(position).get("web"));
        intent_detalle.putExtra("accessibilidad", mData.get(position).get("accesibilidad"));
        startActivity(intent_detalle);

    }
}