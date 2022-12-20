package me.tangobee.saras;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import me.tangobee.saras.application.AppActivity;
import me.tangobee.saras.fragments.LoginFragmentAdapter;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 101;
    TabLayout tabLayout;
    ViewPager2 viewPager;
    FloatingActionButton fb, google, dc;


    private GoogleSignInClient mGoogleSignInClient;
    private final String TAG = "LoginActivity";
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    private String userName;
    private String userEmail;
    private String userPhoto;

    private Dialog dialog;

    float v = 0;

    private final int credits = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        TransparentWidnow.transparentWindow(getWindow());

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

        final LoginFragmentAdapter adapter = new LoginFragmentAdapter(getSupportFragmentManager(), getLifecycle(), tabLayout.getTabCount());
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


        fb.setOnClickListener(v-> Toast.makeText(this, "Coming Soon...", Toast.LENGTH_SHORT).show());
        dc.setOnClickListener(v-> Toast.makeText(this, "Coming Soon...", Toast.LENGTH_SHORT).show());



        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_wait1);


        /*------------------------------- Google Login Code Start from here -----------------------------*/

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        requestGoogleSignIn();

        google.setOnClickListener(v -> signIn());


        /*------------------------------- Google Login Code End from here -----------------------------*/


    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("AUTH", 0);

        boolean isLogged = pref.getBoolean("isLoggedIn", false);
        String email = pref.getString("email_key", null);

        FirebaseUser user = mAuth.getCurrentUser();

        if (isLogged && email != null && user != null){
            Intent intent = new Intent(getApplicationContext(), AppActivity.class);
            intent.putExtra("email", userEmail);
            startActivity(intent);
            finish();
        }
    }

    private void requestGoogleSignIn(){
        // Configure sign-in to request the user’s basic profile like name and email
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                //authenticating user with firebase using received token id
                firebaseAuthWithGoogle(account.getIdToken());

                //assigning user information to variables
                userName = account.getDisplayName();
                userEmail = account.getEmail();
                userPhoto = userPhoto+"?type=large";


                //create sharedPreference to store user data when user signs in successfully
                SharedPreferences.Editor editor = getApplicationContext()
                        .getSharedPreferences("AUTH",MODE_PRIVATE)
                        .edit();
                editor.putBoolean("isLoggedIn", true);
                editor.putString("username_key", userName);
                editor.putString("email_key", userEmail);
                editor.putString("userPhoto", userPhoto);
                editor.apply();

            } catch (ApiException e) {
                dialog.cancel();
                Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {

        //getting user credentials with the help of AuthCredential method and also passing user Token Id.
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        //trying to sign in user using signInWithCredential and passing above credentials of user.
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {

                        boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();

                        CollectionReference Users = firestore.collection("USERS");

                        HashMap<String, Object> user = new HashMap<>();

                        if(isNew) {
                            user.put("email", userEmail);
                            user.put("username", userName);
                            user.put("credits", credits);

                            String currentuserUID = mAuth.getCurrentUser().getUid();

                            Users.document(currentuserUID).set(user);
                        }

                        Log.d(TAG, "signInWithCredential:success");

                        dialog.cancel();
                        // Sign in success, navigate user to Profile Activity
                        Intent intent = new Intent(getApplicationContext(), AppActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        // If sign in fails, display a message to the user.
                        dialog.cancel();
                        Toast.makeText(getApplicationContext(), "User authentication failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}