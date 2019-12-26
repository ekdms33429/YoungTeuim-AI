package com.example.youngteuim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button ttspeech,repeat;
    TextView txtRead;
    private TextToSpeech tts; //TTS 변수
    private boolean isAvailableToTTS = false;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String TAG = "[MainActivity] : ";
    private ArrayList<Sentence> sentences;
    private int nowIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtRead = (TextView) findViewById(R.id.txtRead);
        sentences = new ArrayList<>();

        db.collection("Sentence") //추후 변경
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Map<String, Object> map = null;
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                map = document.getData();
                                sentences.add(new Sentence(map.get("eng").toString(), map.get("kor").toString()));
                                for(Sentence s : sentences){
                                    Log.d(TAG, "sentences : "+s);
                                }
                                //Log.d(TAG, document.getId() + " => eng : " + map.get("eng") + " kor : " + map.get("kor"));

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int isAvailable) {
                if(isAvailable == TextToSpeech.SUCCESS) {
                    int language = tts.setLanguage(Locale.ENGLISH);
                    if(language == TextToSpeech.LANG_MISSING_DATA || language == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(MainActivity.this, "지원되지 않는 언어입니다.", Toast.LENGTH_SHORT).show();
                        isAvailableToTTS = false;
                    } else {
                        isAvailableToTTS = true;
                    }
                }
            }
        });


        ttspeech=(Button)findViewById(R.id.ttspeech);

        ttspeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speech();
            }
        });
        repeat = findViewById(R.id.repeat);
        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nowIndex != 0) {
                    nowIndex--;
                }
                speech();
            }
        });


    }

    public void speech() {
        if(isAvailableToTTS) {
            String text = sentences.get(nowIndex).getEng();
            txtRead.setText(text);
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
            nowIndex++;
        }
    }
}
