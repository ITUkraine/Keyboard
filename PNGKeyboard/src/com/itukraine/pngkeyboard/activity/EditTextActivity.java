package com.itukraine.pngkeyboard.activity;

import android.app.Activity;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.Editable;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.itukraine.pngkeyboard.R;
import com.itukraine.pngkeyboard.listener.KeyboardActionListener;

public class EditTextActivity extends Activity {

	private KeyboardView keyboardView;
	private EditText editText;

	private KeyboardActionListener keyboardListener = new KeyboardActionListener() {
		@Override
		public void onKey(int primaryCode, int[] keyCodes) {
			View focusCurrent = EditTextActivity.this.getWindow().getCurrentFocus();
			EditText edittext = (EditText) focusCurrent;
			Editable editable = edittext.getText();

			int start = edittext.getSelectionStart();
			switch (primaryCode) {
			case Keyboard.KEYCODE_DELETE:
				if (start > 0) {
					editable.delete(start - 1, start);
				}
				break;
			case Keyboard.KEYCODE_DONE:
				hideCustomKeyboard();
				break;
			default:
				editable.insert(start, Character.toString((char) primaryCode));
				editable.setSpan(new ImageSpan(EditTextActivity.this, R.drawable.key_qwas), start, start+1, 0);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.input_activity);
		Keyboard mKeyboard = new Keyboard(this, R.xml.qwerty);
		keyboardView = (KeyboardView) findViewById(R.id.keyboard);
		keyboardView.setKeyboard(mKeyboard);
		keyboardView.setOnKeyboardActionListener(keyboardListener);
		// Hide the standard keyboard
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		editText = (EditText) findViewById(R.id.editText);
		editText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					showCustomKeyboard(v);
				} else {
					hideCustomKeyboard();
				}
			}
		});

		editText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showCustomKeyboard(v);
			}
		});
	}

	@Override
	public void onBackPressed() {
		if (isCustomKeyboardVisible()) {
			hideCustomKeyboard();
		} else {
			super.onBackPressed();
		}
	}

	private void hideCustomKeyboard() {
		keyboardView.setVisibility(View.GONE);
		keyboardView.setEnabled(false);
	}

	private void showCustomKeyboard(View v) {
		keyboardView.setVisibility(View.VISIBLE);
		keyboardView.setEnabled(true);
		if (v != null) {
			((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
					v.getWindowToken(), 0);
		}
	}

	private boolean isCustomKeyboardVisible() {
		return keyboardView.getVisibility() == View.VISIBLE;
	}
}