package com.naver.viewlabs.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.naver.viewlabs.R;
import com.naver.viewlabs.log.Ln;
import com.naver.viewlabs.services.DownloadService;

/**
 * Created by abyss on 2017. 12. 5..
 */

public class ServiceTestActivity extends AppCompatActivity {
    private boolean mBound;
    private Messenger mServiceMessenger;
    private Messenger mClientMessenger = new Messenger(new ClientHandler());
    private TextView txtProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        txtProgress = findViewById(R.id.txt_progress);
        TextView button = findViewById(R.id.btn_another);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceTestActivity.this, ServiceTest2Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        Intent intent = new Intent(this, DownloadService.class);
        startService(intent);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            mBound = true;
            mServiceMessenger = new Messenger(service);
            registerMessenger();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };


    private void requestDownload(int titleNo) {
        sendRemoteMessage(DownloadService.MSG_REQ_START, titleNo, mClientMessenger);
    }
    private void registerMessenger() {
        sendRemoteMessage(DownloadService.MSG_REQ_REGISTER_MESSENGER, null, mClientMessenger);
    }

    private void unregisterMessenger() {
        sendRemoteMessage(DownloadService.MSG_REQ_UNREGISTER_MESSENGER, null, mClientMessenger);
    }

    public void sendRemoteMessage(int message, Object obj, Messenger messenger) {
        Message msg = Message.obtain(null, message);
        msg.obj = obj;
        msg.replyTo = messenger;
        try {
            mServiceMessenger.send(msg);
        } catch (RemoteException e) {
            Ln.e(e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBound) {
            unregisterMessenger();
            unbindService(mConnection);
            mBound = false;
        }
    }

    private class ClientHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DownloadService.MSG_RES_REGISTER_MESSENGER:
                    Ln.d("Success Registered Messenger");
                    requestDownload(1);
                    break;
                case DownloadService.MSG_RES_UNREGISTER_MESSENGER:
                    Ln.d("Success Unregistered Messenger");
                    break;
                case DownloadService.MSG_RES_PROGRESS:
                    onUpdateProgress((Integer) msg.obj);
                    Ln.d(msg.obj);
                    break;
                case DownloadService.MSG_RES_FINISHED:
                    onUpdateProgress(100);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private void onUpdateProgress(int progress) {
        txtProgress.setText(String.valueOf(progress));
    }
}
