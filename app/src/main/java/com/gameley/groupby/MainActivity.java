package com.gameley.groupby;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    int gender = -1;
    EditText editText;
    Button btAdd, btStartGroup;
    RecyclerView recyclerView;
    ProvinceAdapter provinceAdapter;
    RadioGroup genderRadioGroup;
    final Handler handler = new Handler();
    ArrayList<Player> nameList = new ArrayList<>();

    //初始数据
    public void initNameLis() {
        nameList.clear();
        nameList.add(new Player("董瑞攀", 1, 0));
        nameList.add(new Player("王争男", 0, 0));
        nameList.add(new Player("赵瑞宏", 0, 0));
        nameList.add(new Player("孟姝君", 0, 0));
        nameList.add(new Player("房啓钧", 1, 0));
        nameList.add(new Player("岳春霖", 1, 0));
        nameList.add(new Player("王金强", 1, 0));
        nameList.add(new Player("欧燕", 0, 0));
        nameList.add(new Player("王骞", 0, 0));
        nameList.add(new Player("刘冰瑶", 0, 0));
        nameList.add(new Player("王伟", 0, 0));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_main);
        //初始数据
        initNameLis();
        //find组件
        recyclerView = findViewById(R.id.recyclerView);
        editText = findViewById(R.id.editText);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        btAdd = findViewById(R.id.btAdd);
        btStartGroup = findViewById(R.id.btStartGroup);

        //常驻队员列表初始化
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        provinceAdapter = new ProvinceAdapter(this, nameList);
        recyclerView.setAdapter(provinceAdapter);
        //性别选择
        genderRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioBt_0) {
                gender = 0;
            } else if (checkedId == R.id.radioBt_1) {
                gender = 1;
            }
        });
        //添加队员
        btAdd.setOnClickListener(view -> {
            String editStr = editText.getText().toString();
            if (TextUtils.isEmpty(editStr)) {
                Toast.makeText(MainActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
            } else {
                provinceAdapter.addPlayer(new Player(editStr, gender, 0));
                recyclerView.smoothScrollToPosition(provinceAdapter.getItemCount() - 1);
                genderRadioGroup.clearCheck();
                editText.setText("");
                gender = -1;
            }
        });
        //开始分组
        btStartGroup.setOnClickListener(v -> {
            doGroupBy();
        });
    }

    //执行分组
    public void doGroupBy() {
        //获取参与分组的PNameList
        ArrayList<Player> nameList = provinceAdapter.getSelectPlayerList();
        //乱序洗牌
        Collections.shuffle(nameList);
        //进行分组
        ArrayList<String> resultNameList = new ArrayList<>();//盛放分组结果str的集合
        List<List<Player>> groupList = splitList(nameList, 2);//两两分组后，生成的组集合
        for (List<Player> group : groupList) {//group是组集合中的一个组
            StringBuilder stringBuilder = new StringBuilder();
            for (Player player : group) {//player是组中的一个队员
                stringBuilder.append(player.name).append("\n");
            }
            String str = stringBuilder.toString();//组中所有队员名字拼接str
            //遇到以下情况：重新分组
            if ((str.contains("房啓钧") && str.contains("王金强"))
                    || (str.contains("房啓钧") && str.contains("岳春霖"))
                    || (str.contains("王金强") && str.contains("岳春霖"))
                    || (str.contains("赵瑞宏") && str.contains("王骞"))
                    || (str.contains("欧燕") && str.contains("岳春霖"))) {
                //10ms后,重新分组
                handler.postDelayed(this::doGroupBy, 10);
                return;
            }
            resultNameList.add(str);
        }
        //将分组结果传递到ResultActivity进行展示
        Intent intent = new Intent(MainActivity.this, ResultActivity.class);
        intent.putStringArrayListExtra("nameArray", resultNameList);
        startActivity(intent);
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


    //RecyclerView的适配器类
    static class ProvinceAdapter extends RecyclerView.Adapter<ProvinceAdapter.MyViewHolder> {

        Context context;
        ArrayList<Player> playerList;
        ArrayList<Player> selectPlayerList = new ArrayList<>();

        public ProvinceAdapter(Context context, ArrayList<Player> playerList) {
            this.context = context;
            this.playerList = playerList;
            selectPlayerList.addAll(playerList);
        }

        //获取选中的参与分组的selectNameList
        public ArrayList<Player> getSelectPlayerList() {
            return selectPlayerList;
        }

        //增加新 player
        public void addPlayer(Player player) {
            playerList.add(player);
            selectPlayerList.add(player);
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return playerList.size();
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_plate_gongge_6_big, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            //Util.glideLoadImage(context, plate.getGames().get(position).getGame().getRoundIcon(), holder.appIcon);
            holder.pName.setText(playerList.get(position).name);
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean check) {
                    if (check) {
                        selectPlayerList.add(playerList.get(position));
                    } else {
                        selectPlayerList.remove(playerList.get(position));
                    }
                }
            });

            //appContentLayout的背景图设置逻辑
            if (playerList.get(position).gender == 0) {
                holder.appContentLayout.setBackgroundResource(R.drawable.shap_gongge6_big_bg2);
            } else if (playerList.get(position).gender == 1) {
                holder.appContentLayout.setBackgroundResource(R.drawable.shap_gongge6_big_bg4);
            } else if (playerList.get(position).gender == -1) {
                holder.appContentLayout.setBackgroundResource(R.drawable.shap_gongge6_big_bg1);
            }
        }

        static class MyViewHolder extends RecyclerView.ViewHolder {
            CardView appContentLayout;
            CheckBox checkBox;
            TextView pName;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                appContentLayout = itemView.findViewById(R.id.appContentLayout);
                checkBox = itemView.findViewById(R.id.checkBox);
                pName = itemView.findViewById(R.id.pName);
            }
        }
    }


}