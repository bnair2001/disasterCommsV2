package com.codesec.disastercomms;

import android.content.SharedPreferences;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.*;
import com.koushikdutta.async.http.AsyncHttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class BlockchainActivity extends AppCompatActivity {

    SharedPreferences pref;
    TextView display;
    String disaster = "blockchain";
    String obj, url;
    String pathSDCard = Environment.getExternalStorageDirectory() + "/Android/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blockchain);
        pref = getSharedPreferences("settings", 0);
        display = (TextView) findViewById(R.id.textView2);
        display.setMovementMethod(new ScrollingMovementMethod());
        obj = ("" + formatString(pref.getString("blockchain", "{}")) + "");
        display.setText("" + formatString(pref.getString("blockchain", "{}")) + "");
        sendPost();
        try {
            Writer output;
            File file = new File(pathSDCard + disaster + ".json");
            output = new BufferedWriter(new FileWriter(file));
            output.write(obj);
            output.close();
            Toast.makeText(getApplicationContext(), "JSON saved", Toast.LENGTH_LONG).show();


        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    public void sendPost() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {


        //String ip=getHotspotAdress();
                String link = "http://192.168.1.104:5000/sendData";
                String abc= "{ "+ "\"data\""+":"+obj+" }";
                //abc = abc.replace(" ", "");
                //abc = abc.replaceAll("\\s+","");

                try {
                    URL url = new URL(link);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                   // JSONObject jsonParam = new JSONObject();
//                    jsonParam.put("timestamp", 1488873360);
//                    jsonParam.put("uname", message.getUser());
//                    jsonParam.put("message", message.getMessage());
//                    jsonParam.put("latitude", 0D);
//                    jsonParam.put("longitude", 0D);
                    Log.i("JSON", abc.toString());
                    OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream());
                    //DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    //os.writeBytes(URLEncoder.encode(obj.toString(), "UTF-8"));
                    os.write(abc.toString());

                    os.flush();
                    os.close();

                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG" , conn.getResponseMessage());

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }


//    public String getHotspotAdress(){
//        final WifiManager manager = (WifiManager)super.getSystemService(WIFI_SERVICE);
//        final DhcpInfo dhcp = manager.getDhcpInfo();
//        int ipAddress = dhcp.gateway;
//        ipAddress = (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) ?
//                Integer.reverseBytes(ipAddress) : ipAddress;
//        byte[] ipAddressByte = BigInteger.valueOf(ipAddress).toByteArray();
//        try {
//            InetAddress myAddr = InetAddress.getByAddress(ipAddressByte);
//            return myAddr.getHostAddress();
//        } catch (UnknownHostException e) {
//            // TODO Auto-generated catch block
//            Log.e("Wifi Class", "Error getting Hotspot IP address ", e);
//        }
//        return "null";
//    }

    public static String formatString(String text) {

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