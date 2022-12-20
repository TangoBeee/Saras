package me.tangobee.saras.application.app_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;

import java.util.Collections;
import java.util.List;

import me.tangobee.saras.R;

public class afkPageFragment extends Fragment {

//    private TextView noAds;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_afkpage, container, false);
//        noAds = root.findViewById(R.id.noAds);

        List<String> testDeviceIds = Collections.singletonList("0184DBB84EB3BED5336F747D2C772E70");
        RequestConfiguration configuration =
                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);

        AdView adsView = root.findViewById(R.id.adView);
        AdView adsView1 = root.findViewById(R.id.adView1);
        AdView adsView2 = root.findViewById(R.id.adView2);
        AdView adsView3 = root.findViewById(R.id.adView3);
        AdView adsView4 = root.findViewById(R.id.adView4);
        AdView adsView5 = root.findViewById(R.id.adView5);
        AdView adsView6 = root.findViewById(R.id.adView6);

        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adsView.loadAd(adRequest);
        adsView1.loadAd(adRequest);
        adsView2.loadAd(adRequest);
        adsView3.loadAd(adRequest);
        adsView4.loadAd(adRequest);
        adsView5.loadAd(adRequest);
        adsView6.loadAd(adRequest);

        return root;
    }
}