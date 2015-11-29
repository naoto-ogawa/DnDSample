package com.example.dnd03;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

        for (int i = 0; i < 100; i++) {
            Map<String, Object> m = new HashMap<String, Object>();
            m.put("id", i);
            m.put("value", random());
            data.add(m);
        }

        MyAdapter ap = new MyAdapter(this, data);

        ListView lv = (ListView)findViewById(R.id.listView);
        lv.setAdapter(ap);

    }

    // http://stackoverflow.com/questions/12116092/android-random-string-generator
    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        char tempChar;
        for (int i = 0; i < 20 ;i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
}
