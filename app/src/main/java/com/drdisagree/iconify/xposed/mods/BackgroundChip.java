package com.drdisagree.iconify.xposed.mods;

import static com.drdisagree.iconify.common.References.HEADER_CLOCK_SWITCH;
import static com.drdisagree.iconify.common.References.QSPANEL_CLOCKBG;
import static com.drdisagree.iconify.common.References.QSPANEL_STATUSICONSBG;
import static com.drdisagree.iconify.common.References.STATUSBAR_CLOCKBG;
import static com.drdisagree.iconify.common.References.SYSTEM_UI_PACKAGE;
import static com.drdisagree.iconify.config.XPrefs.Xprefs;
import static de.robv.android.xposed.XposedBridge.hookAllConstructors;
import static de.robv.android.xposed.XposedBridge.hookAllMethods;
import static de.robv.android.xposed.XposedBridge.log;
import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.getObjectField;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.drdisagree.iconify.xposed.ModPack;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class BackgroundChip extends ModPack implements IXposedHookLoadPackage {

    private static final String TAG = "Iconify - BackgroundChip: ";
    private static final String CLASS_CLOCK = SYSTEM_UI_PACKAGE + ".statusbar.policy.Clock";
    private static final String CollapsedStatusBarFragmentClass = SYSTEM_UI_PACKAGE + ".statusbar.phone.fragment.CollapsedStatusBarFragment";
    boolean mShowSBClockBg = false;
    boolean mShowQSClockBg = false;
    boolean mShowQSStatusIcongBg = false;
    private Object mCollapsedStatusBarFragment = null;
    private ViewGroup mStatusBar = null;
    private View mClockView = null;
    private View mCenterClockView = null;
    private View mRightClockView = null;
    boolean showHeaderClock = false;
    int clockWidth = -1;
    int clockHeight = 1;
    private String rootPackagePath = "";

    public BackgroundChip(Context context) {
        super(context);
        if (!listensTo(context.getPackageName())) return;
    }

    @Override
    public void updatePrefs(String... Key) {
        if (Xprefs == null) return;

        mShowSBClockBg = Xprefs.getBoolean(STATUSBAR_CLOCKBG, false);
        mShowQSClockBg = Xprefs.getBoolean(QSPANEL_CLOCKBG, true);
        mShowQSStatusIcongBg = Xprefs.getBoolean(QSPANEL_STATUSICONSBG, true);
        showHeaderClock = Xprefs.getBoolean(HEADER_CLOCK_SWITCH, false);

        updateStatusBarClock();
    }

    @Override
    public boolean listensTo(String packageName) {
        return packageName.equals(SYSTEM_UI_PACKAGE);
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (!lpparam.packageName.equals(SYSTEM_UI_PACKAGE))
            return;

        rootPackagePath = lpparam.appInfo.sourceDir;

        final Class<?> Clock = findClass(CLASS_CLOCK, lpparam.classLoader);
        final Class<?> CollapsedStatusBarFragment = findClass(CollapsedStatusBarFragmentClass, lpparam.classLoader);

        hookAllConstructors(CollapsedStatusBarFragment, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) {
                mCollapsedStatusBarFragment = param.thisObject;
            }
        });

        findAndHookMethod(CollapsedStatusBarFragment, "onViewCreated", View.class, Bundle.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) {
                try {
                    mClockView = (View) getObjectField(param.thisObject, "mClockView");
                } catch (Throwable t) {
                    try {
                        Object mClockController = getObjectField(param.thisObject, "mClockController");
                        mClockView = (View) callMethod(mClockController, "getClock");
                    } catch (Throwable th) {
                        try {
                            mClockView = (View) getObjectField(param.thisObject, "mLeftClock");
                        } catch (Throwable thr) {
                            mClockView = null;
                        }
                    }
                }
                try {
                    mCenterClockView = (View) getObjectField(param.thisObject, "mCenterClockView");
                } catch (Throwable t) {
                    try {
                        Object mClockController = getObjectField(param.thisObject, "mClockController");
                        mCenterClockView = (View) callMethod(mClockController, "mCenterClockView");
                    } catch (Throwable th) {
                        try {
                            mCenterClockView = (View) getObjectField(param.thisObject, "mCenterClock");
                        } catch (Throwable thr) {
                            mCenterClockView = null;
                        }
                    }
                }
                try {
                    mRightClockView = (View) getObjectField(param.thisObject, "mRightClockView");
                } catch (Throwable t) {
                    try {
                        Object mClockController = getObjectField(param.thisObject, "mClockController");
                        mRightClockView = (View) callMethod(mClockController, "mRightClockView");
                    } catch (Throwable th) {
                        try {
                            mRightClockView = (View) getObjectField(param.thisObject, "mRightClock");
                        } catch (Throwable thr) {
                            mRightClockView = null;
                        }
                    }
                }

                mStatusBar = (ViewGroup) getObjectField(mCollapsedStatusBarFragment, "mStatusBar");

                mStatusBar.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
                    updateStatusBarClock();
                });
            }
        });

        hookAllMethods(Clock, "updateClock", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) {
                try {
                    TextView clock = (TextView) param.thisObject;
                    clockWidth = clock.getWidth();
                    clockHeight = clock.getHeight();
                } catch (Throwable t) {
                    log(TAG + t);
                }
            }
        });

        updateStatusBarClock();
    }

    private void updateStatusBarClock() {
        float corner = (Xprefs.getInt("cornerRadius", 16) + 8) * mContext.getResources().getDisplayMetrics().density;
        GradientDrawable mDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{
                        mContext.getResources().getColor(android.R.color.holo_blue_light),
                        mContext.getResources().getColor(android.R.color.holo_green_light)
                });
        mDrawable.setCornerRadius(corner);

        if (mShowSBClockBg && clockWidth != -1 && clockHeight != -1) {
            if (mClockView != null) {
                mClockView.setPadding(14, 2, 14, 2);
                mClockView.setBackground(mDrawable);
            }

            if (mCenterClockView != null) {
                mCenterClockView.setPadding(14, 2, 14, 2);
                mCenterClockView.setBackground(mDrawable);
            }

            if (mRightClockView != null) {
                mRightClockView.setPadding(14, 2, 14, 2);
                mRightClockView.setBackground(mDrawable);
            }
        } else {
            @SuppressLint("DiscouragedApi") int clockPaddingStart = mContext.getResources().getDimensionPixelSize(mContext.getResources().getIdentifier("status_bar_clock_starting_padding", "dimen", mContext.getPackageName()));
            @SuppressLint("DiscouragedApi") int clockPaddingEnd = mContext.getResources().getDimensionPixelSize(mContext.getResources().getIdentifier("status_bar_clock_end_padding", "dimen", mContext.getPackageName()));
            @SuppressLint("DiscouragedApi") int leftClockPaddingStart = mContext.getResources().getDimensionPixelSize(mContext.getResources().getIdentifier("status_bar_left_clock_starting_padding", "dimen", mContext.getPackageName()));
            @SuppressLint("DiscouragedApi") int leftClockPaddingEnd = mContext.getResources().getDimensionPixelSize(mContext.getResources().getIdentifier("status_bar_left_clock_end_padding", "dimen", mContext.getPackageName()));

            if (mClockView != null) {
                mClockView.setBackgroundResource(0);
                mClockView.setPaddingRelative(leftClockPaddingStart, 0, leftClockPaddingEnd, 0);
            }

            if (mCenterClockView != null) {
                mCenterClockView.setBackgroundResource(0);
                mCenterClockView.setPaddingRelative(0, 0, 0, 0);
            }

            if (mRightClockView != null) {
                mRightClockView.setBackgroundResource(0);
                mRightClockView.setPaddingRelative(clockPaddingStart, 0, clockPaddingEnd, 0);
            }
        }
    }
}