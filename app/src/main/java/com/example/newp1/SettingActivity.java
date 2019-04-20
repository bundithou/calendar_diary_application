package com.example.newp1;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.Locale;

public class SettingActivity extends AppCompatActivity {

    Spinner lang;
    Button logout;
    Button applybtn;
    Button cancelbtn;
    Locale myLocale;
    SharedPreferences sp;

    LinearLayout apply;

    DisplayMetrics dm;
    Configuration conf;
    Resources res;

    boolean langchange;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        lang = (Spinner) findViewById(R.id.spinnerlangs);
        logout = (Button) findViewById(R.id.logout);
        applybtn = (Button) findViewById(R.id.applybutton);
        cancelbtn = (Button) findViewById(R.id.cancelbutton);
        apply = (LinearLayout) findViewById(R.id.apply);
        apply.setVisibility(View.INVISIBLE);
        sp = getSharedPreferences("login",MODE_PRIVATE);
        langchange = false;
        //logout();
       // setapply();
    }

    private void setapply(){
        if(langchange){
            res.updateConfiguration(conf, dm);
        }

        finish();
    }

    private void setspinner(){
        lang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 1){
                    myLocale = new Locale("TH");
                    res = getResources();
                    dm = res.getDisplayMetrics();
                    conf = res.getConfiguration();
                    conf.locale = myLocale;
                    //res.updateConfiguration(conf, dm);
                    apply.setVisibility(View.VISIBLE);
                }
                if(position == 0){
                    myLocale = new Locale("en");
                    res = getResources();
                    dm = res.getDisplayMetrics();
                    conf = res.getConfiguration();
                    conf.locale = myLocale;
                    //res.updateConfiguration(conf, dm);
                    apply.setVisibility(View.VISIBLE);
                }
                langchange = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //initialdayspinner(31);
            }
        });
    }
    private  void logout(){
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                // Add the buttons
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        //sp.edit().putString("logged","").apply();
                        recreate();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

                // Create the AlertDialog
                AlertDialog dialog = builder.create();
            }
        });
    }
    private  void initialspinner(){

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> ladapter = ArrayAdapter.createFromResource(this,
                R.array.langs, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        ladapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        lang.setAdapter(ladapter);

    }
}
