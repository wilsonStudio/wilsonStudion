package com.example.baopingx.myapplication;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListViewActivity extends Activity {

    // 声明ListView控件
    private ListView mListView;

    // 声明数组链表，其装载的类型是ListItem(封装了一个Drawable和一个String的类)
    private ArrayList<ListItem> mList;

    private  MainListViewAdapter adapter = null;

    /**
     * Acitivity的入口方法
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 指定Activity的布局使用activity_main.xml
        setContentView(R.layout.activity_list_view);

        // 通过findviewByID获取到ListView对象
        mListView = (ListView) findViewById(R.id.listView);


        // 获取Resources对象
        Resources res = this.getResources();

        mList = new ArrayList<ListViewActivity.ListItem>();

        // 初始化data，装载八组数据到数组链表mList中
        ListItem item = new ListItem();
        item.setImage(res.getDrawable(R.mipmap.ic_launcher));
        item.setTitle("项目一");
        mList.add(item);

        item = new ListItem();
        item.setImage(res.getDrawable(R.mipmap.ic_launcher));
        item.setTitle("项目二");
        mList.add(item);

        item = new ListItem();
        item.setImage(res.getDrawable(R.mipmap.ic_launcher));
        item.setTitle("项目三");
        mList.add(item);

        item = new ListItem();
        item.setImage(res.getDrawable(R.mipmap.ic_launcher));
        item.setTitle("项目四");
        mList.add(item);

        item = new ListItem();
        item.setImage(res.getDrawable(R.mipmap.ic_launcher));
        item.setTitle("项目五");
        mList.add(item);

        item = new ListItem();
        item.setImage(res.getDrawable(R.mipmap.ic_launcher));
        item.setTitle("项目六");
        mList.add(item);

        item = new ListItem();
        item.setImage(res.getDrawable(R.mipmap.ic_launcher));
        item.setTitle("项目七");
        mList.add(item);

        item = new ListItem();
        item.setImage(res.getDrawable(R.mipmap.ic_launcher));
        item.setTitle("项目八");
        mList.add(item);

        // 获取MainListAdapter对象
       adapter = new MainListViewAdapter();

        // 将MainListAdapter对象传递给ListView视图
        mListView.setAdapter(adapter);

    }

    public void  setOnItemClickListener (View view)
    {
        Toast.makeText(this, "你选择了" + adapter.getCount(), Toast.LENGTH_SHORT)
                .show();
    }
    /**
     * 定义ListView适配器MainListViewAdapter
     */
    class MainListViewAdapter extends BaseAdapter {

        /**
         * 返回item的个数
         */
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mList.size();
        }

        /**
         * 返回item的内容
         */
        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return mList.get(position);
        }

        /**
         * 返回item的id
         */
        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        /**
         * 返回item的视图
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ListItemView listItemView;

            // 初始化item view
            if (convertView == null) {
                // 通过LayoutInflater将xml中定义的视图实例化到一个View中
                convertView = LayoutInflater.from(ListViewActivity.this).inflate(
                        R.layout.items, null);

                // 实例化一个封装类ListItemView，并实例化它的两个域
                listItemView = new ListItemView();
                listItemView.imageView = (ImageView) convertView
                        .findViewById(R.id.image);
                listItemView.textView = (TextView) convertView
                        .findViewById(R.id.title);

                // 将ListItemView对象传递给convertView
                convertView.setTag(listItemView);
            } else {
                // 从converView中获取ListItemView对象
                listItemView = (ListItemView) convertView.getTag();
            }

            // 获取到mList中指定索引位置的资源
            Drawable img = mList.get(position).getImage();
            String title = mList.get(position).getTitle();

            // 将资源传递给ListItemView的两个域对象
            listItemView.imageView.setImageDrawable(img);
            listItemView.textView.setText(title);

            // 返回convertView对象
            return convertView;
        }

    }

    /**
     * 封装两个视图组件的类
     */
    class ListItemView {
        ImageView imageView;
        TextView textView;
    }

    /**
     * 封装了两个资源的类
     */
    class ListItem {
        private Drawable image;
        private String title;

        public Drawable getImage() {
            return image;
        }

        public void setImage(Drawable image) {
            this.image = image;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

    }
}