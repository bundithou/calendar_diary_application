package com.example.newp1;

import android.app.Activity;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
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

/***
 *
 * Preference Activity for the Calendar and diary application
 * used only set the language of the application
 *
 * features: dialog
 */
public class Preferences extends AppCompatActivity {

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
    String langs[] = {"en","TH"};
    int selectedlang = 0;
    boolean langchange;

    /**
     * @override
     * onCreate method
     *
     * declares initial objects
     * SharedPreferences, Spinner, object on layout
     *
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        System.out.println("Preferences");
        lang = (Spinner) findViewById(R.id.spinnerlangs);
        logout = (Button) findViewById(R.id.logout);
        applybtn = (Button) findViewById(R.id.applybutton);
        cancelbtn = (Button) findViewById(R.id.cancelbutton);
        apply = (LinearLayout) findViewById(R.id.apply);
        apply.setVisibility(View.INVISIBLE);
        sp = getSharedPreferences("login",MODE_PRIVATE);
        langchange = false;
        logoutcheck();
        initialspinner();
        setspinner();
        setapply();
    }

    /**
     *
     * - apply to change language
     *
     * appear when user selects the language to change
     * */
    private void setapply(){
        applybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(langchange){
                    res.updateConfiguration(conf, dm);
                    //sp.edit().putString("lang",langs[selectedlang]).apply();
                }
                recreate();
                //getParent().recreate();
                View inflatedView = getLayoutInflater().inflate(R.layout.activity_main, null);
                //Configuration v_conf = inflatedView.getResources().getConfiguration();
                MainActivity.updateResources(inflatedView.getContext(), langs[selectedlang]);
                sp.edit().putInt("lang_num",selectedlang).apply();
                Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    /**
     *
     * initialize the spinner
     * */
    private void setspinner(){
        lang.setSelection(sp.getInt("lang_num", 0));
        lang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 1){
                    myLocale = new Locale(langs[1]);
                    res = getResources();
                    dm = res.getDisplayMetrics();
                    conf = res.getConfiguration();
                    conf.locale = myLocale;
                    //res.updateConfiguration(conf, dm);
                    selectedlang = 1;
                    //sp.edit().putString("lang",langs[selectedlang]).apply();
                }
                if(position == 0){
                    myLocale = new Locale(langs[0]);
                    res = getResources();
                    dm = res.getDisplayMetrics();
                    conf = res.getConfiguration();
                    conf.locale = myLocale;
                    //res.updateConfiguration(conf, dm);
                    selectedlang = 0;
                }
                if(position != sp.getInt("lang_num", 0)){
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

    /**
     * dialog before logout
     * */
    private  void logoutcheck(){
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Preferences.this);
                builder.setCancelable(false);
                builder.setMessage(R.string.sample_dialog_message);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Do something
                        Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Do something
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    /**
     * initialize spinner
     * */
    private  void initialspinner(){

        // Create an ArrayAdapter using the string array and a default spinner layout
        String List[] = {"English","Thai"};
        ArrayAdapter<String> ladapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, List);


        // Specify the layout to use when the list of choices appears
        ladapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        lang.setAdapter(ladapter);
        //lang.setSelection(0);

    }
}
