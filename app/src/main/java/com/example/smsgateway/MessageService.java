package com.example.smsgateway;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.EditText;
import android.widget.Toast;

public class MessageService extends Service {
    MessageServer messageServer;
    private EditText editTextPort;
    public MessageService() {
    }

    @SuppressLint("ResourceType")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getExtras();
        int port = bundle.getInt("port");
        Toast.makeText(getApplicationContext(),"the service started",Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(),"port from fragment is \t"+port,Toast.LENGTH_SHORT).show();

        try {
            if (port == 0) {
                Toast.makeText(getApplicationContext(), "Port is null", Toast.LENGTH_LONG).show();
                throw new Exception();
            }
            messageServer = new MessageServer(port,getApplicationContext());
            messageServer.start();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "The PORT " + port + " doesn't work, please change it between 1000 and 9999.", Toast.LENGTH_LONG).show();
        }

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
