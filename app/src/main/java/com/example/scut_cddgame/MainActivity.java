package com.example.scut_cddgame;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.init_view);
    }

    //玩家点击START按钮事件
    public void btnStartClicked(View view){
        setContentView(R.layout.login_view);
    }

    //玩家点击QUIT按钮事件
    public void btnQuitClicked(View view){
        AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("你确定要退出程序吗？");
        alertDialogBuilder.setPositiveButton("确定",killProgram);
        alertDialogBuilder.setNegativeButton("取消",cancelKillProgram);
        AlertDialog alertDialog=alertDialogBuilder.create();
        alertDialog.show();
    }

    //玩家选择退出游戏时，杀死进程
    private DialogInterface.OnClickListener killProgram=new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    };

    //取消退出游戏
    private DialogInterface.OnClickListener cancelKillProgram=new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.cancel();
        }
    };

    //玩家在登录界面点击返回按钮
    public void btnLoginReturnClicked(View view){
        setContentView(R.layout.init_view);
    }
}
