package comw.example.rplrus26.moviesendiri;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RViewAdapter extends RecyclerView.Adapter<RViewHolders> {

    private List<MovieData> movieDataArrayList;
    private Context context;
    //private static final String DELETE_DATA_URL = only_url.url + "deleteMovie.php";
    int index;

    public RViewAdapter(Context context, ArrayList<MovieData> movieDataArrayList){
        this.context=context;
        this.movieDataArrayList = movieDataArrayList;
    }

    @Override
    public RViewHolders onCreateViewHolder( ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        RViewHolders view = new RViewHolders(itemView);
        return view;
    }

    @Override
    public void onBindViewHolder(RViewHolders holders, final int position) {
        final MovieData movie = movieDataArrayList.get(position);
        Glide.with(context)
                .load(movie.getPoster_path())
                .into(holders.gambar);

        holders.name.setText(movieDataArrayList.get(position).getTitle());

        holders.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int id = movieDataArrayList.get(position).getId();
                final String title = movieDataArrayList.get(position).getTitle();
                final String overview = movieDataArrayList.get(position).getOverview();
                final String gambar = movieDataArrayList.get(position).getPoster_path();
                final String release_date = movieDataArrayList.get(position).getRelease_date();
                Intent i = new Intent(context.getApplicationContext(), Detail_Movie.class);
                i.putExtra("id_movie",id);
                i.putExtra("title", title);
                i.putExtra("overview", overview);
                i.putExtra("poster_path", gambar);
                i.putExtra("release_date", release_date);
                context.startActivity(i);

            }
        });

        holders.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
//                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"recipient@example.com"});
//                i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
                i.putExtra(Intent.EXTRA_TEXT, movie.getId()+" \n"+movie.getTitle());
                i.setType("text/plain");
                context.startActivity(Intent.createChooser(i, "Send"));
                Toast.makeText(context,"Segera Datang", Toast.LENGTH_SHORT).show();
            }
        });


        holders.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int post = 0;
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete");
                builder.setCancelable(true);
                builder.setMessage(" Are you sure want to delete data ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {
                        // Do nothing but close the dialog
                        final MovieData data = movieDataArrayList.get(post);
                        final int id = data.getId();
                        Deletedata(id);
                        Toast.makeText(context, "" + movieDataArrayList.get(post).getId(), Toast.LENGTH_SHORT).show();
                        movieDataArrayList.remove(post);
                        notifyItemRemoved(post);;
                        notifyItemRangeChanged(post, movieDataArrayList.size());
                        dialog.dismiss();

//                        MemberArrayList.remove(position);
//                        notifyItemRemoved(position);
//                        notifyItemRangeChanged(position, MemberArrayList.size());
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return movieDataArrayList.size();
    }

    private void Deletedata(final int idMovie) {

        @SuppressLint("StaticFieldLeak")
        class DeletedataUser extends AsyncTask<Void, Void, JSONObject> {


            @Override
            protected void onPreExecute() {
                //kasih loading
            }

            @Override
            protected JSONObject doInBackground(Void... params) {
                JSONObject jsonObject;
                try {
                    String url = only_url.url + "deleteMovie.php?idMovie=" + movieDataArrayList.get(index).getId() + "";
                    //String url = new String(DELETE_DATA_URL);
                    System.out.println("urlnya : " + url);
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
                if (jsonObject != null) {
                    try {
                        JSONObject Result = jsonObject.getJSONObject("Result");
                        String sukses = Result.getString("Sukses");
                        Log.d("hasil sukses ", "onPostExecute: " + sukses);

                        if (sukses.equals("true")) {
                            Toast.makeText(context, "Delete berhasil", Toast.LENGTH_SHORT).show();
                            //to main menu
                        } else {
                            Toast.makeText(context, "Delete gagal", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception ignored) {
                        System.out.println("erornya " + ignored);
                    }
                } else {
                }
            }
        }
//        DeletedataUser us = new DeletedataUser();
//        us.execute();
    }
}
