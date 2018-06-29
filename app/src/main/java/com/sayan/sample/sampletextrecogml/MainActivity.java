package com.sayan.sample.sampletextrecogml;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.text);
        new CameraPicProvider(this, true, new CameraPicProvider.GetBitmapListener() {
            @Override
            public void onGetBitmap(Bitmap bitmapImage, String filepath) {
                FirebaseVisionTextDetector visionTextDetector = FirebaseVision.getInstance().getVisionTextDetector();
                visionTextDetector.detectInImage(FirebaseVisionImage.fromBitmap(bitmapImage)).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                    @Override
                    public void onSuccess(FirebaseVisionText firebaseVisionText) {
                        processTextRecognitionResult(firebaseVisionText);
                    }
                });
            }
        });

    }

    private void processTextRecognitionResult(FirebaseVisionText texts) {
        List<FirebaseVisionText.Block> blocks = texts.getBlocks();
        if (!blocks.isEmpty()) {
            for (FirebaseVisionText.Block block :
                    blocks) {
                List<FirebaseVisionText.Line> lines = block.getLines();
                if (!lines.isEmpty()){
                    for (FirebaseVisionText.Line line :
                            lines) {
                        List<FirebaseVisionText.Element> elements = line.getElements();
                        if (!elements.isEmpty()){
                            for (FirebaseVisionText.Element element :
                                    elements) {
                                textView.append(element.getText()+ " ");
                            }
                            textView.append("\n");
                        }
                    }
                    textView.append("\n\n\t");
                }
            }
        }
    }
}
