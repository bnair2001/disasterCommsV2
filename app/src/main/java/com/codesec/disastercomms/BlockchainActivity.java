package com.codesec.disastercomms;

import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

public class BlockchainActivity extends AppCompatActivity {

    SharedPreferences pref;
    TextView display;
    String disaster="blockchain";
    String obj;
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
