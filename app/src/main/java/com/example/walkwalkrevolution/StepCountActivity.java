package com.example.walkwalkrevolution;

import android.os.AsyncTask;
import android.widget.TextView;

public class StepCountActivity extends AsyncTask<String, String, String> {

    private TextView tv;
    private long stepCount;

    public StepCountActivity(TextView tv) {
        this.tv = tv;
    }

    @Override
    protected String doInBackground(String... strings) {
        for(int i = 0; i < 20; i++) {
            try {
                Thread.sleep(1000);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
            int num = Integer.parseInt(strings[0]);
            num++;
            strings[0] = String.valueOf(num);
            publishProgress(String.valueOf(stepCount));
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... text) {
        tv.setText(text[0]);
    }

    public void setStepCount(long sc) {
        stepCount = sc;
    }
}

