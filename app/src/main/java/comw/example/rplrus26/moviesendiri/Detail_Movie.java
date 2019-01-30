package comw.example.rplrus26.moviesendiri;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class Detail_Movie extends AppCompatActivity {

    ImageView img_gambar;
    TextView nama, text_overview, text_releasedate;
    String title;
    String overview;
    String poster_path;
    String releasedate;
    private int id_movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail__movie);

        img_gambar = findViewById(R.id.img_gambar);
        nama = findViewById(R.id.text_title);
        text_overview = findViewById(R.id.text_overview);
        text_releasedate = findViewById(R.id.text_releasedate);

        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        id_movie = bundle.getInt("id_movie");
        title = bundle.getString("title");
        overview = bundle.getString("overview");
        poster_path = bundle.getString("poster_path");
        releasedate = bundle.getString("release_date");
        nama.setText(title);
        text_overview.setText(overview);
        text_releasedate.setText(releasedate);
        Glide.with(Detail_Movie.this).load(poster_path).into(img_gambar);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                Intent i = new Intent(this, EditMovie.class);
                i.putExtra("id_movie",id_movie);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}