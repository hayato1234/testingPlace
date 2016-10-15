package com.example.hayatomoritani.weatherapp.tabDemo2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hayatomoritani.weatherapp.R;

/**
 * Created by hayatomoritani on 10/12/16.
 */

public class FragmentPage extends Fragment {

    public static FragmentPage newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt("page", page);
        FragmentPage fragment = new FragmentPage();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int page = getArguments().getInt("page", 0);
        View view = inflater.inflate(R.layout.page_layout, container, false);
        ((TextView) view.findViewById(R.id.page_text)).setText("Page " + page);
        return view;
    }
}
