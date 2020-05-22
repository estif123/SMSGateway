package com.example.smsgateway.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.smsgateway.Database.MessageDatabase;
import com.example.smsgateway.Database.UserDatabase;
import com.example.smsgateway.MessageServer;
import com.example.smsgateway.MessageService;
import com.example.smsgateway.Model.Message;
import com.example.smsgateway.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static android.content.Context.WIFI_SERVICE;

public class ServerFragment extends Fragment implements  View.OnClickListener {
    private FragmentServerListener listener;
    MessageDatabase messageDatabase ;
    UserDatabase userDatabase;
    private View textViewMessage;
    MessageServer messageServer;
    private TextView textViewIpAccess;
    private EditText editTextPort;
    private static final int DEFAULT_PORT = 8080;
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

    @Override
    public void onClick(View view) {
        //System.out.println(getPhoneNumber());
        // getContext().startService(new Intent(getContext(),MessageService.class));
        Intent intent = new Intent(getActivity(), MessageService.class);
        Bundle bundle = new Bundle();
        bundle.putInt("port", Integer.parseInt(editTextPort.getText().toString()));
        intent.putExtras(bundle);
        //getActivity().startService(intent);


        switch (view.getId()){
            case R.id.floatingActionButtonOnOff:
                if (isConnectedInWifi()) {
                    if (!isStarted && startAndroidWebServer()) {
                        isStarted = true;
                        textViewMessage.setVisibility(View.VISIBLE);
                        floatingActionButtonOnOff.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.colorGreen));
                        editTextPort.setEnabled(false);
                    } else if (stopAndroidWebServer()) {
                        isStarted = false;
                        textViewMessage.setVisibility(View.INVISIBLE);
                        floatingActionButtonOnOff.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.colorRed));
                        editTextPort.setEnabled(true);
                    }
                } else {
                    Toast.makeText(getContext(), getString(R.string.wifi_message), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_server, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.action_call) {
//            Toast.makeText(getActivity(), "Clicked on " + item.getTitle(), Toast.LENGTH_SHORT)
//                    .show();
//        }
        return true;
    }

    private int getPortFromEditText() {
        String valueEditText = editTextPort.getText().toString();
        System.out.print(valueEditText);
        return (valueEditText.length() > 0) ? Integer.parseInt(valueEditText) : DEFAULT_PORT;
    }
    //region Start And Stop AndroidWebServer
    private boolean startAndroidWebServer() {
        // Log.i(TAG,"isstarted:\t"+isStarted);
        if (!isStarted) {


            //       int port = getPortFromEditText();
            //Toast.makeText(getContext(),"the port is "+port, Toast.LENGTH_SHORT).show();
//            try {
//                if (p getContext().startService(new Intent(getContext(),MessageService.class));ort == 0) {
//                    throw new Exception();
//                }
////                messageServer = new MessageServer(port,context);
////                messageServer.start();
            return true;
//            } catch (Exception e) {
//                e.printStackTrace();
//                Toast.makeText(getContext(), "The PORT " + port + " doesn't work, please change it between 1000 and 9999.", Toast.LENGTH_LONG).show();
//            }
        }
        return false;
    }
    private boolean stopAndroidWebServer() {
        if (isStarted && messageServer != null) {
            messageServer.stop();
            return true;
        }
        return false;
    }
    //endregion

    //region Private utils Method
    private void setIpAccess() {
        textViewIpAccess.setText(getIpAccess());
    }

    private void initBroadcastReceiverNetworkStateChanged() {
        final IntentFilter filters = new IntentFilter();
        filters.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filters.addAction("android.net.wifi.STATE_CHANGE");
        broadcastReceiverNetworkState = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                setIpAccess();
            }
        };
        super.getActivity().registerReceiver(broadcastReceiverNetworkState, filters);
    }

    private String getIpAccess() {
        WifiManager wifiManager = (WifiManager)getContext().getApplicationContext().getSystemService(WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
        final String formatedIpAddress = String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
        // Toast.makeText(getContext(),""+formatedIpAddress,Toast.LENGTH_SHORT).show();
        return "http://" + formatedIpAddress + ":";
    }



    public boolean isConnectedInWifi() {
        WifiManager wifiManager = (WifiManager) getContext().getApplicationContext().getSystemService(WIFI_SERVICE);
        NetworkInfo networkInfo = ((ConnectivityManager) getContext().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()
                && wifiManager.isWifiEnabled() && networkInfo.getTypeName().equals("WIFI")) {
            return true;
        }
        return false;
    }

    public boolean onKeyDown(int keyCode, KeyEvent evt) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isStarted) {
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.warning)
                        .setMessage(R.string.dialog_exit_message)
                        .setPositiveButton(getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //finish();
                            }
                        })
                        .setNegativeButton(getResources().getString(android.R.string.cancel), null)
                        .show();
            } else {
                // finish();
            }
            return true;
        }
        return false;
    }

    Message msgModel = new Message();
    public void setListener(FragmentServerListener serverlistener) {
        listener = serverlistener;
    }
    String msg_title="";
    String msg_content="";
    public void SendSMS(String title, String message, final Context context) {
        MessageDatabase messageDatabse = new MessageDatabase(context);

        if(title!=null&&message!=null){
            this.msg_title=title;
            this.msg_content=message;
            System.out.println(msg_title+"\n");
            System.out.println(msg_content);

        }
        if(msg_title!=null && msg_content!=null){
            if(msgModel==null){
                msgModel = new Message();
            }
            msgModel.setTitle(msg_title);
            msgModel.setContent(msg_content);


//            if(listener==null){
//                System.out.println("server listener instance is null");
//            }else {
//                listener.onMessageRecieved(msg_title,msg_content);
//            }

        }
        else {
            System.out.println("null value of title or content is detecte at server fragment\n"+"\n");
        }


        String SENT = "MESSAGE SENT";

        String DELIVERED = "MESSAGE DELIVERED";
        PendingIntent SENTPI = PendingIntent.getBroadcast(context, 0, new Intent(SENT), 0);
        PendingIntent DELIVEREDPI = PendingIntent.getBroadcast(context, 0, new Intent(DELIVERED), 0);
        // ---when the SMS has been sent---
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        createMessage(msg_title,msg_content);
                        Toast.makeText(context, "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(context, "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(context, "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(context, "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(context, "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        ArrayList<String> phone_numbers = getAllUserPhones(context);
        for (int i=0;i<=phone_numbers.size();i++){
            try {
                SmsManager smsgr = SmsManager.getDefault();
                smsgr.sendTextMessage(phone_numbers.get(i), null,  msg_content, SENTPI, DELIVEREDPI);

            } catch (Exception e) {
                Toast.makeText(context, "" + e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        }

    }

    public void createMessage(String title,String content) {
        // inserting note in db and getting
        // newly inserted note id
        if (messageDatabase==null){
            System.out.println("the message helper in null");
        }else {
            long id = messageDatabase.addMessage(title,content);
            if(id==0){
                System.out.println("0 returned");
            }

        }

    }

    public ArrayList<String> getAllUserPhones(Context context){
        if(userDatabase==null){
            userDatabase= new UserDatabase(context);
        }
        ArrayList<String> result = userDatabase.getPhoneNumbers();
        if(result==null){
            Toast.makeText(context,"No user phone found",Toast.LENGTH_LONG).show();
        }
        return result;
    }


    public interface FragmentServerListener {
        void onMessageRecieved(String title,String content);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // model=new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentServerListener) {
            listener = (FragmentServerListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentServerListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }


}
