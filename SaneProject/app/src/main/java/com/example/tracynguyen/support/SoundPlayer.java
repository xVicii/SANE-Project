package com.example.tracynguyen.support;

/**
 * Created by tracy.nguyen on 1/21/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;

import com.example.tracynguyen.saneproject.R;

/**
 * This class is responsible for playing sounds for the user.  It is a part of the user interface.
 * any class that needs to make sounds will use this class to play them.
 * @author pat.smith
 *
 */
public class SoundPlayer {
    private SoundPool notificationSounds;
    Activity parentActivity;
    AudioManager audioManager;

    boolean soundsLoaded;

    private int badPacketSoundID;
    private int packetSentSoundID;
    private int buttonPressSoundID;
    private int receiveMessageSoundID;
    private int sendMessageSoundID;
    private int alertSoundID;

    float maxVolume;
    float actualVolume;
    float currentVolume;

    private final int soundPriority = 0;
    private final int dontLoopSound = 0;
    private final float normalPlaybackRate = (float) 1.0;

    SoundPlayer(Activity activity){
        parentActivity = activity;
        parentActivity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        soundsLoaded = false;

        notificationSounds = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        notificationSounds.setOnLoadCompleteListener(soundsAreDoneLoading);
        loadSounds();
        audioManager = (AudioManager) parentActivity.getSystemService(Context.AUDIO_SERVICE);
        getVolume();
    }

    private void loadSounds(){
        /*badPacketSoundID = notificationSounds.load(parentActivity, R.raw.bad_packet, 1);
        packetSentSoundID = notificationSounds.load(parentActivity, R.raw.packet_sent, 1);
        buttonPressSoundID = notificationSounds.load(parentActivity, R.raw.button_sound, 1);
        receiveMessageSoundID = notificationSounds.load(parentActivity, R.raw.rx_im_sound, 1);
        sendMessageSoundID = notificationSounds.load(parentActivity, R.raw.send_im, 1);
        alertSoundID = notificationSounds.load(parentActivity, R.raw.gromb_sound, 1);*/

    }


    public OnLoadCompleteListener soundsAreDoneLoading = new SoundPool.OnLoadCompleteListener() {

        @Override
        public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
            soundsLoaded = true;
        }

    };

    private void getVolume(){
        try {
            actualVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

            maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            currentVolume = actualVolume/maxVolume;
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void playSound(int soundID){
        switch(soundID){
            case NetworkConstants.badPacketSound :
                playBadPacketSound();
                break;
            case NetworkConstants.packetSentSound :
                playPacketSentSound();
                break;
            case NetworkConstants.buttonPressSound :
                playButtonPressSound();
                break;
            case NetworkConstants.receiveMessageSound :
                playReceiveMessageSound();
                break;
            case NetworkConstants.sendMessageSound :
                playSendMessageSound();
                break;
            case NetworkConstants.alertSound :
                playSendMessageSound();
                break;
        }
    }

    public void playAlertSound(){
        getVolume();
        if (soundsLoaded){
            notificationSounds.play(badPacketSoundID, currentVolume, currentVolume,
                    soundPriority, dontLoopSound, normalPlaybackRate);
        }
    }
    public void playBadPacketSound(){
        getVolume();
        if (soundsLoaded){
            notificationSounds.play(badPacketSoundID, currentVolume, currentVolume,
                    soundPriority, dontLoopSound, normalPlaybackRate);
        }
    }
    public void playPacketSentSound(){
        getVolume();
        if (soundsLoaded){
            notificationSounds.play(packetSentSoundID, currentVolume, currentVolume,
                    soundPriority, dontLoopSound, normalPlaybackRate);
        }
    }
    public void playButtonPressSound(){
        getVolume();
        if (soundsLoaded){
            notificationSounds.play(buttonPressSoundID, currentVolume, currentVolume,
                    soundPriority, dontLoopSound, normalPlaybackRate);
        }
    }
    public void playReceiveMessageSound(){
        getVolume();
        if (soundsLoaded){
            notificationSounds.play(this.receiveMessageSoundID, currentVolume, currentVolume,
                    soundPriority, dontLoopSound, normalPlaybackRate);
        }
    }
    public void playSendMessageSound(){
        getVolume();
        if (soundsLoaded){
            notificationSounds.play(this.sendMessageSoundID, currentVolume, currentVolume,
                    soundPriority, dontLoopSound, normalPlaybackRate);
        }
    }

    public void getObjectReferences(Factory factory){
        // does nothing... currently needs no external objects because I'm just a lowly servant.
    }

}
