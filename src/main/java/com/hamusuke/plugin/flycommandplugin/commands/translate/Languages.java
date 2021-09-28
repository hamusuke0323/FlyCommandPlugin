package com.hamusuke.plugin.flycommandplugin.commands.translate;

public enum Languages {
	DetectLanguage("", "Auto"),
	Icelandic("is", "Icelandic"),
	Irish("ga", "Irish"),
	Azerbaijani("az", "Azerbaijani"),
	Afrikaans("af", "Afrikaans"),
	Amharic("am", "Amharic"),
	Arabic("ar", "Arabic"),
	Albanian("sq", "Albanian"),
	Armenian("hy", "Armenian"),
	Italian("it", "Italian"),
	Yiddish("yi", "Yiddish"),
	Igbo("ig", "Igbo"),
	Indonesian("id", "Indonesian"),
	Uighur("ug", "Uighur"),
	Welsh("cy", "Welsh"),
	Ukrainian("uk", "Ukrainian"),
	Uzbek("uz", "Uzbek"),
	Urdu("ur", "Urdu"),
	Estonian("et", "Estonian"),
	Esperanto("eo", "Esperanto"),
	Dutch("nl", "Dutch"),
	Oriya("or", "Oriya"),
	Kazakh("kk", "Kazakh"),
	Catalan("ca", "Catalan"),
	Galician("gl", "Galician"),
	Kannada("kn", "Kannada"),
	Kinyarwanda("rw", "Kinyarwanda"),
	GreekLanguage("el", "Greek"),
	Kyrgyz("ky", "Kyrgyz"),
	Gujarati("gu", "Gujarati"),
	Khmer("km", "Khmer"),
	Kurdish("ku", "Kurdish"),
	Croatian("hr", "Croatian"),
	Xhosa("xh", "Xhosa"),
	Corsica("co", "Corsica"),
	Samoan("sm", "Samoan"),
	Javanese("jw", "Javanese"),
	Georgian("ka", "Georgian"),
	Shona("sn", "Shona"),
	Sindhi("sd", "Sindhi"),
	Sinhala("si", "Sinhala"),
	Swedish("sv", "Swedish"),
	Zulu("zu", "Zulu"),
	ScottishGaelic("gd", "ScottishGaelic"),
	Spanish("es", "Spanish"),
	Slovak("sk", "Slovak"),
	Slovenian("sl", "Slovenian"),
	Swahili("sw", "Swahili"),
	Sundanese("su", "Sundanese"),
	Cebuano("ceb", "Cebuano"),
	Serbian("sr", "Serbian"),
	Sotho("st", "Sotho"),
	Somali("so", "Somali"),
	Thai("th", "Thai"),
	Tagalog("tl", "Tagalog"),
	Tajik("tg", "Tajik"),
	Tatar("tt", "Tatar"),
	Tamil("ta", "Tamil"),
	CzechLanguage("cs", "Czech"),
	Chewa("ny", "Chewa"),
	Telugu("te", "Telugu"),
	Danish("da", "Danish"),
	German("de", "German"),
	Turkmen("tk", "Turkmen"),
	Turkish("tr", "Turkish"),
	Nepali("ne", "Nepali"),
	Norwegian("no", "Norwegian"),
	Haitian("ht", "Haitian"),
	Hausa("ha", "Hausa"),
	Pashto("ps", "Pashto"),
	Basque("eu", "Basque"),
	Hawaiian("haw", "Hawaiian"),
	Hungarian("hu", "Hungarian"),
	Punjabi("pa", "Punjabi"),
	Hindi("hi", "Hindi"),
	Finnish("fi", "Finnish"),
	French("fr", "French"),
	Frisian("fy", "Frisian"),
	Bulgarian("bg", "Bulgarian"),
	Vietnamese("vi", "Vietnamese"),
	Hebrew("iw", "Hebrew"),
	Belarusian("be", "Belarusian"),
	Persian("fa", "Persian"),
	Bengali("bn", "Bengali"),
	PolishLanguage("pl", "Polish"),
	Bosnian("bs", "Bosnian"),
	Portuguese("pt", "Portuguese"),
	Maori("mi", "Maori"),
	Macedonian("mk", "Macedonian"),
	Marathi("mr", "Marathi"),
	Malagasy("mg", "Malagasy"),
	Malayalam("ml", "Malayalam"),
	Maltese("mt", "Maltese"),
	Malay("ms", "Malay"),
	Myanmar("my", "Myanmar"),
	Mongolian("mn", "Mongolian"),
	Mong("hmn", "Mong"),
	Yoruba("yo", "Yoruba"),
	Lao("lo", "Lao"),
	Latin("la", "Latin"),
	Latvian("lv", "Latvian"),
	Lithuanian("lt", "Lithuanian"),
	Romanian("ro", "Romanian"),
	Luxembourgish("lb", "Luxembourgish"),
	Russian("ru", "Russian"),
	English("en", "English"),
	Korean("ko", "Korean"),
	Chinese("zh-CN", "Chinese"),
	Chinese_Simplified("zh-CN", "Chinese-Simplified"),
	Chinese_Traditional("zh-TW", "Chinese-Traditional"),
	Japanese("ja", "Japanese");

	private final String lang;
	private final String name;

	Languages(String lang, String langName) {
		this.lang = lang;
		this.name = langName;
	}

	public String getLanguageName() {
		return this.name;
	}

	public String getLanguage() {
		return this.lang;
	}
}
