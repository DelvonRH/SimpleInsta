package com.example.simpleinsta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.parse.ParseFile;

import java.util.Date;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder>
{
    private Context context;
    private List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts)
    {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount()
    {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tvUsername, tvDescription, tvCreatedAt;
        private ImageView ivImage;
        private ImageView ivProfileImage;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivImage = itemView.findViewById(R.id.ivImage);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
        }

        public void bind(Post post)
        {
            tvDescription.setText(post.getDescription());
            String createdat = post.getCreatedKeyAt().toString();
            String timedifference = TimeFormatter.getTimeDifference(createdat, context);
            tvCreatedAt.setText(timedifference);
            tvUsername.setText(post.getUser().getUsername());
            ParseFile image = post.getImage();
            if (image != null)
            {
                Glide.with(context).load(post.getImage().getUrl()).into(ivImage);
            }
            ParseFile file = post.getUser().getParseFile("profileImage");
            if(file != null)
            {
                String path = file.getUrl();
                Glide.with(context).load(path).transform(new CircleCrop()).into(ivProfileImage);
            }
        }
    }
}
