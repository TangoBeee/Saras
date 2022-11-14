package me.tangobee.saras;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import me.tangobee.saras.fragments.LoginFragmentAdapter;

public class LoginActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager;
    FloatingActionButton fb, google, dc;

    float v = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(Color.TRANSPARENT);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        setContentView(R.layout.activity_login);


        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        fb = findViewById(R.id.fb_fb);
        google = findViewById(R.id.fb_google);
        dc = findViewById(R.id.fb_discord);

        tabLayout.addTab(tabLayout.newTab().setText("Login"));
        tabLayout.addTab(tabLayout.newTab().setText("Signup"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final LoginFragmentAdapter adapter = new LoginFragmentAdapter(getSupportFragmentManager(), this, getLifecycle(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });


        fb.setTranslationY(300);
        google.setTranslationY(300);
        dc.setTranslationY(300);

        fb.setAlpha(v);
        google.setAlpha(v);
        dc.setAlpha(v);

        fb.animate().translationY(0).alpha(1).setDuration(500).setStartDelay(400).start();
        google.animate().translationY(0).alpha(1).setDuration(500).setStartDelay(600).start();
        dc.animate().translationY(0).alpha(1).setDuration(500).setStartDelay(800).start();


    }
}