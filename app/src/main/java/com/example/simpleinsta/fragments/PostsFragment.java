package com.example.simpleinsta.fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.simpleinsta.EndlessRecyclerViewScrollListener;
import com.example.simpleinsta.Post;
import com.example.simpleinsta.PostsAdapter;
import com.example.simpleinsta.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class PostsFragment extends Fragment
{
    public static final String TAG = "PostsFragment";
    private RecyclerView rvPosts;
    protected PostsAdapter adapter;
    protected List<Post> allPosts;
    private SwipeRefreshLayout swipeContainer;
    private EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    int counter = 1;


    public PostsFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        rvPosts = view.findViewById(R.id.rvPosts);
        swipeContainer = view.findViewById(R.id.swipeContainer);
        allPosts = new ArrayList<>();
        adapter = new PostsAdapter(getContext(), allPosts);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager)
        {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view)
            {
                queryPosts(10,10 * counter++);
            }
        };
        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(linearLayoutManager);
        rvPosts.addOnScrollListener(endlessRecyclerViewScrollListener);
        queryPosts(10,0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            swipeContainer.setProgressBackgroundColorSchemeColor(getResources().getColor(android.R.color.black, null));
        }
        else
        {
            swipeContainer.setProgressBackgroundColorSchemeColor(getResources().getColor(android.R.color.black));
        }

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG,"fetching new data!");
                queryPosts(10,0);
                swipeContainer.setRefreshing(false);
            }
        });
    }

    @Override
    public void onResume()
    {
        super.onResume();
        queryPosts(10, 0);
        adapter.notifyDataSetChanged();
    }

    protected void queryPosts(int limit, int skip)
    {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(limit);
        query.setSkip(skip);
        query.addDescendingOrder(Post.KEY_CREATED_KEY);
        query.findInBackground(new FindCallback<Post>()
        {
            @Override
            public void done(List<Post> posts, ParseException e)
            {
                if (e != null)
                {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                for (Post post : posts)
                {
                    Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                }
                if (skip <= 0)
                {
                    allPosts.clear();
                }
                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        });
    }
}