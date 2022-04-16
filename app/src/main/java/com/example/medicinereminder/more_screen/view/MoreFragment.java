package com.example.medicinereminder.more_screen.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.medicinereminder.HomeScreen.HomeFragment;
import com.example.medicinereminder.R;
import com.example.medicinereminder.patient_screen.view.PatientActivity;
import com.example.medicinereminder.tracker_screen.view.TrackerActivity;
import com.example.medicinereminder.tracker_screen.view.TrackerAdapter;


public class MoreFragment extends Fragment {

    TextView nameTextView;
    TextView emailTextView;
    ImageView imageView;
    Button profileBtn;
    Button patientsBtn;
    Button trackersBtn;

    FragmentTransaction fragmentTransaction;


    public MoreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_more, container, false);

        nameTextView = view.findViewById(R.id.moreName);
        imageView = view.findViewById(R.id.moreImageProfile);
        emailTextView = view.findViewById(R.id.moreEmail);

        profileBtn = view.findViewById(R.id.moreMyProfileButton);
        patientsBtn = view.findViewById(R.id.morePatientButton);
        trackersBtn = view.findViewById(R.id.moreTrackerButton);

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.home_fragmentcontainer,new HomeFragment());
//                fragmentTransaction.commit();
            }
        });
        patientsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), PatientActivity.class));
            }
        });
        trackersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), TrackerActivity.class));
            }
        });

        return view;
    }
}