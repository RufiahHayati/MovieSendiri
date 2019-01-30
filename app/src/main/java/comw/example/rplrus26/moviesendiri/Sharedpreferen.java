package comw.example.rplrus26.moviesendiri;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Sharedpreferen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        if (username.equals("")) {
            Intent intent = new Intent(Sharedpreferen.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else if (username == username){
            Intent i = new Intent(Sharedpreferen.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }
}
