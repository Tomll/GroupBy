package com.gameley.groupby;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gameley.groupby.widget.auto_scroll_viewpager.AutoViewPager;
import com.gameley.groupby.widget.auto_scroll_viewpager.AutoViewPagerAdapter;
import com.gameley.groupby.widget.auto_scroll_viewpager.FixedSpeedScroller;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {
    ArrayList<String> nameList = new ArrayList<>();
    ProvinceAdapter provinceAdapter;
    RecyclerView recyclerView;
    AutoViewPager autoViewPager;
    int countNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //获取分组结果数据
        nameList = getIntent().getStringArrayListExtra("nameArray");
        //find组件
        recyclerView = findViewById(R.id.recyclerView);
        //设置布局管理器
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        //设置适配器
        provinceAdapter = new ProvinceAdapter(this, nameList);
        recyclerView.setAdapter(provinceAdapter);

        //初始化自动轮播画廊组件
        initAutoViewPager();

    }

    //初始化自动轮播画廊组件
    @SuppressLint("UseCompatLoadingForDrawables")
    public void initAutoViewPager() {
        //数据
        ArrayList<Drawable> drawableList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            switch (i) {
                case 0:
                    drawableList.add(getResources().getDrawable(R.drawable._0));
                    break;
                case 1:
                    drawableList.add(getResources().getDrawable(R.drawable._1));
                    break;
                case 2:
                    drawableList.add(getResources().getDrawable(R.drawable._2));
                    break;
                case 3:
                    drawableList.add(getResources().getDrawable(R.drawable._3));
                    break;
                case 4:
                    drawableList.add(getResources().getDrawable(R.drawable._4));
                    break;
                case 5:
                    drawableList.add(getResources().getDrawable(R.drawable._5));
                    break;
                case 6:
                    drawableList.add(getResources().getDrawable(R.drawable._6));
                    break;
                case 7:
                    drawableList.add(getResources().getDrawable(R.drawable._7));
                    break;
            }
        }
        //组件
        autoViewPager = findViewById(R.id.autoViewPager);
        //适配器:创建AutoViewPagerAdapter
        AutoViewPagerAdapter pagerAdapter = new AutoViewPagerAdapter<>(this, drawableList, null);
        //初始化AutoViewPager,传入适配器,并传入指示点布局,如果用户不需要指示点布局，那么传入null即可（指示点布局传入前必须先进行初始化）
        autoViewPager.init(pagerAdapter, null, true, new FixedSpeedScroller(this));
    }


    @Override
    protected void onResume() {
        super.onResume();
        autoViewPager.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        autoViewPager.stop();
    }

    //RecyclerView的适配器类
    static class ProvinceAdapter extends RecyclerView.Adapter<ProvinceAdapter.MyViewHolder> {

        final Context context;
        final ArrayList<String> pNameList;

        public ProvinceAdapter(Context context, ArrayList<String> pNameList) {
            this.context = context;
            this.pNameList = pNameList;
        }

        @Override
        public int getItemCount() {
            return pNameList.size();
        }

        @NonNull
        @Override
        public ProvinceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_plate_gongge_small, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ProvinceAdapter.MyViewHolder holder, final int position) {
            //holder.checkBox.setVisibility(View.GONE);
            holder.pName.setText(pNameList.get(position));
            //appContentLayout的背景图设置逻辑
            int i = position % 6;//当前position对6取余数
            switch (i) {//判断余数i
                case 0:
                    holder.appContentLayout.setBackgroundResource(R.drawable.shap_gongge6_big_bg0);
                    break;
                case 1:
                    holder.appContentLayout.setBackgroundResource(R.drawable.shap_gongge6_big_bg1);
                    break;
                case 2:
                    holder.appContentLayout.setBackgroundResource(R.drawable.shap_gongge6_big_bg2);
                    break;
                case 3:
                    holder.appContentLayout.setBackgroundResource(R.drawable.shap_gongge6_big_bg3);
                    break;
                case 4:
                    holder.appContentLayout.setBackgroundResource(R.drawable.shap_gongge6_big_bg4);
                    break;
                case 5:
                    holder.appContentLayout.setBackgroundResource(R.drawable.shap_gongge6_big_bg5);
                    break;
            }
        }

        static class MyViewHolder extends RecyclerView.ViewHolder {
            CardView appContentLayout;
            //CheckBox checkBox;
            TextView pName;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                appContentLayout = itemView.findViewById(R.id.appContentLayout);
                //checkBox = itemView.findViewById(R.id.checkBox);
                pName = itemView.findViewById(R.id.pName);
            }
        }
    }


}
