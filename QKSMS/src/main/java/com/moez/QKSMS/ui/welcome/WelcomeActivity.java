package com.moez.QKSMS.ui.welcome;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import com.moez.QKSMS.R;
import com.moez.QKSMS.common.LiveViewManager;
import com.moez.QKSMS.enums.QKPreference;
import com.moez.QKSMS.ui.ThemeManager;
import com.moez.QKSMS.ui.base.QKActivity;
import com.moez.QKSMS.ui.settings.SettingsFragment;
import com.moez.QKSMS.ui.view.RobotoTextView;


public class WelcomeActivity extends QKActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    public static final int WELCOME_REQUEST_CODE = 31415;

    private ViewPager mPager;
    private ImageView mPrevious;
    private ImageView mNext;
    private ImageView[] mIndicators;
    private View mBackground;
    private RobotoTextView mSkip;
    private boolean mFinished;
    private boolean mContinue;
    private int checkCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        checkCount = 0;

        getSupportActionBar().hide();

        mBackground = findViewById(R.id.welcome);
        mBackground.setBackgroundColor(ThemeManager.getColor());

        mPrevious = (ImageView) findViewById(R.id.welcome_previous);
        mPrevious.setOnClickListener(this);

        mNext = (ImageView) findViewById(R.id.welcome_next);
        mNext.setOnClickListener(this);

        mSkip = (RobotoTextView) findViewById(R.id.welcome_skip);
        mSkip.setOnClickListener(this);

        mIndicators = new ImageView[]{
                (ImageView) findViewById(R.id.welcome_indicator_0),
                (ImageView) findViewById(R.id.welcome_indicator_1),
                (ImageView) findViewById(R.id.welcome_indicator_2),
                (ImageView) findViewById(R.id.welcome_indicator_3),
                (ImageView) findViewById(R.id.welcome_indicator_4),
                (ImageView) findViewById(R.id.welcome_indicator_5),
                (ImageView) findViewById(R.id.welcome_indicator_6)};
        tintIndicators(0xFFFFFFFF);

        mPager = (ViewPager) findViewById(R.id.welcome_pager);
        BaseWelcomeFragment.setPager(mPager);
        BaseWelcomeFragment.setContext(this);
        mPager.setOnPageChangeListener(this);
        mPager.setAdapter(new WelcomePagerAdapter(getFragmentManager()));

        LiveViewManager.registerView(QKPreference.THEME, this, key -> {
            mBackground.setBackgroundColor(ThemeManager.getColor());
        });
    }

    public void setColorBackground(int color) {
        mBackground.setBackgroundColor(color);
    }

    public void tintIndicators(int color) {
        if (mIndicators != null) {
            for (ImageView indicator : mIndicators) {
                indicator.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            }
        }

        if (mSkip != null) {
            mSkip.setTextColor(color);
        }

        if (mPrevious != null) {
            mPrevious.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }

        if (mNext != null) {
            mNext.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }
    }

    public void setSkip() {
        if (mSkip != null) {
            mSkip.setText(R.string.welcome_skip);
            mSkip.setVisibility(View.VISIBLE);
        }
    }
    public void setFinished() {
     /*   if (mSkip != null) {
            mFinished = true;
            mSkip.setText(R.string.welcome_finish);
            mSkip.setVisibility(View.VISIBLE);
        } */
    }

    public void setContinue() {
       /* if (mSkip != null) {
            mContinue = true;
            mSkip.setText(R.string.welcome_continue);
            mSkip.setVisibility(View.VISIBLE);
        } */
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Fragment fragment = ((WelcomePagerAdapter) mPager.getAdapter()).getItem(position);
        if (fragment instanceof BaseWelcomeFragment.WelcomeScrollListener) {
            ((BaseWelcomeFragment.WelcomeScrollListener) fragment).onScrollOffsetChanged(this, positionOffset);
        }

        if (position + 1 < mPager.getAdapter().getCount()) {
            Fragment fragment2 = ((WelcomePagerAdapter) mPager.getAdapter()).getItem(position + 1);
            if (fragment2 instanceof BaseWelcomeFragment.WelcomeScrollListener) {
                ((BaseWelcomeFragment.WelcomeScrollListener) fragment2).onScrollOffsetChanged(this, 1 - positionOffset);
            }
        }
    }

    @Override
    public void onPageSelected(int i) {
        if (mIndicators != null) {
            for (ImageView indicator : mIndicators) {
                indicator.setAlpha(0.56f);
            }

            mIndicators[i].setAlpha(1.00f);
        }
        mFinished = false;
        if (i < 3 ) {
            mSkip.setText(R.string.welcome_skip);
            mContinue = false;
        }
        //if (mSkip != null) {
        //    mSkip.setVisibility(i == 0 || mFinished ? View.VISIBLE : View.INVISIBLE);
        //}
        if (i == 3) {
            mSkip.setText(R.string.welcome_continue);
            mContinue = true;
        } else if(i == 6) {
            mSkip.setText(R.string.welcome_finish);
            mSkip.setVisibility(View.GONE);
            mFinished = true;
        }


        if (mPrevious != null) {
            mPrevious.setEnabled(i > 0);
            mPrevious.setAlpha(i > 0 ? 1f : 0.6f);
        }

        if (mNext != null) {
            mNext.setEnabled(i + 1 < mPager.getAdapter().getCount());
            mNext.setAlpha(i + 1 < mPager.getAdapter().getCount() ? 1f : 0.6f);
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.welcome_skip:
                if (mFinished) {
                    setResult(RESULT_OK, null);
                    finish();
                } else if (mContinue) {
                    mPager.setCurrentItem(mPager.getCurrentItem() + 1);
                } else {
                    mPager.setCurrentItem(3);
                }
                break;
            case R.id.welcome_previous:
                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
                break;
            case R.id.welcome_next:
                mPager.setCurrentItem(mPager.getCurrentItem() + 1);
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(this).edit();
        prefs.putBoolean(SettingsFragment.WELCOME_SEEN, true);
        prefs.apply();
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkbox_1:
                if (checked) {
                    incrementCheck();
                } else {
                    decrementCheck();
                }
                break;
            case R.id.checkbox_2:
                if (checked) {
                    incrementCheck();
                } else {
                    decrementCheck();
                }
                break;
            case R.id.checkbox_3:
                if (checked) {
                    incrementCheck();
                } else {
                    decrementCheck();

                }
                break;
            case R.id.checkbox_4:
                if (checked) {
                    incrementCheck();
                } else {
                    decrementCheck();
                }
                break;
            // TODO: Veggie sandwich
        }
    }

    private void incrementCheck() {
        checkCount++;
        if (checkCount ==  4) {
            mSkip.setVisibility(View.VISIBLE);
        }
    }

    private void decrementCheck() {
        checkCount--;
        mSkip.setVisibility(View.INVISIBLE);
    }
}
