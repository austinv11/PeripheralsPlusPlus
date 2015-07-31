package com.austinv11.peripheralsplusplus.utils;

import com.gtranslate.Audio;
import com.gtranslate.Language;
import com.gtranslate.Translator;
import rehost.javazoom.jl.decoder.JavaLayerException;

import java.io.IOException;
import java.io.InputStream;

public class TranslateUtils {

	public static String translate(String text, String fromLang, String toLang) {
		return Translator.getInstance().translate(text, fromLang, toLang);
	}

	public static String translate(String text, String toLang) {
		return translate(text, Translator.getInstance().detect(text), toLang);
	}

	public static boolean isPrefix(String lang) {
		return lang.length() == 2 || lang.toLowerCase().contains("-");
	}

	public static String detectLangName(String text) {
		return Language.getInstance().getNameLanguage(Translator.getInstance().detect(text));
	}

	public static String detectLangPrefix(String text) {
		return Translator.getInstance().detect(text);
	}

	public static void playAudio(String text, String lang) throws IOException, JavaLayerException {
		Audio audio = Audio.getInstance();
		InputStream sound  = audio.getAudio(text, lang);
		audio.play(sound);
	}
}
