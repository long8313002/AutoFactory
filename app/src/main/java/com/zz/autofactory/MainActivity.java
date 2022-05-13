package com.zz.autofactory;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.mb.auto.StrategyFactoryAutoFactory;

import java.util.List;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<?> strategyList = StrategyFactoryAutoFactory.createAll();
        for (Object strategy:strategyList){
            Log.i("Strategy",strategy.getClass().getName());
        }
    }

}
