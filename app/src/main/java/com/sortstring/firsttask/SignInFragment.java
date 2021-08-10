package com.sortstring.firsttask;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sortstring.firsttask.databinding.FragmentSignInBinding;


public class SignInFragment extends Fragment {
    private FragmentSignInBinding bind;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mAuth = FirebaseAuth.getInstance();
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind = FragmentSignInBinding.bind(view);
        bind.signUp.setOnClickListener(v -> {
            NavHostFragment.findNavController(SignInFragment.this).navigate(R.id.action_signInFragment_to_signUp2);
        });

        bind.cardSignIn.setOnClickListener(v -> {
            String email = bind.editEmail.getText().toString();
            String password = bind.editPassword.getText().toString();
            InputMethodManager inputManager = (InputMethodManager)
                    getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputManager != null ) {
                inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
            if(email.length()>=10){
                if(password.length()>=8){
                    bind.proLogin.setVisibility(View.VISIBLE); // Show the progress bar
                    mAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(authResult -> {
                        updateUI(authResult.getUser());
                    }).addOnFailureListener(e -> {
                        Snackbar.make(bind.getRoot(),"error:" +e.getMessage(), BaseTransientBottomBar.LENGTH_LONG).show();
                        updateUI(null);
                    });
                }else{
                    bind.editPassword.setError("Invalid Password!");
                    bind.editPassword.requestFocus();
                    updateUI(null);
                }
            }else{
                bind.editEmail.setError("Invalid Email!");
                bind.editEmail.requestFocus();
                updateUI(null);
            }
        });
    }

    private void updateUI(FirebaseUser user) {
        bind.proLogin.setVisibility(View.GONE);
        if(user!=null) {
            NavHostFragment.findNavController(this).navigate(R.id.action_signInFragment_to_productsFragment);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null){
            updateUI(mAuth.getCurrentUser());
        }
    }
}