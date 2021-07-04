package com.gas.swiftel.Fragments;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gas.swiftel.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddAccessFragment extends Fragment {


    public AddAccessFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_access, container, false);



        return root;
    }

}
