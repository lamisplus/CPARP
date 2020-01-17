package org.fhi360.lamis.mobile.cparp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.shashank.sony.fancytoastlib.FancyToast;

import org.fhi360.lamis.mobile.cparp.R;
import org.fhi360.lamis.mobile.cparp.dao.UserDAO;

public class CreateUserActivity extends AppCompatActivity {
    private UserDAO userDAO;
    private Button button;
    EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        button=findViewById(R.id.button);
        userDAO = new UserDAO(this);
        username= ((EditText) findViewById(R.id.username));
        password= ((EditText) findViewById(R.id.password));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = ((EditText) findViewById(R.id.username)).getText().toString();
                String password = ((EditText) findViewById(R.id.password)).getText().toString();
                if(validateInput(username,  password)){
                if(new UserDAO(CreateUserActivity.this).getId(username, password)) {
                    FancyToast.makeText(getApplicationContext(), "User with these credentials already registered", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                }
                else {
                    userDAO.save(username, password);
                    Intent intent = new Intent(CreateUserActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
            }
        });

    }
    private boolean validateInput(String username1, String password1) {
        if (username1.isEmpty()) {
            username.setError("username can not be empty");
            return false;
        } else if (password1.isEmpty()) {
            password.setError("password can not be empty");
            return false;
        }
        return true;


    }


}
