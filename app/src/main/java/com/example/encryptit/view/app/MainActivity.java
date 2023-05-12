package com.example.encryptit.view.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
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
import com.google.firebase.auth.UserInfo;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static FileDAO db;
    Set<Uri> set = new LinkedHashSet<>();
    int id = 0;
    private TextView userEmail, userType;
    private ImageView userAvatar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private Menu navigationMenu;
    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseUser user;
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth auth;
    private String email = "";
    private String type = "";


    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);

        db = new FileDAO(MainActivity.this);
        id = db.getMaxId();

        getCurrentUserAndEmail();

        Log.d("Email-main", "onCreate: " + email);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken("956161453122-ih8cb633chq7572h68fvinqtdph06frb.apps.googleusercontent.com").requestEmail().build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);


        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        userEmail = navigationView.getHeaderView(0).findViewById(R.id.profile_email);
        userAvatar = navigationView.getHeaderView(0).findViewById(R.id.profile_image);
        userType = navigationView.getHeaderView(0).findViewById(R.id.profile_type);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        viewPager = findViewById(R.id.viewPager);
        FloatingActionButton floatingActionButton = findViewById(R.id.floating_button);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("EncryPTIT");

        navigationMenu = navigationView.getMenu();

        authListener = firebaseAuth -> {
            user = firebaseAuth.getCurrentUser();
            if (user == null) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            } else {
                email = user.getEmail();
                if (getUserName() != null) userEmail.setText(getUserName());
                else userEmail.setText(email);

                List<? extends UserInfo> providerData = user.getProviderData();
                for (UserInfo userInfo : providerData) {
                    String providerId = userInfo.getProviderId();
                    switch (providerId) {
                        case "firebase":
                            type = "Firebase";
                            enableOrDisableMenu(true);
                            getAvatar();
                            break;
                        case "google.com":
                            type = "Google.com";
                            enableOrDisableMenu(false);
                            getAvatar();
                            break;
                        case "facebook.com":
                            type = "Facebook.com";
                            enableOrDisableMenu(false);
                            getAvatar();
                            break;
                    }
                }
                userType.setText(type);
            }
        };

        floatingActionButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("*/*");
            String[] mimeTypes = {"image/jpeg", "image/png", "image/bmp", "image/webp", "application/*", "audio/*", "text/*", "video/*"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select file"), 1);
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

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.image_frag:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.file_frag:
                    viewPager.setCurrentItem(1);
                    break;
            }
            return true;
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
                showExitDialog();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getCurrentUserAndEmail();
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
            Toast.makeText(this, "Đã chọn " + set.size(), Toast.LENGTH_SHORT).show();
        } else if (requestCode == 2) {
            Toast.makeText(MainActivity.this, "Thanks for your response ╰(*°▽°*)╯", Toast.LENGTH_SHORT).show();
        } else if (requestCode == 3) {
            getAvatar();
            email = user.getEmail();
            if (getUserName() != null) userEmail.setText(getUserName());
            else userEmail.setText(email);
        }

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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.nav_logout:
                auth = FirebaseAuth.getInstance();
                auth.signOut();
                signOut();
                finish();
                break;
            case R.id.nav_changePass:
                intent = new Intent(MainActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_changeAvatar:
                intent = new Intent(MainActivity.this, ChangeAvatarAndUserNameActivity.class);
                startActivityForResult(intent, 3);
                break;
            case R.id.nav_guide:
                intent = new Intent(MainActivity.this, GuideActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_feedback:
                String recipientEmail = "dodanhtuandbnl@gmail.com";
                String subject = "Feedback for Encryptit";
                intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + recipientEmail));
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, 2);
                } else {
                    Toast.makeText(MainActivity.this, "No email application found!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nav_about:
                showAboutDialog();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signOut() {
        googleSignInClient.signOut().addOnCompleteListener(this, task -> {
            // Google Sign Out completed
        });
    }

    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận thoát ứng dụng");
        builder.setMessage("Bạn có chắc muốn thoát ứng dụng?");
        builder.setPositiveButton("OK", (dialog, which) -> {
            auth.signOut();
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build();
            GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(MainActivity.this, gso);
            googleSignInClient.signOut().addOnCompleteListener(task -> {
            });
            finish();
        }).setNegativeButton("Quay lại", (dialog, which) -> {

        });
        builder.show();
    }

    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View customView = inflater.inflate(R.layout.alertdialog_about, null);
        builder.setView(customView);
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void getCurrentUserAndEmail() {
        email = "";
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            email = user.getEmail();
            if (email != null) {
                List<? extends UserInfo> providerData = user.getProviderData();
                for (UserInfo userInfo : providerData) {
                    String providerId = userInfo.getProviderId();
                    switch (providerId) {
                        case "firebase":
                            email += ".firebase";
                            break;
                        case "google.com":
                            email += ".google";
                            break;
                        case "facebook.com":
                            email += ".facebook";
                            break;
                    }
                }
            }
        }
    }

    private void getAvatar() {
        if (user != null) {
            Uri photoUrl = user.getPhotoUrl();
            if (photoUrl != null) {
                String avatar = user.getPhotoUrl().toString();
                if (!avatar.equals("")) {
                    RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL);
                    Glide.with(MainActivity.this).load(avatar).apply(requestOptions).circleCrop().into(userAvatar);
                }
            }
        }
    }

    private String getUserName() {
        if (user != null) return user.getDisplayName();
        return null;
    }

    public void enableOrDisableMenu(boolean check) {
        MenuItem item1 = navigationMenu.findItem(R.id.nav_changePass);
        item1.setEnabled(check).setVisible(check);
        MenuItem item3 = navigationMenu.findItem(R.id.nav_changeAvatar);
        item3.setEnabled(check).setVisible(check);
    }
}