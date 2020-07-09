package com.a65apps.library.ui.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.a65apps.library.ui.fragments.ContactDetailsFragment;
import com.a65apps.library.ui.fragments.PermissionInfoFragment;
import com.a65apps.library.ui.fragments.PersonListFragment;
import com.a65apps.library.ui.listeners.EventActionBarListener;
import com.a65apps.library.ui.listeners.OnPersonClickedListener;

public class MainActivity extends AppCompatActivity
        implements OnPersonClickedListener, EventActionBarListener {

    final String LOG_TAG = "activity_application";
    final String TAG_FRAGMENT_DETAILS = "TAG_FRAGMENT_DETAILS";
    final String TAG_FRAGMENT_PERM_REQ = "TAG_FRAGMENT_PERM_REQ";
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
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            if (fragmentManager.getFragments().isEmpty()) {
                String id = getIntent().getStringExtra(Constants.KEY_PERSON_ID);
                if (id != null && !id.isEmpty()) {
                    сreateDetailsFragment(id);
                } else {
                    createPersonListFragment();
                }
            }
        } else {
            requestReadContactsPermission();
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Log.d(LOG_TAG, "onCreate end");
    }


    /**
     * Метод создания и отображения фрагмента со списком контактов
     */
    private void createPersonListFragment() {
        Log.d(LOG_TAG, "Создаем фрагмент списка контактов");
        PersonListFragment contactListFragment = (PersonListFragment)fragmentManager.findFragmentByTag(TAG_FRAGMENT_LIST);
        if (contactListFragment == null) {
            contactListFragment = new PersonListFragment();
            if (fragmentManager.getFragments().isEmpty()) {
                fragmentManager.beginTransaction().add(R.id.fragment_container, contactListFragment, TAG_FRAGMENT_LIST).commit();
            } else {
                fragmentManager.beginTransaction().replace(R.id.fragment_container, contactListFragment, TAG_FRAGMENT_LIST).commit();

            }
        }
    }

    /**
     * Метод создания и отображения фрагмента деталей о контакте
     * @param personId идентификатор контакта
     */
    private void сreateDetailsFragment(String personId) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_PERSON_ID, personId);
        ContactDetailsFragment contactDetailsFragment = (ContactDetailsFragment) fragmentManager.findFragmentByTag(TAG_FRAGMENT_DETAILS);
        if (contactDetailsFragment == null) {
            // Фрагмент еще не создан
            contactDetailsFragment = new ContactDetailsFragment();
            contactDetailsFragment.setArguments(bundle);
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
    private void сreatePermissionRequestFragment() {
        PermissionInfoFragment requestPermissonFragment = (PermissionInfoFragment) fragmentManager.findFragmentByTag(TAG_FRAGMENT_PERM_REQ);
        if (requestPermissonFragment == null) {
            requestPermissonFragment = new PermissionInfoFragment();
        }
        if (fragmentManager.getFragments().isEmpty()) {
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, requestPermissonFragment, TAG_FRAGMENT_PERM_REQ)
                    .commit();
        } else {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, requestPermissonFragment, TAG_FRAGMENT_PERM_REQ)
                    .commit();
        }
    }

    @Override
    public void onItemClick(String personId) {
        сreateDetailsFragment(personId);
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
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.permission_request_title))
                    .setMessage(getString(R.string.permission_request_message))
                    .setPositiveButton(getString(R.string.permission_request_btn_positive), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(
                                    MainActivity.this,
                                    new String[]{Manifest.permission.READ_CONTACTS},
                                    Constants.CODE_PERMISSION_READ_CONTACTS);
                        }
                    })
                    .setNegativeButton(getString(R.string.permission_request_btn_negative), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            сreatePermissionRequestFragment();
                        }
                    }).create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, Constants.CODE_PERMISSION_READ_CONTACTS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case Constants.CODE_PERMISSION_READ_CONTACTS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), getString(R.string.permission_granted), Toast.LENGTH_SHORT);
                    String id = getIntent().getStringExtra(Constants.KEY_PERSON_ID);
                    if (id != null && !id.isEmpty()) {
                        сreateDetailsFragment(id);
                    } else {
                        createPersonListFragment();
                    }
                    return;
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.permission_not_received), Toast.LENGTH_SHORT);
                    сreatePermissionRequestFragment();
                }
                break;
            default: {
                Toast.makeText(getApplicationContext(), getString(R.string.permission_not_received), Toast.LENGTH_SHORT);
            }
        }
    }
}