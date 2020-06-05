package com.a65apps.yuhnin.lesson1.ui.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.a65apps.yuhnin.lesson1.R;
import com.a65apps.yuhnin.lesson1.services.DataFetchService;
import com.a65apps.yuhnin.lesson1.ui.fragments.ContactDetailsFragment;
import com.a65apps.yuhnin.lesson1.ui.fragments.ContactListFragment;
import com.a65apps.yuhnin.lesson1.ui.listeners.ContactsResultListener;
import com.a65apps.yuhnin.lesson1.ui.listeners.EventActionBarListener;
import com.a65apps.yuhnin.lesson1.ui.listeners.EventDataFetchServiceListener;
import com.a65apps.yuhnin.lesson1.ui.listeners.OnPersonClickedListener;
import com.a65apps.yuhnin.lesson1.ui.listeners.PersonListResultListener;
import com.a65apps.yuhnin.lesson1.ui.listeners.PersonResultListener;

public class MainActivity extends AppCompatActivity
        implements OnPersonClickedListener, EventActionBarListener, EventDataFetchServiceListener {

    final String LOG_TAG = "activity_application";
    final int CODE_PERMISSION_READ_CONTACTS = 1;
    final String TAG_FRAGMENT_DETAILS = "TAG_FRAGMENT_DETAILS";
    final String TAG_FRAGMENT_LIST = "TAG_FRAGMENT_LIST";

    FragmentManager fragmentManager = getSupportFragmentManager();
    Toolbar toolbar;
    boolean mBound = false;
    @Nullable
    DataFetchService mService;

    @Override
    protected void onStart() {
        Log.d(LOG_TAG, "onStart start");
        super.onStart();
        Log.d(LOG_TAG, "onStart end");
    }

    @Override
    protected void onDestroy() {
        Log.d(LOG_TAG, "onDestroy start");
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy end");
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            DataFetchService.LocalBinder binder = (DataFetchService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            Fragment fragment = fragmentManager.findFragmentByTag(TAG_FRAGMENT_DETAILS);
            if (fragment != null) {
                ((ContactDetailsFragment)fragment).serviceBinded();
            }
            fragment = fragmentManager.findFragmentByTag(TAG_FRAGMENT_LIST);
            if (fragment != null) {
                ((ContactListFragment)fragment).serviceBinded();
            }
            Log.d(LOG_TAG, "Сработал ServiceConnection - onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
            String a = null;
            Log.d(LOG_TAG, "Сработал ServiceConnection - onServiceDisconnected");
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate strat");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        createNotificationChannel();
        fragmentManager = getSupportFragmentManager();
        requestReadContactsPermission();
        Intent intent = new Intent(this, DataFetchService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (fragmentManager.getFragments().isEmpty()) {
            String id = getIntent().getStringExtra("KEY_PERSON_ID");
            if (id != null && !id.isEmpty()) {
                сreateDetailsFragment(id);
            } else {
                createPersonListFragment();
            }
        }

        Log.d(LOG_TAG, "onCreate end");
    }


    private void createPersonListFragment() {
        Log.d(LOG_TAG, "Создаем фрагмент списка контактов");
        ContactListFragment contactListFragment = (ContactListFragment)fragmentManager.findFragmentByTag(TAG_FRAGMENT_LIST);
        if (contactListFragment == null) {
            // Фрагмент еще не создан
            contactListFragment = new ContactListFragment();
            fragmentManager.beginTransaction().add(R.id.fragment_container, contactListFragment, TAG_FRAGMENT_LIST).commit();
        }
    }

    private void сreateDetailsFragment(String personId) {
        Bundle bundle = new Bundle();
        bundle.putString("PERSON_ID", personId);
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

    @Override
    public void getPersonList(PersonListResultListener callback) {
        Log.d(LOG_TAG, "Запрос из фрагмента: getPersonList");
        if (mService != null) {
            mService.fetchPersons(callback);
        }
    }

    @Override
    public void getPersonById(String id, PersonResultListener callback) {
        Log.d(LOG_TAG, "Запрос из фрагмента: getPersonById id=" + id);
        if (mService != null) {
            mService.fetchPersonById(callback, id);
        }
    }

    @Override
    public void getContactsByPerson(String id, ContactsResultListener callback) {
        Log.d(LOG_TAG, "Запрос из фрагмента: getContactsByPerson id=" + id);
        if (mService != null) {
            mService.fetchContactInfo(callback, id);
        }
    }

    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(
                "channel",
                getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription(getString(R.string.notification_channel_desc));
        notificationManager.createNotificationChannel(channel);
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
                                    CODE_PERMISSION_READ_CONTACTS);
                        }
                    })
                    .setNegativeButton(getString(R.string.permission_request_btn_negative), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, CODE_PERMISSION_READ_CONTACTS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case CODE_PERMISSION_READ_CONTACTS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), getString(R.string.permission_granted), Toast.LENGTH_SHORT);
                    return;
                }
                break;
            default: {
                Toast.makeText(getApplicationContext(), getString(R.string.permission_not_received), Toast.LENGTH_SHORT);
                finish();
            }
        }
    }
}
