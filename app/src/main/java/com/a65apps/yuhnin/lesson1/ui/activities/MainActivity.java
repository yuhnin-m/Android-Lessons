package com.a65apps.yuhnin.lesson1.ui.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

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
        private final String LOG_TAG = "activity_application";
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
            Log.i(LOG_TAG, "Сработал ServiceConnection - onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
            Log.i(LOG_TAG, "Сработал ServiceConnection - onServiceDisconnected");

        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "onCreate strat");

        // Bind to LocalService
        Intent intent = new Intent(this, DataFetchService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fragmentManager = getSupportFragmentManager();
        if (findViewById(R.id.fragment_container) != null) {
            if(savedInstanceState == null) {
                createPersonListFragment();
            }
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Log.i(LOG_TAG, "onCreate end");
    }


    private void createPersonListFragment() {
        Log.i(LOG_TAG, "Создаем фрагмент списка контактов");
        ContactListFragment contactListFragment = new ContactListFragment();
        fragmentManager.beginTransaction().add(R.id.fragment_container, contactListFragment).commit();
    }


    @Override
    public void onItemClick(long personId) {
        Bundle bundle = new Bundle();
        bundle.putLong("PERSON_ID", personId);
        ContactDetailsFragment contactDetailsFragment = new ContactDetailsFragment();
        contactDetailsFragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, contactDetailsFragment)
                .addToBackStack(null)
                .commit();
        int num = mService.getRandomNumber();
        Log.d(LOG_TAG, "Сгенерировано новое случайное число: " + num);
    }

    @Override
    public void setVisibleToolBarBackButton(boolean isVisible) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(isVisible);
    }

    @Override
    public void getPersonList(PersonListResultListener callback) {
        Log.d(LOG_TAG, "Запрос из фрагмента: getPersonList");
        if (mService != null) {
            mService.fetchPersons(callback);
        }
    }

    @Override
    public void getPersonById(long id, PersonResultListener callback) {
        Log.d(LOG_TAG, "Запрос из фрагмента: getPersonById id=" + id);
        if (mService != null) {
            mService.fetchPersonById(callback, id);
        }
    }

    @Override
    public void getContactsByPerson(long id, ContactsResultListener callback) {
        Log.d(LOG_TAG, "Запрос из фрагмента: getContactsByPerson id=" + id);
        if (mService != null) {
            mService.fetchContactInfo(callback, id);
        }
    }
}
