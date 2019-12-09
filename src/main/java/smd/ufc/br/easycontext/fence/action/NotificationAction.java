package smd.ufc.br.easycontext.fence.action;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.android.gms.awareness.fence.FenceState;

import smd.ufc.br.easycontext.R;
import smd.ufc.br.easycontext.fence.FenceAction;

public class NotificationAction implements FenceAction {
    private String _title, _channel, _text;
    private int _importance;

    public NotificationAction() {
    }

    public String getTitle() {
        return _title;
    }

    public void setTitle(String _title) {
        this._title = _title;
    }

    public String getChannel() {
        return _channel;
    }

    public void setChannel(String channel) {
        this._channel = channel;
    }

    public String getText() {
        return _text;
    }

    public void setText(String text) {
        this._text = text;
    }

    public int getImportance() {
        return _importance;
    }

    public void setImportance(int importance) {
        if(importance < 0 || importance > 5){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                this._importance = NotificationManager.IMPORTANCE_DEFAULT;
            }
        }
    }

    public NotificationAction(String title, String channel, String text, int importance) {
        this._title = title;
        this._channel = channel;
        this._text = text;
        this._importance = importance;
        if(importance < 0 || importance > 5){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                this._importance = NotificationManager.IMPORTANCE_DEFAULT;
            }
        }
    }

    @Override
    public void doOperation(Context context, FenceState state, Bundle data) {
        String title = data.getString("title");
        String text = data.getString("text");
        String channel = data.getString("channel");
        int importance = data.getInt("importance");
        if(state.getCurrentState() == FenceState.TRUE){
            Notification.Builder notification = new Notification.Builder(context);
            notification.setContentTitle(title);
            notification.setSmallIcon(R.drawable.ic_notifications_black_24dp);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notification.setChannelId(channel);
            }
            notification.setContentText(text);
            notification.build();
            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (nm != null) {
                    nm.createNotificationChannel(new NotificationChannel(channel, channel, importance));
                }
            }
            nm.notify(channel, (int) SystemClock.uptimeMillis(),notification.build());
        }
    }
}
