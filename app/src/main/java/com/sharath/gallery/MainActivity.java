package com.sharath.gallery;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        Intent i = new Intent(this, Gallery.class);
        //i.putExtra(Constants.VIDEO_SELECTION_TITLE, Constants.IMAGE_SELECTION_TITLE_MESSAGE);
        // Mode 1 for both images and videos selection, 2 for images only and 3 for videos!
        i.putExtra("mode", 2);
        i.putExtra("maxSelection", 1);
        startActivityForResult(i, 54);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 54:
                if (resultCode == RESULT_OK && null != data) {
                    String path = data.getStringArrayListExtra("result").get(0);
                }
                break;
        }
    }
}
