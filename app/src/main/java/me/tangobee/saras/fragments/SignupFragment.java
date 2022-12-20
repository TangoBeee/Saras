package me.tangobee.saras.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.regex.Pattern;

import me.tangobee.saras.R;
import me.tangobee.saras.application.AppActivity;

public class SignupFragment extends Fragment {

    private Button signup;

    private EditText em;
    private EditText usname;
    private EditText pass;
    private EditText cpass;
    private TextView tosmsg;

    private CheckBox tos;

    private FirebaseAuth firebaseAuth;
    private Dialog dialog;
    private FirebaseFirestore firestore;

    private final int credits = 5;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup)inflater.inflate(R.layout.signup_tab_fragment, container, false);
        tosmsg = root.findViewById(R.id.tosLink);

        tosmsg.setOnClickListener(v -> {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(requireActivity(), Uri.parse("https://tangobee.netlify.app/saras/tos.html"));
        });

        em = root.findViewById(R.id.email);
        usname = root.findViewById(R.id.username);
        pass = root.findViewById(R.id.password);
        cpass = root.findViewById(R.id.cpassword);
        tos = root.findViewById(R.id.tos);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_wait1);


        //signup
        signup = root.findViewById(R.id.signup_btn);

        signup.setOnClickListener(v -> {

            String email = em.getText().toString().trim().toLowerCase();
            String username = usname.getText().toString().trim().toLowerCase();
            String password = pass.getText().toString();
            String cpassword = cpass.getText().toString();

            if(isEmpty(em)) {
                em.setError("Email is required!");
            }

            if(isEmpty(usname)) {
                usname.setError("Username is required!");
            }
            if(isEmpty(pass)) {
                pass.setError("Password is required!");
            }
            if(isEmpty(cpass)) {
                cpass.setError("Password is required!");
            }

            else if (!isEmailValid(email)){
                em.setError("Invalid Email");
            }

            else if(!password.equals(cpassword)) {
                cpass.setError("Password doesn't match");
            }

            else if(!isValidPassword(password)) {
                pass.setError("Please enter a strong password");
            }

            else if(!tos.isChecked()) {
                tosmsg.setError("Please accept our Terms of Service");
            }

            else {
                dialog.show();

                HashMap<String, Object> user = new HashMap<>();
                user.put("email", email);
                user.put("username", username);
                user.put("credits", credits);

                CollectionReference Users = firestore.collection("USERS");

                Users.whereEqualTo("email", email).get()
                                .addOnSuccessListener(item -> {
                                    if(item.isEmpty()) {
                                        firebaseAuth.createUserWithEmailAndPassword(email, password)
                                                .addOnSuccessListener(authResult -> {

                                                    SharedPreferences pref = requireActivity().getSharedPreferences("AUTH", 0);
                                                    SharedPreferences.Editor editor = pref.edit();

                                                    editor.putBoolean("isLoggedIn", true);
                                                    editor.putString("email_key", email);
                                                    editor.putString("username_key", username);

                                                    editor.apply();

                                                    String currentuserUID = firebaseAuth.getCurrentUser().getUid();

                                                    Users.document(currentuserUID).set(user);
                                                    Intent intent = new Intent(requireActivity(), AppActivity.class);
                                                    intent.putExtra("email", email);
                                                    startActivity(intent);
                                                    requireActivity().finish();
                                                    dialog.cancel();
                                                })
                                                .addOnFailureListener(e -> {
                                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    dialog.cancel();
                                                });
                                    }

                                    else {
                                        dialog.cancel();
                                        em.setError("User already exist");
                                    }
                                });
            }

        });

        return root;
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPassword(String s) {
        Pattern PASSWORD_PATTERN
                = Pattern.compile(
                "[a-zA-Z0-9\\!\\@\\#\\$]{8,24}");

        return !TextUtils.isEmpty(s) && PASSWORD_PATTERN.matcher(s).matches();
    }

    boolean isEmpty(EditText view) {
        return TextUtils.isEmpty(view.getText());
    }
}
