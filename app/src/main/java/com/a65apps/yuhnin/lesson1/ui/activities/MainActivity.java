package com.a65apps.yuhnin.lesson1.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.app.FragmentTransaction;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.a65apps.yuhnin.lesson1.R;
import com.a65apps.yuhnin.lesson1.ui.adapters.PersonListAdapter;
import com.a65apps.yuhnin.lesson1.ui.fragments.ContactDetailsFragment;
import com.a65apps.yuhnin.lesson1.ui.fragments.ContactListFragment;

import java.util.Objects;

public class MainActivity extends AppCompatActivity  {
    private static final String LOG_TAG = "activity_application";

    FragmentManager fragmentManager = getSupportFragmentManager();
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
    }


    private void createPersonListFragment() {
        ContactListFragment contactListFragment = new ContactListFragment();
        fragmentManager.beginTransaction().add(R.id.fragment_container, contactListFragment).commit();
    }

    public void onPersonClicked(long personId) {
        Bundle bundle = new Bundle();
        bundle.putLong("PERSON_ID", personId);
        ContactDetailsFragment contactDetailsFragment = new ContactDetailsFragment();
        contactDetailsFragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, contactDetailsFragment)
                .addToBackStack(null)
                .commit();

    }

    public void showToolbarbackButton(boolean isVisible) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(isVisible);

    }
    public void setToolbarText(String text) {
        getSupportActionBar().setTitle(text);
    }
}
