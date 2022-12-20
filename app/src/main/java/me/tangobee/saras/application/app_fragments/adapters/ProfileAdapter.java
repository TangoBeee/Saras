package me.tangobee.saras.application.app_fragments.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

import me.tangobee.saras.R;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {

    private Context context;
    private ArrayList<String> data;

    private RecyclerView profileView;
    private TextView noProfileItem;

    public ProfileAdapter(Context context, ArrayList<String> data, RecyclerView profileView, TextView noProfileItem) {
        this.context = context;

        if(data.size() <= 0) {
            this.profileView = profileView;
            profileView.setVisibility(View.GONE);
        }
        else {
            this.noProfileItem = noProfileItem;
            noProfileItem.setVisibility(View.GONE);
        }

        this.data = data;
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.profile_item_layout, parent, false);

        return new ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        Glide.with(context).load(data.get(position))
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .error(R.drawable.image_404)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                }).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ProfileViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView img;
        public ProfileViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.fimg);
        }
    }
}
