package com.example.smsgateway;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.os.Bundle;

import com.example.smsgateway.Fragments.MessageFragment;
import com.example.smsgateway.Fragments.ServerFragment;
import com.example.smsgateway.Model.Message;
import com.example.smsgateway.utils.PagerAdapter;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity implements ServerFragment.FragmentServerListener, MessageFragment.FragmentMessageListener {
    TabLayout tabLayout;
    ViewPager viewPager;
    PagerAdapter pageAdapter;
    TabItem tabserver;
    TabItem tabmessages;
    TabItem tabusers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.tablayout);
        tabserver = findViewById(R.id.tabserver);
        tabmessages = findViewById(R.id.tabmessages);
        tabusers = findViewById(R.id.tabusers);
        viewPager = findViewById(R.id.viewPager);

        pageAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);
        viewPager.setOffscreenPageLimit(3);

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{android.Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.READ_PHONE_STATE},
                1);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {


            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }
    MessageFragment msg = new MessageFragment();
    ServerFragment serverFragment=new ServerFragment();

    @Override
    public void onMessageRecieved(String title, String content) {
        // serverFragment.setListener(this);
        msg.createMessage(title,content);
    }


    @Override
    public void onMessageSent(Message msg_model) {

    }
}
