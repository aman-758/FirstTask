package com.sortstring.firsttask;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.sortstring.firsttask.databinding.FragmentProductsBinding;


public class ProductsFragment extends Fragment {
    private FragmentProductsBinding bind;
    private FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        auth = FirebaseAuth.getInstance();
        return inflater.inflate(R.layout.fragment_products, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind = FragmentProductsBinding.bind(view);
        bind.btnLogout.setOnClickListener(v -> {
            if(auth.getCurrentUser()!=null){
                auth.signOut();
                NavHostFragment.findNavController(this).navigateUp();
            }
        });
    }
}