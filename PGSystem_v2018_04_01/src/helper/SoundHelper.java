/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import __main__.GlobalVars;
import java.io.File;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 *
 * @author user
 */
public class SoundHelper {

    public static File DefaultErrorSound = new File("DefaultErrorSound.WAV");
    public static File DefaultOkSound = new File("DefaultOkSound.WAV");

    /**
     *
     * @param sound
     */
    public static void PlayOkSound(File sound) {
        try {
            Clip clip = AudioSystem.getClip();

            if (sound == null) {
                System.out.println(GlobalVars.APP_PROP.getProperty("AUDIO_DIR")+ GlobalVars.APP_PROP.get("DEFAULT_OK_SOUND"));
                clip.open(AudioSystem.getAudioInputStream(new File(""+GlobalVars.APP_PROP.getProperty("AUDIO_DIR")+ GlobalVars.APP_PROP.get("DEFAULT_OK_SOUND"))));
                clip.start();
                Thread.sleep(clip.getMicrosecondLength() / 1000);
            } else {
                clip.open(AudioSystem.getAudioInputStream(sound));
                clip.start();
                Thread.sleep(clip.getMicrosecondLength() / 1000);
            }

        } catch (Exception e) {

        }
    }

    /**
     *
     * @param sound
     */
    public static void PlayErrorSound(File sound) {
        try {
            Clip clip = AudioSystem.getClip();

            if (sound == null) {
                System.out.println(GlobalVars.APP_PROP.getProperty("AUDIO_DIR")+ GlobalVars.APP_PROP.get("DEFAULT_ERROR_SOUND"));
                clip.open(AudioSystem.getAudioInputStream(new File(""+GlobalVars.APP_PROP.getProperty("AUDIO_DIR")+ GlobalVars.APP_PROP.get("DEFAULT_ERROR_SOUND"))));
                clip.start();
            } else {
                clip.open(AudioSystem.getAudioInputStream(sound));
                clip.start();
                Thread.sleep(clip.getMicrosecondLength() / 1000);
            }

        } catch (Exception e) {

        }
    }

}
