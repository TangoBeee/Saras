package me.tangobee.saras.application.app_fragments.art;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import me.tangobee.saras.R;
import me.tangobee.saras.application.AppActivity;

public class Art extends AppCompatActivity {

    private RoundedImageView artView;
    private TextView artPrompt;

    private TextView artHeader;
    private TextView tosmsg;

    private ImageButton back;

    private Button variation;
    private Button editArt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.WHITE);
        setContentView(R.layout.activity_art);

        back = findViewById(R.id.back);
        artView = findViewById(R.id.image_slider);
        artHeader = findViewById(R.id.artHead);
        variation = findViewById(R.id.variation);
        editArt = findViewById(R.id.editart);
        tosmsg = findViewById(R.id.tosLink);
        artPrompt = findViewById(R.id.prompt);

        tosmsg.setOnClickListener(v -> {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            openCustomTab(Art.this, builder.build(), Uri.parse("https://tangobee.netlify.app/saras/tos.html"));
        });

        //changing title color and style
        String heading = "YOUR ART";

        SpannableString headerSpan = new SpannableString(heading);
        headerSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#FC4649")), 5, headerSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        artHeader.setText(headerSpan);


        //--------------------------------------------------------------------------------------
        //making image/art view and storing in firebase storage

        Bundle bundle = getIntent().getExtras();

        // Get the Bitmap and the artPrompt from the ArtInfo object
        String artURL = bundle.getString("artURL");
        String prompt = bundle.getString("artPrompt");

        String finalPrompt = "\""+prompt+"\"";
        artPrompt.setText(finalPrompt);


        if(artURL != null) {
            Glide.with(getApplicationContext()).load(artURL).into(artView);
        }

        else {
            artView.setImageResource(R.drawable.image_404);
            Toast.makeText(getApplicationContext(), "Something went wrong! in Art class", Toast.LENGTH_SHORT).show();
        }


        //--------------------------------------------------------------------------------------







        //adding back imagebtn functions
        back.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AppActivity.class);
            startActivity(intent);

            finish();
        });


        //button coming soon(variation/editart)
        variation.setOnClickListener(v -> Toast.makeText(getApplicationContext(), "Coming Soon...", Toast.LENGTH_SHORT).show());
        editArt.setOnClickListener(v -> Toast.makeText(getApplicationContext(), "Coming Soon...", Toast.LENGTH_SHORT).show());


        //TOS
        tosmsg.setMovementMethod(LinkMovementMethod.getInstance());
        tosmsg.setText(Html.fromHtml(getResources().getString(R.string.tos)));
    }

    private void openCustomTab(Activity activity, CustomTabsIntent customTabsIntent, Uri uri) {
        // package name is the default package
        // for our custom chrome tab
        String packageName = "com.android.chrome";
        if (packageName != null) {

            // we are checking if the package name is not null
            // if package name is not null then we are calling
            // that custom chrome tab with intent by passing its
            // package name.
            customTabsIntent.intent.setPackage(packageName);

            // in that custom tab intent we are passing
            // our url which we have to browse.
            customTabsIntent.launchUrl(activity, uri);
        } else {
            // if the custom tabs fails to load then we are simply
            // redirecting our user to users device default browser.
            activity.startActivity(new Intent(Intent.ACTION_VIEW, uri));
        }
    }
}