        package com.example.krzysztof.clubmemberapplication;

        import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

        public class Login extends AppCompatActivity  {
            private EditText userId, userPassword;
            private static final String TAG_RESULTS="login";
            private static final String TAG_EMAIL = "email";
            private static final String TAG_PASSWORD = "password";

            private Button login, cancel;
            String ID, EMAIL="", PASSWORD="",FIRST_N,LAST_N,ADD1,ADD2,CITY,PHONE;
            String Email, Password;
            String response;
            ProgressDialog progressDialog;
            Context ctx;
            String myJSON;

            ConnectivityManager conMgr;
            NetworkInfo mobileNwInfo,wifiNwInfo;
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_login);

                conMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                mobileNwInfo = conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                wifiNwInfo   = conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);


                ctx = this;
                if (!mobileNwInfo.isConnected()&&!wifiNwInfo.isConnected()) {
                    Toast.makeText(Login.this,
                            "You Are Offline", Toast.LENGTH_LONG).show();
                }
                userId =(EditText)findViewById(R.id.idText);
                userPassword =(EditText)findViewById(R.id.passwordText);
                //userId.addTextChangedListener(tw);
                //password.addTextChangedListener(tw);

                login=(Button)findViewById(R.id.loginBtnId);
                cancel=(Button)findViewById(R.id.cancelBtnId);



                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

            }
            public void main_login(View v){


                if (mobileNwInfo.isConnected()||wifiNwInfo.isConnected())
                {
                    Email = userId.getEditableText().toString();
                    Password = userPassword.getEditableText().toString();
                    if(Email.equals("")||Password.equals("")) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(Login.this);
                        builder1.setTitle("Info");
                        builder1.setMessage("Please provide your Id and password");
                        builder1.setCancelable(true);
                        builder1.setPositiveButton(android.R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                    else
                    {
                        BackGround b = new BackGround();
                        b.execute(Email, Password);
                    }
                }

                 if ( !mobileNwInfo.isConnected()&&!wifiNwInfo.isConnected()) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(Login.this);
                    builder1.setTitle("No Internet Connection!");
                    builder1.setMessage("You Are Offline Please Check Your Internet Connection");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            }

            class BackGround extends AsyncTask<String, String, String> {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progressDialog = new ProgressDialog(ctx);
                    progressDialog.setMessage("Logging in...");
                    progressDialog.show();

                }

                @Override
                protected String doInBackground(String... params) {
                    String email = params[0].toString();
                    String password = params[1].toString();

            try {

                URL url = new URL("https://androidaccess.eu-gb.mybluemix.net/");
                String urlParams = "email=" + Email + "&password=" + Password;

                HttpsURLConnection httpURLConnection = (HttpsURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
                StringBuilder stringBuilder = new StringBuilder();

                String line = "";
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                response = stringBuilder.toString();
                reader.close();
                is.close();
                httpURLConnection.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
                    return response;
                }

                @Override
                protected void onPostExecute(String s)
                {
                    myJSON = response;
                    progressDialog.dismiss();
                    try
                    {
                        JSONObject root = new JSONObject(myJSON);
                        JSONObject user_data = root.getJSONObject("login");
                        ID = user_data.getString("memberID");
                        PASSWORD = user_data.getString("password");
                        EMAIL = user_data.getString("email");
                        FIRST_N = user_data.getString("firstName");
                        LAST_N= user_data.getString("lastName");
                        ADD1= user_data.getString("a1");
                        ADD2= user_data.getString("a2");
                        CITY= user_data.getString("city");
                        PHONE= user_data.getString("phone");

                        if(EMAIL.equals("0")||PASSWORD.equals("0"))
                        {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(Login.this);
                            builder1.setTitle("Incorrect id or password!");
                            builder1.setMessage("Please re-enter ");
                            builder1.setCancelable(true);
                            builder1.setPositiveButton(android.R.string.ok,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                            AlertDialog alert11 = builder1.create();
                            alert11.show();
                            userId.setText("");
                            userPassword.setText("");
                        }
                        else
                        {
                            if (EMAIL.equals(Email) && PASSWORD.equals(Password)) {
                                Intent i = new Intent(ctx, UpdatePersonalDetails.class);
                                i.putExtra("id", ID);
                                i.putExtra("fName", FIRST_N);
                                i.putExtra("lName", LAST_N);
                                i.putExtra("a1", ADD1);
                                i.putExtra("a2", ADD2);
                                i.putExtra("city", CITY);
                                i.putExtra("phone", PHONE);
                                i.putExtra("email", EMAIL);
                                startActivity(i);
                            }
                        }
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();

                    }

                }
            }
           


        }
