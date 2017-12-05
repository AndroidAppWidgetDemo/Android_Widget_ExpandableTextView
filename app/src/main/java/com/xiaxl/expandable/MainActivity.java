package com.xiaxl.expandable;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    public ExpandableTextView cb_myanswer_desc_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        cb_myanswer_desc_tv = (ExpandableTextView) findViewById(R.id.cb_qstn_dtl_header_desc_tv);
        cb_myanswer_desc_tv.updateUI("1111111111\n1111111111\n1111111111\n1111111111\n1111111111\n1111111111\n1111111111\n1111111111\n1111111111\n1111111111\n");


    }

}
