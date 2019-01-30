package comw.example.rplrus26.moviesendiri;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<MovieData> movieDataArrayList;
    int index;
    RViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.RV_detail);
        recyclerView.setHasFixedSize(true);
        movieDataArrayList = new ArrayList<>();

        new ambildata().execute();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        adapter = new RViewAdapter(getApplicationContext(),movieDataArrayList);
        RViewAdapter adapter = new RViewAdapter(MainActivity.this, movieDataArrayList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.RV_detail){
            this.getMenuInflater().inflate(R.menu.menu_main, menu);
        }
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @SuppressLint("StaticFieldLeak")
    public class ambildata extends AsyncTask<Void, Void, JSONObject> {


        @Override
        protected void onPreExecute() {
            //kasih loading
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject jsonObject;
            try {
                String url = only_url.url + "getMovie.php";
                System.out.println("url ku " + url);
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                InputStream inputStream = httpEntity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
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
            System.out.println("Hasilnya " + jsonObject.toString());
            try {
                JSONArray Hasiljson = jsonObject.getJSONArray("result");
                movieDataArrayList = new ArrayList<MovieData>();
                for (int i = 0; i < Hasiljson.length(); i++) {
                    MovieData a = new MovieData();
                    a.setId(Hasiljson.getJSONObject(i).getInt("idMovie"));
                    a.setTitle(Hasiljson.getJSONObject(i).getString("title"));
                    a.setPoster_path(Hasiljson.getJSONObject(i).getString("poster_path"));
                    a.setOverview(Hasiljson.getJSONObject(i).getString("overview"));
                    a.setRelease_date(Hasiljson.getJSONObject(i).getString("release_date"));

                    Log.d("1", "onPostExecute: "+ Hasiljson.getJSONObject(i).getInt("idMovie"));

                    movieDataArrayList.add(a);
                }
                //pasang data arraylist ke recyclerview
                adapter = new RViewAdapter(MainActivity.this, movieDataArrayList);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                linearLayoutManager.setOrientation(linearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                adapter.notifyDataSetChanged();
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerView.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}