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
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

import me.tangobee.saras.R;

public class DiscoverAdapter extends RecyclerView.Adapter<DiscoverAdapter.DiscoverViewHolder> {

    private ArrayList<String> data;
    private Context context;

    private RecyclerView discover;
    private TextView noDiscover;

    public DiscoverAdapter(Context context, ArrayList<String> data, TextView noDiscover, RecyclerView discover) {
        this.context = context;
        if(data.size() <= 0) {
            this.discover = discover;
            discover.setVisibility(View.GONE);
        }
        else {
            this.noDiscover = noDiscover;
            noDiscover.setVisibility(View.GONE);
        }
        this.data = data;
    }

    @NonNull
    @Override
    public DiscoverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_layout, parent, false);

        return new DiscoverViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscoverViewHolder holder, int position) {
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

    public static class DiscoverViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView img;
        public DiscoverViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
        }
    }
}
