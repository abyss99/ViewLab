package com.naver.viewlabs.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.webkit.URLUtil;

import com.naver.viewlabs.R;
import com.naver.viewlabs.ZipUtil;
import com.naver.viewlabs.activities.ServiceTestActivity;
import com.naver.viewlabs.http.DefaultHeaderInterceptor;
import com.naver.viewlabs.log.Ln;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.zip.CRC32;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import okio.BufferedSource;

/**
 * Created by abyss on 2017. 12. 5..
 */

public class DownloadService extends Service {
    public static final int MSG_REQ_REGISTER_MESSENGER = 0x7200;
    public static final int MSG_RES_REGISTER_MESSENGER = 0x7201;
    public static final int MSG_REQ_UNREGISTER_MESSENGER = 0x7300;
    public static final int MSG_RES_UNREGISTER_MESSENGER = 0x7301;

    public static final int MSG_REQ_START = 0x7304;
    public static final int MSG_RES_PROGRESS = 0x7305;
    public static final int MSG_RES_FINISHED = 0x7306;
    private ArrayList<Messenger> mClients = new ArrayList<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    protected NotificationManager mNotificationManager;
    private NotificationCompat.Builder notificationBuilder;

//    protected static final String ACTION_DOWNLOAD_CANCEL = "com.naver.linewebtoon.ACTION_CANCEL";
//    protected static final String CANCEL_TITLE_NO = "titleNo";

    protected static final int NOTI_ID_PREFIX = 0x789;
    private int notiId;

    @Override
    public void onCreate() {
        super.onCreate();
        Ln.d("onCreate Service");
        initNotificationManager();

//        startCount();

    }

    private int getNotiId() {
        return NOTI_ID_PREFIX + notiId;
    }

    private void startDownload(int titleNo) {
        notiId = titleNo;
        getObservable(getDownloadTitleDir(getApplicationContext(), titleNo))
                .distinct()
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        updateNotification(0);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        remoteSendMessage(MSG_RES_PROGRESS, integer);
//                notifyProgress(integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        updateNotification(2);
                        stopSelf();
                    }

                    @Override
                    public void onComplete() {
                        updateNotification(1);
//                        mNotificationManager.cancel("태그", NOTI_ID);
                        stopSelf();
                        remoteSendMessage(MSG_RES_FINISHED, null);
                    }
                });
    }

    private void initNotificationManager() {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationBuilder = new NotificationCompat.Builder(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Ln.d("onStartCommand Service");
        return super.onStartCommand(intent, flags, startId);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Ln.d("onBind Service");
        return new Messenger(new ServiceHandler()).getBinder();
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
        Ln.d("unbind Service");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Ln.d("onUnbind Service");
        return super.onUnbind(intent);
    }

    class ServiceHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REQ_REGISTER_MESSENGER:
                    mClients.add(msg.replyTo);
                    remoteSendMessage(MSG_RES_REGISTER_MESSENGER, null);
                    break;
                case MSG_REQ_UNREGISTER_MESSENGER:
                    mClients.remove(msg.replyTo);
                    remoteSendMessage(MSG_RES_UNREGISTER_MESSENGER, null);
                    break;
                case MSG_REQ_START:
                    startDownload((Integer) msg.obj);
                    break;
                default:
                    break;
            }
        }
    }

    protected Notification updateNotification(int state) {
//        Intent cancelIntent = new Intent(ACTION_DOWNLOAD_CANCEL);
//        cancelIntent.putExtra(CANCEL_TITLE_NO, 123);
//        PendingIntent pendingCancelIntent = PendingIntent.getBroadcast(this, 123, cancelIntent, PendingIntent.FLAG_ONE_SHOT);
//        NotificationCompat.Action cancelAction = new NotificationCompat.Action(R.mipmap.ic_launcher_round, "캔슬", pendingCancelIntent);

        switch (state) {
            case 0: //시작
                notificationBuilder.setTicker("티커 영역")
                        .setCategory(Notification.CATEGORY_PROGRESS)
                        .setContentTitle("진행중")
                        .setContentText("진행중입니다")
//                        .addAction(cancelAction)
                        .setProgress(0, 0, true)
                        .setOngoing(true);
                break;
            case 1: //완료
                notificationBuilder.setTicker("티커 영역")
                        .setCategory(Notification.CATEGORY_ALARM)
                        .setContentTitle("완료")
                        .setContentText("완료되었습니다")
                        .setProgress(100, 100, false)
                        .setOngoing(false);

                Intent intent = new Intent(this, ServiceTestActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                int requestCode = (int) System.currentTimeMillis();
                PendingIntent resultPendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                notificationBuilder.setContentIntent(resultPendingIntent);

                break;
            case 2: //실패
                notificationBuilder.setTicker("티커 영역")
                        .setCategory(Notification.CATEGORY_ERROR)
                        .setContentTitle("실패")
                        .setContentText("실패하였습니다")
                        .setProgress(0, 0, false)
                        .setOngoing(false);

                break;
        }
        notificationBuilder.setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setColor(Color.parseColor("#00d92d"));


        Notification notification = notificationBuilder.build();

        mNotificationManager.notify("태그", getNotiId(), notification);

        return notification;
    }

    private void notifyProgress(int progress) {
        if (notificationBuilder != null) {
            notificationBuilder.setProgress(100, progress, false);
            mNotificationManager.notify("태그", getNotiId(), notificationBuilder.build());
        }
    }

    private static Observable<Integer> getObservable(final File destDirectory) {
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                BufferedInputStream input = null;
                BufferedSource source = null;
                OutputStream output = null;
                String fileName = null;
                String url = "http://mobilecomicapp.naver.com/wip/mobileappimg/685989/2/asset/phoneghost_episode_2_android.zip";
                try {
                    Request request = new Request.Builder().url(url).build();
                    OkHttpClient.Builder builder = getBuilder();

                    fileName = URLUtil.guessFileName(url, null, null);
                    destDirectory.mkdirs();

                    File file = new File(destDirectory, fileName);
                    long resumeBytes = 0;
                    if (file.exists()) {
                        InputStream inputStream = new FileInputStream(file);
                        CRC32 checksum = new CRC32();
                        try {
                            byte[] buffer = new byte[1024];
                            int bytesRead = inputStream.read(buffer);
                            while (bytesRead >= 0) {
                                checksum.update(buffer, 0, bytesRead);
                                bytesRead = inputStream.read(buffer);
                            }
                        } catch (IOException e) {
                            Ln.e(e);
                        } finally {
                            inputStream.close();
                        }
                        Ln.d(checksum.getValue());
                        file.delete();
//                        resumeBytes = file.length();
//                        builder.addInterceptor(new ResumeHeaderInterceptor(resumeBytes));
                    }
                    Response response = builder.build().newCall(request).execute();
                    ResponseBody body = response.body();
                    long contentLength = Long.valueOf(response.header("Content-Length", "0"));
//                    long contentLength = body.contentLength();
                    if (body == null) {
                        emitter.onComplete();
                        return;
                    }
                    source = body.source();

                    input = new BufferedInputStream(body.byteStream());

                    if (resumeBytes != 0) {
                        output = new FileOutputStream(file, true);
                    } else {
                        output = new FileOutputStream(file, false);
                    }

//                    sink = Okio.buffer(Okio.sink(file));
//                    Buffer sinkBuffer = sink.buffer();
//                    long totalBytesRead = resumeBytes;
//                    int bufferSize = 8 * 1024;
//                    long bytesRead;

                    long currentDownloadedSize = 0;
                    long currentTotalByteSize = contentLength;
                    byte[] data = new byte[1024];
                    int count;

                    while ((count = input.read(data)) != -1) {
                        currentDownloadedSize += count;
                        output.write(data, 0, count);
                        int progress = (int) ((currentDownloadedSize * 100) / contentLength);
                        Ln.d("progress:%s, totalBytesRead:%s, contentLength:%s", String.valueOf(progress), String.valueOf(currentDownloadedSize), String.valueOf(contentLength));
                        emitter.onNext(progress);
                    }
                } catch (IOException e) {
                    Ln.e(e);
                    emitter.onError(e);
                } catch (Exception e) {
                    Ln.e(e);
                    emitter.onError(e);
                } finally {
                    Util.closeQuietly(input);
                    Util.closeQuietly(source);
                    Util.closeQuietly(output);
                }
                emitter.onComplete();
                ZipUtil.unzip(destDirectory + "/" + fileName, destDirectory + "/" + "temp");
            }
        }).throttleFirst(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private static OkHttpClient.Builder getBuilder() {
        return new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(new DefaultHeaderInterceptor());
    }

    private void startCount() {
        Observable.range(1, 30)
                .zipWith(Observable.interval(1, TimeUnit.SECONDS), (integer, interval) -> integer)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        updateNotification(0);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        remoteSendMessage(MSG_RES_PROGRESS, integer);
                        notifyProgress(integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        updateNotification(2);
                        stopSelf();
                    }

                    @Override
                    public void onComplete() {
                        updateNotification(1);
//                        mNotificationManager.cancel("태그", NOTI_ID);
                        stopSelf();
                    }
                });
    }

    public void remoteSendMessage(int msgCode, Object obj) {
        for (int i = 0; i < mClients.size(); i++) {
            try {
                Message msg = Message.obtain(null, msgCode);
                msg.obj = obj;
                mClients.get(i).send(msg);
            } catch (RemoteException e) {
                Ln.e(e);
            }
        }
    }

    @Override
    public void onDestroy() {
        Ln.d("onDestroy Service");
        super.onDestroy();
        compositeDisposable.clear();
    }


    private static final String DATA_PATH = "Android/data";
    private static final String DOWNLOAD_DIR = "artoon_resource";

    public static File getDownloadTitleDir(Context context, int titleNo) {
        return new File(getAvailableDownloadDir(context), String.valueOf(titleNo));
    }

    public static File getAvailableDownloadDir(Context context) {
        File root;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            root = Environment.getExternalStorageDirectory();
        } else {
            root = Environment.getDataDirectory();
        }
        return new File(root, DATA_PATH + "/" + context.getPackageName() + "/" + DOWNLOAD_DIR);
    }

}
