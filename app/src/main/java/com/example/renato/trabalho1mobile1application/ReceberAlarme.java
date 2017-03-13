package com.example.renato.trabalho1mobile1application;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Renato on 12/03/2017.
 */

public class ReceberAlarme extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarme!!!!", Toast.LENGTH_SHORT).show();
    }
}
