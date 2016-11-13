package com.android.konng.beatbox;


public class Sound {

    private String mAssetPath;
    private String mName;
    /**
     * 注意， mSoundId用了Integer类型而不是int。这样，在Sound的mSoundId没有值时可以设
     * 置其为null值。
     */
    private Integer mSoundId;
    /**
     * 为有效显示声音文件名，我们在构造方法中对其进行处理。首先使用String.split(String)
     *方法分离出文件名，再使用String.replace(String, String)方法删除.wav后缀。
     */
    public Sound(String assetPath) {
        mAssetPath = assetPath;
        String[] components = assetPath.split("/");
        String filename = components[components.length - 1];
        mName = filename.replace(".wav", "");
    }
    public String getAssetPath() {
        return mAssetPath;
    }
    public String getName() {
        return mName;
    }

    public Integer getSoundId() {
        return mSoundId;
    }
    public void setSoundId(Integer soundId) {
        mSoundId = soundId;
    }
}
