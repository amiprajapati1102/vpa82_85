package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.scaledrone.lib.Room;
import com.scaledrone.lib.RoomListener;
import com.scaledrone.lib.Scaledrone;
import android.content.Context;
import android.content.BroadcastReceiver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity{

    EditText userInput;
    RecyclerView recyclerView;
    List<ResponseMessage> responseMessageList;
    MessageAdapter messageAdapter;

    String userinput;
    private TextToSpeech tts;
    private ArrayList<String> questions;
    private String name, surname, age, asName;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private static final String PREFS = "prefs";
    private static final String NEW = "new";
    private static final String NAME = "name";
    private static final String AGE = "age";
    private static final String AS_NAME = "as_name";
    private WebView webView;

    private static MainActivity lastPausedActivity = null;
//
//    @Override
//    protected void onPause() {
//
//        super.onPause();
//        lastPausedActivity = this;
//    }
//
//    @Override
//    protected void onResume() {
//
//        super.onResume();
//        if(this == lastPausedActivity) {
//            lastPausedActivity = null;
//            Intent intent = new Intent(this, MainActivity.class);
//            intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
//            startActivity( intent );
//        }
//    }

    private static final String GOOGLE_SERACH_URL = "https://www.google.com/search?q=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences(PREFS,0);
        editor = preferences.edit();

        findViewById(R.id.microphoneButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"click...", Toast.LENGTH_LONG).show();
                listen();
            }
        });
        loadQuestions();

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "This Language is not supported");
                    }
//                    speak("hello.");

                } else {
                    Log.e("TTS", "Initilization Failed!");
                }
            }
        });


        userInput = findViewById(R.id.userInput);
        recyclerView = findViewById(R.id.conversation);
        responseMessageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(responseMessageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(messageAdapter);
        webView = (WebView) findViewById(R.id.webview);


        userInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEND){



                    userinput = userInput.getText().toString().toLowerCase();

//                    Toast.makeText(getApplicationContext(), userinput, Toast.LENGTH_LONG).show();

                    if(userinput.contains("hi") || userinput.contains("hello")){
                        ResponseMessage message = new ResponseMessage(userInput.getText().toString(),true);
                        responseMessageList.add(message);

                        ResponseMessage message2 = new ResponseMessage("how can i help you.",false);
                        speak("how can i help you.");
                        responseMessageList.add(message2);

                    } else if (userinput.contains("clear")){
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                        startActivity( intent );
                    } else if (userinput.contains("what is weather")){

                        webView.getSettings().setJavaScriptEnabled(true);
                        webView.loadUrl(GOOGLE_SERACH_URL +"today's weather");

                        ResponseMessage message = new ResponseMessage(userInput.getText().toString(),true);
                        responseMessageList.add(message);

                        ResponseMessage message2 = new ResponseMessage("Today's weather is",false);
                        responseMessageList.add(message2);

                    } else if (userinput.contains("+")){
                        Log.e("text", "" + userinput);
                        StringTokenizer sum = new StringTokenizer(userinput,"+");
                        String a = sum.nextToken();
                        String b = sum.nextToken();

                        float aa = Float.parseFloat(a);
                        float bb = Float.parseFloat(b);
                        float c  = aa + bb;

                        Log.e("sum:: ",""+sum);
                        Log.e("aa ::",""+ aa);
                        Log.e("bb ::",""+ bb);
                        Log.e("c ::",""+ c);

                        ResponseMessage message = new ResponseMessage(userInput.getText().toString(),true);
                        responseMessageList.add(message);

                        ResponseMessage message2 = new ResponseMessage("sum of " + aa + " and " + bb + " is " + c ,false);
                        responseMessageList.add(message2);
                        speak("sum of " + aa + " and " + bb + " is " + c );

                    }else if (userinput.contains("-")){
                        Log.e("text", "" + userinput);
                        StringTokenizer sum = new StringTokenizer(userinput,"-");
                        String a = sum.nextToken();
                        String b = sum.nextToken();

                        float aa = Float.parseFloat(a);
                        float bb = Float.parseFloat(b);
                        float c  = aa - bb;

                        Log.e("sum:: ",""+sum);
                        Log.e("aa ::",""+ aa);
                        Log.e("bb ::",""+ bb);
                        Log.e("c ::",""+ c);

                        ResponseMessage message = new ResponseMessage(userInput.getText().toString(),true);
                        responseMessageList.add(message);

                        ResponseMessage message2 = new ResponseMessage("Difference of " + aa + " and " + bb + " is " + c ,false);
                        responseMessageList.add(message2);
                        speak("Difference of " + aa + " and " + bb + " is " + c);

                    }else if (userinput.contains("*")){
                        Log.e("text", "" + userinput);
                        StringTokenizer sum = new StringTokenizer(userinput,"*");
                        String a = sum.nextToken();
                        String b = sum.nextToken();

                        float aa = Float.parseFloat(a);
                        float bb = Float.parseFloat(b);
                        float c  = aa * bb;

                        Log.e("sum:: ",""+sum);
                        Log.e("aa ::",""+ aa);
                        Log.e("bb ::",""+ bb);
                        Log.e("c ::",""+ c);

                        ResponseMessage message = new ResponseMessage(userInput.getText().toString(),true);
                        responseMessageList.add(message);

                        ResponseMessage message2 = new ResponseMessage("Multiplication of " + aa + " and " + bb + " is " + c,false);
                        responseMessageList.add(message2);
                        speak("Multiplication of " + aa + " and " + bb + " is " + c);

                    }else if (userinput.contains("/")){
                        Log.e("text", "" + userinput);
                        StringTokenizer sum = new StringTokenizer(userinput,"/");
                        String a = sum.nextToken();
                        String b = sum.nextToken();

                        float aa = Float.parseFloat(a);
                        float bb = Float.parseFloat(b);
                        if( aa != 0){
                            if( bb != 0){
                                float c  = aa / bb;

                                Log.e("sum:: ",""+sum);
                                Log.e("aa ::",""+ aa);
                                Log.e("bb ::",""+ bb);
                                Log.e("c ::",""+ c);

                                ResponseMessage message = new ResponseMessage(userInput.getText().toString(),true);
                                responseMessageList.add(message);

                                ResponseMessage message2 = new ResponseMessage("Division of " + aa + " and " + bb + " is " + c ,false);
                                responseMessageList.add(message2);
                                speak("Division of " + aa + " and " + bb + " is " + c );

                            } else {
                                ResponseMessage message = new ResponseMessage(userInput.getText().toString(),true);
                                responseMessageList.add(message);

                                ResponseMessage message2 = new ResponseMessage("Division of " + aa + " and " + bb + " is not possible" ,false);
                                responseMessageList.add(message2);
                                speak("Division of " + aa + " and " + bb + " is not possible");
                            }

                        } else {
                            ResponseMessage message = new ResponseMessage(userInput.getText().toString(),true);
                            responseMessageList.add(message);

                            ResponseMessage message2 = new ResponseMessage("Division of " + aa + " and " + bb + " is 0" ,false);
                            responseMessageList.add(message2);
                            speak("Division of " + aa + " and " + bb + " is 0");
                        }

                    }else if (userinput.contains("%")){
                        Log.e("text", "" + userinput);
                        StringTokenizer sum = new StringTokenizer(userinput,"%");
                        String a = sum.nextToken();
                        String b = sum.nextToken();

                        float aa = Float.parseFloat(a);
                        float bb = Float.parseFloat(b);
                        float c  = aa % bb;

                        Log.e("sum:: ",""+sum);
                        Log.e("aa ::",""+ aa);
                        Log.e("bb ::",""+ bb);
                        Log.e("c ::",""+ c);

                        ResponseMessage message = new ResponseMessage(userInput.getText().toString(),true);
                        responseMessageList.add(message);

                        ResponseMessage message2 = new ResponseMessage("Modulo of " + aa + " and " + bb + " is " + c ,false);
                        responseMessageList.add(message2);
                        speak("Modulo of " + aa + " and " + bb + " is " + c);

                    }else if (userinput.contains("^") || userinput.contains("power")){
                        Log.e("text", "" + userinput);
                        StringTokenizer sum = new StringTokenizer(userinput,"^,power");
                        String a = sum.nextToken();
                        String b = sum.nextToken();

                        float aa = Float.parseFloat(a);
                        float bb = Float.parseFloat(b);
                        float c  = (float) Math.pow(aa,bb);

                        Log.e("sum:: ",""+sum);
                        Log.e("aa ::",""+ aa);
                        Log.e("bb ::",""+ bb);
                        Log.e("c ::",""+ c);

                        ResponseMessage message = new ResponseMessage(userInput.getText().toString(),true);
                        responseMessageList.add(message);

                        ResponseMessage message2 = new ResponseMessage("power of " + aa +" and " + bb + " is "+c ,false);
                        responseMessageList.add(message2);
                        speak("power of " + aa +" and " + bb + " is "+c);

                    }else if (userinput.contains("equality")){
                        String remove = "equality";
//            Log.e("remove word :",""+text);
                        String tempWord = "equality" + " ";
                        userinput = userinput.replaceAll(tempWord, "");
                        Log.e("text", "" + userinput);
                        StringTokenizer sum = new StringTokenizer(userinput,",");
                        String a = sum.nextToken();
                        String b = sum.nextToken();

                        float aa = Float.parseFloat(a);
                        float bb = Float.parseFloat(b);
                        if( aa == bb){
                            ResponseMessage message = new ResponseMessage(userInput.getText().toString(),true);
                            responseMessageList.add(message);

                            ResponseMessage message2 = new ResponseMessage(aa + " and " + bb + " are equal." ,false);
                            responseMessageList.add(message2);
                            speak(aa + " and " + bb + " are equal.");

                        } else if( aa > bb){
                            ResponseMessage message = new ResponseMessage(userInput.getText().toString(),true);
                            responseMessageList.add(message);

                            ResponseMessage message2 = new ResponseMessage(aa + " is grater than " + bb,false);
                            responseMessageList.add(message2);
                            speak(aa + " is grater than " + bb);

                        } else if( aa < bb){
                            ResponseMessage message = new ResponseMessage(userInput.getText().toString(),true);
                            responseMessageList.add(message);

                            ResponseMessage message2 = new ResponseMessage(aa + " is less than " + bb ,false);
                            responseMessageList.add(message2);
                            speak(aa + " is less than " + bb);

                        }else {
                            ResponseMessage message = new ResponseMessage(userInput.getText().toString(),true);
                            responseMessageList.add(message);

                            ResponseMessage message2 = new ResponseMessage(aa + " and " + bb + " are not equal." ,false);
                            responseMessageList.add(message2);
                            speak(aa + " and " + bb + " are not equal.");

                        }

                        Log.e("sum:: ",""+sum);
                        Log.e("aa ::",""+ aa);
                        Log.e("bb ::",""+ bb);

                    } else if (userinput.contains("tell me about you")){

                        String[] speech = userinput.split(" ");
                        name = speech[speech.length-1];
                        Log.e("THIS", "" + name);
                        editor.putString(NAME,name).apply();
                        speak(questions.get(1));

                        ResponseMessage message = new ResponseMessage(userInput.getText().toString(),true);
                        responseMessageList.add(message);

                        ResponseMessage message2 = new ResponseMessage("I am your friendly assistant, I have designed to help HUMAN COMMUNITY,It is my pleasure to make your life more easier.",false);
                        responseMessageList.add(message2);

                    } else if (userinput.contains("what is time")){
                        SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm");//dd/MM/yyyy
                        Date now = new Date();
                        String[] strDate = sdfDate.format(now).split(":");
                        if(strDate[1].contains("00"))
                            strDate[1] = "o'clock";
                        speak("The time is " + sdfDate.format(now));

                        ResponseMessage message = new ResponseMessage(userInput.getText().toString(),true);
                        responseMessageList.add(message);

                        ResponseMessage message2 = new ResponseMessage(sdfDate.format(now),false);
                        responseMessageList.add(message2);

                    } else if (userinput.contains("what is date")){
                        SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM.yyyy");//dd/MM/yyyy
                        Date now = new Date();
                        String[] strDate = sdfDate.format(now).split(".");
                        speak("The Date is " + sdfDate.format(now));

                        ResponseMessage message = new ResponseMessage(userInput.getText().toString(),true);
                        responseMessageList.add(message);

                        ResponseMessage message2 = new ResponseMessage(sdfDate.format(now),false);
                        responseMessageList.add(message2);

                    } else if (userinput.contains("thank you")){

                        ResponseMessage message = new ResponseMessage(userInput.getText().toString(),true);
                        responseMessageList.add(message);

                        ResponseMessage message2 = new ResponseMessage("Thank you too ",false);
                        speak("Thank you too " + preferences.getString(NAME, null));
                        responseMessageList.add(message2);

                    } else if (userinput.contains("gm") || userinput.contains("good morning") || userinput.contains("Good morning")){

                        ResponseMessage message = new ResponseMessage(userInput.getText().toString(),true);
                        responseMessageList.add(message);

                        ResponseMessage message2 = new ResponseMessage(" Good Morning",false);
                        speak(questions.get(2));
                        responseMessageList.add(message2);

                    } else if (userinput.contains("gn") || userinput.contains("good night") || userinput.contains("Good morning")){

                        ResponseMessage message = new ResponseMessage(userInput.getText().toString(),true);
                        responseMessageList.add(message);

                        ResponseMessage message2 = new ResponseMessage(" Good night",false);
                        speak(questions.get(5));
                        responseMessageList.add(message2);

                    } else if (userinput.contains("good afternoon") || userinput.contains("Good afternoon")){

                        ResponseMessage message = new ResponseMessage(userInput.getText().toString(),true);
                        responseMessageList.add(message);

                        ResponseMessage message2 = new ResponseMessage("Good afternoon ",false);
                        speak(questions.get(3));
                        responseMessageList.add(message2);

                    } else if (userinput.contains("good evening") || userinput.contains("Good evening")){

                        ResponseMessage message = new ResponseMessage(userInput.getText().toString(),true);
                        responseMessageList.add(message);

                        ResponseMessage message2 = new ResponseMessage("Good evening",false);
                        speak(questions.get(4));
                        responseMessageList.add(message2);

                    } else if (userinput.contains("open whatsapp")){
                        Intent i = getPackageManager().getLaunchIntentForPackage("com.whatsapp");
                        if (i != null) {
                            startActivity(i);
                        } else {
                            speak("Application is not recognoize or not install in your device.");
                        }
                        ResponseMessage message = new ResponseMessage(userInput.getText().toString(),true);
                        responseMessageList.add(message);

                        ResponseMessage message2 = new ResponseMessage("ohky",false);
                        responseMessageList.add(message2);

                    } else if (userinput.contains("open facebook")){
                        Intent i = getPackageManager().getLaunchIntentForPackage("com.facebook.katana");
                        if (i != null) {
                            startActivity(i);
                        } else {
                            speak("Application is not recognoize or not install in your device.");
                        }

                        ResponseMessage message = new ResponseMessage(userInput.getText().toString(),true);
                        responseMessageList.add(message);

                        ResponseMessage message2 = new ResponseMessage("ohky",false);
                        responseMessageList.add(message2);

                    } else if (userinput.contains("open youtube")){
                        Intent i = getPackageManager().getLaunchIntentForPackage("com.google.android.youtube");
                        if (i != null) {
                            startActivity(i);
                        } else {
                            speak("Application is not recognoize or not install in your device.");
                        }

                        ResponseMessage message = new ResponseMessage(userInput.getText().toString(),true);
                        responseMessageList.add(message);

                        ResponseMessage message2 = new ResponseMessage("ohky ",false);
                        responseMessageList.add(message2);

                    } else if (userinput.contains("open gallery")){
                        Intent i = getPackageManager().getLaunchIntentForPackage("com.vivo.gallery");
                        if (i != null) {
                            startActivity(i);
                        } else {
                            speak("Application is not recognoize or not install in your device.");
                        }
                        ResponseMessage message = new ResponseMessage(userInput.getText().toString(),true);
                        responseMessageList.add(message);

                        ResponseMessage message2 = new ResponseMessage("ohky",false);
                        responseMessageList.add(message2);

                    } else if (userinput.contains("open camera")){
                        Intent i = getPackageManager().getLaunchIntentForPackage("com.oppo.camera");
                        if (i != null) {
                            startActivity(i);
                        } else {
                            speak("Application is not recognoize or not install in your device.");
                        }
                        ResponseMessage message = new ResponseMessage(userInput.getText().toString(),true);
                        responseMessageList.add(message);

                        ResponseMessage message2 = new ResponseMessage("ohky",false);
                        responseMessageList.add(message2);

                    } else if (userinput.contains("open contact list")){
                        Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                        if (i != null) {
                            startActivity(i);
                        } else {
                            speak("Application is not recognoize or not install in your device.");
                        }
                        ResponseMessage message = new ResponseMessage(userInput.getText().toString(),true);
                        responseMessageList.add(message);

                        ResponseMessage message2 = new ResponseMessage("ohky",false);
                        responseMessageList.add(message2);

                    } else if (userinput.contains("open music player")){
                        Intent i = getPackageManager().getLaunchIntentForPackage("com.amazon.mp3");
                        if (i != null) {
                            startActivity(i);
                        } else {
                            speak("Application is not recognoize or not install in your device.");
                        }
                        ResponseMessage message = new ResponseMessage(userInput.getText().toString(),true);
                        responseMessageList.add(message);

                        ResponseMessage message2 = new ResponseMessage("ohky",false);
                        responseMessageList.add(message2);

                    } else if (userinput.contains("search")){
                        Intent i = new Intent(Intent.ACTION_WEB_SEARCH);
//            Log.e("Search :",""+text);
                        String remove = "search";
//            Log.e("remove word :",""+text);
                        String tempWord = "search" + " ";
                        userinput = userinput.replaceAll(tempWord, "");
//            Log.e("final search :",""+text);
                        i.putExtra(SearchManager.QUERY, userinput);

                        webView.getSettings().setJavaScriptEnabled(true);
                        webView.loadUrl(GOOGLE_SERACH_URL + userinput);

//                        if (i != null) {
//                            startActivity(i);
//                        } else {
//                            speak("Application is not recognoize or not install in your device.");
//                        }

                        ResponseMessage message = new ResponseMessage(userInput.getText().toString(),true);
                        responseMessageList.add(message);

                        ResponseMessage message2 = new ResponseMessage("ohky",false);
                        responseMessageList.add(message2);

                    } else if (userinput.contains("by")){
                        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                        homeIntent.addCategory( Intent.CATEGORY_HOME );
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                        startActivity( intent );

                        startActivity(homeIntent);

                        ResponseMessage message = new ResponseMessage(userInput.getText().toString(),true);
                        responseMessageList.add(message);

                        ResponseMessage message2 = new ResponseMessage("ohky",false);
                        responseMessageList.add(message2);

                    } else {
                        ResponseMessage message = new ResponseMessage(userInput.getText().toString(),true);
                        responseMessageList.add(message);

                        ResponseMessage message2 = new ResponseMessage("Try Something which is known.",false);
                        responseMessageList.add(message2);
                        speak("Try Something which is known.");
                    }

                    messageAdapter.notifyDataSetChanged();

                    if(!isVisible()){
                        recyclerView.smoothScrollToPosition(messageAdapter.getItemCount()-1);
                    }
                }
                userInput.setText("");

                return true;
            }
        });

    }

    public boolean isVisible(){
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int positionOfLastVisibleItem = linearLayoutManager.findLastCompletelyVisibleItemPosition();
        int itemCount = recyclerView.getAdapter().getItemCount();
        return (positionOfLastVisibleItem >= itemCount);
    }

    private void loadQuestions(){
        questions = new ArrayList<>();
        questions.clear();
        questions.add("How can I help you?");
        questions.add("I am your friendly assistant, I have designed to help HUMAN COMMUNITY,It is my pleasure to make your life more easier.");
        questions.add("Good Morning! , It’s a brand new day");
        questions.add("Good Afternoon");
        questions.add("Good Evening");
        questions.add("Good Night");
    }

    private void listen(){
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something");

        try {
            startActivityForResult(i, 100);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(MainActivity.this, "Your device doesn't support Speech Recognition", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    private void speak(String text){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);

        }else{
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> res = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String inSpeech = res.get(0);
                recognition(inSpeech);
            }
        }
    }

    private void recognition(String text){


        Log.e("Speech",""+text);
        String[] speech = text.split(" ");
        text = text.toLowerCase();

        if(text.contains("hello") | text.contains("hi") | text.contains("hey") ){

            ResponseMessage message = new ResponseMessage(text,true);
            responseMessageList.add(message);

            ResponseMessage message2 = new ResponseMessage("how can i help you.",false);
            responseMessageList.add(message2);

            speak(questions.get(0));

        }

        if(text.contains("tell me about you")){
            ResponseMessage message = new ResponseMessage(text,true);
            responseMessageList.add(message);

            ResponseMessage message2 = new ResponseMessage("I am your friendly assistant, I have designed to help HUMAN COMMUNITY,It is my pleasure to make your life more easier.",false);
            responseMessageList.add(message2);

            name = speech[speech.length-1];
            Log.e("THIS", "" + name);
            editor.putString(NAME,name).apply();
            speak(questions.get(1));

        }

        if(text.contains("what is time")){
            SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm");//dd/MM/yyyy
            Date now = new Date();
            String[] strDate = sdfDate.format(now).split(":");
            if(strDate[1].contains("00"))
                strDate[1] = "o'clock";
            speak("The time is " + sdfDate.format(now));

            ResponseMessage message = new ResponseMessage(text,true);
            responseMessageList.add(message);

            ResponseMessage message2 = new ResponseMessage(sdfDate.format(now),false);
            responseMessageList.add(message2);

        }

        if(text.contains("what is date")){
            SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM.yyyy");//dd/MM/yyyy
            Date now = new Date();
            String[] strDate = sdfDate.format(now).split(".");
//            if(strDate[1].contains("00"))
//                strDate[1] = "abcd";
            speak("The Date is " + sdfDate.format(now));

            ResponseMessage message = new ResponseMessage(text,true);
            responseMessageList.add(message);

            ResponseMessage message2 = new ResponseMessage(sdfDate.format(now),false);
            responseMessageList.add(message2);

        }

        if(text.contains("set alarm")){
            Log.e("texttt ::",""+text);
            Log.e("Speechh ::",""+speech);
            speak(speech[speech.length-1]);

            String remove = "set alarm";
//            Log.e("remove word :",""+text);
            String tempWord = "set alarm" + " ";
            text = text.replaceAll(tempWord, "");
            Log.e("final time :: ",""+text);

//            String[] time = text.split(".");
            StringTokenizer time = new StringTokenizer(text,".");
            String hour = time.nextToken();
            String minutes = time.nextToken();

            int hr = Integer.parseInt(hour);
            int min = Integer.parseInt(minutes);

            Log.e("time :: ",""+time);
            Log.e("hour ::",""+ hr);
            Log.e("min ::",""+ min);
            Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
            i.putExtra(AlarmClock.EXTRA_HOUR, hr);
            i.putExtra(AlarmClock.EXTRA_MINUTES, min);
//            startActivity(i);

            speak("Setting alarm to ring at " + hour + ":" + minutes);

            ResponseMessage message = new ResponseMessage(text,true);
            responseMessageList.add(message);

            ResponseMessage message2 = new ResponseMessage("Alarm set",false);
            responseMessageList.add(message2);

        }

        if(text.contains("thank you")){
            speak("Thank you too " + preferences.getString(NAME, null));
            ResponseMessage message = new ResponseMessage(text,true);
            responseMessageList.add(message);

            ResponseMessage message2 = new ResponseMessage( "Thank you",false);
            responseMessageList.add(message2);

        }

        if(text.contains("gm") | text.contains("good morning")){
            speak(questions.get(2));

            ResponseMessage message = new ResponseMessage(text,true);
            responseMessageList.add(message);

            ResponseMessage message2 = new ResponseMessage("Good morning" ,false);
            responseMessageList.add(message2);

        }

        if(text.contains("good afternoon")){
            speak(questions.get(3));

            ResponseMessage message = new ResponseMessage(text,true);
            responseMessageList.add(message);

            ResponseMessage message2 = new ResponseMessage( "Good night",false);
            responseMessageList.add(message2);

        }

        if(text.contains("good evening")){
            speak(questions.get(4));
            ResponseMessage message = new ResponseMessage(text,true);
            responseMessageList.add(message);

            ResponseMessage message2 = new ResponseMessage( "Good evening",false);
            responseMessageList.add(message2);

        }

        if(text.contains("good night") | text.contains("gn")){
            speak(questions.get(5));

            ResponseMessage message = new ResponseMessage(text,true);
            responseMessageList.add(message);

            ResponseMessage message2 = new ResponseMessage("Good night" ,false);
            responseMessageList.add(message2);

        }

        if(text.contains("open whatsapp")){
            ResponseMessage message = new ResponseMessage(text,true);
            responseMessageList.add(message);

            ResponseMessage message2 = new ResponseMessage( "ohky",false);
            responseMessageList.add(message2);

            Intent i = getPackageManager().getLaunchIntentForPackage("com.whatsapp");
            if (i != null) {
                startActivity(i);
            } else {
                speak("Application is not recognoize or not install in your device.");
            }

        }

        if(text.contains("open facebook")){
            ResponseMessage message = new ResponseMessage(text,true);
            responseMessageList.add(message);

            ResponseMessage message2 = new ResponseMessage( "ohky",false);
            responseMessageList.add(message2);

            Intent i = getPackageManager().getLaunchIntentForPackage("com.facebook.katana");
            if (i != null) {
                startActivity(i);
            } else {
                speak("Application is not recognoize or not install in your device.");
            }

        }

        if(text.contains("open youtube")){
            ResponseMessage message = new ResponseMessage(text,true);
            responseMessageList.add(message);

            ResponseMessage message2 = new ResponseMessage( "ohky",false);
            responseMessageList.add(message2);

            Intent i = getPackageManager().getLaunchIntentForPackage("com.google.android.youtube");
            if (i != null) {
                startActivity(i);
            } else {
                speak("Application is not recognoize or not install in your device.");
            }

        }

        if(text.contains("open gallery")){
            ResponseMessage message = new ResponseMessage(text,true);
            responseMessageList.add(message);

            ResponseMessage message2 = new ResponseMessage( "ohky",false);
            responseMessageList.add(message2);

            Intent i = getPackageManager().getLaunchIntentForPackage("com.coloros.gallery3d");
            if (i != null) {
                startActivity(i);
            } else {
                speak("Application is not recognoize or not install in your device.");
            }

        }

        if(text.contains("open camera")){
            ResponseMessage message = new ResponseMessage(text,true);
            responseMessageList.add(message);

            ResponseMessage message2 = new ResponseMessage( "ohky",false);
            responseMessageList.add(message2);

            Intent i = getPackageManager().getLaunchIntentForPackage("com.android.camera");
            if (i != null) {
                startActivity(i);
            } else {
                speak("Application is not recognoize or not install in your device.");
            }

        }

        if(text.contains("open contact list")){
            ResponseMessage message = new ResponseMessage(text,true);
            responseMessageList.add(message);

            ResponseMessage message2 = new ResponseMessage( "ohky",false);
            responseMessageList.add(message2);

            Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            if (i != null) {
                startActivity(i);
            } else {
                speak("Application is not recognoize or not install in your device.");
            }

        }

        if(text.contains("open music player")){
            ResponseMessage message = new ResponseMessage(text,true);
            responseMessageList.add(message);

            ResponseMessage message2 = new ResponseMessage( "ohky",false);
            responseMessageList.add(message2);

            Intent i = getPackageManager().getLaunchIntentForPackage("om.amazon.mp3");
            if (i != null) {
                startActivity(i);
            } else {
                speak("Application is not recognoize or not install in your device.");
            }

        }

        if(text.contains("call")){
            ResponseMessage message = new ResponseMessage(text,true);
            responseMessageList.add(message);

            ResponseMessage message2 = new ResponseMessage( "ohky",false);
            responseMessageList.add(message2);

        }

        if(text.contains("search")){
            ResponseMessage message = new ResponseMessage(text,true);
            responseMessageList.add(message);

            ResponseMessage message2 = new ResponseMessage( "ohky",false);
            responseMessageList.add(message2);

            Intent i = new Intent(Intent.ACTION_WEB_SEARCH);
//            Log.e("Search :",""+text);
            String remove = "search";
//            Log.e("remove word :",""+text);
            String tempWord = "search" + " ";
            text = text.replaceAll(tempWord, "");
//            Log.e("final search :",""+text);
            i.putExtra(SearchManager.QUERY, text);

            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(GOOGLE_SERACH_URL + text);

            if (i != null) {
//                startActivity(i);
            } else {
                speak("Application is not recognoize or not install in your device.");
            }

        }

        if(text.contains("what is your name")){
            ResponseMessage message = new ResponseMessage(text,true);
            responseMessageList.add(message);

            ResponseMessage message2 = new ResponseMessage( "ohky",false);
            responseMessageList.add(message2);

            String as_name = preferences.getString(AS_NAME,"");
            if(as_name.equals(""))
                speak("How do you want to call me?");
            else
                speak("My name is "+as_name);

        }

        if(text.contains("call you")){
            ResponseMessage message = new ResponseMessage(text,true);
            responseMessageList.add(message);

            ResponseMessage message2 = new ResponseMessage( "ohky",false);
            responseMessageList.add(message2);

            String name = speech[speech.length-1];
            editor.putString(AS_NAME,name).apply();
            speak("I like it, thank you "+preferences.getString(NAME,null));

        }

        if(text.contains("what is my name")){
            ResponseMessage message = new ResponseMessage(text,true);
            responseMessageList.add(message);

            ResponseMessage message2 = new ResponseMessage( "ohky",false);
            responseMessageList.add(message2);

            speak("Your name is "+preferences.getString(NAME,null));

        }

        if(text.contains("clear")){
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
            startActivity( intent );
        }

        if (text.contains("+") || text.contains("plus")){
            Log.e("text", "" + text);
            StringTokenizer sum = new StringTokenizer(text,"+,plus");
            Log.e("sum:: ",""+sum);
            String a = sum.nextToken();
            Log.e("a:: ",""+a);
            String b = sum.nextToken();
            Log.e("b:: ",""+b);

            float aa = Float.parseFloat(a);
            float bb = Float.parseFloat(b);
            float c  = aa + bb;


            Log.e("aa ::",""+ aa);
            Log.e("bb ::",""+ bb);
            Log.e("c ::",""+ c);

            ResponseMessage message = new ResponseMessage(text,true);
            responseMessageList.add(message);

            ResponseMessage message2 = new ResponseMessage("sum of " + aa + " and " + bb + " is " + c ,false);
            responseMessageList.add(message2);
            speak("sum of " + aa + " and " + bb + " is " + c );

        }

        if (text.contains("-") || text.contains("minus") || text.contains("difference")){
            Log.e("text", "" + text);
            StringTokenizer sum = new StringTokenizer(text,"-,minus,difference");
            String a = sum.nextToken();
            String b = sum.nextToken();

            float aa = Float.parseFloat(a);
            float bb = Float.parseFloat(b);
            float c  = aa - bb;

            Log.e("sum:: ",""+sum);
            Log.e("aa ::",""+ aa);
            Log.e("bb ::",""+ bb);
            Log.e("c ::",""+ c);

            ResponseMessage message = new ResponseMessage(text,true);
            responseMessageList.add(message);

            ResponseMessage message2 = new ResponseMessage("Difference of " + aa + " and " + bb + " is " + c ,false);
            responseMessageList.add(message2);
            speak("Difference of " + aa + " and " + bb + " is " + c);

        }

        if (text.contains("*") || text.contains("multiply")){
            Log.e("text", "" + text);
            StringTokenizer sum = new StringTokenizer(text,"*,multiply");
            String a = sum.nextToken();
            String b = sum.nextToken();

            float aa = Float.parseFloat(a);
            float bb = Float.parseFloat(b);
            float c  = aa * bb;

            Log.e("sum:: ",""+sum);
            Log.e("aa ::",""+ aa);
            Log.e("bb ::",""+ bb);
            Log.e("c ::",""+ c);

            ResponseMessage message = new ResponseMessage(text,true);
            responseMessageList.add(message);

            ResponseMessage message2 = new ResponseMessage("Multiplication of " + aa + " and " + bb + " is " + c,false);
            responseMessageList.add(message2);
            speak("Multiplication of " + aa + " and " + bb + " is " + c);

        }

        if (text.contains("/") || text.contains("division") || text.contains("divide") || text.contains("divided by")){
            Log.e("text", "" + text);
            StringTokenizer sum = new StringTokenizer(text,"/,division,divide,divided by");
            String a = sum.nextToken();
            String b = sum.nextToken();

            float aa = Float.parseFloat(a);
            float bb = Float.parseFloat(b);
            if( aa != 0){
                if( bb != 0){
                    float c  = aa / bb;

                    Log.e("sum:: ",""+sum);
                    Log.e("aa ::",""+ aa);
                    Log.e("bb ::",""+ bb);
                    Log.e("c ::",""+ c);

                    ResponseMessage message = new ResponseMessage(text,true);
                    responseMessageList.add(message);

                    ResponseMessage message2 = new ResponseMessage("Division of " + aa + " and " + bb + " is " + c ,false);
                    responseMessageList.add(message2);
                    speak("Division of " + aa + " and " + bb + " is " + c );

                } else {
                    ResponseMessage message = new ResponseMessage(text,true);
                    responseMessageList.add(message);

                    ResponseMessage message2 = new ResponseMessage("Division of " + aa + " and " + bb + " is not possible" ,false);
                    responseMessageList.add(message2);
                    speak("Division of " + aa + " and " + bb + " is not possible");
                }

            } else {
                ResponseMessage message = new ResponseMessage(text,true);
                responseMessageList.add(message);

                ResponseMessage message2 = new ResponseMessage("Division of " + aa + " and " + bb + " is 0" ,false);
                responseMessageList.add(message2);
                speak("Division of " + aa + " and " + bb + " is 0");
            }

        }

        if (text.contains("%") || text.contains("modulo")){
            Log.e("text", "" +text);
            StringTokenizer sum = new StringTokenizer(text,"%,modulo");
            String a = sum.nextToken();
            String b = sum.nextToken();

            float aa = Float.parseFloat(a);
            float bb = Float.parseFloat(b);
            float c  = aa % bb;

            Log.e("sum:: ",""+sum);
            Log.e("aa ::",""+ aa);
            Log.e("bb ::",""+ bb);
            Log.e("c ::",""+ c);

            ResponseMessage message = new ResponseMessage(text,true);
            responseMessageList.add(message);

            ResponseMessage message2 = new ResponseMessage("Modulo of " + aa + " and " + bb + " is " + c ,false);
            responseMessageList.add(message2);
            speak("Modulo of " + aa + " and " + bb + " is " + c);

        }

        if (text.contains("^") || text.contains("power")){
            Log.e("text", "" + text);
            StringTokenizer sum = new StringTokenizer(text,"^,power");
            String a = sum.nextToken();
            String b = sum.nextToken();

            float aa = Float.parseFloat(a);
            float bb = Float.parseFloat(b);
            float c  = (float) Math.pow(aa,bb);

            Log.e("sum:: ",""+sum);
            Log.e("aa ::",""+ aa);
            Log.e("bb ::",""+ bb);
            Log.e("c ::",""+ c);

            ResponseMessage message = new ResponseMessage(text,true);
            responseMessageList.add(message);

            ResponseMessage message2 = new ResponseMessage("power of " + aa +" and " + bb + " is "+c ,false);
            responseMessageList.add(message2);
            speak("power of " + aa +" and " + bb + " is "+c);

        }

        if (text.contains("equality")){
            String remove = "equality";
//            Log.e("remove word :",""+text);
            String tempWord = "equality" + " ";
            text = text.replaceAll(tempWord, "");
            Log.e("text", "" + text);
            StringTokenizer sum = new StringTokenizer(text,"and");
            String a = sum.nextToken();
            String b = sum.nextToken();

            float aa = Float.parseFloat(a);
            float bb = Float.parseFloat(b);
            if( aa == bb){
                ResponseMessage message = new ResponseMessage(text,true);
                responseMessageList.add(message);

                ResponseMessage message2 = new ResponseMessage(aa + " and " + bb + " are equal." ,false);
                responseMessageList.add(message2);
                speak(aa + " and " + bb + " are equal.");

            } else if( aa > bb){
                ResponseMessage message = new ResponseMessage(text,true);
                responseMessageList.add(message);

                ResponseMessage message2 = new ResponseMessage(aa + " is grater than " + bb,false);
                responseMessageList.add(message2);
                speak(aa + " is grater than " + bb);

            } else if( aa < bb){
                ResponseMessage message = new ResponseMessage(text,true);
                responseMessageList.add(message);

                ResponseMessage message2 = new ResponseMessage(aa + " is less than " + bb ,false);
                responseMessageList.add(message2);
                speak(aa + " is less than " + bb);

            }else {
                ResponseMessage message = new ResponseMessage(text,true);
                responseMessageList.add(message);

                ResponseMessage message2 = new ResponseMessage(aa + " and " + bb + " are not equal." ,false);
                responseMessageList.add(message2);
                speak(aa + " and " + bb + " are not equal.");

            }

            Log.e("sum:: ",""+sum);
            Log.e("aa ::",""+ aa);
            Log.e("bb ::",""+ bb);

        }

        if(text.contains("by") || text.contains("bye")){
            ResponseMessage message = new ResponseMessage(text,true);
            responseMessageList.add(message);

            ResponseMessage message2 = new ResponseMessage( "close app",false);
            responseMessageList.add(message2);

            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory( Intent.CATEGORY_HOME );
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
            startActivity( intent );

            startActivity(homeIntent);


        }



    }

}