package com.cnpc.demo.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.rokid.glass.instruct.entity.EntityKey;
import com.rokid.glass.instruct.entity.IInstructReceiver;
import com.rokid.glass.instruct.entity.InstructConfig;
import com.rokid.glass.instruct.entity.InstructEntity;
import com.rokid.glass.instruct.type.NumberKey;
import com.rokid.glass.instruct.type.NumberTypeControler;
import com.unity3d.player.UnityPlayer;

public class UXRUnityActivity extends VoiceUPActivity {

    int REQUEST_CODE_SCAN_INFO = 110;

    private static final String TAG = "UXRActivity" ;




    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Log.i(TAG, "UXRUnityActivity onCreate");


    }

    protected void onStart() {
        Log.i(TAG, "onStart");
        super.onStart();


    }


    @Override
    protected void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();

        //start();

    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause");
        super.onPause();


    }

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop");
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();



    }

// --------------------------voice instruct -----------------------------------

    @Override
    public InstructConfig configInstruct() {
        InstructConfig config = new InstructConfig();
        config.setActionKey(UXRUnityActivity.class.getName() + InstructConfig.ACTION_SUFFIX);
        config.addInstructEntity(
                new InstructEntity()
                        .addEntityKey(new EntityKey("测试", null))
                        .addEntityKey(new EntityKey(EntityKey.Language.en, "test this"))
                        .setShowTips(true)
                        .setIgnoreHelp(true)
                        .setCallback(new IInstructReceiver() {
                            @Override
                            public void onInstructReceive(Activity act, String key, InstructEntity instruct) {
                                //doFunc();
                            }
                        })
        );

        return config;
    }

    /**
     * 是否拦截处理当前语音指令，拦截后之前配置的指令闭包不会被调用
     * （可以用来提前处理一些指令，然后返回false）
     * @param command
     * @return true：拦截事件 false：不进行拦截
     */
    @Override
    public boolean doReceiveCommand(String command) {
        Log.d(TAG, "doReceiveCommand command = " + command);

        if ("测试".equals(command)) {
            return true;
        }
        return false;
    }


    // --------------------------voice instruct language zh-----------
    public  void removeInstructZH( String name ) {
        Log.d(TAG, "---java removeInstruct---" + name);

        mInstructionManager.removeInstruct(EntityKey.Language.zh,name);
        mInstructionManager.sendWtWords();
    }

    public  void clearUserInstruct(  ) {
        Log.d(TAG, "---java clearUserInstruct---" );

        mInstructionManager.clearUserInstruct();
        mInstructionManager.sendWtWords();
    }

    public  void addInstructZH( String name, String pinyin, String unityGameObject,String unityFunc,String tmp) {

        addInstructZH(name, pinyin, true,true,true, unityGameObject, unityFunc, tmp);
    }

    public  void addInstructZH( String name, String pinyin, boolean showTips, boolean ignoreHelp, boolean ignoreToast, String unityGameObject,String unityFunc,String tmp) {
        Log.d(TAG, "---java addInstructZH---" + name);

        InstructConfig mconfig = mInstructionManager.getInstructConfig();

        final String tunityGameObject = unityGameObject;
        final String tunityFunc = unityFunc;
        final String ttmp = tmp;

        mconfig.addInstructEntity(
                new InstructEntity()
                        .addEntityKey(new EntityKey(name, pinyin))
                        .setShowTips(showTips)
                        .setIgnoreHelp(ignoreHelp)
                        .setIgnoreToast(ignoreToast)
                        .setCallback(new IInstructReceiver() {
                            @Override
                            public void onInstructReceive(Activity act, String key, InstructEntity instruct) {
                                Log.d(TAG, "---java onInstructReceive---" + tunityFunc );
                                UnityPlayer.UnitySendMessage( tunityGameObject,tunityFunc,ttmp);
                            }
                        })
        );

        mInstructionManager.sendWtWords();

    }

    public  void addInstructListZH( String prefix, String subfix, String helpContent,int startNumber, int endNumber, String unityGameObject,String unityFunc) {
        Log.d(TAG, "---java addInstructListZH---" + prefix + subfix);

        InstructConfig mconfig = mInstructionManager.getInstructConfig();

        final String tunityGameObject = unityGameObject;
        final String tunityFunc = unityFunc;

        mconfig.addInstructList(NumberTypeControler.doTypeControl(startNumber, endNumber,
                new NumberTypeControler.NumberTypeCallBack() {
                    @Override
                    public void onInstructReceive(Activity act, String key, int number, InstructEntity instruct) {
                        Log.d(TAG, "-- java AudioAi Number onInstructReceive command = " + key + ", number = " + number);

                        UnityPlayer.UnitySendMessage( tunityGameObject,tunityFunc,key+"-"+number);
                    }
                },
                new NumberKey(EntityKey.Language.zh, prefix, subfix, helpContent)//,
                //new NumberKey(EntityKey.Language.en, "the", "page", "the 3/4.../20 page")
                )
        );

        mInstructionManager.sendWtWords();
    }

    // --------------------------language check---------------------------------------------------------
    public  void removeInstruct( int languageEnum, String name ) {
        Log.d(TAG, "---java removeInstruct---" + languageEnum+name);
        if(languageEnum == 0){
            mInstructionManager.removeInstruct(EntityKey.Language.zh,name);
        }else if(languageEnum == 1){
            mInstructionManager.removeInstruct(EntityKey.Language.en,name);
        }
        mInstructionManager.sendWtWords();
    }

    public  void addInstruct( int languageEnum, String name, String unityGameObject,String unityFunc,String tmp) {

        addInstruct(languageEnum,name, true,true,true, unityGameObject, unityFunc, tmp);
    }

    public  void addInstruct( int languageEnum,String name, boolean showTips, boolean ignoreHelp, boolean ignoreToast, String unityGameObject,String unityFunc,String tmp) {
        Log.d(TAG, "---java addInstruct---" +languageEnum+ name);

        InstructConfig mconfig = mInstructionManager.getInstructConfig();

        final String tunityGameObject = unityGameObject;
        final String tunityFunc = unityFunc;
        final String ttmp = tmp;

        if(languageEnum == 0){//zh
            mconfig.addInstructEntity(
                    new InstructEntity()
                            .addEntityKey(new EntityKey(name,null))
                            .setShowTips(showTips)
                            .setIgnoreHelp(ignoreHelp)
                            .setIgnoreToast(ignoreToast)
                            .setCallback(new IInstructReceiver() {
                                @Override
                                public void onInstructReceive(Activity act, String key, InstructEntity instruct) {
                                    Log.d(TAG, "---java onInstructReceive---" + tunityFunc );
                                    UnityPlayer.UnitySendMessage( tunityGameObject,tunityFunc,ttmp);
                                }
                            })
            );
        }else if(languageEnum == 1){//en
            mconfig.addInstructEntity(
                    new InstructEntity()
                            .addEntityKey(new EntityKey(EntityKey.Language.en,name))
                            .setShowTips(showTips)
                            .setIgnoreHelp(ignoreHelp)
                            .setIgnoreToast(ignoreToast)
                            .setCallback(new IInstructReceiver() {
                                @Override
                                public void onInstructReceive(Activity act, String key, InstructEntity instruct) {
                                    Log.d(TAG, "---java onInstructReceive---" + tunityFunc );
                                    UnityPlayer.UnitySendMessage( tunityGameObject,tunityFunc,ttmp);
                                }
                            })
            );
        }

        mInstructionManager.sendWtWords();
    }

    public  void addInstructList( int languageEnum, String prefix, String subfix, String helpContent,int startNumber, int endNumber, String unityGameObject,String unityFunc) {
        Log.d(TAG, "---java addInstructList---" + prefix + subfix);

        InstructConfig mconfig = mInstructionManager.getInstructConfig();

        final String tunityGameObject = unityGameObject;
        final String tunityFunc = unityFunc;

        NumberKey key = new NumberKey(EntityKey.Language.zh, prefix, subfix, helpContent);//default to 0: zh
        if(languageEnum == 1) {
            key = new NumberKey(EntityKey.Language.en, prefix, subfix, helpContent);
        }

        mconfig.addInstructList(NumberTypeControler.doTypeControl(startNumber, endNumber,
                new NumberTypeControler.NumberTypeCallBack() {
                    @Override
                    public void onInstructReceive(Activity act, String key, int number, InstructEntity instruct) {
                        Log.d(TAG, "-- java AudioAi Number onInstructReceive command = " + key + ", number = " + number);

                        UnityPlayer.UnitySendMessage( tunityGameObject,tunityFunc,key+"-"+number);
                    }
                },
                key
                )
        );

        mInstructionManager.sendWtWords();
    }

    public void startScan(String unityGameObject,String unityFunc){

        final String tunityFunc = unityFunc;
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.rokid.glass.scan2",
                "com.rokid.glass.scan2.activity.QrCodeActivity");
        intent.setComponent(comp);
        startActivityForResult(intent, REQUEST_CODE_SCAN_INFO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_SCAN_INFO) {
            String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
            UnityPlayer.UnitySendMessage("Voice","ScanCallback",result);
        }
    }
}
