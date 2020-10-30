package com.sim.test.activity;

import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.Nullable;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.sim.test.R;
import com.sim.test.activity.base.BaseActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.ButterKnife;

/**
 * 利用File类开发的一个简单的文件管理器
 */

public class FileManagerActivity extends BaseActivity {

    ListView listView;
    TextView textView;
    //记录当前的父文件夹
    File currentParent;
    //记录当前路径下的所有文件的文件数组
    File[] currenFiles;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_manager);
        ButterKnife.bind(this);

        //获取列出全部文件的List View
        listView = findViewById(R.id.file_list);
        textView = findViewById(R.id.path_show);
        //获取系统的SD卡目录
        File root = new File(Environment.getExternalStorageDirectory().getPath());
        if (root.exists()) {
            currentParent = root;
            currenFiles = root.listFiles();
            //使用当前目录下的全部文件、文件夹来填充ListView
            inflateListView(currenFiles);
        }
        //为List View的列表项的单击事件绑定监听器
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //用户单击了文件，直接返回不做任何处理
                if (currenFiles[position].isFile()) return;
                File[] tmp = currenFiles[position].listFiles();
                if (tmp == null || tmp.length == 0) {
                    Toast.makeText(FileManagerActivity.this, "该路径下没有文件或当前路径不可访问", Toast.LENGTH_SHORT).show();
                } else {
                    //获取用户单击的列表项对应的文件夹，设为当前的父文件夹
                    currentParent = currenFiles[position];
                    //保存当前父文件夹内的全部文件和文件夹
                    currenFiles = tmp;
                    //再次更新ListView
                    inflateListView(currenFiles);
                }
            }
        });
        Button upper_level = findViewById(R.id.upper_level);
        upper_level.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!currentParent.getCanonicalPath().equals(Environment.getExternalStorageDirectory().getPath())) {
                        //获取上一级目录
                        currentParent = currentParent.getParentFile();
                        //列出当前目录下的所有文件
                        currenFiles = currentParent.listFiles();
                        //再次更新ListView
                        inflateListView(currenFiles);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void inflateListView(File[] files) {
        //创建一个List集合，List集合的元素是Map
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < files.length; i++) {
            Map<String, Object> listItem = new HashMap<String, Object>();
            //如果当前File是文件夹，使用folder图标，否则使用file图标
            if (files[i].isDirectory()) {
                listItem.put("fileIcon", R.drawable.flie_manger_folder);
            } else {
                listItem.put("fileIcon", R.drawable.file_manger_file);
            }
            listItem.put("fileName", files[i].getName());
            listItems.add(listItem);
        }
        //创建一个SimpleAdpter
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.item_file_manger_list, new String[]{"fileIcon", "fileName"}, new int[]{R.id.file_icon, R.id.file_name});
        //为ListView设置Adapter
        listView.setAdapter(simpleAdapter);
        try {
            textView.setText("当前路径为：" + currentParent.getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
