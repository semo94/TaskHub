package com.example.saleem.testgithub.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.saleem.testgithub.activity.MainActivity;

import java.io.PrintWriter;
import java.io.StringWriter;


public class MyExceptionHandler implements
        Thread.UncaughtExceptionHandler {
    private final Context myContext;
    private final Class<?> myActivityClass;
    private String className = "";

    public MyExceptionHandler(Context context, Class<?> c, String className) {

        myContext = context;
        myActivityClass = c;
        this.className = className;

    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

        StringWriter stackTrace = new StringWriter();
        ex.printStackTrace(new PrintWriter(stackTrace));
        Log.e("Exiption " + className, stackTrace.toString());

        Intent intent = new Intent(myContext, MainActivity.class);
        intent.putExtra("StackTrace", stackTrace.toString() + " ");
        intent.putExtra("ClassName", className + " ");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        myContext.startActivity(intent);

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }
}
