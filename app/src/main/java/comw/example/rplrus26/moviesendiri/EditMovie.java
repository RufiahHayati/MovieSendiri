package comw.example.rplrus26.moviesendiri;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class EditMovie extends AppCompatActivity {

    TextView txtUserID;
    EditText edttitle, edtoverview, edtrelease;
    MovieData edit;
    ProgressBar load;
    Button btnsave;
    private int id_movie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_movie);

        txtUserID = (TextView)findViewById(R.id.txtUserID);
        edttitle = (EditText) findViewById(R.id.edt_title);
        edtoverview = (EditText) findViewById(R.id.edt_overview);
        edit = new MovieData();
        edtrelease = (EditText) findViewById(R.id.edt_releasedate);
        btnsave = (Button) findViewById(R.id.btn_save);
        load =(ProgressBar)findViewById(R.id.progressBarmutar);
        Bundle extras = getIntent().getExtras();
        if (extras !=null) {
            id_movie = extras.getInt("id_movie");
        }
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit.setTitle(edttitle.getText().toString());
                edit.setOverview(edtoverview.getText().toString());
                edit.setRelease_date(edtrelease.getText().toString());
                new editData().execute();
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    public class editData extends AsyncTask<Void, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            //kasih loading
            load.setVisibility(View.VISIBLE);
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject jsonObject;

            try {
                String tmpTitle = edit.getTitle().replaceAll(" ","%20");
                String tmpOverview = edit.getOverview().replaceAll(" ","%20");
                String tmpRelease = edit.getRelease_date().replaceAll(" ","%20");
                String url = only_url.url + "editMovie.php?title=" +tmpTitle+"&overview=" +tmpOverview+"&release_date=" +tmpRelease+ "&idMovie=" +id_movie+ "";
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
            Log.d("hasil json ", "onPostExecute: " + jsonObject.toString());
            load.setVisibility(View.INVISIBLE);
            if (jsonObject != null) {
                try {
                    JSONObject Result = jsonObject.getJSONObject("Result");
                    String sukses = Result.getString("Sukses");
                    Log.d("hasil sukses ", "onPostExecute: " + sukses);
                    if (sukses.equals("true")) {
                        Toast.makeText(getApplicationContext(), "Save sukses", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditMovie.this,MainActivity.class);
                        startActivity(intent);

                        //to main menu
                    } else {
                        Toast.makeText(getApplicationContext(), "Save gagal", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception ignored) {
                    System.out.println("erornya "+ignored);
                }
            } else {
            }
        }
    }
}