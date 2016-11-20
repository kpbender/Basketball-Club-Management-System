package com.example.krzysztof.clubmemberapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

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

public class UpdatePersonalDetails extends AppCompatActivity {

    private CheckBox emailCheckBox,addressCheckBox,phoneCheckBox;
    private Button update;
    private EditText emailUpdate,name,houseNoUpdate,streetUpdate,cityUpdate,phoneUpdate;
    String first,last,id;
    String RESULT;
    String response;
    String myJSON,county;
    ConnectivityManager conMgr;
    NetworkInfo mobileNwInfo,wifiNwInfo;

    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_personal_details);


         spinner = (Spinner) findViewById(R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.counties, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setSelection(5);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
             county = spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });


        emailCheckBox=(CheckBox)findViewById(R.id.checkBoxEmail);
        addressCheckBox=(CheckBox)findViewById(R.id.checkBoxAddress);
        phoneCheckBox=(CheckBox)findViewById(R.id.checkBoxPhone);


        name = (EditText)findViewById(R.id.lastName);
        emailUpdate=(EditText)findViewById(R.id.editEmail);
        houseNoUpdate=(EditText)findViewById(R.id.editHouseNumber);
        streetUpdate=(EditText)findViewById(R.id.editStreet);
        cityUpdate=(EditText)findViewById(R.id.editCity);
        phoneUpdate=(EditText)findViewById(R.id.editPhone);

        emailUpdate.setEnabled(false);
        houseNoUpdate.setEnabled(false);
        streetUpdate.setEnabled(false);
        cityUpdate.setEnabled(false);
        spinner.setEnabled(false);
        phoneUpdate.setEnabled(false);

        update=(Button)findViewById(R.id.button);


        conMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        mobileNwInfo = conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        wifiNwInfo   = conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            id = extras.getString("id");
            first = extras.getString("fName");
            last = extras.getString("lName");
            String email = extras.getString("email");
            String phone = extras.getString("phone");
            String a1 = extras.getString("a1");
            String a2 = extras.getString("a2");
            String city = extras.getString("city");
            name.setText("You are logged in as: "+first+" "+last);
            emailUpdate.setText(email);
            houseNoUpdate.setText(a1);
            streetUpdate.setText(a2);
            cityUpdate.setText(city);

            phoneUpdate.setText(phone);
        }

        View.OnClickListener checkBoxListener =new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(emailCheckBox.isChecked()) {
                    emailUpdate.setEnabled(true);
                }
                else
                    emailUpdate.setEnabled(false);
                if(addressCheckBox.isChecked()) {
                    houseNoUpdate.setEnabled(true);
                    streetUpdate.setEnabled(true);
                    cityUpdate.setEnabled(true);
                    spinner.setEnabled(true);
                }
                else {
                    houseNoUpdate.setEnabled(false);
                    streetUpdate.setEnabled(false);
                    cityUpdate.setEnabled(false);
                    spinner.setEnabled(false);
                }
                if(phoneCheckBox.isChecked()) {
                    phoneUpdate.setEnabled(true);
                }
                else{
                    phoneUpdate.setEnabled(false);
                }
            }
        };

        emailCheckBox.setOnClickListener(checkBoxListener);
        addressCheckBox.setOnClickListener(checkBoxListener);
        phoneCheckBox.setOnClickListener(checkBoxListener);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
        });
    }
    private void updateUser()

    {
        String email = emailUpdate.getText().toString();
        String houseNo = houseNoUpdate.getText().toString();
        String street = streetUpdate.getText().toString();
        String city = cityUpdate.getText().toString();
        String phone = phoneUpdate.getText().toString();

        if (mobileNwInfo.isConnected()||wifiNwInfo.isConnected())
        {

            if(email.equals("")||houseNo.equals("")||county.equals("")||city.equals("")||phone.equals(""))
            {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(UpdatePersonalDetails.this);
                builder1.setTitle("Info");
                builder1.setMessage("Please provide all required fields");
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
                b.execute(id,houseNo,street,city,county,phone,email);

            }
        }

        if ( !mobileNwInfo.isConnected()&&!wifiNwInfo.isConnected()) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(UpdatePersonalDetails.this);
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
        }

        @Override
        protected String doInBackground(String... params) {
            String id = params[0].toString();
            String add1 = params[1].toString();
            String add2 = params[2].toString();
            String city = params[3].toString();
            String county = params[4].toString();
            String phone = params[5].toString();
            String email = params[6].toString();


            try
            {
                URL url = new URL("https://androidaccess.eu-gb.mybluemix.net/update.php/");
                String urlParams = "id=" + id + "&add1=" + add1+ "&add2=" +add2+ "&city=" +city+ "&county=" +county+ "&phone=" +phone+ "&email=" +email;

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
            try
            {
                JSONObject root = new JSONObject(myJSON);
                JSONObject user_data = root.getJSONObject("result");
                RESULT = user_data.getString("back");


                if(RESULT.equals("1"))
                {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(UpdatePersonalDetails.this);
                    builder1.setTitle("Info");
                    builder1.setMessage("Details Updated ");
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
                    if (RESULT.equals("0")) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(UpdatePersonalDetails.this);
                        builder1.setTitle("Error");
                        builder1.setMessage("Database connection error! Please try again ");
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
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

        }
    }

}
