package com.example.myapplication;

import android.app.Application;

import com.parse.Parse;

public class ParseApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("3LbOEv7af2DuB6uS9tnyySUrVk52wHWZ1iwb1rtV")
                .clientKey("meB25a5WId1iV2CApXbxikxk6q8vGTVcsWcfp14L")
                .server("https://instagram-codepath-jm.herokuapp.com/parse")
                .build()
        );
    }
}
