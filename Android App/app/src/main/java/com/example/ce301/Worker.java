package com.example.ce301;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

public class Worker implements Handler.Callback {
    @Override
    public boolean handleMessage(@NonNull Message message) {
        return false;
    }
}
