package me.tangobee.saras.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import me.tangobee.saras.R;
import me.tangobee.saras.application.AppActivity;

public class LoginFragment extends Fragment {

    private EditText email;
    private EditText password;

    private TextView forgotPass;

    private Button loginbtn;

    private FirebaseAuth firebaseAuth;

    private FirebaseFirestore firebaseAuthStore;

    private Dialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment, container, false);

        email = root.findViewById(R.id.loginEmail);
        password = root.findViewById(R.id.loginPassword);

        forgotPass = root.findViewById(R.id.forgotpassword);

        loginbtn = root.findViewById(R.id.login_btn);

        firebaseAuth = FirebaseAuth.getInstance();


        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_wait1);



        loginbtn.setOnClickListener(v -> {

            if(TextUtils.isEmpty(email.getText().toString().trim())) {
                email.setError("Email is required!");
            }
            if(TextUtils.isEmpty(password.getText().toString().trim())) {
                password.setError("Password is required!");
            }

            else {
                dialog.show();
                String em = email.getText().toString().trim();
                String pass = password.getText().toString();

                firebaseAuth.signInWithEmailAndPassword(em, pass)
                        .addOnSuccessListener(authResult -> {



                            String currentuserUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            firebaseAuthStore = FirebaseFirestore.getInstance();

                            firebaseAuthStore.collection("USERS").document(currentuserUID).get()
                                    .addOnSuccessListener(name -> {
                                        SharedPreferences pref = requireActivity().getSharedPreferences("AUTH", 0);
                                        SharedPreferences.Editor editor = pref.edit();

                                        editor.putBoolean("isLoggedIn", true);
                                        editor.putString("email_key", em);
                                        editor.putString("username_key", name.get("username").toString());
                                        editor.apply();
                                    })
                                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show());



                            Intent intent = new Intent(requireActivity(), AppActivity.class);
                            intent.putExtra("email", em);
                            startActivity(intent);
                            requireActivity().finish();

                            dialog.cancel();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        });
            }
        });



        forgotPass.setOnClickListener(v -> {
            if(TextUtils.isEmpty(email.getText())) {
                email.setError("Email is required!");
            }

            else {
                dialog.show();
                firebaseAuth.sendPasswordResetEmail(email.getText().toString().trim())
                        .addOnSuccessListener(unused -> {
                            dialog.cancel();
                            Toast.makeText(getContext(), "Reset Password link has been send", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            dialog.cancel();
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });


        return root;
    }
}
