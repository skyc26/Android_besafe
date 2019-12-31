package net.android.besafeapp.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.NumberKeyListener;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import net.android.besafeapp.R;


/**
 * 带边框的editText
 */
public class FrameEditText extends LinearLayout {
    private View rootView;

    private ImageView ivIcon;
    private TextView tvOne;
    private EditText edit;

    private String title;
    private String hind;
    private int inputType = InputType.TYPE_CLASS_TEXT;

    public FrameEditText(Context context) {
        super(context);
        initView(context);
    }

    @SuppressLint("Recycle")
    public FrameEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FrameEditText);
        title = typedArray.getString(R.styleable.FrameEditText_text);
        hind = typedArray.getString(R.styleable.FrameEditText_hind);
        typedArray.recycle();
        initView(context);
    }

    public FrameEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        rootView = View.inflate(context, R.layout.ui_frame_edittext, this);
        ivIcon = rootView.findViewById(R.id.iv_icon);
        tvOne = rootView.findViewById(R.id.tv_one);
        edit = rootView.findViewById(R.id.edit_);
        if (title != null) {
            tvOne.setText(title);
        }
        if (hind != null) {
            edit.setHint(hind);
        }
        edit.setInputType(inputType);
    }


    public String getString() {
        if (TextUtils.isEmpty(edit.getText().toString())) {
            return null;
        }
        return edit.getText().toString();
    }


    public void setInputType(int inputType) {
        this.inputType = inputType;
        edit.setInputType(inputType);
    }

    public void setText(String str) {
        edit.setText(str);
    }


    public void setKeyListener(final char numberChars[]) {
        edit.setKeyListener(new NumberKeyListener() {

            @Override
            public int getInputType() {
                return 0;
            }

            protected char[] getAcceptedChars() {
                return numberChars;
            }
        });
    }
}
