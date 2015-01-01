package com.austinv11.peripheralsplusplus.speech;

public enum Voices {

	BITS_1(Gender.FEMALE, "bits1-hsmm", Languages.GERMAN),BITS_3(Gender.MALE, "bits3-hsmm", Languages.GERMAN), PAVOQUE(Gender.MALE, "dfki-pavoque-neutral-hsmm", Languages.GERMAN),
	OBADIAH(Gender.MALE, "dfki-obadiah-hsmm", Languages.ENGLISH_GB), POPPY(Gender.FEMALE, "dfki-poppy-hsmm", Languages.ENGLISH_GB), PRUDENCE(Gender.FEMALE, "dfki-prudence-hsmm", Languages.ENGLISH_GB), SPIKE(Gender.MALE, "dfki-spike-hsmm", Languages.ENGLISH_GB),
	BDL(Gender.MALE, "cmu-bdl-hsmm", Languages.ENGLISH_US), RMS(Gender.MALE, "cmu-rms-hsmm", Languages.ENGLISH_US), SLT(Gender.FEMALE, "cmu-slt-hsmm", Languages.ENGLISH_US),
	CAMILLE(Gender.FEMALE, "camille-hsmm-hsmm", Languages.FRENCH), DENNY(Gender.MALE, "enst-dennys-hsmm", Languages.FRENCH), JESSICA(Gender.FEMALE, "jessica_voice-hsmm", Languages.FRENCH), PIERRE(Gender.MALE, "pierre-voice-hsmm", Languages.FRENCH),
	LUCIA(Gender.FEMALE, "istc-lucia-hsmm", Languages.ITALIAN),
	//RUSSIAN_1(Gender.MALE, "voxforge-ru-nsh", Languages.RUSSIAN), Disabled due to large file size
	NK(Gender.FEMALE, "cmu-nk-hsmm", Languages.TELUGU),
	OT(Gender.MALE, "dfki-ot-hsmm", Languages.TURKISH);

	private Gender gender;
	private String name;
	private Languages lang;

	private Voices (Gender g, String n, Languages l) {
		gender = g;
		name = n;
		lang = l;
	}

	public Gender getGender() {
		return gender;
	}

	public String getName() {
		return name;
	}

	public Languages getLang() {
		return lang;
	}

	public enum Languages {

		GERMAN("de"),ENGLISH_GB("en-GB"),ENGLISH_US("en-US"),FRENCH("fr"),ITALIAN("it"),/*RUSSIAN("ru"),SWEDISH("sv"),FIXME: No currently available voices*/TELUGU("te"),TURKISH("tr");

		private String code;

		private Languages(String s) {
			code = s;
		}

		@Override
		public String toString() {
			return code;
		}
	}

	public enum Gender {
		MALE,FEMALE
	}
}
