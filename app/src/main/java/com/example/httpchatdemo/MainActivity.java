package com.example.httpchatdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.httpchatdemo.adapter.ContactAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mList;
    private ContactAdapter contactAdapter;
   private ArrayList<Map<String,Object>> contactList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mList =(RecyclerView)findViewById(R.id.contact_list);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        mList.setLayoutManager(lm);
        mList.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        contactAdapter = new ContactAdapter(this,contactList);
        mList.setAdapter(contactAdapter);
        getContactList();
    }

    private void getContactList() {
        for(int i=0;i<8;i++){
            Map map = new HashMap();
            map.put("id",i+"");
            map.put("title","我是标题"+i);
            map.put("head",R.mipmap.test_icon);
            map.put("content","我是内容"+i);
            contactList.add(map);
        }
        contactAdapter.notifyDataSetChanged();
        contactAdapter.setOnItemClickLitener(new ContactAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MainActivity.this,ChatActivity.class);
                Map map  =contactList.get(position);
                intent.putExtra("id", map.get("id").toString());
               startActivity(intent);
            }
        });
    }
}
