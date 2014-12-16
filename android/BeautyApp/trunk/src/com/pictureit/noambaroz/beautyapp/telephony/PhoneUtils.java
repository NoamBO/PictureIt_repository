package com.pictureit.noambaroz.beautyapp.telephony;

import java.util.HashMap;
import java.util.Locale;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PhoneUtils {

	private static PhoneUtils instance;

	private Context mCtx;
	private HashMap<String, Integer> mCountryCodeMap;

	public static PhoneUtils get(Context ctx) {
		if (instance == null) {
			instance = new PhoneUtils(ctx);
		}
		return instance;
	}

	public String getPhoneNumberWithZipCodeByIso(String oldPhoneNumber) {
		String newPhoneNum = oldPhoneNumber;
		if (!oldPhoneNumber.contains("+")) {
			TelephonyManager tm = (TelephonyManager) mCtx.getSystemService(Context.TELEPHONY_SERVICE);
			String iso = tm.getSimCountryIso();
			if (mCountryCodeMap.containsKey(iso.toUpperCase(Locale.ENGLISH))) {
				String zip = "+" + mCountryCodeMap.get(iso.toUpperCase(Locale.ENGLISH));
				oldPhoneNumber = oldPhoneNumber.replaceFirst("0", "");
				newPhoneNum = zip + oldPhoneNumber;
				Log.i("ssss", newPhoneNum);
			}

		}
		return newPhoneNum;
	}

	private PhoneUtils(Context ctx) {
		mCtx = ctx;
		setHash();
	}

	private void setHash() {
		mCountryCodeMap = new HashMap<String, Integer>();
		mCountryCodeMap.put("AF", 93);
		mCountryCodeMap.put("AL", 355);
		mCountryCodeMap.put("DZ", 213);
		mCountryCodeMap.put("AD", 376);
		mCountryCodeMap.put("AO", 244);
		mCountryCodeMap.put("AQ", 672);
		mCountryCodeMap.put("AR", 54);
		mCountryCodeMap.put("AM", 374);
		mCountryCodeMap.put("AW", 297);
		mCountryCodeMap.put("AU", 61);
		mCountryCodeMap.put("AT", 43);
		mCountryCodeMap.put("AZ", 994);
		mCountryCodeMap.put("BH", 973);
		mCountryCodeMap.put("BD", 880);
		mCountryCodeMap.put("BY", 375);
		mCountryCodeMap.put("BE", 32);
		mCountryCodeMap.put("BZ", 501);
		mCountryCodeMap.put("BJ", 229);
		mCountryCodeMap.put("BT", 975);
		mCountryCodeMap.put("BO", 591);
		mCountryCodeMap.put("BA", 387);
		mCountryCodeMap.put("BW", 267);
		mCountryCodeMap.put("BR", 55);
		mCountryCodeMap.put("BN", 673);
		mCountryCodeMap.put("BG", 359);
		mCountryCodeMap.put("BF", 226);
		mCountryCodeMap.put("MM", 95);
		mCountryCodeMap.put("BI", 257);
		mCountryCodeMap.put("KH", 855);
		mCountryCodeMap.put("CM", 237);
		mCountryCodeMap.put("CA", 1);
		mCountryCodeMap.put("CV", 238);
		mCountryCodeMap.put("CF", 236);
		mCountryCodeMap.put("TD", 235);
		mCountryCodeMap.put("CL", 56);
		mCountryCodeMap.put("CN", 86);
		mCountryCodeMap.put("CX", 61);
		mCountryCodeMap.put("CC", 61);
		mCountryCodeMap.put("CO", 57);
		mCountryCodeMap.put("KM", 269);
		mCountryCodeMap.put("CG", 242);
		mCountryCodeMap.put("CD", 243);
		mCountryCodeMap.put("CK", 682);
		mCountryCodeMap.put("CR", 506);
		mCountryCodeMap.put("HR", 385);
		mCountryCodeMap.put("CU", 53);
		mCountryCodeMap.put("CY", 357);
		mCountryCodeMap.put("CZ", 420);
		mCountryCodeMap.put("DK", 45);
		mCountryCodeMap.put("DJ", 253);
		mCountryCodeMap.put("TL", 670);
		mCountryCodeMap.put("EC", 593);
		mCountryCodeMap.put("EG", 20);
		mCountryCodeMap.put("SV", 503);
		mCountryCodeMap.put("GQ", 240);
		mCountryCodeMap.put("ER", 291);
		mCountryCodeMap.put("EE", 372);
		mCountryCodeMap.put("ET", 251);
		mCountryCodeMap.put("FK", 500);
		mCountryCodeMap.put("FO", 298);
		mCountryCodeMap.put("FJ", 679);
		mCountryCodeMap.put("FI", 358);
		mCountryCodeMap.put("FR", 33);
		mCountryCodeMap.put("PF", 689);
		mCountryCodeMap.put("GA", 241);
		mCountryCodeMap.put("GM", 220);
		mCountryCodeMap.put("GE", 995);
		mCountryCodeMap.put("DE", 49);
		mCountryCodeMap.put("GH", 233);
		mCountryCodeMap.put("GI", 350);
		mCountryCodeMap.put("GR", 30);
		mCountryCodeMap.put("GL", 299);
		mCountryCodeMap.put("GT", 502);
		mCountryCodeMap.put("GN", 224);
		mCountryCodeMap.put("GW", 245);
		mCountryCodeMap.put("GY", 592);
		mCountryCodeMap.put("HT", 509);
		mCountryCodeMap.put("HN", 504);
		mCountryCodeMap.put("HK", 852);
		mCountryCodeMap.put("HU", 36);
		mCountryCodeMap.put("IN", 91);
		mCountryCodeMap.put("ID", 62);
		mCountryCodeMap.put("IR", 98);
		mCountryCodeMap.put("IQ", 964);
		mCountryCodeMap.put("IE", 353);
		mCountryCodeMap.put("IM", 44);
		mCountryCodeMap.put("IL", 972);
		mCountryCodeMap.put("IT", 39);
		mCountryCodeMap.put("CI", 225);
		mCountryCodeMap.put("JP", 81);
		mCountryCodeMap.put("JO", 962);
		mCountryCodeMap.put("KZ", 7);
		mCountryCodeMap.put("KE", 254);
		mCountryCodeMap.put("KI", 686);
		mCountryCodeMap.put("KW", 965);
		mCountryCodeMap.put("KG", 996);
		mCountryCodeMap.put("LA", 856);
		mCountryCodeMap.put("LV", 371);
		mCountryCodeMap.put("LB", 961);
		mCountryCodeMap.put("LS", 266);
		mCountryCodeMap.put("LR", 231);
		mCountryCodeMap.put("LY", 218);
		mCountryCodeMap.put("LI", 423);
		mCountryCodeMap.put("LT", 370);
		mCountryCodeMap.put("LU", 352);
		mCountryCodeMap.put("MO", 853);
		mCountryCodeMap.put("MK", 389);
		mCountryCodeMap.put("MG", 261);
		mCountryCodeMap.put("MW", 265);
		mCountryCodeMap.put("MY", 60);
		mCountryCodeMap.put("MV", 960);
		mCountryCodeMap.put("ML", 223);
		mCountryCodeMap.put("MT", 356);
		mCountryCodeMap.put("MH", 692);
		mCountryCodeMap.put("MR", 222);
		mCountryCodeMap.put("MU", 230);
		mCountryCodeMap.put("YT", 262);
		mCountryCodeMap.put("MX", 52);
		mCountryCodeMap.put("FM", 691);
		mCountryCodeMap.put("MD", 373);
		mCountryCodeMap.put("MC", 377);
		mCountryCodeMap.put("MN", 976);
		mCountryCodeMap.put("ME", 382);
		mCountryCodeMap.put("MA", 212);
		mCountryCodeMap.put("MZ", 258);
		mCountryCodeMap.put("NA", 264);
		mCountryCodeMap.put("NR", 674);
		mCountryCodeMap.put("NP", 977);
		mCountryCodeMap.put("NL", 31);
		mCountryCodeMap.put("AN", 599);
		mCountryCodeMap.put("NC", 687);
		mCountryCodeMap.put("NZ", 64);
		mCountryCodeMap.put("NI", 505);
		mCountryCodeMap.put("NE", 227);
		mCountryCodeMap.put("NG", 234);
		mCountryCodeMap.put("NU", 683);
		mCountryCodeMap.put("KP", 850);
		mCountryCodeMap.put("NO", 47);
		mCountryCodeMap.put("OM", 968);
		mCountryCodeMap.put("PK", 92);
		mCountryCodeMap.put("PW", 680);
		mCountryCodeMap.put("PA", 507);
		mCountryCodeMap.put("PG", 675);
		mCountryCodeMap.put("PY", 595);
		mCountryCodeMap.put("PE", 51);
		mCountryCodeMap.put("PH", 63);
		mCountryCodeMap.put("PN", 870);
		mCountryCodeMap.put("PL", 48);
		mCountryCodeMap.put("PT", 351);
		mCountryCodeMap.put("PR", 1);
		mCountryCodeMap.put("QA", 974);
		mCountryCodeMap.put("RO", 40);
		mCountryCodeMap.put("RU", 7);
		mCountryCodeMap.put("RW", 250);
		mCountryCodeMap.put("BL", 590);
		mCountryCodeMap.put("WS", 685);
		mCountryCodeMap.put("SM", 378);
		mCountryCodeMap.put("ST", 239);
		mCountryCodeMap.put("SA", 966);
		mCountryCodeMap.put("SN", 221);
		mCountryCodeMap.put("RS", 381);
		mCountryCodeMap.put("SC", 248);
		mCountryCodeMap.put("SL", 232);
		mCountryCodeMap.put("SG", 65);
		mCountryCodeMap.put("SK", 421);
		mCountryCodeMap.put("SI", 386);
		mCountryCodeMap.put("SB", 677);
		mCountryCodeMap.put("SO", 252);
		mCountryCodeMap.put("ZA", 27);
		mCountryCodeMap.put("KR", 82);
		mCountryCodeMap.put("ES", 34);
		mCountryCodeMap.put("LK", 94);
		mCountryCodeMap.put("SH", 290);
		mCountryCodeMap.put("PM", 508);
		mCountryCodeMap.put("SD", 249);
		mCountryCodeMap.put("SR", 597);
		mCountryCodeMap.put("SZ", 268);
		mCountryCodeMap.put("SE", 46);
		mCountryCodeMap.put("CH", 41);
		mCountryCodeMap.put("SY", 963);
		mCountryCodeMap.put("TW", 886);
		mCountryCodeMap.put("TJ", 992);
		mCountryCodeMap.put("TZ", 255);
		mCountryCodeMap.put("TH", 66);
		mCountryCodeMap.put("TG", 228);
		mCountryCodeMap.put("TK", 690);
		mCountryCodeMap.put("TO", 676);
		mCountryCodeMap.put("TN", 216);
		mCountryCodeMap.put("TR", 90);
		mCountryCodeMap.put("TM", 993);
		mCountryCodeMap.put("TV", 688);
		mCountryCodeMap.put("AE", 971);
		mCountryCodeMap.put("UG", 256);
		mCountryCodeMap.put("GB", 44);
		mCountryCodeMap.put("UA", 380);
		mCountryCodeMap.put("UY", 598);
		mCountryCodeMap.put("US", 1);
		mCountryCodeMap.put("UZ", 998);
		mCountryCodeMap.put("VU", 678);
		mCountryCodeMap.put("VA", 39);
		mCountryCodeMap.put("VE", 58);
		mCountryCodeMap.put("VN", 84);
		mCountryCodeMap.put("WF", 681);
		mCountryCodeMap.put("YE", 967);
		mCountryCodeMap.put("ZM", 260);
		mCountryCodeMap.put("ZW", 263);
	}
}
