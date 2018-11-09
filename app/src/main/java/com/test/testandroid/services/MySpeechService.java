package com.test.testandroid.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by habeeb on 7/09/18.
 */

public class MySpeechService extends Service {
    protected static AudioManager mAudioManager;
    protected SpeechRecognizer mSpeechRecognizer;
    protected Intent mSpeechRecognizerIntent;
    protected final Messenger mServerMessenger = new Messenger(new IncomingHandler(this));

    protected boolean mIsListening;
    protected volatile boolean mIsCountDownOn;
    private static boolean mIsStreamSolo;

    static final int MSG_RECOGNIZER_START_LISTENING = 1;
    static final int MSG_RECOGNIZER_CANCEL = 2;

    // onCreate is executed during the first initialization
    @Override
    public void onCreate() {
        super.onCreate();
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mSpeechRecognizer.setRecognitionListener(new SpeechRecognitionListener());
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
    }

    protected static class IncomingHandler extends Handler {
        private WeakReference<MySpeechService> mtarget;

        IncomingHandler(MySpeechService target) {
            mtarget = new WeakReference<MySpeechService>(target);
        }

        @Override
        public void handleMessage(Message msg) {
            final MySpeechService target = mtarget.get();
            // Log.d("myfriend", "inside handle message");
            switch (msg.what) {
            case MSG_RECOGNIZER_START_LISTENING:
                // Log.d("myfriend", "message received");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && !mIsStreamSolo) {
                    // turn off beep sound
                    mAudioManager.setStreamSolo(AudioManager.STREAM_VOICE_CALL, true);
                    mIsStreamSolo = true;
                }
                if (!target.mIsListening) {
                    target.mSpeechRecognizer.startListening(target.mSpeechRecognizerIntent);
                    target.mIsListening = true;

                }
                break;

            case MSG_RECOGNIZER_CANCEL:
                if (mIsStreamSolo) {
                    mAudioManager.setStreamSolo(AudioManager.STREAM_VOICE_CALL, false);
                    mIsStreamSolo = false;
                }
                target.mSpeechRecognizer.cancel();
                target.mIsListening = false;

                break;
            }
        }
    }

    // Count down timer for Jelly Bean work around
    protected CountDownTimer mNoSpeechCountDown = new CountDownTimer(5000, 5000) {

        @Override
        public void onTick(long millisUntilFinished) {
            // TODO Auto-generated method stub

        }

        // onFinish is triggered when listening activity is finished
        @Override
        public void onFinish() {
            mIsCountDownOn = false;
            Message message = Message.obtain(null, MSG_RECOGNIZER_CANCEL);
            try {
                mServerMessenger.send(message);
                message = Message.obtain(null, MSG_RECOGNIZER_START_LISTENING);
                mServerMessenger.send(message);
            } catch (RemoteException e) {

            }
        }
    };

    @Override
    public void onDestroy() {
        if (mIsCountDownOn) {
            mNoSpeechCountDown.cancel();
        }
        if (mSpeechRecognizer != null) {
            mSpeechRecognizer.destroy();
        }
        super.onDestroy();
    }

    protected class SpeechRecognitionListener implements RecognitionListener {
        // when speechrecognizer is ready to take input
        @Override
        public void onBeginningOfSpeech() {
            Log.d("myfriend", "speech recog");
            // speech input will be processed, so there is no need for count
            // down anymore
            if (mIsCountDownOn) {
                mIsCountDownOn = false;
                mNoSpeechCountDown.cancel();
            }

        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            Log.d("myfriend", "onBuffer");
        }

        // When the speech input is finished
        @Override
        public void onEndOfSpeech() {
            Log.d("myfriend", "end of speech");
        }

        // Whenever error is encountered during speech recognition
        // doesnt include network or undetected word error
        @Override
        public void onError(int error) {
            if (mIsCountDownOn) {
                mIsCountDownOn = false;
                mNoSpeechCountDown.cancel();
            }
            mIsListening = false;
            Message message = Message.obtain(null, MSG_RECOGNIZER_START_LISTENING);
            try {
                mServerMessenger.send(message);
            } catch (RemoteException e) {

            }
        }

        @Override
        public void onEvent(int eventType, Bundle params) {
            Log.d("myfriend", "onEvents");
        }

        @Override
        public void onPartialResults(Bundle partialResults) {
            Log.d("myfriend", "onPartialresults");
        }

        @Override
        public void onReadyForSpeech(Bundle params) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mIsCountDownOn = true;
                mNoSpeechCountDown.start();

            }
            Log.d("myfriend", "onReadyForSpeech");
        }

        // onResults triggered when results are found
        @Override
        public void onResults(Bundle results) {

            Log.d("myfriend", "onResults");
            // strlist array stores the results
            ArrayList strlist = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            for (int i = 0; i < strlist.size(); i++) {
                Log.d("myfriend", "result=" + strlist.get(i));
                String result = (String) strlist.get(i);
                Log.d("myfriend", "" + result);
                // whenever any one of the results matches with "help"
                if (result.toLowerCase().contains("help")) {

                    // // Launching activity Worddetected class
                    // Intent dialogIntent = new Intent(getBaseContext(), WordDetected.class);
                    // dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    // getApplication().startActivity(dialogIntent);
                }

                mIsListening = false;
                Message message = Message.obtain(null, MSG_RECOGNIZER_START_LISTENING);
                try {
                    // Sending message again (to the same service)
                    mServerMessenger.send(message);
                } catch (RemoteException e) {

                }
            }
        }

        @Override
        public void onRmsChanged(float rmsdB) {
            // Log.d("myfriend", "onRmsChanged");
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("myfriend", "onBind");

        return mServerMessenger.getBinder();
    }

}
