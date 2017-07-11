/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agrqqd to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * Sqq the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.httpchatdemo.utils;

import android.content.Context;
import android.text.Spannable;
import android.text.Spannable.Factory;
import android.text.style.ImageSpan;

import com.example.httpchatdemo.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SmileUtils {
	public static final String qq_01 = "[):]";
	public static final String qq_02 = "[:D]";
	public static final String qq_03 = "[;)]";
	public static final String qq_04 = "[:-o]";
	public static final String qq_05 = "[:p]";
	public static final String qq_06 = "[(H)]";
	public static final String qq_07 = "[:@]";
	public static final String qq_08 = "[:s]";
	public static final String qq_09 = "[:$]";
	public static final String qq_10 = "[:(]";
	public static final String qq_11 = "[:'(]";
	public static final String qq_12 = "[:|]";
	public static final String qq_13 = "[(a)]";
	public static final String qq_14 = "[8o|]";
	public static final String qq_15 = "[8-|]";
	public static final String qq_16 = "[+o(]";
	public static final String qq_17 = "[<o)]";
	public static final String qq_18 = "[|-)]";
	public static final String qq_19 = "[*-)]";
	public static final String qq_20 = "[:-#]";
	public static final String qq_21 = "[:-*]";
	public static final String qq_22 = "[^o)]";
	public static final String qq_23 = "[8-)]";
	public static final String qq_24 = "[(|)]";
	public static final String qq_25 = "[(u)]";
	public static final String qq_26 = "[(S)]";
	public static final String qq_27 = "[(*)]";
	public static final String qq_28 = "[(#)]";
	public static final String qq_29 = "[(R)]";
	public static final String qq_30 = "[({)]";
	public static final String qq_31 = "[(})]";
	public static final String qq_32 = "[(k)]";
	public static final String qq_33 = "[(F)]";
	public static final String qq_34 = "[(W)]";
	public static final String qq_35 = "[(D)]";
	public static final String qq_36 = "[(A)]";
	public static final String qq_37= "[(B)]";
	public static final String qq_38 = "[(C)]";
	public static final String qq_39 = "[(E)]";
	public static final String qq_40 = "[(E)]";

	private static final Factory spannableFactory = Factory
			.getInstance();

	private static final Map<Pattern, Integer> emoticons = new HashMap<Pattern, Integer>();

	static {

		addPattern(emoticons, qq_01, R.mipmap.qq_01);
		addPattern(emoticons, qq_02, R.mipmap.qq_02);
		addPattern(emoticons, qq_03, R.mipmap.qq_03);
		addPattern(emoticons, qq_04, R.mipmap.qq_04);
		addPattern(emoticons, qq_05, R.mipmap.qq_05);
		addPattern(emoticons, qq_06, R.mipmap.qq_06);
		addPattern(emoticons, qq_07, R.mipmap.qq_07);
		addPattern(emoticons, qq_08, R.mipmap.qq_08);
		addPattern(emoticons, qq_09, R.mipmap.qq_09);
		addPattern(emoticons, qq_10, R.mipmap.qq_10);
		addPattern(emoticons, qq_11, R.mipmap.qq_11);
		addPattern(emoticons, qq_12, R.mipmap.qq_12);
		addPattern(emoticons, qq_13, R.mipmap.qq_13);
		addPattern(emoticons, qq_14, R.mipmap.qq_14);
		addPattern(emoticons, qq_15, R.mipmap.qq_15);
		addPattern(emoticons, qq_16, R.mipmap.qq_16);
		addPattern(emoticons, qq_17, R.mipmap.qq_17);
		addPattern(emoticons, qq_18, R.mipmap.qq_18);
		addPattern(emoticons, qq_19, R.mipmap.qq_19);
		addPattern(emoticons, qq_20, R.mipmap.qq_20);
		addPattern(emoticons, qq_21, R.mipmap.qq_21);
		addPattern(emoticons, qq_22, R.mipmap.qq_22);
		addPattern(emoticons, qq_23, R.mipmap.qq_23);
		addPattern(emoticons, qq_24, R.mipmap.qq_24);
		addPattern(emoticons, qq_25, R.mipmap.qq_25);
		addPattern(emoticons, qq_26, R.mipmap.qq_26);
		addPattern(emoticons, qq_27, R.mipmap.qq_27);
		addPattern(emoticons, qq_28, R.mipmap.qq_28);
		addPattern(emoticons, qq_29, R.mipmap.qq_29);
		addPattern(emoticons, qq_30, R.mipmap.qq_30);
		addPattern(emoticons, qq_31, R.mipmap.qq_31);
		addPattern(emoticons, qq_32, R.mipmap.qq_32);
		addPattern(emoticons, qq_33, R.mipmap.qq_33);
		addPattern(emoticons, qq_34, R.mipmap.qq_34);
		addPattern(emoticons, qq_35, R.mipmap.qq_35);
		addPattern(emoticons, qq_36, R.mipmap.qq_36);
		addPattern(emoticons, qq_37, R.mipmap.qq_37);
		addPattern(emoticons, qq_38, R.mipmap.qq_38);
		addPattern(emoticons, qq_39, R.mipmap.qq_39);
		addPattern(emoticons, qq_40, R.mipmap.qq_40);
	}

	private static void addPattern(Map<Pattern, Integer> map, String smile,
								   int resource) {
		map.put(Pattern.compile(Pattern.quote(smile)), resource);
	}

	/**
	 * replace existing spannable with smiles
	 * @param context
	 * @param spannable
	 * @return
	 */
	public static boolean addSmiles(Context context, Spannable spannable) {
		boolean hasChanges = false;
		for (Entry<Pattern, Integer> entry : emoticons.entrySet()) {
			Matcher matcher = entry.getKey().matcher(spannable);
			while (matcher.find()) {
				boolean set = true;
				for (ImageSpan span : spannable.getSpans(matcher.start(),
						matcher.end(), ImageSpan.class))
					if (spannable.getSpanStart(span) >= matcher.start()
							&& spannable.getSpanEnd(span) <= matcher.end())
						spannable.removeSpan(span);
					else {
						set = false;
						break;
					}
				if (set) {
					hasChanges = true;
					spannable.setSpan(new ImageSpan(context, entry.getValue()),
							matcher.start(), matcher.end(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}
		}
		return hasChanges;
	}

	public static Spannable getSmiledText(Context context, CharSequence text) {
		Spannable spannable = spannableFactory.newSpannable(text);
		addSmiles(context, spannable);
		return spannable;
	}

	public static boolean containsKey(String key){
		boolean b = false;
		for (Entry<Pattern, Integer> entry : emoticons.entrySet()) {
			Matcher matcher = entry.getKey().matcher(key);
			if (matcher.find()) {
				b = true;
				break;
			}
		}

		return b;
	}



}
