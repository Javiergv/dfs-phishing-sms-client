package com.moez.QKSMS.ui.welcome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moez.QKSMS.R;
import com.moez.QKSMS.ui.view.QKTextView;

public class WelcomeAgreementFragment extends BaseWelcomeFragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome_agreement, container, false);


        return view;
    }

    @Override
    public void onClick(View v) {
    }
}
