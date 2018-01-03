package com.example.sebastian.adminapp;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.ui.IconGenerator;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import javax.net.ssl.HttpsURLConnection;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {



    ProgressDialog pd;

    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(MapsActivity.this);
            pd.setMessage("Descargando datos..");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()){
                pd.dismiss();
            }
            try{

                JSONObject jObject = new JSONObject(result);
                String aJsonString = jObject.getString("minutos");
                minutos = aJsonString;

                Log.d("myTag", minutos);
                alertTest(minutos);
            }
            catch (JSONException e) {}
            finally {
                minutos = "lol";
            }
        }
    }

    LatLng trayecto_inicio;
    LatLng trayecto_fin;
    Date fecha;
    String hora;

    String minutos;

    int puntos_elegidos = 0;

    private GoogleMap mMap;

    Marker start,finish;

    List<LatLng> routePoints;

     BooVariable bv = new BooVariable();

    @Override
    public void onBackPressed() {

        if(puntos_elegidos==0){
            finish();
        }
        if (puntos_elegidos == 1) {
            start.remove();
            puntos_elegidos--;
        }
        if (puntos_elegidos == 2) {
            finish.remove();
            puntos_elegidos--;
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Bienvenid@");
        alert.setMessage("Selecciona donde comienza y termina tu viaje, presionando durante 1 segundo sobre el recorrido marcado");


        alert.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        /*alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });*/

        alert.show();



    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options, menu);
        return true;
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        UiSettings uiSettings = mMap.getUiSettings();

        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);

        PolylineOptions rectOptions = new PolylineOptions().width(15).color(Color.parseColor("#448aff")).geodesic(true);
        rectOptions.add(new LatLng(-30.14591167,-51.12986667));
        rectOptions.add(new LatLng(-30.14659667,-51.13059667));
        rectOptions.add(new LatLng(-30.147375,-51.13100833));
        rectOptions.add(new LatLng(-30.147865,-51.13129333));
        rectOptions.add(new LatLng(-30.14861667,-51.13171667));
        rectOptions.add(new LatLng(-30.14936167,-51.13211333));
        rectOptions.add(new LatLng(-30.15011,-51.13266333));
        rectOptions.add(new LatLng(-30.15079333,-51.13329333));
        rectOptions.add(new LatLng(-30.151265,-51.13386333));
        rectOptions.add(new LatLng(-30.151595,-51.13475833));
        rectOptions.add(new LatLng(-30.15246167,-51.13542));
        rectOptions.add(new LatLng(-30.15310167,-51.13582833));
        rectOptions.add(new LatLng(-30.15341333,-51.13685333));
        rectOptions.add(new LatLng(-30.15413833,-51.13810167));
        rectOptions.add(new LatLng(-30.15425833,-51.13834833));
        rectOptions.add(new LatLng(-30.15481833,-51.13943167));
        rectOptions.add(new LatLng(-30.15509333,-51.14024667));
        rectOptions.add(new LatLng(-30.15532167,-51.141175));
        rectOptions.add(new LatLng(-30.15541667,-51.14151167));
        rectOptions.add(new LatLng(-30.15574667,-51.14249667));
        rectOptions.add(new LatLng(-30.15607167,-51.14332833));
        rectOptions.add(new LatLng(-30.15655667,-51.14386));
        rectOptions.add(new LatLng(-30.15711833,-51.14450167));
        rectOptions.add(new LatLng(-30.157965,-51.14543167));
        rectOptions.add(new LatLng(-30.15888833,-51.14663667));
        rectOptions.add(new LatLng(-30.15984667,-51.14788167));
        rectOptions.add(new LatLng(-30.16118,-51.14890833));
        rectOptions.add(new LatLng(-30.16239,-51.14994833));
        rectOptions.add(new LatLng(-30.16391,-51.15146333));
        rectOptions.add(new LatLng(-30.16478,-51.15291667));
        rectOptions.add(new LatLng(-30.16538,-51.15419667));
        rectOptions.add(new LatLng(-30.16538,-51.15419667));
        rectOptions.add(new LatLng(-30.16519833,-51.15528167));
        rectOptions.add(new LatLng(-30.16465333,-51.15535667));
        rectOptions.add(new LatLng(-30.163355,-51.15582667));
        rectOptions.add(new LatLng(-30.16218667,-51.15776833));
        rectOptions.add(new LatLng(-30.161125,-51.15968667));
        rectOptions.add(new LatLng(-30.15910333,-51.16071167));
        rectOptions.add(new LatLng(-30.157625,-51.161865));
        rectOptions.add(new LatLng(-30.156445,-51.16351));
        rectOptions.add(new LatLng(-30.156445,-51.16482167));
        rectOptions.add(new LatLng(-30.15642833,-51.16626833));
        rectOptions.add(new LatLng(-30.15637,-51.16794));
        rectOptions.add(new LatLng(-30.15625333,-51.16994333));
        rectOptions.add(new LatLng(-30.15600333,-51.17186333));
        rectOptions.add(new LatLng(-30.155665,-51.17369667));
        rectOptions.add(new LatLng(-30.155515,-51.17526833));
        rectOptions.add(new LatLng(-30.15525833,-51.17721333));
        rectOptions.add(new LatLng(-30.15534333,-51.178735));
        rectOptions.add(new LatLng(-30.15544,-51.18052167));
        rectOptions.add(new LatLng(-30.15563,-51.18186667));
        rectOptions.add(new LatLng(-30.15618667,-51.18351167));
        rectOptions.add(new LatLng(-30.15629667,-51.18562333));
        rectOptions.add(new LatLng(-30.15720167,-51.18777333));
        rectOptions.add(new LatLng(-30.158095,-51.18911667));
        rectOptions.add(new LatLng(-30.15831167,-51.19004));
        rectOptions.add(new LatLng(-30.15847667,-51.1911));
        rectOptions.add(new LatLng(-30.158535,-51.19158667));
        rectOptions.add(new LatLng(-30.15844833,-51.19343833));
        rectOptions.add(new LatLng(-30.15774833,-51.19561167));
        rectOptions.add(new LatLng(-30.15637333,-51.19731167));
        rectOptions.add(new LatLng(-30.15501833,-51.19921333));
        rectOptions.add(new LatLng(-30.15346333,-51.20051667));
        rectOptions.add(new LatLng(-30.1519,-51.20107667));
        rectOptions.add(new LatLng(-30.14983333,-51.2019));
        rectOptions.add(new LatLng(-30.14812167,-51.20362667));
        rectOptions.add(new LatLng(-30.14789667,-51.20391));
        rectOptions.add(new LatLng(-30.14757,-51.20509833));
        rectOptions.add(new LatLng(-30.14752167,-51.205765));
        rectOptions.add(new LatLng(-30.14724833,-51.20833833));
        rectOptions.add(new LatLng(-30.14639333,-51.21064333));
        rectOptions.add(new LatLng(-30.14528667,-51.213035));
        rectOptions.add(new LatLng(-30.143985,-51.21512833));
        rectOptions.add(new LatLng(-30.14281167,-51.217205));
        rectOptions.add(new LatLng(-30.14151,-51.21908667));
        rectOptions.add(new LatLng(-30.13990667,-51.22010667));
        rectOptions.add(new LatLng(-30.139565,-51.22030833));
        rectOptions.add(new LatLng(-30.13801,-51.22011167));
        rectOptions.add(new LatLng(-30.13801,-51.22011167));
        rectOptions.add(new LatLng(-30.13593833,-51.21913333));
        rectOptions.add(new LatLng(-30.13376,-51.21823667));
        rectOptions.add(new LatLng(-30.13149167,-51.21793333));
        rectOptions.add(new LatLng(-30.12965,-51.21848833));

        // Mitad

        rectOptions.add(new LatLng(-30.125636670,-51.220008330));
        rectOptions.add(new LatLng(-30.123310,-51.220856670));
        rectOptions.add(new LatLng(-30.122488330,-51.221520));
        rectOptions.add(new LatLng(-30.120171670,-51.223891670));
        rectOptions.add(new LatLng(-30.119741670,-51.224036670));
        rectOptions.add(new LatLng(-30.117708330,-51.224263330));
        rectOptions.add(new LatLng(-30.114613330,-51.225856670));
        rectOptions.add(new LatLng(-30.112540,-51.226490));
        rectOptions.add(new LatLng(-30.111141670,-51.226716670));
        rectOptions.add(new LatLng(-30.108793330,-51.226996670));
        rectOptions.add(new LatLng(-30.107480,-51.227448330));
        rectOptions.add(new LatLng(-30.105371670,-51.229606670));
        rectOptions.add(new LatLng(-30.104050,-51.230));
        rectOptions.add(new LatLng(-30.1028350,-51.230601670));
        rectOptions.add(new LatLng(-30.1023550,-51.230843330));
        rectOptions.add(new LatLng(-30.101613330,-51.230996670));
        rectOptions.add(new LatLng(-30.101268330,-51.231040));
        rectOptions.add(new LatLng(-30.100548330,-51.231141670));
        rectOptions.add(new LatLng(-30.098831670,-51.231346670));
        rectOptions.add(new LatLng(-30.098458330,-51.231413330));
        rectOptions.add(new LatLng(-30.097470,-51.23080));
        rectOptions.add(new LatLng(-30.096981670,-51.228073330));
        rectOptions.add(new LatLng(-30.0954550,-51.225931670));
        rectOptions.add(new LatLng(-30.095328330,-51.2257450));
        rectOptions.add(new LatLng(-30.0950550,-51.225308330));
        rectOptions.add(new LatLng(-30.093116670,-51.223241670));
        rectOptions.add(new LatLng(-30.090533330,-51.219251670));
        rectOptions.add(new LatLng(-30.086648330,-51.215736670));
        rectOptions.add(new LatLng(-30.084186670,-51.2140550));
        rectOptions.add(new LatLng(-30.082528330,-51.213186670));
        rectOptions.add(new LatLng(-30.081601670,-51.211591670));
        rectOptions.add(new LatLng(-30.081368330,-51.210743330));
        rectOptions.add(new LatLng(-30.080206670,-51.209676670));
        rectOptions.add(new LatLng(-30.078156670, -51.208103330));
        rectOptions.add(new LatLng(-30.076158330,-51.207930));
        rectOptions.add(new LatLng(-30.073250, -51.208998330));
        rectOptions.add(new LatLng(-30.071166670,-51.2101950));
        rectOptions.add(new LatLng(-30.069046670,-51.2102550));
        rectOptions.add(new LatLng(-30.067320,-51.2106850));
        rectOptions.add(new LatLng(-30.065556670,-51.212018330));
        rectOptions.add(new LatLng(-30.065323330,-51.212376670));
        rectOptions.add(new LatLng(-30.064880,-51.212283330));
        rectOptions.add(new LatLng(-30.059046670,-51.212628330));
        rectOptions.add(new LatLng(-30.056248330,-51.214678330));
        rectOptions.add(new LatLng(-30.055743330, -51.214841670));
        rectOptions.add(new LatLng(-30.054178330,-51.2142550));
        rectOptions.add(new LatLng(-30.052683330,-51.2135950));
        rectOptions.add(new LatLng(-30.051623330,-51.213151670));
        rectOptions.add(new LatLng(-30.050953330, -51.212580));
        rectOptions.add(new LatLng(-30.049786670,-51.210576670));
        rectOptions.add(new LatLng(-30.049601670,-51.210266670));
        rectOptions.add(new LatLng(-30.048150,-51.2108150));
        rectOptions.add(new LatLng(-30.047386670,-51.211378330));
        rectOptions.add(new LatLng(-30.043913330,-51.214088330));
        rectOptions.add(new LatLng(-30.043570,-51.214316670));
        rectOptions.add(new LatLng(-30.043476670,-51.214403330));
        rectOptions.add(new LatLng(-30.0417750,-51.215721670));
        rectOptions.add(new LatLng(-30.040866670,-51.216388330));
        rectOptions.add(new LatLng(-30.039483330,-51.217496670));
        rectOptions.add(new LatLng(-30.037076670,-51.219371670));
        rectOptions.add(new LatLng(-30.0352750,-51.220741670));
        rectOptions.add(new LatLng(-30.034543330,-51.221331670));
        rectOptions.add(new LatLng(-30.033880,-51.221888330));
        rectOptions.add(new LatLng(-30.033651670,-51.222043330));
        rectOptions.add(new LatLng(-30.033490,-51.2221750));
        rectOptions.add(new LatLng(-30.033196670,-51.222411670));
        rectOptions.add(new LatLng(-30.033036670,-51.222518330));
        rectOptions.add(new LatLng(-30.0320750, -51.223343330));
        rectOptions.add(new LatLng(-30.031740, -51.223591670));
        rectOptions.add(new LatLng(-30.031616670,-51.223681670));
        rectOptions.add(new LatLng(-30.031408330,-51.223786670));
        rectOptions.add(new LatLng(-30.031311670,-51.223756670));
        rectOptions.add(new LatLng(-30.0309650,-51.224276670));
        rectOptions.add(new LatLng(-30.030790, -51.226216670));
        rectOptions.add(new LatLng(-30.030846670,-51.227208330));
        rectOptions.add(new LatLng(-30.030960,-51.227358330));


        routePoints = rectOptions.getPoints();

        Polyline polyline = mMap.addPolyline(rectOptions);

        LatLng inicio = new LatLng(-30.14591167,-51.12986667);
        LatLng fin = new LatLng(-30.030960,-51.227358330);
        mMap.addMarker(new MarkerOptions().position(inicio).title("Inicio Recorrido"));
        mMap.addMarker(new MarkerOptions().position(fin).title("Fin Recorrido"));




        final IconGenerator iconFactory = new IconGenerator(this);
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng point) {

                minutos = "foo";
                if(puntos_elegidos<2){
                    if(puntos_elegidos==0){
                        iconFactory.setStyle(IconGenerator.STYLE_DEFAULT);
                        point = rectificarPunto(point);
                        trayecto_inicio = point;
                        addIcon(iconFactory, "Inicio", point);
                    }
                    else{
                        iconFactory.setStyle(IconGenerator.STYLE_RED);
                        point = rectificarPunto(point);
                        trayecto_fin = point;
                        addIcon(iconFactory, "Fin", point);

                        Log.d("myTag", minutos);
                        //new JsonTask().execute("http://ip.jsontest.com");
                        String lat_inicio = String.valueOf(trayecto_inicio.latitude);
                        String lng_inicio = String.valueOf(trayecto_inicio.longitude);
                        String lat_fin = String.valueOf(trayecto_fin.latitude);
                        String lng_fin = String.valueOf(trayecto_fin.longitude);

                          DateFormat df = new SimpleDateFormat("MM-dd");
                          DateFormat ff = new SimpleDateFormat("HH:mm:ss");

                        String fecha = df.format(Calendar.getInstance().getTime());
                        String hora = ff.format(Calendar.getInstance().getTime());

                        Log.d("Fecha ", fecha);
                        Log.d("Hora ", hora);
                        String url = "http://192.81.219.132/api/script/"+lat_inicio+"/"+lng_inicio+"/"+lat_fin+"/"+lng_fin+"/"+hora+"/"+fecha;
                        new JsonTask().execute(url);
                        Log.d("url ", url);
                        /*try{

                            JSONObject jObject = new JSONObject(result);
                            String aJsonString = jObject.getString("ip");
                            minutos = aJsonString;
                        }
                        catch (JSONException e) {}
                        finally {
                            minutos = "lol";
                        }*/
                    }
                    puntos_elegidos++;
                }



            }

        });


        bv.setListener(new BooVariable.ChangeListener() {
            @Override
            public void onChange() {
                //alertTest(minutos);
            }
        });


    }

    //
    //
    //  String url_JSON = "https://localhost:8080/app/-30.032321670/-51.2276750/-30.144076670/-51.1322650/17:00:00/28-05-17"
    //
    //

    public static String getJSON(String url) {
        HttpsURLConnection con = null;
        try {
            URL u = new URL(url);
            con = (HttpsURLConnection) u.openConnection();

            con.connect();


            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            return sb.toString();


        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.disconnect();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }


    public LatLng rectificarPunto(LatLng point){

        LatLng ptoRectificado = new LatLng(point.latitude,point.longitude);
        List<Double> distancias = new ArrayList<Double>();
        for(LatLng pto: routePoints){

            distancias.add(distance(pto.latitude,pto.longitude, point.latitude,point.longitude));

        }
        ptoRectificado = routePoints.get(getIndexOfMin(distancias));
        return ptoRectificado;
    }

    public int getIndexOfMin(List<Double> data) {
        double min = Float.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < data.size(); i++) {
            Double f = data.get(i);
            if (Double.compare(f.doubleValue(), min) < 0) {
                min = f.doubleValue();
                index = i;
            }
        }
        return index;
    }

    private double distance(double lat1, double lng1, double lat2, double lng2) {

        double earthRadius = 3958.75; // in miles, change to 6371 for kilometer output

        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double dist = earthRadius * c;

        return dist; // output distance, in MILES
    }


    public void addIcon(IconGenerator iconFactory, CharSequence text, LatLng position) {
        MarkerOptions markerOptions = new MarkerOptions().
                icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(text))).
                position(position)
                .draggable(true)
                .anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());


        if(puntos_elegidos==0){
            start =   mMap.addMarker(markerOptions);
        }
        else{
            finish =   mMap.addMarker(markerOptions);
            //int i = 99999999;
            //while (i>0) i--;
            bv.setBoo(false);
        }
    }

    public void alertTest(String minutos) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tiempo de viaje estimado:");
        minutos = minutos.replace("[", "");
        minutos = minutos.replace("]", "");
        minutos = minutos.replace("\"", "");
        builder.setMessage(minutos);
        builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.show();

// Must call show() prior to fetching text view
        TextView messageView = (TextView)dialog.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);
/*
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        //alert.setTitle("Bienvenid@");
        alert.setMessage("Tiempo de viaje estimado: 23 minutos");

        // Set an EditText view to get user input


        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = "testing aaaaaa sjsdh ssss";
                // Do something with value!
            }
        });

        *//*alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });*//*

        alert.show();

        TextView messageView = (TextView)dialog.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);*/
    }

    public String getTiempo_estimado(LatLng inicio, LatLng fin){

        String time_estimate="-11 minutos";



        return time_estimate;
    }

/*    public void getPointsRoute(){
        //List<Double> points = new ArrayList<Double>();
        arrPointsRoute = new ArrayList<Double>();
        // [lat,long ] invertido
        String strRoute = "[-51.12986667, -30.14591167], [-51.13059667, -30.14659667], [-51.13100833, -30.147375], [-51.13129333, -30.147865], [-51.13171667, -30.14861667], [-51.13211333, -30.14936167], [-51.13266333, -30.15011], [-51.13329333, -30.15079333], [-51.13386333, -30.151265], [-51.13475833, -30.151595], [-51.13542, -30.15246167], [-51.13582833, -30.15310167], [-51.13685333, -30.15341333], [-51.13810167, -30.15413833], [-51.13834833, -30.15425833], [-51.13943167, -30.15481833], [-51.14024667,-30.15509333], [-51.141175, -30.15532167], [-51.14151167, -30.15541667], [-51.14249667, -30.15574667], [-51.14332833, -30.15607167], [-51.14386, -30.15655667], [-51.14450167, -30.15711833], [-51.14543167, -30.157965], [-51.14663667, -30.15888833], [-51.14788167, -30.15984667], [-51.14890833, -30.16118], [-51.14994833, -30.16239], [-51.15146333, -30.16391], [-51.15291667, -30.16478], [-51.15419667, -30.16538], [-51.15419667, -30.16538], [-51.15528167, -30.16519833], [-51.15535667, -30.16465333], [-51.15582667, -30.163355], [-51.15776833, -30.16218667], [-51.15968667, -30.161125], [-51.16071167, -30.15910333], [-51.161865, -30.157625], [-51.16351, -30.156445], [-51.16482167, -30.156445], [-51.16626833, -30.15642833], [-51.16794, -30.15637], [-51.16994333, -30.15625333], [-51.17186333, -30.15600333], [-51.17369667, -30.155665], [-51.17526833, -30.155515], [-51.17721333, -30.15525833], [-51.178735, -30.15534333], [-51.18052167, -30.15544], [-51.18186667, -30.15563], [-51.18351167, -30.15618667], [-51.18562333, -30.15629667], [-51.18777333, -30.15720167], [-51.18911667, -30.158095], [-51.19004, -30.15831167], [-51.1911, -30.15847667], [-51.19158667, -30.158535], [-51.19343833, -30.15844833], [-51.19561167, -30.15774833], [-51.19731167, -30.15637333], [-51.19921333, -30.15501833], [-51.20051667, -30.15346333], [-51.20107667, -30.1519], [-51.2019, -30.14983333], [-51.20362667, -30.14812167], [-51.20391, -30.14789667], [-51.20509833, -30.14757], [-51.205765, -30.14752167], [-51.20833833, -30.14724833], [-51.21064333, -30.14639333], [-51.213035, -30.14528667], [-51.21512833, -30.143985], [-51.217205, -30.14281167], [-51.21908667, -30.14151], [-51.22010667, -30.13990667], [-51.22030833, -30.139565], [-51.22011167, -30.13801], [-51.22011167, -30.13801], [-51.21913333, -30.13593833], [-51.21823667, -30.13376], [-51.21793333, -30.13149167], [-51.21848833, -30.12965], [-51.21939167, -30.12770333], [-51.22002333, -30.12579333], [-51.22049833, -30.12422833], [-51.22100833, -30.12325667], [-51.22138667, -30.12265167], [-51.22171333, -30.12237667], [-51.22308167, -30.12113333], [-51.22415667, -30.11937167], [-51.224475, -30.11713833], [-51.22564167, -30.115], [-51.22635167, -30.11313333], [-51.22677833, -30.111265], [-51.22694333, -30.109505], [-51.22704667, -30.10863333], [-51.22727167, -30.107825], [-51.22743, -30.1076], [-51.22865833, -30.10655167], [-51.229875, -30.10468667], [-51.23079667, -30.10268333], [-51.23090167, -30.102445], [-51.23102, -30.10162], [-51.23115333, -30.1008], [-51.23149833, -30.098585], [-51.23152667, -30.09834833], [-51.23078, -30.097425], [-51.23000833, -30.097355], [-51.22732167, -30.09663167], [-51.22599333, -30.09552], [-51.22576667, -30.09536167], [-51.22524, -30.09502167], [-51.22458, -30.09451], [-51.22226833, -30.09233833], [-51.21989333, -30.091165], [-51.21826167, -30.08958], [-51.21667, -30.08786333], [-51.21505833, -30.08569667], [-51.213475, -30.08328667], [-51.21179, -30.08164833], [-51.20970333, -30.08028333], [-51.20890833, -30.07913333], [-51.20806167, -30.07811], [-51.20771, -30.07736167], [-51.20794333, -30.07592667], [-51.20811, -30.07483167], [-51.208775, -30.072655], [-51.20995333, -30.07124333], [-51.21030667, -30.06938833], [-51.210965, -30.06869833], [-51.21050667, -30.06640167], [-51.21137, -30.06570833], [-51.21205167, -30.06555167], [-51.212335, -30.06519], [-51.21221, -30.064625], [-51.21166167, -30.06202], [-51.21210667, -30.05974833], [-51.21369833, -30.05764333], [-51.21475167, -30.05620333], [-51.21486667, -30.05589], [-51.214845, -30.055775], [-51.21428, -30.054395], [-51.213445, -30.05239833], [-51.212215, -30.050725], [-51.21073833, -30.04983167], [-51.21009333, -30.049115], [-51.21128167, -30.04761667], [-51.21272167, -30.045765], [-51.21410833, -30.04390833], [-51.21447667, -30.04344667], [-51.21582833, -30.04169333], [-51.21582833, -30.04169333], [-51.21766833, -30.039395], [-51.21940167, -30.03714667], [-51.22075, -30.03530833], [-51.22111667, -30.03487833], [-51.221475, -30.03445167], [-51.222185, -30.03358667], [-51.22234833, -30.03338833], [-51.22272667, -30.03292833], [-51.22365667, -30.031675], [-51.22371167, -30.03158167], [-51.223855, -30.031305], [-51.22441167, -30.031045], [-51.22609833, -30.03097167]]";
        String[] arrRoute = strRoute.split("\\s");  //        String strRoute = "[-51.12986667,
                                                                            // -30.14591167],
        //                                                                     [-51.13059667,
        //                                                                     -30.14659667],
        //                                                                     [-51.13100833,
        //                                                                       -30.147375],
        // [-51.13129333, -30.147865], [-51.13171667, -30.14861667], [-51.13211333, -30.14936167], [-51.13266333, -30.15011], [-51.13329333, -30.15079333], [-51.13386333, -30.151265], [-51.13475833, -30.151595], [-51.13542, -30.15246167], [-51.13582833, -30.15310167], [-51.13685333, -30.15341333], [-51.13810167, -30.15413833], [-51.13834833, -30.15425833], [-51.13943167, -30.15481833], [-51.14024667,-30.15509333], [-51.141175, -30.15532167], [-51.14151167, -30.15541667], [-51.14249667, -30.15574667], [-51.14332833, -30.15607167], [-51.14386, -30.15655667], [-51.14450167, -30.15711833], [-51.14543167, -30.157965], [-51.14663667, -30.15888833], [-51.14788167, -30.15984667], [-51.14890833, -30.16118], [-51.14994833, -30.16239], [-51.15146333, -30.16391], [-51.15291667, -30.16478], [-51.15419667, -30.16538], [-51.15419667, -30.16538], [-51.15528167, -30.16519833], [-51.15535667, -30.16465333], [-51.15582667, -30.163355], [-51.15776833, -30.16218667], [-51.15968667, -30.161125], [-51.16071167, -30.15910333], [-51.161865, -30.157625], [-51.16351, -30.156445], [-51.16482167, -30.156445], [-51.16626833, -30.15642833], [-51.16794, -30.15637], [-51.16994333, -30.15625333], [-51.17186333, -30.15600333], [-51.17369667, -30.155665], [-51.17526833, -30.155515], [-51.17721333, -30.15525833], [-51.178735, -30.15534333], [-51.18052167, -30.15544], [-51.18186667, -30.15563], [-51.18351167, -30.15618667], [-51.18562333, -30.15629667], [-51.18777333, -30.15720167], [-51.18911667, -30.158095], [-51.19004, -30.15831167], [-51.1911, -30.15847667], [-51.19158667, -30.158535], [-51.19343833, -30.15844833], [-51.19561167, -30.15774833], [-51.19731167, -30.15637333], [-51.19921333, -30.15501833], [-51.20051667, -30.15346333], [-51.20107667, -30.1519], [-51.2019, -30.14983333], [-51.20362667, -30.14812167], [-51.20391, -30.14789667], [-51.20509833, -30.14757], [-51.205765, -30.14752167], [-51.20833833, -30.14724833], [-51.21064333, -30.14639333], [-51.213035, -30.14528667], [-51.21512833, -30.143985], [-51.217205, -30.14281167], [-51.21908667, -30.14151], [-51.22010667, -30.13990667], [-51.22030833, -30.139565], [-51.22011167, -30.13801], [-51.22011167, -30.13801], [-51.21913333, -30.13593833], [-51.21823667, -30.13376], [-51.21793333, -30.13149167], [-51.21848833, -30.12965], [-51.21939167, -30.12770333], [-51.22002333, -30.12579333], [-51.22049833, -30.12422833], [-51.22100833, -30.12325667], [-51.22138667, -30.12265167], [-51.22171333, -30.12237667], [-51.22308167, -30.12113333], [-51.22415667, -30.11937167], [-51.224475, -30.11713833], [-51.22564167, -30.115], [-51.22635167, -30.11313333], [-51.22677833, -30.111265], [-51.22694333, -30.109505], [-51.22704667, -30.10863333], [-51.22727167, -30.107825], [-51.22743, -30.1076], [-51.22865833, -30.10655167], [-51.229875, -30.10468667], [-51.23079667, -30.10268333], [-51.23090167, -30.102445], [-51.23102, -30.10162], [-51.23115333, -30.1008], [-51.23149833, -30.098585], [-51.23152667, -30.09834833], [-51.23078, -30.097425], [-51.23000833, -30.097355], [-51.22732167, -30.09663167], [-51.22599333, -30.09552], [-51.22576667, -30.09536167], [-51.22524, -30.09502167], [-51.22458, -30.09451], [-51.22226833, -30.09233833], [-51.21989333, -30.091165], [-51.21826167, -30.08958], [-51.21667, -30.08786333], [-51.21505833, -30.08569667], [-51.213475, -30.08328667], [-51.21179, -30.08164833], [-51.20970333, -30.08028333], [-51.20890833, -30.07913333], [-51.20806167, -30.07811], [-51.20771, -30.07736167], [-51.20794333, -30.07592667], [-51.20811, -30.07483167], [-51.208775, -30.072655], [-51.20995333, -30.07124333], [-51.21030667, -30.06938833], [-51.210965, -30.06869833], [-51.21050667, -30.06640167], [-51.21137, -30.06570833], [-51.21205167, -30.06555167], [-51.212335, -30.06519], [-51.21221, -30.064625], [-51.21166167, -30.06202], [-51.21210667, -30.05974833], [-51.21369833, -30.05764333], [-51.21475167, -30.05620333], [-51.21486667, -30.05589], [-51.214845, -30.055775], [-51.21428, -30.054395], [-51.213445, -30.05239833], [-51.212215, -30.050725], [-51.21073833, -30.04983167], [-51.21009333, -30.049115], [-51.21128167, -30.04761667], [-51.21272167, -30.045765], [-51.21410833, -30.04390833], [-51.21447667, -30.04344667], [-51.21582833, -30.04169333], [-51.21582833, -30.04169333], [-51.21766833, -30.039395], [-51.21940167, -30.03714667], [-51.22075, -30.03530833], [-51.22111667, -30.03487833], [-51.221475, -30.03445167], [-51.222185, -30.03358667], [-51.22234833, -30.03338833], [-51.22272667, -30.03292833], [-51.22365667, -30.031675], [-51.22371167, -30.03158167], [-51.223855, -30.031305], [-51.22441167, -30.031045], [-51.22609833, -30.03097167]]";
        int size = arrRoute.length;
        int i = 0;
        while( i < size) {
        //for (String s: arrRoute) {

            arrRoute[i] = arrRoute[i].replace("[", "");
            arrRoute[i] = arrRoute[i].replace(",", "");
            arrRoute[i] = arrRoute[i].replace("]", "");

*//*            arrRoute[i+1] = arrRoute[i+1].replace("[", "");
            arrRoute[i+1] = arrRoute[i+1].replace(",", "");
            arrRoute[i+1] = arrRoute[i+1].replace("]", "");

            double lat = Double.valueOf(arrRoute[i+1]);*//*
            Double value = Double.parseDouble(arrRoute[i]);
            for(int j = 0; i < arrPointsRoute.size(); j++)  arrPointsRoute.add(value);
            i++;

        }

    }*/


}
