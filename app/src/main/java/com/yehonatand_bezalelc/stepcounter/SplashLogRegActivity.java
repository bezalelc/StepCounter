package com.yehonatand_bezalelc.stepcounter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class SplashLogRegActivity extends AppCompatActivity implements CallbackFragment {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_log_reg);
        addFragment(savedInstanceState);
    }

    public void addFragment(Bundle savedInstanceState) {
        if (!userConnected()) {
            LoginFragment loginFragment = new LoginFragment(this);
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .add(R.id.fragment_container_login_register, loginFragment, null)
                        .commit();
            }
        }
    }

//    public void replaceFragment(Fragment fragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.replace(R.id.fragment_container_login_register, fragment);
//        fragmentTransaction.commit();
//    }


    private boolean userConnected() {
        return false;
    }


//    public void replaceFragment()
    @Override
    public void changeFragment(boolean login) {
        LoginRegisterFragment fragment;
        if (login) {
            fragment = new LoginFragment(this);
        } else {
            fragment = new RegisterFragment(this);
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.fragment_container_login_register, fragment);
        fragmentTransaction.commit();
    }
}