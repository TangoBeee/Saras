package me.tangobee.saras.fragments;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import me.tangobee.saras.R;

public class SignupFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup)inflater.inflate(R.layout.signup_tab_fragment, container, false);


        String msgTOS = "I agree to the ";
        String anchorTOS = "<font color='#0000EE'>Terms of Service</font>";

        CheckBox tos = root.findViewById(R.id.tos);
        tos.setText(Html.fromHtml(msgTOS+anchorTOS));

        return root;
    }
}
