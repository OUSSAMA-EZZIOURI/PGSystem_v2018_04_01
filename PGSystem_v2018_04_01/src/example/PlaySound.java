/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import sun.applet.Main;
import sun.audio.AudioDataStream;

/**
 *
 * @author user
 */
public class PlaySound {

    public static void main(String[] args){
        File ErrorSound = new File("error.WAV");
        PlaySound(ErrorSound);
    }

    static void PlaySound(File sound) {
        try{
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(sound));
            clip.start();
            
            Thread.sleep(clip.getMicrosecondLength()/1000);
        }catch(Exception e){
            
        }
    }
}
