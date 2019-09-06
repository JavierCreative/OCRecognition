package com.example.ocrecognition.ui.resulttext;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.ocrecognition.R;

import butterknife.Unbinder;

public class ResultTextActivity extends AppCompatActivity
{
    private static final String RESULTS_KEY = "result_key";
    private Unbinder mUnbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_text);
        Log.wtf("RESULTS",getIntent().getStringExtra(RESULTS_KEY));
    }

    public static Intent createIntent(Context context, String results)
    {
        Intent result = new Intent(context,ResultTextActivity.class);
        result.putExtra(RESULTS_KEY,results);
        return result;
    }

}
