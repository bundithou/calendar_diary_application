package com.example.newp1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.jar.Attributes;

/***
 *
 * RegisterActivity
 *
 * insert data to create account in database
 */
public class register extends AppCompatActivity {


    Button CancelButton, RegisterButton ;
    EditText name, Password, Email, ConfirmPassword;
    usermanager userdb;
    user user;

    /**
     * @override
     * onCreate method
     *
     * declares initial objects
     *
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().hide();

        initViews();
        initListeners();
        initObjects();
    }

    /**
     * initialize views
     * */
    private void initViews() {
        name = (EditText) findViewById(R.id.name);
        Password = (EditText) findViewById(R.id.password);
        Email = (EditText) findViewById(R.id.email);
        ConfirmPassword = (EditText) findViewById(R.id.confirmpassword);
        CancelButton = (Button) findViewById(R.id.cancel);
        RegisterButton = (Button) findViewById(R.id.ok);
    }

    /**
     * initialize listeners
     * */
    private void initListeners() {
        CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postDataToSQLite();
            }
        });
    }
    /**
     * initialize database
     * */
    private void initObjects() {

        userdb = new usermanager(this);
        user = new user();

    }

    /**
     * post created user to SQLite
     *
     * */
    private void postDataToSQLite() {

        String namevalue = name.getText().toString().trim();
        String emailvalue = Email.getText().toString().trim();
        String passwordvalue = Password.getText().toString().trim();
        String confirmpasswordvalue = ConfirmPassword.getText().toString().trim();

        if(namevalue.isEmpty()){
            Toast.makeText(this, "Please input your name", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if(emailvalue.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailvalue).matches()){
            Toast.makeText(this, "Please input your email", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if(passwordvalue.isEmpty()){
            Toast.makeText(this, "Please input your password", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if(!passwordvalue.equals(confirmpasswordvalue)){
            Toast.makeText(this, "password is not match", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if(!userdb.checkUser(emailvalue)){
            user.setName(namevalue);
            user.setEmail(emailvalue);
            user.setPassword(passwordvalue);

            userdb.addUser(user);

            emptyInputEditText();
            Toast.makeText(this, "register successfully", Toast.LENGTH_LONG)
                    .show();
            finish();

        } else {
            Toast.makeText(this, "this email already exists", Toast.LENGTH_LONG)
                    .show();
        }

    }

    /* empty input */
    private void emptyInputEditText() {
        name.setText(null);
        Email.setText(null);
        Password.setText(null);
        ConfirmPassword.setText(null);
    }
}
