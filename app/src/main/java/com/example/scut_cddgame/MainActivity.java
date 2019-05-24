package com.example.scut_cddgame;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    //MainActivity的入口
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.init_view);
    }

    //玩家点击START按钮事件
    public void btnStartClicked(View view) {
        setContentView(R.layout.login_view);
    }

    //玩家点击QUIT按钮事件
    public void btnQuitClicked(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("你确定要退出程序吗？");
        alertDialogBuilder.setPositiveButton("确定", killProgram);
        alertDialogBuilder.setNegativeButton("取消", cancelKillProgram);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    //玩家选择退出游戏时，杀死进程
    private DialogInterface.OnClickListener killProgram = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    };

    //取消退出游戏
    private DialogInterface.OnClickListener cancelKillProgram = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.cancel();
        }
    };

    //玩家在登录界面点击返回按钮
    public void btnLoginReturnClicked(View view) {
        setContentView(R.layout.init_view);
    }

    //玩家在登录界面点击登录按钮
    public void btnLoginClicked(View view) {
        //得到玩家账号和密码
        EditText editText = findViewById(R.id.txtAccount);
        String accountName = editText.getText().toString();
        editText = findViewById(R.id.txtPasswd);
        String password = editText.getText().toString();

        //判断用户名是否为空
        if (accountName == null || accountName.length() == 0) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("用户名不能为空！");
            alertDialogBuilder.setPositiveButton("确定", dealWithEmpty);
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else if (password == null || password.length() == 0) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("密码不能为空！");
            alertDialogBuilder.setPositiveButton("确定", dealWithEmpty);
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        //check account todo

        //start game todo
    }

    //玩家输入用户名或密码为空时，弹出对话框的按钮动作
    private DialogInterface.OnClickListener dealWithEmpty = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.cancel();
        }
    };

    //玩家点击注册按钮事件
    public void btnRegistClicked(View view){
        setContentView(R.layout.regist_view);
    }

    //玩家在注册账号页面点击返回按钮事件
    public void btnRegistReturnClicked(View view){
        setContentView(R.layout.login_view);
    }
}
