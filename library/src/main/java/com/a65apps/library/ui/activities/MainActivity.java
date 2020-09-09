package com.a65apps.library.ui.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.a65apps.library.Constants;
import com.a65apps.library.R;
import com.a65apps.library.ui.fragments.PermissionInfoFragment;
import com.a65apps.library.ui.fragments.PersonDetailsFragment;
import com.a65apps.library.ui.fragments.PersonListFragment;
import com.a65apps.library.ui.fragments.PersonMapsFragment;
import com.a65apps.library.ui.listeners.EventActionBarListener;
import com.a65apps.library.ui.listeners.OnPersonClickedListener;
import com.a65apps.library.ui.listeners.OnPersonSetLocation;

public class MainActivity extends AppCompatActivity
        implements OnPersonClickedListener, OnPersonSetLocation, EventActionBarListener {
    final String LOG_TAG = "activity_application";
    final String TAG_FRAGMENT_DETAILS = "TAG_FRAGMENT_DETAILS";
    final String TAG_FRAGMENT_PERM_REQ = "TAG_FRAGMENT_PERM_REQ";
    final String TAG_FRAGMENT_PERSON_LOCATION = "TAG_FRAGMENT_PERSON_LOCATION";
    final String TAG_FRAGMENT_LIST = "TAG_FRAGMENT_LIST";

    FragmentManager fragmentManager = getSupportFragmentManager();

    @Nullable
    Toolbar toolbar;

    @Override
    protected void onStart() {
        Log.d(LOG_TAG, "onStart start");
        super.onStart();
        Log.d(LOG_TAG, "onStart end");
    }

    @Override
    protected void onDestroy() {
        Log.d(LOG_TAG, "onDestroy start");
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy end");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        createNotificationChannel();
        fragmentManager = getSupportFragmentManager();
        int permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        permissionStatus += ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        permissionStatus += ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            if (fragmentManager.getFragments().isEmpty()) {
                String id = getIntent().getStringExtra(Constants.KEY_PERSON_ID);
                if (id != null && !id.isEmpty()) {
                    createDetailsFragment(id);
                } else {
                    createPersonListFragment();
                }
            }
        } else {
            requestReadContactsPermission();
        }
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(v -> onBackPressed());
        }
        Log.d(LOG_TAG, "onCreate end");
    }

    /**
     * Метод создания и отображения фрагмента со списком контактов
     */
    private void createPersonListFragment() {
        Log.d(LOG_TAG, "Создаем фрагмент списка контактов");
        PersonListFragment contactListFragment = (PersonListFragment) fragmentManager.findFragmentByTag(TAG_FRAGMENT_LIST);
        if (contactListFragment == null) {
            contactListFragment = PersonListFragment.Companion.newInstance();
            if (fragmentManager.getFragments().isEmpty()) {
                fragmentManager.beginTransaction().add(R.id.fragment_container, contactListFragment, TAG_FRAGMENT_LIST).commit();
            } else {
                fragmentManager.beginTransaction().replace(R.id.fragment_container, contactListFragment, TAG_FRAGMENT_LIST).commit();

            }
        }
    }

    /**
     * Метод создания и отображения фрагмента деталей о контакте
     *
     * @param personId идентификатор контакта
     */
    private void createDetailsFragment(String personId) {
        PersonDetailsFragment contactDetailsFragment = (PersonDetailsFragment) fragmentManager.findFragmentByTag(TAG_FRAGMENT_DETAILS);
        if (contactDetailsFragment == null) {
            // Фрагмент еще не создан
            contactDetailsFragment = PersonDetailsFragment.Companion.newInstance(personId);
            if (fragmentManager.getFragments().isEmpty()) {
                fragmentManager.beginTransaction()
                        .add(R.id.fragment_container, contactDetailsFragment, TAG_FRAGMENT_DETAILS)
                        .commit();
            } else {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, contactDetailsFragment, TAG_FRAGMENT_DETAILS)
                        .addToBackStack(null)
                        .commit();
            }
        }
    }

    /**
     * Метод создания и отображения фрагмента вежливого запроса разрешений
     */
    private void createPermissionRequestFragment() {
        PermissionInfoFragment requestPermissonFragment = (PermissionInfoFragment) fragmentManager.findFragmentByTag(TAG_FRAGMENT_PERM_REQ);
        if (requestPermissonFragment == null) {
            requestPermissonFragment = PermissionInfoFragment.Companion.newInstance();
        }
        if (fragmentManager.getFragments().isEmpty()) {
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, requestPermissonFragment, TAG_FRAGMENT_PERM_REQ)
                    .commit();
        } else {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, requestPermissonFragment, TAG_FRAGMENT_PERM_REQ)
                    .addToBackStack(null)
                    .commit();
        }
    }

    /**
     * Метод создания и отображения фрагмента задания местоположения контакта
     */
    private void createPersonMapFragment(String personId, String name) {
        PersonMapsFragment personMapsFragment = (PersonMapsFragment) fragmentManager
                .findFragmentByTag(TAG_FRAGMENT_PERSON_LOCATION);
        if (personMapsFragment == null) {
            personMapsFragment = PersonMapsFragment.Companion.newInstance(personId, name);
        }
        if (fragmentManager.getFragments().isEmpty()) {
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, personMapsFragment, TAG_FRAGMENT_PERSON_LOCATION)
                    .commit();
        } else {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, personMapsFragment, TAG_FRAGMENT_PERSON_LOCATION)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onItemClick(String personId) {
        createDetailsFragment(personId);
    }

    @Override
    public void setVisibleToolBarBackButton(boolean isVisible) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(isVisible);
        }
    }

    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(
                    Constants.NOTIFY_CHANNEL_ID,
                    Constants.NOTIFY_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(getString(R.string.notification_channel_desc));
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Метод запрашивающий разрешение на чтение контактов
     */
    private void requestReadContactsPermission() {
        boolean isNeedRequestPermissions = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS);
        isNeedRequestPermissions |= ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        isNeedRequestPermissions |= ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION);
        isNeedRequestPermissions |= ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (isNeedRequestPermissions) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.permission_request_title))
                    .setMessage(getString(R.string.permission_request_message))
                    .setPositiveButton(getString(R.string.permission_request_btn_positive), (dialog, which) -> androidx.core.app.ActivityCompat.requestPermissions(
                            com.a65apps.library.ui.activities.MainActivity.this,
                            new String[]{
                                    android.Manifest.permission.READ_CONTACTS,
                                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            com.a65apps.library.Constants.CODE_PERMISSION_REQUEST_CODE))
                    .setNegativeButton(getString(R.string.permission_request_btn_negative), (dialog, which) -> {
                        dialog.dismiss();
                        createPermissionRequestFragment();
                    }).create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, Constants.CODE_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Constants.CODE_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(),
                        getString(R.string.permission_granted),
                        Toast.LENGTH_SHORT).show();
                String id = getIntent().getStringExtra(Constants.KEY_PERSON_ID);
                if (id != null && !id.isEmpty()) {
                    createDetailsFragment(id);
                } else {
                    createPersonListFragment();
                }
            } else {
                Toast.makeText(getApplicationContext(),
                        getString(R.string.permission_not_received),
                        Toast.LENGTH_SHORT).show();
                createPermissionRequestFragment();
            }
        } else {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.permission_not_received),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPersonSetLocation(String personId, String name) {
        createPersonMapFragment(personId, name);
    }
}
