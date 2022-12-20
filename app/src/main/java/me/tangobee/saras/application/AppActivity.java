package me.tangobee.saras.application;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import me.tangobee.saras.R;
import me.tangobee.saras.application.app_fragments.afkPageFragment;
import me.tangobee.saras.application.app_fragments.homeFragment;
import me.tangobee.saras.application.app_fragments.profileFragment;
import me.tangobee.saras.databinding.ActivityAppBinding;

public class AppActivity extends AppCompatActivity {

    ActivityAppBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(Color.WHITE);

        binding = ActivityAppBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragments(new homeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch(item.getItemId()) {
                case R.id.ic_home:
                    replaceFragments(new homeFragment());
                    break;
                case R.id.ic_afk:
                    replaceFragments(new afkPageFragment());
                    break;
                case R.id.ic_user:
                    replaceFragments(new profileFragment());
                    break;
            }

            return true;
        });
    }

    private void replaceFragments(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.framelayout_app, fragment);
        fragmentTransaction.commit();
    }
}