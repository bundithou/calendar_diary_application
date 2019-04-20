package com.example.newp1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class login extends AppCompatActivity {

    Button LogInButton, RegisterButton , loginas;
    EditText Username, Password ;//Email
    String EmailHolder, PasswordHolder;
    Boolean EditTextEmptyHolder;
    SQLiteDatabase sqLiteDatabaseObj;
    usermanager userdb;
    Cursor cursor;
    String TempPassword = "NOT_FOUND" ;
    public static final String UserEmail = "";
    SharedPreferences sp;

    String email;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        initViews();
        initListeners();
        initObjects();

    }


    private void initViews() {
        Username = (EditText) findViewById(R.id.username);
        Password = (EditText) findViewById(R.id.password);
        LogInButton = (Button) findViewById(R.id.button_login);
        RegisterButton = (Button) findViewById(R.id.register);

        loginas = (Button) findViewById(R.id.login_as);
        loginas.setVisibility(View.INVISIBLE);

        userdb = new usermanager(this);


    }

    private void initListeners() {
        LogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboardFrom();
                verifyFromSQLite(null, null);
            }
        });

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRegister = new Intent(login.this, register.class);
                startActivity(intentRegister);
            }
        });
        loginas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(login.this, "login successfully", Toast.LENGTH_LONG)
                        .show();
                sp.edit().putString("logged", email).apply();
                Intent resultIntent = new Intent();

                resultIntent.putExtra("u_email", email);
                resultIntent.putExtra("u_name", finduser(email).getName());
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    private void initObjects() {
        sp = getSharedPreferences("login",MODE_PRIVATE);

        if(!sp.getString("logged","").equals("")
                && sp.getString("logged","") != null
                && userdb.checkUser(sp.getString("logged",""))){
            email = sp.getString("logged","");
            System.out.println("sp = "+email);
            loginas.setText("LOGIN AS "+email);
            loginas.setVisibility(View.VISIBLE);
            //Username.setText(email, TextView.BufferType.EDITABLE);
        }

    }

    private void verifyFromSQLite(String u_value, String p_value) {

        if(u_value == null){
            u_value = Username.getText().toString().trim();
        }
        if(p_value == null){
            p_value = Password.getText().toString().trim();
        }


        if (u_value.isEmpty()) {
            Toast.makeText(this, "Please input your email", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(u_value).matches()) {
            Toast.makeText(this, "Please input your email correctly", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (p_value.isEmpty()) {
            Toast.makeText(this, "Please input your password", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if(u_value == null || p_value == null){
            Toast.makeText(this, "null", Toast.LENGTH_LONG)
                    .show();
        }
        boolean c = userdb.checkUser(u_value, p_value);

        if (c) {
            Toast.makeText(this, "login complete", Toast.LENGTH_LONG)
                    .show();

            sp.edit().putString("logged", u_value).apply();
            Intent resultIntent = new Intent();

            resultIntent.putExtra("u_email", u_value);
            resultIntent.putExtra("u_name", this.finduser(u_value).getName());
            setResult(Activity.RESULT_OK, resultIntent);
            finish();

        } else {
            // Snack Bar to show success message that record is wrong
            Toast.makeText(this, "Wrong Email or Password", Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void hideKeyboardFrom() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }

    private user finduser(String email){
        List<user> userlist = userdb.getAllUser();
        for(user u : userlist){
            if(u.getEmail().equals(email)){
                return u;
            }
        }

        return null;
    }
}
