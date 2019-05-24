package com.example.scut_cddgame;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.sql.*;

public class MainActivity extends AppCompatActivity {

    //MainActivity的入口
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.init_view);
    }

    //玩家在初始界面点击START按钮事件
    public void btnStartClicked(View view) {
        setContentView(R.layout.login_view);
    }

    //玩家在初始界面点击QUIT按钮事件
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

    //玩家在初始界面点击设置按钮事件
    public void btnSettingsClicked(View view) {
        setContentView(R.layout.settings_view);
    }

    //玩家在登录界面点击返回按钮
    public void btnLoginReturnClicked(View view) {
        setContentView(R.layout.init_view);
    }

    //玩家在登录界面点击登录按钮
    public void btnLoginClicked(View view) throws java.sql.SQLException,ClassNotFoundException {
        //得到玩家账号和密码
        EditText editText = findViewById(R.id.txtAccount);
        String accountName = editText.getText().toString();
        editText = findViewById(R.id.txtPasswd);
        String password = editText.getText().toString();

        //判断用户名密码是否为空或有错
        if (accountName == null || accountName.length() == 0) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("用户名不能为空！");
            alertDialogBuilder.setPositiveButton("确定", dealWithError);
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else if (password == null || password.length() == 0) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("密码不能为空！");
            alertDialogBuilder.setPositiveButton("确定", dealWithError);
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else if (!checkAccount(accountName, password)) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("用户名或密码错误！");
            alertDialogBuilder.setPositiveButton("确定", dealWithError);
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        // TODO: 19-5-24 start game
    }

    //玩家输入用户名或密码为空或用户名密码错误时，弹出对话框的按钮动作
    private DialogInterface.OnClickListener dealWithError = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.cancel();
        }
    };

    //检查玩家给定的账号密码是否正确
    private boolean checkAccount(String accountName, String passwd) throws java.sql.SQLException,ClassNotFoundException {
        boolean findAccount = false;
        try {
            Class.forName("org.mariadb.jdbc.Driver"); //如果本机安装的是mysql，把mariadb改成mysql即可，下同
            Connection connection = DriverManager.getConnection("jdbc:mariadb://IPAddress:3306/cddgame", "cddgame", "cddgame");
            String sql = "select * from account";
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);
            while (resultSet.next()) {
                String currAccountName=resultSet.getString("accountName");
                String currPassword=resultSet.getString("password");
                if (currAccountName==accountName && currPassword==passwd){
                    findAccount=true;
                    break;
                }
            }

        } catch (SQLException exc) {
            return false;
        }
        if (!findAccount) return false;
        else return true;
    }

    //玩家在登录界面点击注册按钮事件
    public void btnRegistClicked(View view) {
        setContentView(R.layout.regist_view);
    }

    //玩家在注册账号页面点击注册按钮事件
    public void btnRegistRegistClicked(View view) {
        // TODO: 19-5-24
    }

    //玩家在注册账号页面点击返回按钮事件
    public void btnRegistReturnClicked(View view) {
        setContentView(R.layout.login_view);
    }

    //玩家在设置页面点击返回按钮事件
    public void btnSettingsReturnClicked(View view) {
        setContentView(R.layout.init_view);
    }
}
