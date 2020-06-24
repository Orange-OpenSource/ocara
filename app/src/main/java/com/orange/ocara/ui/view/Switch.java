/*
 * Software Name: OCARA
 *
 * SPDX-FileCopyrightText: Copyright (c) 2015-2020 Orange
 * SPDX-License-Identifier: MPL v2.0
 *
 * This software is distributed under the Mozilla Public License v. 2.0,
 * the text of which is available at http://mozilla.org/MPL/2.0/ or
 * see the "license.txt" file for more details.
 */

/**
 * Created by Andy
 *
 * Original source code is here https://bitbucket.org/ankri/switchbutton/src
 *
 * Beerware License
 *
 */

package com.orange.ocara.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.widget.CompoundButton;

import com.orange.ocara.R;

/**
 * The switch has two states similar to a checkbox or toggle button. But unlike the two, you see the
 * other state which is currently not selected. The switch flips from the left to the right side,
 * selecting either the right or the left side.
 */
public class Switch extends CompoundButton {
    // left and right text
    private CharSequence textLeft;
    private CharSequence textRight;

    // the background
    private Drawable drawableBackground;

    // the switch
    private Drawable drawableSwitch;

    // helper for left and right text
    private Layout layoutLeft;
    private Layout layoutRight;

    // max width of the whole switch
    private int switchMaxWidth;

    // actual width and height of the switch
    private int width;
    private int height;

    // the padding left+right inside of the switch
    private int innerPadding;

    // the space between the text to the left of the switch and the switch
    private int outerPadding;

    // the colors for the text of the switch
    private int textColorChecked;
    private int textColorUnChecked;
    private OnClickListener onClickListener;

    /**
     * Construct a new Switch with default styling
     *
     * @param context The Context that will determine this widget's theming
     */
    public Switch(Context context) {
        this(context, null);
    }

    /**
     * Construct a new Switch with default styling, overriding specific style attributes as
     * requested.
     *
     * @param context The Context that will determine this widget's theming.
     * @param attrs   Specification of attributes that should deviate from default styling.
     */
    public Switch(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.customSwitchStyle);
    }

    /**
     * Construct a new Switch with a default style determined by the given theme attribute,
     * overriding specific style attributes as requested.
     *
     * @param context  The Context that will determine this widget's theming.
     * @param attrs    Specification of attributes that should deviate from the default styling.
     * @param defStyle An attribute ID within the active theme containing a reference to the
     *                 default
     *                 style for this widget. e.g. android.R.attr.switchStyle.
     */

    public Switch(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        this.getPaint().setAntiAlias(true);

        // load the default values
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomSwitch, defStyle, 0);
        this.textLeft = a.getText(R.styleable.CustomSwitch_textLeft);
        this.textRight = a.getText(R.styleable.CustomSwitch_textRight);
        this.switchMaxWidth = a.getDimensionPixelSize(R.styleable.CustomSwitch_switchMaxWidth, -1);
        this.textColorUnChecked = a.getColor(R.styleable.CustomSwitch_colorUnChecked, Color.WHITE);
        this.textColorChecked = a.getColor(R.styleable.CustomSwitch_colorChecked, Color.WHITE);
        this.drawableBackground = a.getDrawable(R.styleable.CustomSwitch_backgroundDrawable);
        this.drawableSwitch = a.getDrawable(R.styleable.CustomSwitch_switchDrawable);
        this.outerPadding = a.getDimensionPixelSize(R.styleable.CustomSwitch_outerPadding, 32);
        this.innerPadding = a.getDimensionPixelSize(R.styleable.CustomSwitch_innerPadding, 20);
        this.setChecked(a.getBoolean(R.styleable.CustomSwitch_isChecked, false));
        a.recycle();

        // throw an error if the texts have not been set
        if (this.textLeft == null || this.textRight == null)
            throw new IllegalStateException("Either textLeft or textRight is null. Please them via the attributes with the same name in the layout");
    }

    /**
     * Returns the text on the left
     *
     * @return the text on the left. <code>null</code> shouldn't be possible, due to the button
     * throwing an error if the texts are <code>null</code>
     */
    public CharSequence getTextLeft() {
        return this.textLeft;
    }

    /**
     * Sets the text displayed on the left side.
     *
     * @param textLeft The left text. Not <code>null</code>
     */
    public void setTextLeft(CharSequence textLeft) {
        if (textLeft == null)
            throw new IllegalArgumentException("The text for the left side must not be null!");

        this.textLeft = textLeft;
        this.requestLayout();
    }

    /**
     * Returns the text on the right.
     *
     * @return the text on the right. <code>null</code> shouldn't be possible, due to the button
     * throwing an error if the texts are <code>null</code>
     */
    public CharSequence getTextRight() {
        return this.textRight;
    }

    /**
     * Sets the text displayed on the right side
     *
     * @param textRight The right text. Not <code>null</code>
     */
    public void setTextRight(CharSequence textRight) {

        if (textRight == null)
            throw new IllegalArgumentException("The text for the right side must not be null!");

        this.textRight = textRight;
        this.requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // create the helper layouts if necessary
        if (this.layoutLeft == null)
            this.layoutLeft = this.makeLayout(this.textLeft);

        if (this.layoutRight == null)
            this.layoutRight = this.makeLayout(this.textRight);


        this.width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        this.height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);

        if (switchMaxWidth > 0) {
            this.width = Math.min(this.switchMaxWidth, this.width);
        }

        // set the dimensions for this view
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // calculate the left and right values
        int right = this.getWidth() - this.getPaddingRight();
        int left = right - this.width;

        // draw background
        this.drawableBackground.setBounds(left, 0, right, this.height);
        this.drawableBackground.draw(canvas);

        // draw switch
        if (!this.isChecked())
            this.drawableSwitch.setBounds(left, 0, left + (this.width / 2), this.height);
        else
            this.drawableSwitch.setBounds(left + this.width / 2, 0, right, this.height);

        this.drawableSwitch.draw(canvas);

        // draw left text
        this.getPaint().setColor(!this.isChecked() ? this.textColorChecked : this.textColorUnChecked);
        canvas.translate(left + (this.width / 2 - this.layoutLeft.getWidth()) / 2, (this.height - this.layoutLeft.getHeight()) / 2);
        this.layoutLeft.draw(canvas);

        // draw right text
        this.getPaint().setColor(this.isChecked() ? this.textColorChecked : this.textColorUnChecked);
        canvas.translate((this.width + this.width / 2 - this.layoutRight.getWidth()) / 2 - (left + (this.width / 2 - this.layoutLeft.getWidth()) / 2), 0);
        this.layoutRight.draw(canvas);
    }

    @Override
    public int getCompoundPaddingRight() {
        int padding = super.getCompoundPaddingRight() + this.width;
        if (!TextUtils.isEmpty(getText()))
            padding += this.outerPadding;

        return padding;
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(l);

        this.onClickListener = l;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                this.setChecked(!this.isChecked());
                invalidate();

                // call the onClickListener
                if (this.onClickListener != null)
                    this.onClickListener.onClick(this);
                return false;
        }


        return super.onTouchEvent(event);
    }

    /**
     * Make a layout for the text
     *
     * @param text The text. Neither <code>null</code> nor empty
     * @return The layout
     */
    private Layout makeLayout(CharSequence text) {
        return new StaticLayout(text, this.getPaint(), (int) Math.ceil(Layout.getDesiredWidth(text, this.getPaint())), Layout.Alignment.ALIGN_NORMAL, 1f, 0, true);
    }

    @Override
    protected void drawableStateChanged() {

        super.drawableStateChanged();

        int[] myDrawableState = getDrawableState();

        if (this.drawableSwitch != null)
            this.drawableSwitch.setState(myDrawableState);

        invalidate();
    }
}
