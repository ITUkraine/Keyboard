package com.itukraine.pngkeyboard.service;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;

import com.itukraine.pngkeyboard.R;
import com.itukraine.pngkeyboard.listener.KeyboardActionListener;

public class PNGKeyboardService extends InputMethodService {

	private KeyboardView kv;
	private Keyboard keyboard;
	private InputConnection ic;
	private SpannableStringBuilder builder = new SpannableStringBuilder();

	private KeyboardActionListener keyboardListener = new KeyboardActionListener() {
		@Override
		public void onKey(int primaryCode, int[] keyCodes) {
			ic = getCurrentInputConnection();
			builder.clear();
			switch (primaryCode) {
			case Keyboard.KEYCODE_DELETE:
				ic.deleteSurroundingText(1, 0);
				break;
			case Keyboard.KEYCODE_DONE:
				ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN,
						KeyEvent.KEYCODE_ENTER));
				break;
			default:
				builder.append((char) primaryCode);
				builder.setSpan(new ImageSpan(PNGKeyboardService.this,
						R.drawable.key_qwas), 0, 1, 0);
				ic.commitText(builder, 1);
			}
		}
	};

	@Override
	public View onCreateInputView() {
		kv = (KeyboardView) getLayoutInflater()
				.inflate(R.layout.keyboard, null);
		keyboard = new Keyboard(this, R.xml.qwerty);
		kv.setKeyboard(keyboard);
		kv.setOnKeyboardActionListener(keyboardListener);
		return kv;
	}
}