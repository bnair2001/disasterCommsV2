package com.codesec.disastercomms;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;

import android.os.BatteryManager;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    String coordinates, amt, to;
    double frequency;
    double signal;
    //int MY_PERMISSIONS_REQUEST_READ_STORAGE;
    private AsyncHttpServer server = new AsyncHttpServer();
    private AsyncServer mAsyncServer = new AsyncServer();
/*    LocationManager locationManager;
    LocationListener locationListener;*/
    //int success;



    //TextView trans
    TextView progperc;
    Spinner dropdown;
    Button pay, bc, con;
    //int requestCode = 1;
    SharedPreferences pref;
    final Context context = this;
    boolean consensus_bool = false;
    String finalloc;

    Set<String> nums = new HashSet<String>();
    Set<String> nodes = new HashSet<String>();
    JSONObject current_location = new JSONObject();
    String usrdeet,usraddress, usrphno;
    LocationManager locationManager;
    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*final Handler handler = new Handler();


        Runnable run = new Runnable() {
            @Override
            public void run() {
                wifirange();
                double answer = calculateDistance(signal, frequency);
                Toast.makeText(MainActivity.this, "Distance in (m): "+ answer, Toast.LENGTH_SHORT).show();
                handler.postDelayed(this, 20000);
            }
        };
        handler.post(run);*/


        /*TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        PhoneStateListener signalListener = new PhoneStateListener() {
            public void onSignalStrengthChanged(int asu) {
                //asu is the signal strength
                if(asu<0) {
                    Toast.makeText(MainActivity.this, "Mobile network detected, So please switch to mobile data in order to upload the blockchain !!", Toast.LENGTH_LONG).show();
                }
                }
        };

        telephonyManager.listen(signalListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTH);

*/
/*        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("Location",location.toString());
                double lat = location.getLatitude();
                double longi = location.getLongitude();
                String fin = Double.toString(lat);
                String fin1 = Double.toString(longi);
                finalloc = fin+", "+fin1;
                String tempy = location.toString();
                Pattern pattern = Pattern.compile("(gps[^;]*)\\{");
                Matcher matcher = pattern.matcher(tempy);
                if (matcher.find())
                {
                    coordinates=matcher.group(1);
                }
                //coordinates = coordinates.replaceAll(" ", "");
                //coordinates = coordinates.replaceAll("\n", "").replaceAll("\r", "");
                //coordinates = coordinates.replaceAll("\\s+","");
                finalloc = finalloc.replaceAll("\\s+","");
                Log.i("COROROOIROJO", finalloc);

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }*/

//location get version 2.0




















        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_WIFI_STATE},1);
        } else {
            Toast.makeText(
                    MainActivity.this,
                    "device wifi is readable",
                    Toast.LENGTH_LONG
            ).show();
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        } else {
            Toast.makeText(
                    MainActivity.this,
                    "device storage is accesible",
                    Toast.LENGTH_LONG
            ).show();
        }


        pref = getSharedPreferences("settings", 0);
        //startActivityForResult(new Intent("coordinates.java"),requestCode);

        // Number
        if(pref.getString("num", "-").equals("-")) {
            setContentView(R.layout.front_page);
            Button save = (Button)findViewById(R.id.button);

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText num = (EditText)findViewById(R.id.editText);
                    EditText add = (EditText)findViewById(R.id.editText1);
                    EditText phone = (EditText)findViewById(R.id.editText2);
                    String num_s = num.getText().toString();
                    String user_address=  add.getText().toString();
                    String user_phnumber = phone.getText().toString();

                    //prompt code starts here

                    //prompt code ends here


                    if(!num_s.isEmpty()) {
                        pref.edit().putString("num", num_s).apply();
                        pref.edit().putString("add", user_address);
                        pref.edit().putString("phone", user_phnumber);
                        Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                        recreate();
                    }
                }
            });
        }
        else {
            setContentView(R.layout.activity_main);
            Toast.makeText(this, "Hi " + pref.getString("num", "client") + "!", Toast.LENGTH_LONG).show();

            dropdown = (Spinner)findViewById(R.id.spinner1);
            //balance = (TextView) findViewById(R.id.textView);
            //trans = (TextView) findViewById(R.id.textView3);
            progperc = (TextView) findViewById(R.id.textView4);
            //trans.setMovementMethod(new ScrollingMovementMethod());
            pay = (Button) findViewById(R.id.button3);
            bc = (Button) findViewById(R.id.button2);
            con = (Button) findViewById(R.id.button4);
            final String usradd = pref.getString("add", "");
            final String usrphnumm = pref.getString("phone", "");
            //String usrnam = pref.getString("num", "client");
            // Genesis Block
            //getdeets();
            if (pref.getString("blockchain", "-").equals("-")) {
                JSONArray blockchain = new JSONArray();
                JSONObject block = new JSONObject();
                String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                Log.i("DATETIME", currentDateTimeString);
                try {
                    JSONObject location = new JSONObject();
                    block.put("index", 1);
                    block.put("time", currentDateTimeString);
                    block.put("location", coordinates);
                    block.put("nounce", 0);
                    block.put("prehash", "0");
                    blockchain.put(block);
                    pref.edit().putString("blockchain", blockchain.toString()).apply();
                    JSONArray locations = new JSONArray();
                    locations.put(location);
                    pref.edit().putString("locations", locations.toString()).apply();
                    Log.i("MainActivity", blockchain.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            // Default Difficulty
            if (pref.getInt("difficulty", 0) == 0) {
                pref.edit().putInt("difficulty", 2).apply();
            }

            new consensus().execute("");
            //update_balance();
            //balance.setText("Balance: " + pref.getFloat("balance", 0) + "");
            //trans.setText("" + formatString(pref.getString("locations", "{}")) + "");

            pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(consensus_bool) {
                        try {
                        to = dropdown.getSelectedItem().toString();
                        EditText amount = (EditText) findViewById(R.id.editText5);

                        if(!amount.getText().toString().isEmpty()) {
                            amt = amount.getText().toString();
                            //double bal = pref.getFloat("balance", 0);
                            double batpercent=batterypercet();
                            usrdeet = usradd+", "+usrphnumm;
                            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                            int a=0;
                            if (a==0 ) {
                                //Log.i("SHIT", coordinates);
                                //coordinates = coordinates.replaceAll("\n", "").replaceAll("\r", "");
                                JSONObject t = new JSONObject();
                                try {
                                    t.put("sender", pref.getString("num", ""));
                                    t.put("receiver", to);
                                    t.put("message", amt);
                                    t.put("time", currentDateTimeString);
                                    t.put("userinfo", usrdeet);
                                    t.put("coordinates", finalloc);
                                    t.put("battery", batpercent);
                                    //t.put("userinfo", usrdeet);
                                    new_location(t);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                amount.setText("");
                                Toast.makeText(MainActivity.this, "Broadcast Queued!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Give Valid Input", Toast.LENGTH_LONG).show();
                            }
                        }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Two or More Nodes Needed", Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Please Wait", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            bc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this, BlockchainActivity.class);
                    startActivity(i);
                }
            });
            con.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new consensus().execute("");
                }
            });
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        server.stop();
        mAsyncServer.stop();
    }

   /* @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }*/

    @Override
    public void onResume() {
        super.onResume();
        startServer();
    }

    private void startServer() {
        server.get("/blockchain", new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                try {
                    response.code(200);
                    JSONObject res = new JSONObject();
                    JSONArray blockchain = new JSONArray(pref.getString("blockchain", ""));
                    JSONArray locations = new JSONArray(pref.getString("locations", ""));
                    res.put("num",pref.getString("num", "0"));
                    res.put("blockchain", blockchain);
                    res.put("length", blockchain.length());
                    res.put("locations", locations);
                    response.send(res);
                } catch (JSONException e) {
                    Log.e("MainActivity", e.getMessage(), e);
                }
            }
        });
        server.listen(mAsyncServer, 8080);
    }

    private void new_location(JSONObject location) {
        try {
            JSONArray locations = new JSONArray(pref.getString("locations", ""));
            locations.put(location);
            pref.edit().putString("locations", locations.toString()).apply();
            current_location = location;
            new consensus().execute("mine");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String hash(String input) {
        try {
            char[] chars = input.toCharArray();
            Arrays.sort(chars);
            String sorted = new String(chars);
            Log.e("HASHINPUT", sorted);
            MessageDigest mDigest = MessageDigest.getInstance("SHA1");
            byte[] result = mDigest.digest(sorted.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < result.length; i++) {
                sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
            }
            Log.e("HASHINPUT", sb.toString());
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        int difficulty = pref.getInt("difficulty", 2);
        return String.format("%0" + difficulty + "d", 0);
    }

    private String last_block() throws JSONException {
        JSONArray blockchain = new JSONArray(pref.getString("blockchain", ""));
        return blockchain.getJSONObject(blockchain.length()-1).toString();
    }

    private boolean validate_chain(JSONArray blockchain) throws JSONException {
        JSONObject curr, prev = blockchain.getJSONObject(0);
        int difficulty = pref.getInt("difficulty", 2);
        for(int i=1; i<blockchain.length(); ++i) {
            curr = blockchain.getJSONObject(i);
            Log.e("VALIDATE", "1");
            if(!curr.getString("prehash").equals(hash(prev.toString()))) {
                return false;
            }
            Log.e("VALIDATE", "2");
            if(!hash(prev + String.valueOf(curr.getInt("nounce"))).substring(0, difficulty).equals(String.format("%0" + difficulty + "d", 0))){
                return false;
            }
            Log.e("VALIDATE", "3");
            prev = curr;
        }
        return true;
    }

//    private void update_balance() {
//        try {
//            JSONArray transactions = new JSONArray(pref.getString("transactions", ""));
//            Log.e("BALANCE TRANS", transactions.toString());
//            JSONObject ledger = new JSONObject();
//            for(int j=0; j<transactions.length(); ++j) {
//                JSONObject transaction = transactions.getJSONObject(j);
//                String receiver = transaction.getString("receiver");
//                String sender = transaction.getString("sender");
//                double amount = transaction.getDouble("amount");
//                double prev_amount;
//                if(!sender.equals("0")) {
//                    prev_amount = 0;
//                    if(ledger.has(sender)) {
//                        prev_amount = ledger.getDouble(sender);
//                    }
//                    ledger.put(sender, prev_amount - amount);
//                }
//                prev_amount = 0;
//                if(ledger.has(receiver)) {
//                    prev_amount = ledger.getDouble(receiver);
//                }
//                ledger.put(receiver, prev_amount + amount);
//            }
//            double amount = 0;
//            if(ledger.has(pref.getString("num", ""))) {
//                amount = ledger.getDouble(pref.getString("num", ""));
//                Log.i("Balance From", "" + amount + "");
//            }
//            Log.i("Balance", "" + amount + "");
//            pref.edit().putFloat("balance", Float.parseFloat(String.valueOf(amount))).apply();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    private class consensus extends AsyncTask<String, String, String> {
        String server_response;
        @Override
        protected String doInBackground(String ... strings) {
            try
            {
                if(strings[0].equals("mine")) {

                    for (String fullpath : nodes) {
                        try {
                            Log.e("PING", fullpath);
                            URL url = new URL(fullpath);
                            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                            int responseCode = urlConnection.getResponseCode();

                            if (responseCode == HttpURLConnection.HTTP_OK) {
                                server_response = readStream(urlConnection.getInputStream());
                                Log.v("CURL", server_response);

                                JSONObject res = new JSONObject(server_response);
                                JSONArray nblockchain = res.getJSONArray("blockchain");
                                JSONArray ntransactions = res.getJSONArray("locations");
                                int nlength = res.getInt("length");
                                nums.add(res.getString("num"));
                                nodes.add(fullpath);
                                JSONArray blockchain = new JSONArray(pref.getString("blockchain", ""));
                                if (nlength > blockchain.length() && validate_chain(nblockchain)) {
                                    pref.edit().putString("blockchain", nblockchain.toString()).apply();
                                    pref.edit().putString("locations", ntransactions.toString()).apply();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    String lb = last_block();

                    double batpercent=batterypercet();
                    String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                    //finalloc = finalloc.replaceAll("\n", "");

                    long nounce = 0;
                    int difficulty = pref.getInt("difficulty", 2);
                    // Proof of Work
                    while (!hash(lb + String.valueOf(nounce)).substring(0, difficulty).equals(String.format("%0" + difficulty + "d", 0))) {
                        nounce++;
                    }
                    // Get Reward
                    JSONArray all_locations = new JSONArray(pref.getString("locations", ""));
                    JSONArray locations = new JSONArray();
                    JSONObject location = new JSONObject();
                    location.put("sender", "0");
                    location.put("receiver", pref.getString("num", ""));
                    location.put("location", coordinates);
                    all_locations.put(location);
                    locations.put(location);
                    locations.put(current_location);
                    // Create New Block
                    //finalloc = finalloc.replaceAll("", "");
                    //usrdeet = usrdeet.replaceAll(" ", "");
                    JSONArray blockchain = new JSONArray(pref.getString("blockchain", ""));
                    JSONObject block = new JSONObject();
                    block.put("index", blockchain.length() + 1);
                    block.put("time", currentDateTimeString);
                    block.put("locations", coordinates);
                    //block.put("user details", usrdeet);
                    block.put("message", amt);
                    block.put("battery", batpercent);
                    block.put("nounce", nounce);
                    block.put("prehash", hash(lb));
                    blockchain.put(block);

                    // Save Blockchain
                    pref.edit().putString("blockchain", blockchain.toString()).apply();
                    pref.edit().putString("locations", all_locations.toString()).apply();
                    current_location = null;
                }
                else {
                    WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
                    String myip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
                    InetAddress host;
                    Log.e("PING", "STARTED");
                    host = InetAddress.getByName(String.valueOf(myip));
                    byte[] ip = host.getAddress();

                    for (int i = 1; i < 255; i++) {
                        try {
                            ip[3] = (byte) i;
                            InetAddress address = InetAddress.getByAddress(ip);
                            publishProgress(address.toString().substring(1, address.toString().length()));
                            if (!address.toString().equals("/" + myip) && address.isReachable(100)) {
                                String fullpath = "http:/" + address.toString() + ":8080/blockchain";
                                Log.e("PING", fullpath);
                                URL url = new URL(fullpath);
                                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                                int responseCode = urlConnection.getResponseCode();

                                if (responseCode == HttpURLConnection.HTTP_OK) {
                                    server_response = readStream(urlConnection.getInputStream());
                                    Log.v("CURL", server_response);

                                    JSONObject res = new JSONObject(server_response);
                                    JSONArray nblockchain = res.getJSONArray("blockchain");
                                    JSONArray ntransactions = res.getJSONArray("locations");
                                    int nlength = res.getInt("length");
                                    nums.add(res.getString("num"));
                                    nodes.add(fullpath);
                                    JSONArray blockchain = new JSONArray(pref.getString("blockchain", ""));
                                    if (nlength > blockchain.length() && validate_chain(nblockchain)) {
                                        pref.edit().putString("blockchain", nblockchain.toString()).apply();
                                        pref.edit().putString("locations", ntransactions.toString()).apply();
                                    }
                                }
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                    Log.e("PING", "ENDED");
                }
            } catch(Exception e1) {
                e1.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String ... values) {
            super.onProgressUpdate(values);
            progperc.setText("scanning " + values[0] + "");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            consensus_bool = false;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dropdown.setAdapter(new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, nums.toArray()));
            consensus_bool = true;
            progperc.setText("");
            //update_balance();
            //balance.setText("Balance: " + pref.getFloat("balance", 0) + "");
            //trans.setText("" + formatString(pref.getString("locations", "{}")) + "");
            Toast.makeText(MainActivity.this, "Consensus Met", Toast.LENGTH_LONG).show();
        }
    }

// Converting InputStream to String

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }

    public void piweb(View view){
        Intent myIntent = new Intent(this, Portal.class);
        startActivity(myIntent);
    }

    public void sendMessage(View view)
    {
        boolean wifi=ApManager.isApOn(MainActivity.this);
        if(wifi)
        {
            Toast.makeText(
                    MainActivity.this,
                    "Hotspot seems to be on !!",
                    Toast.LENGTH_LONG
            ).show();

        }
        else{
            boolean wifiresult=ApManager.configApState(MainActivity.this);
            if(wifiresult)
            {
                Toast.makeText(
                        MainActivity.this,
                        "Hotspot initiated",
                        Toast.LENGTH_LONG
                ).show();
            }
            else
            {
                Toast.makeText(
                        MainActivity.this,
                        "Please follow the process manually, hotspot initiate failed",
                        Toast.LENGTH_LONG
                ).show();
                Intent intent = new Intent(MainActivity.this, connect.class);
                startActivity(intent);
            }
        }
        //Intent intent = new Intent(MainActivity.this, connect.class);
        //startActivity(intent);
    }

    public void walkietalkie(View view)
    {
        Toast.makeText(
                MainActivity.this,
                "comming soon, work in progress",
                Toast.LENGTH_LONG
        ).show();
    }

    private void copyFiletoExternalStorage(int resourceId, String resourceName){
        String pathSDCard = Environment.getExternalStorageDirectory() + "/Android/" + resourceName;
        try{
            InputStream in = getResources().openRawResource(resourceId);
            FileOutputStream out = null;
            out = new FileOutputStream(pathSDCard);
            byte[] buff = new byte[1024];
            int read = 0;
            try {
                while ((read = in.read(buff)) > 0) {
                    out.write(buff, 0, read);
                }
            } finally {
                in.close();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }

    public double calculateDistance(double signalLevelInDb, double freqInMHz) {
        double exp = (27.55 - (20 * Math.log10(freqInMHz)) + Math.abs(signalLevelInDb)) / 20.0;
        double dist = Math.pow(10.0, exp);
        Toast.makeText(
                MainActivity.this,
                "Distance with connection : "+dist,
                Toast.LENGTH_LONG
        ).show();
        return Math.pow(10.0, exp);
    }

    public void wifirange(){
        WifiManager wifiMgr = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        frequency = wifiInfo.getFrequency() *1000;//Ghz to Mhz
        signal = wifiInfo.getRssi();
    }

    public double batterypercet(){
        BatteryManager bm = (BatteryManager)getSystemService(BATTERY_SERVICE);
        int batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

        return batLevel;
    }

    public double getBatteryCapacity(Context context) {
        Object mPowerProfile;
        double batteryCapacity = 0;
        final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";

        try {
            mPowerProfile = Class.forName(POWER_PROFILE_CLASS)
                    .getConstructor(Context.class)
                    .newInstance(context);

            batteryCapacity = (double) Class
                    .forName(POWER_PROFILE_CLASS)
                    .getMethod("getBatteryCapacity")
                    .invoke(mPowerProfile);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return batteryCapacity;

    }

    public void getdeets() {
        //prompt code starts here
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.prompts, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        alertDialogBuilder.setView(promptsView);

        final EditText address = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);
        final EditText number = (EditText) promptsView
                .findViewById(R.id.editText3);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
                                //result.setText(userInput.getText());
                                usraddress = address.getText().toString();
                                usrphno = number.getText().toString();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();


        //prompt code ends here
    }

    public static String formatString(String text){

        StringBuilder json = new StringBuilder();
        String indentString = "";

        for (int i = 0; i < text.length(); i++) {
            char letter = text.charAt(i);
            switch (letter) {
                case '{':
                case '[':
                    json.append("\n").append(indentString).append(letter).append("\n");
                    indentString = indentString + "\t";
                    json.append(indentString);
                    break;
                case '}':
                case ']':
                    indentString = indentString.replaceFirst("\t", "");
                    json.append("\n").append(indentString).append(letter);
                    break;
                case ',':
                    json.append(letter).append("\n").append(indentString);
                    break;

                default:
                    json.append(letter);
                    break;
            }
        }

        return json.toString();
    }

}
