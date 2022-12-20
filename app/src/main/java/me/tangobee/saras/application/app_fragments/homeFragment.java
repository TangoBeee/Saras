package me.tangobee.saras.application.app_fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import me.tangobee.saras.R;
import me.tangobee.saras.application.app_fragments.adapters.DiscoverAdapter;
import me.tangobee.saras.application.app_fragments.adapters.RecentAdapter;
import me.tangobee.saras.application.app_fragments.art.Art;
import me.tangobee.saras.application.app_fragments.art.IArtAPI;
import me.tangobee.saras.model.ArtModel;
import me.tangobee.saras.model.ArtRequestModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class homeFragment extends Fragment {

    private TextView header;

    private RecyclerView recentView;
    private RecyclerView discoverView;
    private TextView noRecent;
    private TextView noDiscover;

    private EditText searchArt;

    private Dialog dialog;

    private StorageReference storageReference;
    private FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        //Header for home fragment
        header = root.findViewById(R.id.header_home);
        String headOfHome = "TYPE YOUR ART";
        SpannableString ss = new SpannableString(headOfHome);
        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#FC4649")), 10, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        header.setText(ss);

        noRecent = root.findViewById(R.id.no_recent);
        noDiscover = root.findViewById(R.id.no_discover);

        searchArt = root.findViewById(R.id.search_art);


        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_wait1);
        recentView = root.findViewById(R.id.recent);


        //Data for recent items ---------------------------->
        user = FirebaseAuth.getInstance().getCurrentUser();
        StorageReference userImageStorageReference = FirebaseStorage.getInstance().getReference().child(user.getUid());
        userImageStorageReference.listAll()
                .addOnSuccessListener(listResult -> {

                    ArrayList<String> recentURL = new ArrayList<>();

                    for(StorageReference file:listResult.getItems()){
                        file.getDownloadUrl().addOnSuccessListener(uri -> {
                            recentURL.add(uri.toString());
                            Log.e("Itemvalue",uri.toString());

                        }).addOnSuccessListener(uri -> { //Recent View
                            if(!recentURL.isEmpty()) recentView.setVisibility(View.VISIBLE);
                            RecentAdapter adapter = new RecentAdapter(getContext(), recentURL, noRecent, recentView);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                            recentView.setLayoutManager(layoutManager);
                            recentView.setAdapter(adapter);
                        });
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("ERR_RECENT_IMG_LOAD", e.getMessage());
                    Toast.makeText(requireContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                });





        //Data for discover items ---------------------------->
        ArrayList<String> discoverURL = new ArrayList<>();
        discoverURL.add("https://instagram.fdel74-1.fna.fbcdn.net/v/t51.2885-15/279030281_1400042067126255_579170202382443933_n.jpg?stp=dst-jpg_e35&_nc_ht=instagram.fdel74-1.fna.fbcdn.net&_nc_cat=103&_nc_ohc=Na6i98UBWIsAX_1hhba&edm=ALQROFkBAAAA&ccb=7-5&ig_cache_key=MjgyNDU2NzQyNjg5Mzk4NzYyOQ%3D%3D.2-ccb7-5&oh=00_AfDekFvk2d8Icd-nRQ1WTEmW20g35skjrpaMpOOJkYfc0A&oe=6391D1F5&_nc_sid=30a2ef");
        discoverURL.add("https://pbs.twimg.com/media/FRGHiOUVUAA5vt9?format=jpg&name=medium");
        discoverURL.add("https://cdn.searchenginejournal.com/wp-content/uploads/2022/08/screenshot018-62f4c2969f0dd-sej-480x481.jpg");
        discoverURL.add("https://cdn.searchenginejournal.com/wp-content/uploads/2022/08/screenshot023-62f4c57eecbbf-sej.jpg");
        //end here discover items
        //discover view
        discoverView = root.findViewById(R.id.discover);
        discoverView.setVisibility(View.VISIBLE);
        DiscoverAdapter discover = new DiscoverAdapter(getContext(), discoverURL, noDiscover, discoverView);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        discoverView.setLayoutManager(layoutManager1);
        discoverView.setAdapter(discover);



        //Searching ART
        searchArt.setOnKeyListener((v, keyCode, event) -> {

            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER && !searchArt.getText().toString().isEmpty()) {

                String currentuserUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                DocumentReference creditRef = FirebaseFirestore.getInstance().collection("USERS").document(currentuserUID);

                creditRef.get().addOnSuccessListener(cred -> {
                    if((Long)cred.get("credits") <= 0)
                        creditRef.update("credits", 0);
                    else
                        creditRef.update("credits", FieldValue.increment(-1));
                });

                hideSoftKeyboard(requireActivity());


                creditRef.get().addOnSuccessListener(credit -> {
                    Long c;
                    c = (Long) credit.get("credits");

                    if(c == null)
                        Toast.makeText(requireContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();

                    else if(c > 0) {
                        String artPrompt = searchArt.getText().toString();
                        searchArt.getText().clear();
                        dialog.show();
                        imageGenerate(artPrompt);
                    }

                    else {
                        Toast.makeText(requireContext(), "You don't have enough credits", Toast.LENGTH_SHORT).show();
                    }
                });

                return true;
            }

            else if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER && searchArt.getText().toString().isEmpty()) {
                hideSoftKeyboard(requireActivity());
                Toast.makeText(requireContext(), "BRUH! You atleast type something \uD83D\uDC80", Toast.LENGTH_SHORT).show();
            }

            return false;
        });

        return root;
    }

    public void imageGenerate(String prompt) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IArtAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IArtAPI artAPI = retrofit.create(IArtAPI.class);

        Call<ArtModel> artModelCall = artAPI.getArt(new ArtRequestModel(prompt, 1, "1024x1024"));

        artModelCall.enqueue(new Callback<ArtModel>() {
            @Override
            public void onResponse(@NonNull Call<ArtModel> call, @NonNull Response<ArtModel> response) {


                Log.d("RESPONSE_PORT", String.valueOf(response.code()));
                if(response.body() != null) {
                    ArtModel artModel = new ArtModel();

                    artModel.setCreated(response.body().getCreated());
                    artModel.setData(response.body().getData());

                    try {
                        imageGetter(prompt, artModel.getData().get(0).getUrl(), artModel.getCreated().toString());
                    } catch (IOException e) {
                        dialog.cancel();
                        Log.d("ERR_IN_IMG_GEN", e.getMessage());
                        Toast.makeText(requireContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                    }

                }

                else {
                    dialog.cancel();
                    Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                    Log.d("RESPONSEBODY", "ITS NULL");
                }

            }

            @Override
            public void onFailure(@NonNull Call<ArtModel> call, @NonNull Throwable t) {
                dialog.cancel();
                Toast.makeText(getContext(), t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void imageGetter(String artPrompt, String artURL, String Created) throws IOException {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                URLConnection urlConnection = new URL(artURL).openConnection();
                urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0");

                String fileName = "Saras-Image-" + Created;

                user = FirebaseAuth.getInstance().getCurrentUser();

//                Timestamp timestamp = Firebase;

                StorageReference userStorageRef = FirebaseStorage.getInstance().getReference().child(user.getUid());
                storageReference = userStorageRef.child(fileName);

                storageReference.putStream(urlConnection.getInputStream())
                        .addOnSuccessListener(taskSnapshot -> {

                            Intent intent = new Intent(getActivity(), Art.class);
                            intent.putExtra("artPrompt", artPrompt);
                            intent.putExtra("artURL", artURL);
                            dialog.dismiss();
                            startActivity(intent);
                            Toast.makeText(requireContext(), "Woop! Your ART is ready.", Toast.LENGTH_SHORT).show();
                            Toast.makeText(requireContext(), "Please wait while we load your image...", Toast.LENGTH_SHORT).show();
                            requireActivity().finish();

                        })
                        .addOnFailureListener(e -> {
                            dialog.dismiss();
                            Toast.makeText(requireContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                            Log.e("ERR_IN_IMG_GETTER", e.getMessage());
                        });
            }

            catch (IOException e) {
                Log.e("ERR_CONVERT_URL-BITMAP", e.getMessage());
            }
        });
    }


    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
}