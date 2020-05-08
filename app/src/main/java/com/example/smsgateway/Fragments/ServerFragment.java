package com.example.smsgateway.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.smsgateway.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ServerFragment extends Fragment {
    private View textViewMessage;
    private TextView textViewIpAccess;
    private EditText editTextPort;
    private BroadcastReceiver broadcastReceiverNetworkState;
    private static boolean isStarted = false;
    private FloatingActionButton floatingActionButtonOnOff;
    public static  String TAG = " message";
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //newUserDBHelper = new NewUserDBHelper(getContext());
        context = getContext();
       // newUserDBHelper = new NewUserDBHelper(context);


      //  View serverfragment_layout = inflater.inflate(R.layout.fragment_server, container,false);

        // INIT BROADCAST RECEIVER TO LISTEN NETWORK STATE CHANGED
       // initBroadcastReceiverNetworkStateChanged();

        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_server, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        textViewMessage = view.findViewById(R.id.tabmessages);
        editTextPort =  view.findViewById(R.id.editTextPort);
        textViewMessage = view.findViewById(R.id.textViewMessage);
        textViewIpAccess = view.findViewById(R.id.textViewIpAccess);
        floatingActionButtonOnOff = view.findViewById(R.id.floatingActionButtonOnOff);
        //floatingActionButtonOnOff.setOnClickListener( this);
    }


}
