package tatastrive.application.textspeech33;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextToSpeech ttsobject;
    int result;
    EditText et;
    String text;
 Button speechbtn;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        speechbtn = findViewById(R.id.speechbtn);
        et = findViewById(R.id.editText1);
speechbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                askSpeechInput();
            }
        });


        ttsobject = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {

                if (status == TextToSpeech.SUCCESS) {

                    result = ttsobject.setLanguage(Locale.UK);

                } else {

                    Toast.makeText(getApplicationContext(),
                            "Feature not Supported in Your Device",
                            Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    private void askSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Hi speak something");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }

    // Receiving speech input

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra( RecognizerIntent.EXTRA_RESULTS);
                   et.setText(result.get(0));
                }
                break;
            }

        }
    }

    public void doSomething(View v) {
        // TODO Auto-generated method stub

        switch (v.getId()) {
            case R.id.bspeak:

                if(result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA){

                    Toast.makeText(getApplicationContext(),
                            "Feature not Supported in Your Device",
                            Toast.LENGTH_SHORT).show();

                }else{
                    text = et.getText().toString();

                    ttsobject.speak(text, TextToSpeech.QUEUE_FLUSH, null);

                }


                break;

            case R.id.bstopspeaking:

                if(ttsobject != null){

                    ttsobject.stop();


                }

                break;
        }


    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if(ttsobject != null){

            ttsobject.stop();
            ttsobject.shutdown();

        }

    }

}
