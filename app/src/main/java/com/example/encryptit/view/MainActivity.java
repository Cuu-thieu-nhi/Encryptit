package com.example.encryptit.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.encryptit.R;
import com.example.encryptit.adapter.ViewPageAdapter;
import com.example.encryptit.database.FileDAO;
import com.example.encryptit.utils.AddFileToEncrypt;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.LinkedHashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    FileDAO db;

    AddFileToEncrypt addFileToEncrypt;
    Set<Uri> set = new LinkedHashSet<>();
    int id = 0;
    private BottomNavigationView navigationView;
    private ViewPager viewPager;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);

        db = new FileDAO(MainActivity.this);
        addFileToEncrypt = new AddFileToEncrypt(MainActivity.this, db);

        id = db.getMaxId();

        navigationView = findViewById(R.id.bottom_nav);
        viewPager = findViewById(R.id.viewPager);
        floatingActionButton = findViewById(R.id.floating_button);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("*/*");
                String[] mimeTypes = {"image/jpeg", "image/png"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                }
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select picture"), 1);
            }
        });

        ViewPageAdapter adapter = new ViewPageAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        navigationView.getMenu().findItem(R.id.image_frag).setChecked(true);
                        break;
                    case 1:
                        navigationView.getMenu().findItem(R.id.file_frag).setChecked(true);
                        break;
                    case 2:
                        navigationView.getMenu().findItem(R.id.account_frag).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.image_frag:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.file_frag:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.account_frag:
                        viewPager.setCurrentItem(2);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        int currentFragmentPosition = viewPager.getCurrentItem();
        if (currentFragmentPosition > 0) {
            viewPager.setCurrentItem(currentFragmentPosition - 1);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            // xử lý nếu chọn 1 lúc nhiều ảnh
            if (data.getClipData() != null) {
                int x = data.getClipData().getItemCount();
                for (int i = 0; i < x; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    set.add(imageUri);
                }
                for (Uri uri : set) {
//                    Thread thread = new Thread(() -> addFileToEncrypt.AddSingleFile(uri));
//                    thread.start();
                    addFileToEncrypt.AddSingleFile(uri);
                }
            } else if (data.getData() != null) {
                Uri fileUri = data.getData();
//                Thread thread = new Thread(() -> addFileToEncrypt.AddSingleFile(fileUri));
//                thread.start();
                addFileToEncrypt.AddSingleFile(fileUri);
            }
        }
        Toast.makeText(this, "Đã chọn " + set.size(), Toast.LENGTH_SHORT).show();
    }
}