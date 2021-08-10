package com.sortstring.firsttask;

import android.app.AlertDialog;
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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.sortstring.firsttask.databinding.FragmentSignUpBinding;


public class SignUp extends Fragment {
    private FragmentSignUpBinding bind;
    private FirebaseAuth mAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mAuth = FirebaseAuth.getInstance();
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind = FragmentSignUpBinding.bind(view);
        bind.textSignIn.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_signUp2_to_signInFragment);
        });
        bind.cardSignUp.setOnClickListener(v -> {


            String email = bind.editUEmail.getText().toString();
            String password = bind.editUPassword.getText().toString();
            InputMethodManager inputManager = (InputMethodManager)
                    getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputManager != null ) {
                inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
            if(!email.isEmpty()&&email.length()>=10){
                if(!password.isEmpty()&&password.length()>=8){
                    bind.proSignUp.setVisibility(View.VISIBLE);
                    mAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(authResult -> {
                        String uid = authResult.getUser().getUid();
                        SignUpModel signUpModel = new SignUpModel(uid,email);
                        updateUserProfile(signUpModel);
                    }).addOnFailureListener(e -> {
                        Snackbar.make(bind.getRoot(), e.getMessage(), BaseTransientBottomBar.LENGTH_LONG).show();
                        updateUserProfile(null);
                        bind.proSignUp.setVisibility(View.GONE);
                    });
                }else{
                    bind.editUPassword.setError("Password must be provided!");
                    bind.editUPassword.requestFocus();
                    updateUserProfile(null);
                    bind.proSignUp.setVisibility(View.GONE);
                }
            }else{
                bind.editUEmail.setError("Email must be provided!");
                bind.editUEmail.requestFocus();
                updateUserProfile(null);
                bind.proSignUp.setVisibility(View.GONE);
            }

        });

    }
    private void updateUserProfile(SignUpModel signUpModel){
        if(signUpModel!=null){
            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser()
            .getUid()).setValue(signUpModel).addOnSuccessListener(unused -> {
               ShowSuccess("User Signup Successfully");
            }).addOnFailureListener(e -> {
                showError(e.getMessage());
                bind.proSignUp.setVisibility(View.GONE);
            });
        }
    }

    private void showError(String message) {
        new AlertDialog.Builder(getActivity()).setTitle("Error").setMessage(message).setPositiveButton("OK",
                (dialog, which) -> {
                }).show();
    }

    private void ShowSuccess(String message) {
        new AlertDialog.Builder(getActivity()).setTitle("Success").setMessage(message).setPositiveButton("Continue",
                ((dialog, which) -> {
                    NavHostFragment.findNavController(this).navigate(R.id.action_signUp2_to_productsFragment);
        })).show();
    }
}