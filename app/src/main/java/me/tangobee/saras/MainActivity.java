package me.tangobee.saras;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import me.tangobee.saras.application.AppActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TransparentWidnow.transparentWindow(getWindow());

        setContentView(R.layout.activity_main);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("AUTH", 0);

        boolean isLogged = pref.getBoolean("isLoggedIn", false);
        String email = pref.getString("email_key", null);

        try {
            if (!isInternetAvailable()) {
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                while(!isInternetAvailable());
            }

            else if (isInternetAvailable()) {

                if (!isLogged && email == null) {
                    new Handler().postDelayed(() -> {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        }, 1500);
                }
                else {
                    new Handler().postDelayed(() -> {
                        Intent intent = new Intent(MainActivity.this, AppActivity.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                        finish();
                        }, 1500);
                }
            }

            else {
                Toast.makeText(this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
            }

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();

        }


    }

    public boolean isInternetAvailable() throws InterruptedException, IOException {
        String command = "ping -c 1 google.com";
        return Runtime.getRuntime().exec(command).waitFor() == 0;
    }
}