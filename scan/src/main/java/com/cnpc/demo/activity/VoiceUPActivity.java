package com.cnpc.demo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.rokid.glass.instruct.InstructionManager;
import com.rokid.glass.instruct.Integrate.IInstruction;
import com.rokid.glass.instruct.VoiceInstruction;
import com.rokid.glass.instruct.entity.InstructConfig;
import com.unity3d.player.UnityPlayerActivity;

public abstract class VoiceUPActivity extends UnityPlayerActivity implements IInstruction {

    protected InstructionManager mInstructionManager;
    protected InstructionManager.IInstructionListener mIInstructionListener = new InstructionManager.IInstructionListener() {
        @Override
        public boolean onReceiveCommand(String command) {
            return doReceiveCommand(command);
        }

        @Override
        public void onHelpLayerShow(boolean show) {

        }
    };

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Log.i("VoiceUPActivity", "onCreate");

        VoiceInstruction.init(this);
        //Log.i("VoiceUPActivity", "onStart" + VoiceInstruction.getInstance().mAppContext);

        mInstructionManager = new InstructionManager(this, closeInstruction(),configInstruct(),mIInstructionListener);
    }

    protected void onStart() {
        Log.i("VoiceUPActivity", "onStart");
        super.onStart();
        if(mInstructionManager != null) {
            mInstructionManager.onStart();
        }
    }

    @Override
    protected void onResume() {
        Log.i("VoiceUPActivity", "onResume");
        if (mInstructionManager != null) {
            mInstructionManager.onResume();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.i("VoiceUPActivity", "onPause");
        if (mInstructionManager != null) {
            mInstructionManager.onPause();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.i("VoiceUPActivity", "onDestroy");
        if (mInstructionManager != null) {
            mInstructionManager.onDestroy();
            mInstructionManager = null;
        }
        super.onDestroy();
    }

    protected void onRestart() {
        Log.i("VoiceUPActivity", "onRestart");
        super.onRestart();
    }


    /**
     * 是否关闭语音指令开关， 默认开启，继承可以选择关闭
     *
     * @return false:开启， true:关闭
     */
    @Override
    public boolean closeInstruction() {
        return false;
    }



    /**
     * 插件浮层相关UI已经准备并添加到主View树完毕，可以进行UI相关修改
     */
    @Override
    public void onInstrucUiReady() {
        if (mInstructionManager != null) {
            mInstructionManager.hideTipsLayer();//hide the tips at the screen bottom
        }
    }


    /**
     * 获取语音指令配置
     *
     * @return
     */
    @Override
    public abstract InstructConfig configInstruct();

    /**
     * 是否拦截处理当前语音指令，拦截后之前配置的指令闭包不会被调用
     * （可以用来提前处理一些指令，然后返回false）
     * @param command
     * @return true：拦截事件 false：不进行拦截
     */
    @Override
    public abstract boolean doReceiveCommand(String command);


}
