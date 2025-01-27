package com.drdisagree.iconify.xposed.mods;

/* Modified from AOSPMods
 * https://github.com/siavash79/AOSPMods/blob/canary/app/src/main/java/sh/siava/AOSPMods/systemui/BatteryStyleManager.java
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see [http://www.gnu.org/licenses/].
 */

import static com.drdisagree.iconify.common.Const.SYSTEMUI_PACKAGE;
import static com.drdisagree.iconify.common.Preferences.BATTERY_STYLE_CUSTOM_LANDSCAPE;
import static com.drdisagree.iconify.common.Preferences.BATTERY_STYLE_CUSTOM_RLANDSCAPE;
import static com.drdisagree.iconify.common.Preferences.BATTERY_STYLE_DEFAULT;
import static com.drdisagree.iconify.common.Preferences.BATTERY_STYLE_DEFAULT_LANDSCAPE;
import static com.drdisagree.iconify.common.Preferences.BATTERY_STYLE_DEFAULT_RLANDSCAPE;
import static com.drdisagree.iconify.common.Preferences.BATTERY_STYLE_LANDSCAPE_BATTERYA;
import static com.drdisagree.iconify.common.Preferences.BATTERY_STYLE_LANDSCAPE_BATTERYB;
import static com.drdisagree.iconify.common.Preferences.BATTERY_STYLE_LANDSCAPE_BATTERYC;
import static com.drdisagree.iconify.common.Preferences.BATTERY_STYLE_LANDSCAPE_BATTERYD;
import static com.drdisagree.iconify.common.Preferences.BATTERY_STYLE_LANDSCAPE_BATTERYE;
import static com.drdisagree.iconify.common.Preferences.BATTERY_STYLE_LANDSCAPE_BATTERYF;
import static com.drdisagree.iconify.common.Preferences.BATTERY_STYLE_LANDSCAPE_BATTERYG;
import static com.drdisagree.iconify.common.Preferences.BATTERY_STYLE_LANDSCAPE_BATTERYH;
import static com.drdisagree.iconify.common.Preferences.BATTERY_STYLE_LANDSCAPE_BATTERYI;
import static com.drdisagree.iconify.common.Preferences.BATTERY_STYLE_LANDSCAPE_BATTERYJ;
import static com.drdisagree.iconify.common.Preferences.BATTERY_STYLE_LANDSCAPE_BATTERYK;
import static com.drdisagree.iconify.common.Preferences.BATTERY_STYLE_LANDSCAPE_BATTERYL;
import static com.drdisagree.iconify.common.Preferences.BATTERY_STYLE_LANDSCAPE_BATTERYM;
import static com.drdisagree.iconify.common.Preferences.BATTERY_STYLE_LANDSCAPE_BATTERYN;
import static com.drdisagree.iconify.common.Preferences.BATTERY_STYLE_LANDSCAPE_BATTERYO;
import static com.drdisagree.iconify.common.Preferences.BATTERY_STYLE_LANDSCAPE_COLOROS;
import static com.drdisagree.iconify.common.Preferences.BATTERY_STYLE_LANDSCAPE_IOS_15;
import static com.drdisagree.iconify.common.Preferences.BATTERY_STYLE_LANDSCAPE_IOS_16;
import static com.drdisagree.iconify.common.Preferences.BATTERY_STYLE_LANDSCAPE_MIUI_PILL;
import static com.drdisagree.iconify.common.Preferences.BATTERY_STYLE_LANDSCAPE_SMILEY;
import static com.drdisagree.iconify.common.Preferences.BATTERY_STYLE_LANDSCAPE_STYLE_A;
import static com.drdisagree.iconify.common.Preferences.BATTERY_STYLE_LANDSCAPE_STYLE_B;
import static com.drdisagree.iconify.common.Preferences.BATTERY_STYLE_PORTRAIT_AIROO;
import static com.drdisagree.iconify.common.Preferences.BATTERY_STYLE_PORTRAIT_CAPSULE;
import static com.drdisagree.iconify.common.Preferences.BATTERY_STYLE_PORTRAIT_LORN;
import static com.drdisagree.iconify.common.Preferences.BATTERY_STYLE_PORTRAIT_MX;
import static com.drdisagree.iconify.common.Preferences.BATTERY_STYLE_PORTRAIT_ORIGAMI;
import static com.drdisagree.iconify.common.Preferences.BATTERY_STYLE_RLANDSCAPE_COLOROS;
import static com.drdisagree.iconify.common.Preferences.BATTERY_STYLE_RLANDSCAPE_STYLE_A;
import static com.drdisagree.iconify.common.Preferences.BATTERY_STYLE_RLANDSCAPE_STYLE_B;
import static com.drdisagree.iconify.common.Preferences.CUSTOM_BATTERY_HEIGHT;
import static com.drdisagree.iconify.common.Preferences.CUSTOM_BATTERY_MARGIN;
import static com.drdisagree.iconify.common.Preferences.CUSTOM_BATTERY_STYLE;
import static com.drdisagree.iconify.common.Preferences.CUSTOM_BATTERY_WIDTH;
import static com.drdisagree.iconify.config.XPrefs.Xprefs;
import static com.drdisagree.iconify.xposed.HookRes.resparams;
import static de.robv.android.xposed.XposedBridge.hookAllConstructors;
import static de.robv.android.xposed.XposedBridge.hookAllMethods;
import static de.robv.android.xposed.XposedBridge.log;
import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.findAndHookConstructor;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.findClassIfExists;
import static de.robv.android.xposed.XposedHelpers.getAdditionalInstanceField;
import static de.robv.android.xposed.XposedHelpers.getBooleanField;
import static de.robv.android.xposed.XposedHelpers.getIntField;
import static de.robv.android.xposed.XposedHelpers.getObjectField;
import static de.robv.android.xposed.XposedHelpers.setAdditionalInstanceField;
import static de.robv.android.xposed.XposedHelpers.setObjectField;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.XResources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.drdisagree.iconify.xposed.ModPack;
import com.drdisagree.iconify.xposed.mods.batterystyles.BatteryDrawable;
import com.drdisagree.iconify.xposed.mods.batterystyles.LandscapeBattery;
import com.drdisagree.iconify.xposed.mods.batterystyles.LandscapeBatteryA;
import com.drdisagree.iconify.xposed.mods.batterystyles.LandscapeBatteryB;
import com.drdisagree.iconify.xposed.mods.batterystyles.LandscapeBatteryC;
import com.drdisagree.iconify.xposed.mods.batterystyles.LandscapeBatteryColorOS;
import com.drdisagree.iconify.xposed.mods.batterystyles.LandscapeBatteryD;
import com.drdisagree.iconify.xposed.mods.batterystyles.LandscapeBatteryE;
import com.drdisagree.iconify.xposed.mods.batterystyles.LandscapeBatteryF;
import com.drdisagree.iconify.xposed.mods.batterystyles.LandscapeBatteryG;
import com.drdisagree.iconify.xposed.mods.batterystyles.LandscapeBatteryH;
import com.drdisagree.iconify.xposed.mods.batterystyles.LandscapeBatteryI;
import com.drdisagree.iconify.xposed.mods.batterystyles.LandscapeBatteryJ;
import com.drdisagree.iconify.xposed.mods.batterystyles.LandscapeBatteryK;
import com.drdisagree.iconify.xposed.mods.batterystyles.LandscapeBatteryL;
import com.drdisagree.iconify.xposed.mods.batterystyles.LandscapeBatteryM;
import com.drdisagree.iconify.xposed.mods.batterystyles.LandscapeBatteryMIUIPill;
import com.drdisagree.iconify.xposed.mods.batterystyles.LandscapeBatteryN;
import com.drdisagree.iconify.xposed.mods.batterystyles.LandscapeBatteryO;
import com.drdisagree.iconify.xposed.mods.batterystyles.LandscapeBatterySmiley;
import com.drdisagree.iconify.xposed.mods.batterystyles.LandscapeBatteryStyleA;
import com.drdisagree.iconify.xposed.mods.batterystyles.LandscapeBatteryStyleB;
import com.drdisagree.iconify.xposed.mods.batterystyles.LandscapeBatteryiOS15;
import com.drdisagree.iconify.xposed.mods.batterystyles.LandscapeBatteryiOS16;
import com.drdisagree.iconify.xposed.mods.batterystyles.PortraitBatteryAiroo;
import com.drdisagree.iconify.xposed.mods.batterystyles.PortraitBatteryCapsule;
import com.drdisagree.iconify.xposed.mods.batterystyles.PortraitBatteryLorn;
import com.drdisagree.iconify.xposed.mods.batterystyles.PortraitBatteryMx;
import com.drdisagree.iconify.xposed.mods.batterystyles.PortraitBatteryOrigami;
import com.drdisagree.iconify.xposed.mods.batterystyles.RLandscapeBattery;
import com.drdisagree.iconify.xposed.mods.batterystyles.RLandscapeBatteryColorOS;
import com.drdisagree.iconify.xposed.mods.batterystyles.RLandscapeBatteryStyleA;
import com.drdisagree.iconify.xposed.mods.batterystyles.RLandscapeBatteryStyleB;
import com.drdisagree.iconify.xposed.utils.SettingsLibUtils;

import java.util.ArrayList;
import java.util.Objects;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class BatteryStyleManager extends ModPack {

    private static final String TAG = "Iconify - " + BatteryStyleManager.class.getSimpleName() + ": ";
    private static final ArrayList<Object> batteryViews = new ArrayList<>();
    private static final int BatteryIconOpacity = 100;
    public static int BatteryStyle = 0;
    public static boolean showPercentInside = false;
    public static int scaleFactor = 100;
    public static int batteryRotation = 0;
    private static boolean customBatteryEnabled = false;
    private static int customBatteryWidth = 20;
    private static int customBatteryHeight = 20;
    private int frameColor;
    private Object BatteryController = null;
    private int customBatteryMargin = 6;
    private int textColorPrimary = Color.TRANSPARENT;

    public BatteryStyleManager(Context context) {
        super(context);
    }

    private static void refreshBatteryIcons() {
        try {
            for (Object view : batteryViews) {
                ImageView mBatteryIconView = (ImageView) getObjectField(view, "mBatteryIconView");
                if (mBatteryIconView != null) {
                    mBatteryIconView.setRotation(batteryRotation);
                }

                if (customBatteryEnabled) {
                    scale(mBatteryIconView);
                    try {
                        BatteryDrawable drawable = (BatteryDrawable) getAdditionalInstanceField(view, "mBatteryDrawable");
                        drawable.setShowPercentEnabled(showPercentInside);
                        drawable.setAlpha(Math.round(BatteryIconOpacity * 2.55f));
                        drawable.invalidateSelf();
                    } catch (Throwable ignored) {
                    }
                }
            }
        } catch (Throwable throwable) {
            log(TAG + throwable);
        }
    }

    public static void scale(Object thisObject) {
        ImageView mBatteryIconView = (ImageView) getObjectField(thisObject, "mBatteryIconView");
        scale(mBatteryIconView);
    }

    @SuppressLint("DiscouragedApi")
    public static void scale(ImageView mBatteryIconView) {
        if (mBatteryIconView == null) {
            return;
        }

        try {
            Context context = mBatteryIconView.getContext();
            Resources res = context.getResources();

            TypedValue typedValue = new TypedValue();

            res.getValue(res.getIdentifier("status_bar_icon_scale_factor", "dimen", context.getPackageName()), typedValue, true);
            float iconScaleFactor = typedValue.getFloat() * (scaleFactor / 100f);

            int batteryHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, customBatteryHeight, mBatteryIconView.getContext().getResources().getDisplayMetrics());
            int batteryWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, customBatteryWidth, mBatteryIconView.getContext().getResources().getDisplayMetrics());

            ViewGroup.LayoutParams scaledLayoutParams = mBatteryIconView.getLayoutParams();
            scaledLayoutParams.height = (int) (batteryHeight * iconScaleFactor);
            scaledLayoutParams.width = (int) (batteryWidth * iconScaleFactor);

            mBatteryIconView.setLayoutParams(scaledLayoutParams);
        } catch (Throwable throwable) {
            log(TAG + throwable);
        }
    }

    public void updatePrefs(String... Key) {
        if (Xprefs == null) return;

        int batteryStyle = Xprefs.getInt(CUSTOM_BATTERY_STYLE, 0);
        customBatteryEnabled = batteryStyle != BATTERY_STYLE_DEFAULT
                && batteryStyle != BATTERY_STYLE_DEFAULT_LANDSCAPE
                && batteryStyle != BATTERY_STYLE_DEFAULT_RLANDSCAPE;
        customBatteryWidth = Xprefs.getInt(CUSTOM_BATTERY_WIDTH, 20);
        customBatteryHeight = Xprefs.getInt(CUSTOM_BATTERY_HEIGHT, 20);
        customBatteryMargin = Xprefs.getInt(CUSTOM_BATTERY_MARGIN, 6);

        if (batteryStyle == BATTERY_STYLE_DEFAULT_RLANDSCAPE) {
            batteryRotation = 90;
        } else if (batteryStyle == BATTERY_STYLE_DEFAULT_LANDSCAPE) {
            batteryRotation = 270;
        } else {
            batteryRotation = 0;
        }

        showPercentInside = batteryStyle == BATTERY_STYLE_LANDSCAPE_IOS_16 ||
                batteryStyle == BATTERY_STYLE_LANDSCAPE_BATTERYM;

        if (BatteryStyle != batteryStyle) {
            BatteryStyle = batteryStyle;
            try {
                for (Object view : batteryViews) {
                    ImageView mBatteryIconView = (ImageView) getObjectField(view, "mBatteryIconView");
                    if (mBatteryIconView != null) {
                        mBatteryIconView.setRotation(batteryRotation);
                    }

                    boolean mCharging = (boolean) getObjectField(view, "mCharging");
                    int mLevel = (int) getObjectField(view, "mLevel");

                    if (customBatteryEnabled) {
                        BatteryDrawable newDrawable = getNewDrawable(mContext);
                        if (newDrawable != null) {
                            if (mBatteryIconView != null) {
                                mBatteryIconView.setImageDrawable(newDrawable);
                            }
                            setAdditionalInstanceField(view, "mBatteryDrawable", newDrawable);
                            newDrawable.setBatteryLevel(mLevel);
                            newDrawable.setChargingEnabled(mCharging);
                        }
                    }
                }
            } catch (Throwable throwable) {
                log(TAG + throwable);
            }
        }

        refreshBatteryIcons();

        if (Key.length > 0 && (Objects.equals(Key[0], CUSTOM_BATTERY_WIDTH) || Objects.equals(Key[0], CUSTOM_BATTERY_HEIGHT) || Objects.equals(Key[0], CUSTOM_BATTERY_MARGIN))) {
            setCustomBatteryDimens();
        }
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        Class<?> BatteryControllerImplClass = findClass(SYSTEMUI_PACKAGE + ".statusbar.policy.BatteryControllerImpl", lpparam.classLoader);
        Class<?> BatteryMeterViewClass = findClassIfExists(SYSTEMUI_PACKAGE + ".battery.BatteryMeterView", lpparam.classLoader);
        if (BatteryMeterViewClass == null) {
            BatteryMeterViewClass = findClass(SYSTEMUI_PACKAGE + ".BatteryMeterView", lpparam.classLoader);
        }

        try {
            findAndHookConstructor("com.android.settingslib.graph.ThemedBatteryDrawable", lpparam.classLoader, Context.class, int.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) {
                    frameColor = (int) param.args[1];
                }
            });
        } catch (Throwable throwable) {
            log(TAG + throwable);
        }

        try {
            hookAllConstructors(BatteryControllerImplClass, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) {
                    BatteryController = param.thisObject;
                }
            });
        } catch (Throwable throwable) {
            log(TAG + throwable);
        }

        try {
            hookAllMethods(BatteryControllerImplClass, "fireBatteryUnknownStateChanged", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) {
                    if (!customBatteryEnabled) return;

                    for (Object view : batteryViews) {
                        BatteryDrawable mBatteryDrawable = (BatteryDrawable) getAdditionalInstanceField(view, "mBatteryDrawable");
                        callMethod(view, "setImageDrawable", mBatteryDrawable);
                    }
                }
            });
        } catch (Throwable throwable) {
            log(TAG + throwable);
        }

        try {
            XC_MethodHook batteryDataRefreshHook = new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) {
                    int level = getIntField(param.thisObject, "mLevel");
                    boolean charging = getBooleanField(param.thisObject, "mPluggedIn")
                            || getBooleanField(param.thisObject, "mCharging")
                            || getBooleanField(param.thisObject, "mWirelessCharging");
                    boolean powerSave = getBooleanField(param.thisObject, "mPowerSave");

                    if (!customBatteryEnabled) return;

                    try {
                        for (Object view : batteryViews) {
                            ((View) view).post(() -> {
                                BatteryDrawable drawable = (BatteryDrawable) getAdditionalInstanceField(view, "mBatteryDrawable");
                                if (drawable != null) {
                                    drawable.setBatteryLevel(level);
                                    drawable.setChargingEnabled(charging);
                                    drawable.setPowerSavingEnabled(powerSave);
                                }
                                scale(view);
                            });
                        }
                    } catch (Throwable ignored) {
                    }
                }
            };

            hookAllMethods(BatteryControllerImplClass, "fireBatteryLevelChanged", batteryDataRefreshHook);
            hookAllMethods(BatteryControllerImplClass, "firePowerSaveChanged", batteryDataRefreshHook);
        } catch (Throwable throwable) {
            log(TAG + throwable);
        }

        try {
            View.OnAttachStateChangeListener listener = new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(@NonNull View v) {
                    batteryViews.add(v);
                    new Thread(() -> {
                        try {
                            Thread.sleep(500);
                            if (BatteryController != null) {
                                callMethod(BatteryController, "fireBatteryLevelChanged");
                            }
                        } catch (Throwable ignored) {
                        }
                    }).start();
                }

                @Override
                public void onViewDetachedFromWindow(@NonNull View v) {
                    batteryViews.remove(v);
                }
            };

            hookAllConstructors(BatteryMeterViewClass, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) {
                    ((View) param.thisObject).addOnAttachStateChangeListener(listener);

                    ImageView mBatteryIconView = initBatteryIfNull(param, (ImageView) getObjectField(param.thisObject, "mBatteryIconView"));

                    if (customBatteryEnabled || BatteryStyle == BATTERY_STYLE_DEFAULT_LANDSCAPE || BatteryStyle == BATTERY_STYLE_DEFAULT_RLANDSCAPE) {
                        mBatteryIconView.setRotation(batteryRotation);
                    }

                    if (!customBatteryEnabled) return;

                    BatteryDrawable mBatteryDrawable = getNewDrawable(mContext);

                    if (mBatteryDrawable != null) {
                        setAdditionalInstanceField(param.thisObject, "mBatteryDrawable", mBatteryDrawable);
                        mBatteryIconView.setImageDrawable(mBatteryDrawable);
                        setObjectField(param.thisObject, "mBatteryIconView", mBatteryIconView);
                    }

                    if (BatteryController != null) {
                        callMethod(BatteryController, "fireBatteryLevelChanged");
                    }

                    hidePercentage(param);
                }
            });
        } catch (Throwable throwable) {
            log(TAG + throwable);
        }

        try {
            findAndHookMethod(BatteryMeterViewClass, "updateColors", int.class, int.class, int.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) {
                    if (!customBatteryEnabled) return;

                    BatteryDrawable mBatteryDrawable = (BatteryDrawable) getAdditionalInstanceField(param.thisObject, "mBatteryDrawable");
                    if (mBatteryDrawable != null) {
                        mBatteryDrawable.setColors((int) param.args[0], (int) param.args[1], (int) param.args[2]);
                    }

                    hidePercentage(param);
                }
            });
        } catch (Throwable throwable) {
            log(TAG + throwable);
        }

        try {
            Class<?> ShadeHeaderControllerClass = findClassIfExists(SYSTEMUI_PACKAGE + ".shade.ShadeHeaderController", lpparam.classLoader);
            if (ShadeHeaderControllerClass == null)
                ShadeHeaderControllerClass = findClass(SYSTEMUI_PACKAGE + ".shade.LargeScreenShadeHeaderController", lpparam.classLoader);

            hookAllMethods(ShadeHeaderControllerClass, "updateResources", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) {
                    if (!customBatteryEnabled) return;

                    updateBatteryResources(mContext, param);
                    hidePercentage(param);
                }
            });

            hookAllMethods(ShadeHeaderControllerClass, "onConfigurationChanged", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) {
                    if (!customBatteryEnabled) return;

                    updateBatteryResources(mContext, param);
                    hidePercentage(param);
                }
            });
        } catch (Throwable ignored) {
        }

        try {
            hookAllMethods(BatteryMeterViewClass, "setPercentShowMode", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) {
                    if (showPercentInside) {
                        param.setResult(2);
                    }
                }
            });

            hookAllMethods(BatteryMeterViewClass, "updateShowPercent", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) {
                    hidePercentage(param);
                }
            });
        } catch (Throwable throwable) {
            log(TAG + throwable);
        }

        removeBatteryMeterViewMethods(BatteryMeterViewClass);
        setCustomBatteryDimens();
    }

    private void updateBatteryResources(Context context, XC_MethodHook.MethodHookParam param) {
        try {
            int textColor = SettingsLibUtils.getColorAttrDefaultColor(context, android.R.attr.textColorPrimary);
            LinearLayout batteryIcon = (LinearLayout) getObjectField(param.thisObject, "batteryIcon");

            if (textColor != textColorPrimary) {
                int textColorSecondary = SettingsLibUtils.getColorAttrDefaultColor(context, android.R.attr.textColorHint);
                textColorPrimary = textColor;
                if (getObjectField(param.thisObject, "iconManager") != null) {
                    callMethod(getObjectField(param.thisObject, "iconManager"), "setTint", textColor);
                }
                callMethod(batteryIcon, "updateColors", textColorPrimary, textColorSecondary, textColorPrimary);
            }
        } catch (Throwable throwable) {
            log(TAG + throwable);
        }
    }

    @SuppressLint("DiscouragedApi")
    private ImageView initBatteryIfNull(XC_MethodHook.MethodHookParam param, ImageView mBatteryIconView) {
        if (mBatteryIconView == null) {
            mBatteryIconView = new ImageView(mContext);

            try {
                mBatteryIconView.setImageDrawable((Drawable) getObjectField(param.thisObject, "mAccessorizedDrawable"));
            } catch (Throwable throwable) {
                try {
                    mBatteryIconView.setImageDrawable((Drawable) getObjectField(param.thisObject, "mThemedDrawable"));
                } catch (Throwable throwable1) {
                    mBatteryIconView.setImageDrawable((Drawable) getObjectField(param.thisObject, "mDrawable"));
                }
            }

            final ViewGroup.MarginLayoutParams mlp = new ViewGroup.MarginLayoutParams(mContext.getResources().getDimensionPixelSize(mContext.getResources().getIdentifier("status_bar_battery_icon_width", "dimen", mContext.getPackageName())), mContext.getResources().getDimensionPixelSize(mContext.getResources().getIdentifier("status_bar_battery_icon_height", "dimen", mContext.getPackageName())));
            mlp.setMargins(0, 0, 0, mContext.getResources().getDimensionPixelOffset(mContext.getResources().getIdentifier("battery_margin_bottom", "dimen", mContext.getPackageName())));
            setObjectField(param.thisObject, "mBatteryIconView", mBatteryIconView);
            callMethod(param.thisObject, "addView", mBatteryIconView, mlp);
        }

        return mBatteryIconView;
    }

    private BatteryDrawable getNewDrawable(Context context) {
        BatteryDrawable mBatteryDrawable = null;

        switch (BatteryStyle) {
            case BATTERY_STYLE_DEFAULT:
            case BATTERY_STYLE_DEFAULT_RLANDSCAPE:
            case BATTERY_STYLE_DEFAULT_LANDSCAPE:
                break;
            case BATTERY_STYLE_CUSTOM_RLANDSCAPE:
                mBatteryDrawable = new RLandscapeBattery(context, frameColor);
                break;
            case BATTERY_STYLE_CUSTOM_LANDSCAPE:
                mBatteryDrawable = new LandscapeBattery(context, frameColor);
                break;
            case BATTERY_STYLE_PORTRAIT_CAPSULE:
                mBatteryDrawable = new PortraitBatteryCapsule(context, frameColor);
                break;
            case BATTERY_STYLE_PORTRAIT_LORN:
                mBatteryDrawable = new PortraitBatteryLorn(context, frameColor);
                break;
            case BATTERY_STYLE_PORTRAIT_MX:
                mBatteryDrawable = new PortraitBatteryMx(context, frameColor);
                break;
            case BATTERY_STYLE_PORTRAIT_AIROO:
                mBatteryDrawable = new PortraitBatteryAiroo(context, frameColor);
                break;
            case BATTERY_STYLE_RLANDSCAPE_STYLE_A:
                mBatteryDrawable = new RLandscapeBatteryStyleA(context, frameColor);
                break;
            case BATTERY_STYLE_LANDSCAPE_STYLE_A:
                mBatteryDrawable = new LandscapeBatteryStyleA(context, frameColor);
                break;
            case BATTERY_STYLE_RLANDSCAPE_STYLE_B:
                mBatteryDrawable = new RLandscapeBatteryStyleB(context, frameColor);
                break;
            case BATTERY_STYLE_LANDSCAPE_STYLE_B:
                mBatteryDrawable = new LandscapeBatteryStyleB(context, frameColor);
                break;
            case BATTERY_STYLE_LANDSCAPE_IOS_15:
                mBatteryDrawable = new LandscapeBatteryiOS15(context, frameColor);
                break;
            case BATTERY_STYLE_LANDSCAPE_IOS_16:
                mBatteryDrawable = new LandscapeBatteryiOS16(context, frameColor);
                break;
            case BATTERY_STYLE_PORTRAIT_ORIGAMI:
                mBatteryDrawable = new PortraitBatteryOrigami(context, frameColor);
                break;
            case BATTERY_STYLE_LANDSCAPE_SMILEY:
                mBatteryDrawable = new LandscapeBatterySmiley(context, frameColor);
                break;
            case BATTERY_STYLE_LANDSCAPE_MIUI_PILL:
                mBatteryDrawable = new LandscapeBatteryMIUIPill(context, frameColor);
                break;
            case BATTERY_STYLE_LANDSCAPE_COLOROS:
                mBatteryDrawable = new LandscapeBatteryColorOS(context, frameColor);
                break;
            case BATTERY_STYLE_RLANDSCAPE_COLOROS:
                mBatteryDrawable = new RLandscapeBatteryColorOS(context, frameColor);
                break;
            case BATTERY_STYLE_LANDSCAPE_BATTERYA:
                mBatteryDrawable = new LandscapeBatteryA(context, frameColor);
                break;
            case BATTERY_STYLE_LANDSCAPE_BATTERYB:
                mBatteryDrawable = new LandscapeBatteryB(context, frameColor);
                break;
            case BATTERY_STYLE_LANDSCAPE_BATTERYC:
                mBatteryDrawable = new LandscapeBatteryC(context, frameColor);
                break;
            case BATTERY_STYLE_LANDSCAPE_BATTERYD:
                mBatteryDrawable = new LandscapeBatteryD(context, frameColor);
                break;
            case BATTERY_STYLE_LANDSCAPE_BATTERYE:
                mBatteryDrawable = new LandscapeBatteryE(context, frameColor);
                break;
            case BATTERY_STYLE_LANDSCAPE_BATTERYF:
                mBatteryDrawable = new LandscapeBatteryF(context, frameColor);
                break;
            case BATTERY_STYLE_LANDSCAPE_BATTERYG:
                mBatteryDrawable = new LandscapeBatteryG(context, frameColor);
                break;
            case BATTERY_STYLE_LANDSCAPE_BATTERYH:
                mBatteryDrawable = new LandscapeBatteryH(context, frameColor);
                break;
            case BATTERY_STYLE_LANDSCAPE_BATTERYI:
                mBatteryDrawable = new LandscapeBatteryI(context, frameColor);
                break;
            case BATTERY_STYLE_LANDSCAPE_BATTERYJ:
                mBatteryDrawable = new LandscapeBatteryJ(context, frameColor);
                break;
            case BATTERY_STYLE_LANDSCAPE_BATTERYK:
                mBatteryDrawable = new LandscapeBatteryK(context, frameColor);
                break;
            case BATTERY_STYLE_LANDSCAPE_BATTERYL:
                mBatteryDrawable = new LandscapeBatteryL(context, frameColor);
                break;
            case BATTERY_STYLE_LANDSCAPE_BATTERYM:
                mBatteryDrawable = new LandscapeBatteryM(context, frameColor);
                break;
            case BATTERY_STYLE_LANDSCAPE_BATTERYN:
                mBatteryDrawable = new LandscapeBatteryN(context, frameColor);
                break;
            case BATTERY_STYLE_LANDSCAPE_BATTERYO:
                mBatteryDrawable = new LandscapeBatteryO(context, frameColor);
                break;
        }

        if (mBatteryDrawable != null) {
            mBatteryDrawable.setShowPercentEnabled(showPercentInside);
            mBatteryDrawable.setAlpha(Math.round(BatteryIconOpacity * 2.55f));
        }

        return mBatteryDrawable;
    }

    private void hidePercentage(XC_MethodHook.MethodHookParam param) {
        if (showPercentInside) {
            try {
                setObjectField(param.thisObject, "mShowPercentMode", 2);
            } catch (Throwable ignored) {
            }
            try {
                callMethod(param.thisObject, "removeView", getObjectField(param.thisObject, "mBatteryPercentView"));
                setObjectField(param.thisObject, "mBatteryPercentView", null);
            } catch (Throwable ignored) {
            }
        }
    }

    private void setCustomBatteryDimens() {
        XC_InitPackageResources.InitPackageResourcesParam ourResparam = resparams.get(SYSTEMUI_PACKAGE);
        if (ourResparam == null) return;

        if (BatteryStyle != BATTERY_STYLE_DEFAULT) {
            ourResparam.res.setReplacement(SYSTEMUI_PACKAGE, "dimen", "status_bar_battery_icon_width", new XResources.DimensionReplacement(customBatteryWidth, TypedValue.COMPLEX_UNIT_DIP));
            ourResparam.res.setReplacement(SYSTEMUI_PACKAGE, "dimen", "status_bar_battery_icon_height", new XResources.DimensionReplacement(customBatteryHeight, TypedValue.COMPLEX_UNIT_DIP));
            ourResparam.res.setReplacement(SYSTEMUI_PACKAGE, "dimen", "signal_cluster_battery_padding", new XResources.DimensionReplacement(customBatteryMargin, TypedValue.COMPLEX_UNIT_DIP));
        }
    }

    private void removeBatteryMeterViewMethods(Class<?> BatteryMeterViewClass) {
        if (customBatteryEnabled) {
            String[] methodNames = {"updateDrawable", "updateDrawable", "updateBatteryStyle", "updateSettings"};
            XC_MethodReplacement methodReplacement = new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam methodHookParam) {
                    return null;
                }
            };

            for (String methodName : methodNames) {
                try {
                    hookAllMethods(BatteryMeterViewClass, methodName, methodReplacement);
                } catch (Throwable ignored) {
                }
            }
        }
    }
}