package com.example.commonapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.bt_commont_base_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, NoLoadMoreAndNoPullReshActivity.class);

                intent.putExtra("pageType",1);

                MainActivity.this.startActivity(intent);
            }
        });
        findViewById(R.id.bt_commont_base_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, NoLoadMoreAndNoPullReshActivity.class);

                intent.putExtra("pageType",2);

                MainActivity.this.startActivity(intent);
            }
        });
        findViewById(R.id.bt_commont_base_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, NoLoadMoreAndNoPullReshActivity.class);

                intent.putExtra("pageType",4);

                MainActivity.this.startActivity(intent);
            }
        });
        findViewById(R.id.bt_commont_base_4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, NoLoadMoreAndNoPullReshActivity.class);

                intent.putExtra("pageType",3);

                MainActivity.this.startActivity(intent);
            }
        });


        findViewById(R.id.bt_commont_item_type_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, MoreItemTypeActivity.class);


                MainActivity.this.startActivity(intent);
            }
        });


        findViewById(R.id.bt_commont_bg_type_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, NoLoadMoreAndNoPullReshActivity.class);

                 intent.putExtra("bgType",1);
                MainActivity.this.startActivity(intent);
            }
        });

        findViewById(R.id.bt_commont_bg_type_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, NoLoadMoreAndNoPullReshActivity.class);

                intent.putExtra("bgType",2);
                MainActivity.this.startActivity(intent);
            }
        });

        findViewById(R.id.bt_commont_pull_type_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, NoLoadMoreAndNoPullReshActivity.class);

                intent.putExtra("refreshType",1);
                MainActivity.this.startActivity(intent);
            }
        });
        findViewById(R.id.bt_commont_pull_type_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, NoLoadMoreAndNoPullReshActivity.class);

                intent.putExtra("refreshType",2);
                MainActivity.this.startActivity(intent);
            }
        });
        findViewById(R.id.bt_commont_pull_type_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, NoLoadMoreAndNoPullReshActivity.class);

                intent.putExtra("refreshType",3);
                MainActivity.this.startActivity(intent);
            }
        });
        findViewById(R.id.bt_commont_pull_type_4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, NoLoadMoreAndNoPullReshActivity.class);

                intent.putExtra("refreshType",4);
                MainActivity.this.startActivity(intent);
            }
        });
        findViewById(R.id.bt_commont_pull_type_5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, NoLoadMoreAndNoPullReshActivity.class);

                intent.putExtra("refreshType",5);
                MainActivity.this.startActivity(intent);
            }
        });
        findViewById(R.id.bt_commont_pull_type_6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, NoLoadMoreAndNoPullReshActivity.class);

                intent.putExtra("refreshType",6);
                MainActivity.this.startActivity(intent);
            }
        });

        findViewById(R.id.bt_commont_first_no_data_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, NoLoadMoreAndNoPullReshActivity.class);

                intent.putExtra("nodata_first_in",1);
                MainActivity.this.startActivity(intent);
            }
        });
        findViewById(R.id.bt_commont_first_no_data_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, NoLoadMoreAndNoPullReshActivity.class);

                intent.putExtra("nodata_first_in",2);
                MainActivity.this.startActivity(intent);
            }
        });
        findViewById(R.id.bt_commont_first_no_data_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, NoLoadMoreAndNoPullReshActivity.class);

                intent.putExtra("nodata_first_in",3);
                MainActivity.this.startActivity(intent);
            }
        });
    }
}
