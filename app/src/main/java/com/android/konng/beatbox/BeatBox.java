package com.android.konng.beatbox;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class BeatBox {
    private static final String TAG = "BeatBox";
    private static final String SOUNDS_FOLDER = "sample_sounds";
    private static final int MAX_SOUNDS = 5;

    private List<Sound> mSounds = new ArrayList<>();
    private SoundPool mSoundPool;

    /**
     * Lollipop引入了新的方式创建SoundPool：使用SoundPool.Builder。不过，为了兼容API 16
     * 最低级别，只能选择使用SoundPool(int, int, int)这个老构造方法了。
     * 第一个参数指定同时播放多少个音频。这里指定了5个。在播放5个音频时，如果尝试再播放
     * 第6个， SoundPool会停止播放原来的音频。
     * 第二个参数确定音频流类型。 Android有很多不同的音频流，它们都有各自独立的音量控制
     * 选项。这就是调低音乐音量，闹钟音量却不受影响的原因。打开文档，查看AudioManager类的
     * AUDIO_*常量，还可以看到其他控制选项。STREAM_MUSIC使用的是同音乐和游戏一样的音量控制。
     * 最后一个参数指定采样率转换品质。参考文档说这个参数不起作用，所以这里传入0值。
     */
    private AssetManager mAssets;

    public BeatBox(Context context) {
        mAssets = context.getAssets();
        mSoundPool = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0);
        loadSounds();
    }

    /**
     * 播放前要检查并确保soundId不是null值。 Sound加载失败会导致soundId出现null值。
     *    检查通过以后，就可以调用SoundPool.play(int, float, float, int, int, float)方法
     *    播放音频了。这些参数依次是：音频ID、左音量、右音量、优先级（无效）、是否循环以及播放
     *    速率。我们需要最大音量和常速播放，所以传入值1.0。是否循环参数传入0值，代表不循环。（如
     *    果想无限循环，可以传入-1。我们觉得这会非常令人讨厌。）
     */
    public void play(Sound sound) {
        Integer soundId = sound.getSoundId();
        if (soundId == null) {
            return;
        }
        mSoundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    /**
     * 释放音乐
     * */
    public void release() {
        mSoundPool.release();
    }
    /**
     * AssetManager.list(String)方法能列出指定目录中的所有文件名。因此，只要传入声音资
     * 源所在的目录，就能看到其中的所有.wav文件。
     */
    private void loadSounds() {
        String[] soundNames;
        try {
            soundNames = mAssets.list(SOUNDS_FOLDER);
            Log.i(TAG, "Found " + soundNames.length + " sounds");
        } catch (IOException ioe) {
            Log.e(TAG, "Could not list assets", ioe);
            return;
        }

        for (String filename : soundNames) {
            try {
                String assetPath = SOUNDS_FOLDER + "/" + filename;
                Sound sound = new Sound(assetPath);
                load(sound);
                mSounds.add(sound);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public List<Sound> getSounds() {
        return mSounds;
    }

    /**
     * 调用mSoundPool.load(AssetFileDescriptor, int)方法可以把文件载入SoundPool待播。
     * 为方便管理、重播或卸载音频文件， mSoundPool.load(...)方法会返回一个int型ID。这实际就
     * 是存储在mSoundId中的ID。调用openFd(String)方法有可能抛出IOException， load(Sound)
     * 方法也是如此
     */
    private void load(Sound sound) throws IOException {
        AssetFileDescriptor afd = mAssets.openFd(sound.getAssetPath());
        int soundId = mSoundPool.load(afd, 1);
        sound.setSoundId(soundId);
    }

}
