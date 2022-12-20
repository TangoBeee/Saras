package me.tangobee.saras.application.app_fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import me.tangobee.saras.LoginActivity;
import me.tangobee.saras.R;
import me.tangobee.saras.application.app_fragments.adapters.ProfileAdapter;

public class profileFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {

    private RecyclerView profileView;
    private TextView noProfileItem;

    private TextView profileName;

    private FloatingActionButton setting;

    private FirebaseFirestore firebaseAuth;
    private FirebaseAuth auth;

    private GoogleSignInClient mGoogleSignInClient;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        pref = getActivity().getSharedPreferences("AUTH", 0);
        editor = pref.edit();
        firebaseAuth = FirebaseFirestore.getInstance();

        profileView = root.findViewById(R.id.profileItems);
        noProfileItem = root.findViewById(R.id.noProfile);
        setting = root.findViewById(R.id.setting);
        profileName = root.findViewById(R.id.profileName);

        profileView.setHasFixedSize(true);

        //Data for profile items ---------------------------->

        user = FirebaseAuth.getInstance().getCurrentUser();
        StorageReference userImageStorageReference = FirebaseStorage.getInstance().getReference().child(user.getUid());
        userImageStorageReference.listAll()
                .addOnSuccessListener(listResult -> {

                    ArrayList<String> profileURL = new ArrayList<>();

                    for(StorageReference file:listResult.getItems()){
                        file.getDownloadUrl().addOnSuccessListener(uri -> {
                            profileURL.add(uri.toString());
                            Log.e("Itemvalue",uri.toString());

                        }).addOnSuccessListener(uri -> { //Recent View
                            if(!profileURL.isEmpty()) profileView.setVisibility(View.VISIBLE);
                            ProfileAdapter adapter = new ProfileAdapter(getContext(), profileURL, profileView, noProfileItem);
                            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                            profileView.setLayoutManager(layoutManager);
                            profileView.setAdapter(adapter);
                        });
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("ERR_RECENT_IMG_LOAD", e.getMessage());
                    Toast.makeText(requireContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                });

        //------fetch from firestore database------


        setting.setOnClickListener(v -> {
            PopupMenu menu = new PopupMenu(getContext(), v);
            menu.setOnMenuItemClickListener(this);
            menu.inflate(R.menu.setting_menu);
            menu.show();
        });


        //---------------------------- Google Signout Stuff--------->

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        //-------------- profile username-------------//
        SharedPreferences pref = requireContext().getSharedPreferences("AUTH", 0);
        String name = pref.getString("username_key", "NULL");

        profileName.setText(name);

        return root;
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.credits: {

                String currentuserUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                FirebaseFirestore firebaseAuthStore = FirebaseFirestore.getInstance();

                firebaseAuthStore.collection("USERS").document(currentuserUID).get().addOnSuccessListener(credits -> {
                    Toast.makeText(requireContext(), "You have " + credits.get("credits").toString() + " credits", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("ERR_GETTING_CREDIT", e.getMessage());
                    Toast.makeText(requireContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                });
            }
            return true;
            case R.id.logout: signOut();
                return true;
            default: return false;
        }
    }

    //-------------- Google-------------//
    private void signOut() {
        mGoogleSignInClient.signOut().addOnCompleteListener(task -> {
            auth = FirebaseAuth.getInstance();
            editor.clear();
            auth.signOut();
            editor.commit();
            Intent i = new Intent(getContext(), LoginActivity.class);
            startActivity(i);
            requireActivity().finish();
        });
    }

    //-------------- Google-------------//
}