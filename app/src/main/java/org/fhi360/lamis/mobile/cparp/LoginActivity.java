package org.fhi360.lamis.mobile.cparp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.fhi360.lamis.mobile.cparp.dao.UserDAO;
import org.fhi360.lamis.mobile.cparp.R;
import org.fhi360.lamis.mobile.cparp.model.UsernameAndPassword;
import org.fhi360.lamis.mobile.cparp.webservice.APIService;
import org.fhi360.lamis.mobile.cparp.webservice.ClientAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText username;
    private ShowHidePasswordEditText password;
    private TextView createUser, text_forget_password;
    private Button login;
    private ProgressDialog progressdialog;
    String deviceconfigId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        deviceconfigId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        text_forget_password = findViewById(R.id.text_forget_password);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        createUser = findViewById(R.id.text_create_account);
        login = findViewById(R.id.login);
        createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create();
            }
        });
        createUser.setMovementMethod(LinkMovementMethod.getInstance());
        text_forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert(deviceconfigId);
            }
        });
        int id = new UserDAO(this).getId();
        if (id == 0) {
            createUser.setVisibility(View.VISIBLE);
        } else {
            createUser.setVisibility(View.GONE);
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput(username.getText().toString(), password.getText().toString())) {
                    if (new UserDAO(LoginActivity.this).getId(username.getText().toString(), password.getText().toString())) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        FancyToast.makeText(getApplicationContext(), "Wrong username or password", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();

                    }

                }
            }
        });
    }


    private void create() {
        Intent intent = new Intent(this, CreateUserActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);     //Disable going back to the MainActivity
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

    private void showAlert(final String deviceconfigId) {
        LayoutInflater li = LayoutInflater.from(LoginActivity.this);
        View promptsView = li.inflate(R.layout.forget_pop_up, null);
        final AlertDialog dialog = new AlertDialog.Builder(LoginActivity.this).create();
        dialog.setView(promptsView);
        final TextView notitopOk, notitopNotnow;
        final EditText notitoptxt;
        notitopOk = promptsView.findViewById(R.id.notitopOk);
        notitopNotnow = promptsView.findViewById(R.id.notitopNotnow);
        notitoptxt = promptsView.findViewById(R.id.notitoptxt);
        notitopNotnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        notitopOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notitoptxt.getText().toString().isEmpty()) {
                    notitoptxt.setError("Acivation code can't empty");
                } else {
                    UsernameAndPassword usernameAndPassword = new UserDAO(getApplicationContext()).getUsrnameAndPassword();
                    if (usernameAndPassword != null) {
                        username.setText(usernameAndPassword.getAccountUserName());
                        password.setText(usernameAndPassword.getAccountPassword());
                    }
                    dialog.dismiss();

                }

            }
        });

        dialog.setCancelable(false);
        dialog.show();
    }


    private void getPassworFromLamis(String deviceId, String pin) {

        progressdialog = new ProgressDialog(LoginActivity.this);
        progressdialog.setMessage("App Loading credential please wait...");
        progressdialog.setCancelable(false);
        progressdialog.setIndeterminate(false);
        progressdialog.setMax(100);
        progressdialog.show();
        APIService apiService = new APIService();
        ClientAPI clientAPI = apiService.createService(ClientAPI.class);

        Call<UsernameAndPassword> objectCall = clientAPI.getUsernamePasswordFromLamis(deviceId, pin);

        objectCall.enqueue(new Callback<UsernameAndPassword>() {
            @Override
            public void onResponse(Call<UsernameAndPassword> call, Response<UsernameAndPassword> response) {
                if (response.code() == 200) {
                    UsernameAndPassword dataObject = response.body();
                    username.setText(dataObject.getAccountUserName());
                    password.setText(dataObject.getAccountPassword());
                    if (dataObject.getAccountUserName() == null && dataObject.getAccountPassword() == null || dataObject.getAccountUserName() == "" && dataObject.getAccountPassword() == "") {
                        FancyToast.makeText(getApplicationContext(), "Your account does not exist on the server", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                        progressdialog.dismiss();
                    }
                    progressdialog.dismiss();
                } else if (response.code() == 500) {
                    FancyToast.makeText(getApplicationContext(), "No Server response, contact System Admin", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    progressdialog.dismiss();
                } else if (response.code() == 400) {
                    FancyToast.makeText(getApplicationContext(), "No Server response, contact System Admin", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    progressdialog.dismiss();
                } else if (response.code() == 404) {
                    FancyToast.makeText(getApplicationContext(), "No Server response, contact System Admin", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    progressdialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<UsernameAndPassword> call, Throwable t) {
                t.printStackTrace();
                FancyToast.makeText(getApplicationContext(), "No Internet Connection", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                progressdialog.dismiss();
            }


        });

    }


}
