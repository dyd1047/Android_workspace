package com.koreait.viewpager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    ViewPager viewPager;
    MyViewPagerAdapter myViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager)findViewById(R.id.viewPager);

        myViewPagerAdapter = new MyViewPagerAdapter(this.getSupportFragmentManager(), 0);
        viewPager.setAdapter(myViewPagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=this.getMenuInflater();
        menuInflater.inflate(R.menu.navi, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String msg=null;

        switch (item.getItemId()){
            case R.id.yellow:showPage(0);break;
            case R.id.red:showPage(1);break;
            case R.id.blue:showPage(2);break;
        }
        return true;
    }

    public void showPage(int position){
        viewPager.setCurrentItem(position, true);
    }
}