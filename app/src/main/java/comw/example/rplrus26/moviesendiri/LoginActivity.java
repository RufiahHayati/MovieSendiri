package comw.example.rplrus26.moviesendiri;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LoginActivity extends AppCompatActivity {

    SharedPreferences pref;
    EditText edtusername, edtpassword;
    Button btnlogin;
    ProgressBar mutar;
    CheckBox checkBox;
    LoginUsernamePassword user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtusername = (EditText) findViewById(R.id.edt_username);
        edtpassword = (EditText) findViewById(R.id.edt_password);
        mutar = (ProgressBar) findViewById(R.id.loading);
        checkBox = (CheckBox)findViewById(R.id.checkbox_Pass);

        user = new LoginUsernamePassword();

        btnlogin = (Button) findViewById(R.id.btn_login);

        SharedPreferences.Editor editor;
        pref = getSharedPreferences("testapp", MODE_PRIVATE);
        editor = pref.edit();
        editor.commit();

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.setUsername(edtusername.getText().toString());
                user.setPassword(edtpassword.getText().toString());
                if (user.getUsername().equals("") || user.getPassword().equals("")){
                    Toast.makeText(getApplicationContext(),"Notway Empty", Toast.LENGTH_SHORT).show();
                }else {
                    new LoginProcess().execute();
                }
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (!isChecked){
                    edtpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else {
                    edtpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppTheme);
        builder.setTitle(" Apakah Anda ingin keluar ?");
        builder.setCancelable(true);
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                    navigation.this.finish();
                finish();
                moveTaskToBack(true);
                LoginActivity.this.finish();
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @SuppressLint("StaticFieldLeak")
    public class LoginProcess extends AsyncTask<Void, Void, JSONObject> {


        @Override
        protected void onPreExecute() {
            //kasih loading
            mutar.setVisibility(View.VISIBLE);
            btnlogin.setVisibility(View.INVISIBLE);
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject jsonObject;
            try {
                String url = only_url.url + "loginMovie.php?username=" +user.getUsername()+ "&password=" +user.getPassword()+ "";
                System.out.println("urlnya : "+url);
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                InputStream inputStream = httpEntity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        inputStream, "iso-8859-1"
                ), 8);
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                inputStream.close();
                String json = stringBuilder.toString();
                jsonObject = new JSONObject(json);
            } catch (Exception e) {
                jsonObject = null;
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            //String url = "http://192.168.6.204/login/getData.php";
            System.out.println("hasil" +jsonObject.toString());
            Log.d("hasil json ", "onPostExecute: " + jsonObject.toString());
            mutar.setVisibility(View.VISIBLE);
            if (jsonObject != null) {
                try {
                    JSONObject Result = jsonObject.getJSONObject("Result");
                    String sukses = Result.getString("Sukses");
                    Log.d("hasil sukses ", "onPostExecute: " + sukses);

                    if (sukses.equals("true")) {
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        Toast.makeText(getApplicationContext(), "login berhasil", Toast.LENGTH_SHORT).show();
                        String username = edtusername.getText().toString();
                        SharedPreferences sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username", username);
                        editor.commit();
                        startActivity(intent);
                        finish();
                        //to main menu
                    } else {
                        Toast.makeText(getApplicationContext(), "login gagal", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception ignored) {
                }
            } else {
            }
        }
    }
}
