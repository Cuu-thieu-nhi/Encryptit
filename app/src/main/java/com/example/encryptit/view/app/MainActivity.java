package com.example.encryptit.view.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.encryptit.R;
import com.example.encryptit.adapter.ViewPageAdapter;
import com.example.encryptit.background.AddFileToEncryptTask;
import com.example.encryptit.database.FileDAO;
import com.example.encryptit.view.account.LoginActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.LinkedHashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static FileDAO db;
    Set<Uri> set = new LinkedHashSet<>();
    int id = 0;

    private TextView userEmail;
    private ImageView userAvatar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;
    private FloatingActionButton floatingActionButton;
    private FirebaseAuth.AuthStateListener authListener;

    private GoogleSignInOptions gso;
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth auth;
    private String email = "";
    private String avatar = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);

        db = new FileDAO(MainActivity.this);
        id = db.getMaxId();

        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) email = currentUser.getEmail();
        Log.d("MainActivity", "onCreate: " + email);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("956161453122-ih8cb633chq7572h68fvinqtdph06frb.apps.googleusercontent.com")
                .requestEmail().build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        userEmail = navigationView.getHeaderView(0).findViewById(R.id.profile_email);
        userAvatar = navigationView.getHeaderView(0).findViewById(R.id.profile_image);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        viewPager = findViewById(R.id.viewPager);
        floatingActionButton = findViewById(R.id.floating_button);

        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("EncryPTIT");

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                } else {
                    email = user.getEmail();
                    userEmail.setText(email);

                    avatar = user.getPhotoUrl().toString();

                    if (!avatar.equals("")) {
                        RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL);

                        Glide.with(MainActivity.this).load(avatar).apply(requestOptions).into(userAvatar);
                    }
                }
            }
        };

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("*/*");
                String[] mimeTypes = {"image/jpeg", "image/png", "image/bmp", "image/webp", "application/*", "audio/*", "text/*", "video/*"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                }
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select file"), 1);
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
                        bottomNavigationView.getMenu().findItem(R.id.image_frag).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.file_frag).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.image_frag:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.file_frag:
                        viewPager.setCurrentItem(1);
                        break;
                }
                return true;
            }
        });


    }


    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            int currentFragmentPosition = viewPager.getCurrentItem();
            if (currentFragmentPosition > 0) {
                viewPager.setCurrentItem(currentFragmentPosition - 1);
            } else {
                auth.signOut();
                googleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Google Sign Out completed
                    }
                });

                finish();
                super.onBackPressed();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data.getClipData() != null) {
                int x = data.getClipData().getItemCount();
                for (int i = 0; i < x; i++) {
                    Uri fileUri = data.getClipData().getItemAt(i).getUri();
                    set.add(fileUri);
                }
                Uri[] uriArray = set.toArray(new Uri[set.size()]);
                AddFileToEncryptTask task = new AddFileToEncryptTask(MainActivity.this, email);
                task.execute(uriArray);
            } else if (data.getData() != null) {
                Uri fileUri = data.getData();
                AddFileToEncryptTask task = new AddFileToEncryptTask(MainActivity.this, email);
                task.execute(fileUri);
            }
        }
        Toast.makeText(this, "Đã chọn " + set.size(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//        signOut();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_logout:
                auth = FirebaseAuth.getInstance();
                auth.signOut();
                signOut();
                finish();
                break;
            case R.id.nav_changePass:
                Intent intent = new Intent(MainActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_feedback:

                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signOut() {
        googleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // Google Sign Out completed
            }
        });
    }
}