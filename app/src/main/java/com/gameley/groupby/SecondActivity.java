package com.gameley.groupby;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SecondActivity extends AppCompatActivity {
    EditText editText;
    Button btStartGroup;
    ArrayList<String> nameList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_second);
        //find组件
        btStartGroup = findViewById(R.id.btStartGroup);
        editText = findViewById(R.id.editText);
        btStartGroup.setOnClickListener(view -> {
            //根据editText的输入获取到人数,并将人对应的数字标号赋值到nameList中
            if (!initNameLis()) return;//数据初始化失败则return
            //乱序洗牌
            Collections.shuffle(nameList);
            //进行分组
            ArrayList<String> resultNameList = new ArrayList<>();//盛放分组结果的集合
            List<List<String>> splitList = splitList(nameList, 2);//两两分组
            for (List<String> list : splitList) {
                StringBuilder stringBuilder = new StringBuilder();
                for (String s : list) {
                    stringBuilder.append(s).append("\n");
                }
                resultNameList.add(stringBuilder.toString());
            }
            //将分组结果传递到ResultActivity进行展示
            Intent intent = new Intent(SecondActivity.this, ResultActivity.class);
            intent.putStringArrayListExtra("nameArray", resultNameList);
            startActivity(intent);
        });
        //editText获取焦点
        editText.requestFocus();
        //打开软键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    //初始数据
    public boolean initNameLis() {
        nameList.clear();
        if (TextUtils.isEmpty(editText.getText().toString())) {
            Toast.makeText(this, "输入为空，重新输入", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (editText.getText().toString().trim().contains(".")) {
            Toast.makeText(this, "请输入正整数", Toast.LENGTH_SHORT).show();
            return false;
        }
        int num = Integer.parseInt(editText.getText().toString().trim());//人数
        for (int i = 1; i <= num; i++) {
            nameList.add(String.valueOf(i));
        }
        return true;
    }


    /**
     * 数组分割方法
     * 思路也比较简单,就是遍历加切块,
     * 若toIndex大于list的size说明已越界,需要将toIndex设为list的size值
     */
    public static <T> List<List<T>> splitList(List<T> list, int pageSize) {
        List<List<T>> listArray = new ArrayList<>();
        for (int i = 0; i < list.size(); i += pageSize) {
            int toIndex = Math.min(i + pageSize, list.size());
            listArray.add(list.subList(i, toIndex));
        }
        return listArray;
    }


}