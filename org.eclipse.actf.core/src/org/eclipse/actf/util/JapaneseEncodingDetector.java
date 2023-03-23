/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

// TODO
//
// one-byte katakana
// one-byte katakana with not EUC code -> mihgt be one-byte katakana
//
// EUC1st=0x8f -> two trailers (same code region)

/*
 * information
 * 
 * currently ignore vendor specific EUC chars currently ignore reserved word of
 * SJIS (0xF0-0xfc)
 * 
 * Other JIS Escape sequence(start) "ESC $ @" 0x1b 0x24 0x40 "ESC & @ ESC $ B"
 * 0x1b 0x26 0x40 0x1b 0x24 0x42 "ESC $ ( D" 0x1b 0x24 0x28 0x44 etc.
 * 
 * Escape sequence(fin) "ESC ( J" "ESC ( H" "ESC ( B" "ESC ( I" etc.
 */

/**
 * Utility class for detecting Japanese encoding.
 */
public class JapaneseEncodingDetector {
	private static final int J_SJIS = 0;

	private static final int J_EUC = 1;

	private static final int J_JIS = 2;

	@SuppressWarnings("unused")
	private static final int J_MIX = 3;

	private static final int J_UTF8 = 4;

	private static final int LATIN1 = 5;

	// TODO SJIS, EUC_JP, ISO2022JP
	private static final String JIS = "ISO-2022-JP"; //$NON-NLS-1$

	private static final String EUC = "EUC-JP"; //$NON-NLS-1$

	private static final String SJIS = "Shift_JIS"; //$NON-NLS-1$

	private static final String UTF_8 = "UTF8"; //$NON-NLS-1$

	private static final String ISO_8859_1 = "ISO-8859-1"; //$NON-NLS-1$

	// JIS escape sequence
	//
	// "Esc $ B"
	private static final byte JIS_ESCAPE = (byte) 0x1b;// Esc

	private static final byte JIS_DOUBLE = (byte) 0x24;// $ double-byte

	private static final byte JIS_SINGLE = (byte) 0x28;// ( single-byte

	private static final byte JIS_B = (byte) 0x42;// B new-JIS

	private static final byte JIS_AT = (byte) 0x40;// @ old-JIS

	private static final byte JIS_D = (byte) 0x44;// D JIS-sup

	private static final byte JIS_J = (byte) 0x4A;// J JIS-roma

	private static final byte JIS_I = (byte) 0x49;// I JIS-ASCII

	// private static final byte JIS_S_PAYLOAD_BEGIN = (byte)0x20;
	// private static final byte JIS_S_PAYLOAD_END = (byte)0x7e;

	private static final byte JIS_KANA_PAYLOAD_BEGIN = (byte) 0x21;

	private static final byte JIS_KANA_PAYLOAD_END = (byte) 0x5f;

	private static final byte JIS_D_PAYLOAD_BEGIN = (byte) 0x21;

	private static final byte JIS_D_PAYLOAD_END = (byte) 0x7E;

	// EUC
	private static final byte E_KANA_1ST = (byte) 0x8e; // 2nd a1-df

	private static final byte E_HOJO_1ST = (byte) 0x8f; // 2byte payload

	private static final byte E_KISYU_1ST = (byte) 0xa0; // 2nd, 3rd payload?

	private static final byte E_KANJI_BEGIN = (byte) 0xa1; // 2nd,3rd payload

	private static final byte E_KANJI_END = (byte) 0xfe;

	private static final byte E_KANA_BEGIN = (byte) 0xa1; // kana payload

	private static final byte E_KANA_END = (byte) 0xdf; // kana payload

	private static final byte E_PAYLOAD_BEGIN = (byte) 0xa0;

	private static final byte E_PAYLOAD_END = (byte) 0xff;

	// SJIS
	private static final byte S1st1begin = (byte) 0x81;

	private static final byte S1st1end = (byte) 0x9f;

	private static final byte S1st2begin = (byte) 0xe0;

	private static final byte S1st2end = (byte) 0xef;

	private static final byte S2nd1begin = (byte) 0x40;

	private static final byte S2nd1end = (byte) 0x7e;

	private static final byte S2nd2begin = (byte) 0x80;

	private static final byte S2nd2end = (byte) 0xfc;

	// SJIS kana
	private static final byte Skanabegin = (byte) 0xa1;

	private static final byte Skanaend = (byte) 0xdf;

	// UTF-8
	private static final byte ASCII_BEGIN = (byte) 0x00;

	private static final byte ASCII_END = (byte) 0x7F;

	private static final byte U_2BYTE_BEGIN = (byte) 0xc0;

	private static final byte U_2BYTE_END = (byte) 0xdf;

	private static final byte U_3BYTE_BEGIN = (byte) 0xe0;

	private static final byte U_3BYTE_END = (byte) 0xef;

	private static final byte U_4BYTE_BEGIN = (byte) 0xf0;

	private static final byte U_4BYTE_END = (byte) 0xf7;

	private static final byte U_PAYLOAD_BEGIN = (byte) 0x80;

	private static final byte U_PAYLOAD_END = (byte) 0xbf;

	// ISO-8859-1 (Latin1)
	private static final byte LATIN_BEGIN = (byte) 0xa0;

	private static final byte LATIN_END = (byte) 0xff;

	private byte buf[] = new byte[8192];

	private int length;

	private int errorJIS;

	private int errorUTF8;

	private int errorLATIN1;

	private int errorSJIS;

	private int errorEUC;

	private InputStream is;

	private Vector<Integer> eucRemoveV = new Vector<Integer>();

	/**
	 * Constructor for Japanese encoding detector.
	 * 
	 * @param is
	 *            the target input stream
	 */
	public JapaneseEncodingDetector(InputStream is) {
		this.is = is;
	}

	private String toString(int ret) {
		switch (ret) {
		case J_UTF8:
			return (UTF_8);
		case J_JIS:
			return (JIS);
		case J_EUC:
			return (EUC);
		case LATIN1:
			return (ISO_8859_1);
		case J_SJIS:
		default:
			return (SJIS);
		}
	}

	/**
	 * Return input stream that includes content of the target input stream.
	 * Need to call detect method before calling this method.
	 * 
	 * @return input stream that includes content of the target input stream
	 */
	public InputStream getInputStream() {
		return (new ByteArrayInputStream(buf, 0, length));
	}

	/**
	 * Return length of the target input stream.
	 * 
	 * @return length of the target input stream
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Return byte array that includes content of the target input stream. Need
	 * to call detect method before calling this method.
	 * 
	 * @return byte array that includes content of the target input stream
	 */
	public byte[] getByteArray() {
		byte bytebuf[] = new byte[length];
		System.arraycopy(buf, 0, bytebuf, 0, length);
		return (bytebuf);
	}

	private void removeKisyuIzonEUC() {
		for (int i = eucRemoveV.size() - 1; i > -1; i--) {
			byte bytebuf[] = new byte[length - 2];
			int index = eucRemoveV.get(i).intValue();
			System.arraycopy(buf, 0, bytebuf, 0, index);
			System
					.arraycopy(buf, index + 2, bytebuf, index, length - index
							- 2);
			buf = bytebuf;
			// length = index;
			length = length - 2;
		}
	}

	private boolean isJIS(byte[] target, int length) {
		int index = 0;
		byte b;

		boolean inSingle = true;
		boolean inKana = false;
		boolean inKanji = false;
		boolean hasEscape = false;

		for (b = buf[index]; index < length; b = buf[index]) {
			if (b == JIS_ESCAPE && index + 2 < length) {
				byte c = buf[index + 1];
				byte d = buf[index + 2];
				if (c == JIS_SINGLE) {
					switch (d) {
					case JIS_B:
					case JIS_J:
						inSingle = true;
						inKana = false;
						inKanji = false;
						index += 3;
						hasEscape = true;
						break;
					case JIS_I:
						inKana = true;
						inSingle = false;
						inKanji = false;
						index += 3;
						hasEscape = true;
						break;
					default:
						errorJIS++;
						index++;
					}
				} else if (c == JIS_DOUBLE) {
					switch (d) {
					case JIS_AT:
					case JIS_B:
					case JIS_D:
						inKanji = true;
						inSingle = false;
						inKana = false;
						index += 3;
						hasEscape = true;
						break;
					default:
						errorJIS++;
						index++;
					}
				} else {
					errorJIS++;
					index++;
				}
			} else {
				if (inKanji && index + 1 < length && isPayloadJISkanji(b)
						&& isPayloadJISkanji(buf[index + 1])) {
					index += 2;
				} else if (inSingle && isASCII(b)) {
					index++;
				} else if (inKana && isPayloadJISkana(b)) {
					index++;
				} else {
					errorJIS++;
					index++;
				}
			}
		}

		// System.out.println("JIS : "+errorJIS+" "+hasEscape);

		return (errorJIS == 0 && hasEscape);
		// return true;
	}

	private boolean isUTF8(byte[] target, int length) {
		int index = 0;
		byte b;
		for (b = buf[index]; index < length; b = buf[index]) {
			if (isASCII(b)) {
				index++;
			} else if (is2byteUTF8(b) && index + 1 < length) {
				if (isPayloadUTF8(buf[index + 1])) {
					index += 2;
				} else {
					errorUTF8++;
					index++;
					// return (false);
				}
			} else if (is3byteUTF8(b) && index + 2 < length) {
				if (isPayloadUTF8(buf[index + 1])
						&& isPayloadUTF8(buf[index + 2])) {
					index += 3;
				} else {
					errorUTF8++;
					index++;
					// return (false);
				}
			} else if (is4byteUTF8(b) && index + 3 < length) {
				if (isPayloadUTF8(buf[index + 1])
						&& isPayloadUTF8(buf[index + 2])
						&& isPayloadUTF8(buf[index + 3])) {
					index += 4;
				} else {
					errorUTF8++;
					index++;
					// return (false);
				}
			} else {
				errorUTF8++;
				index++;
				// return (false);
			}
		}

		return (errorUTF8 == 0);
		// return true;
	}

	private boolean isLATIN1(byte[] target, int length) {
		int index = 0;
		byte b;
		for (b = buf[index]; index < length; b = buf[index]) {
			if (isASCII(b) || isLATIN1(b)) {
				index++;
			} else {
				index++;
				errorLATIN1++;
			}
		}

		return (errorLATIN1 == 0);
		// return true;
	}

	private boolean isSJIS(byte[] target, int length) {
		int index = 0;

		byte b;
		for (b = buf[index]; index < length; b = buf[index]) {
			if (isASCII(b) || isSJISkana(b)) {
				index++;
			} else if (isSJIS1st(b) && index + 1 < length) {
				if (isSJIS2nd(buf[index + 1])) {
					index += 2;
				} else {
					index++;
					errorSJIS++;
				}
			} else {
				index++;
				errorSJIS++;
			}
		}

		return (errorSJIS == 0);

	}

	private boolean isEUC(byte[] target, int length) {
		int index = 0;

		byte b;
		for (b = buf[index]; index < length; b = buf[index]) {
			if (isASCII(b)) {
				index++;
			} else if (isEUCkanji(b) && index + 1 < length) {
				if (isPayloadEUC(buf[index + 1])) {
					index += 2;
				} else {
					index++;

					// System.out.println("a");
					errorEUC++;
				}
			} else if (isEUCkana(b) && index + 1 < length) {
				if (isKanaPayloadEUC(buf[index + 1])) {
					index += 2;
				} else {
					index++;
					// System.out.println("b");
					errorEUC++;
				}
			} else if (isEUChojo(b) && index + 2 < length) {
				if (isPayloadEUC(buf[index + 1])
						&& isPayloadEUC(buf[index + 2])) {
					index += 3;
				} else {
					index++;
					// System.out.println("c");
					errorEUC++;
				}
			} else if (isEUCKisyu(b) && index + 1 < length) {
				if (isPayloadEUC(buf[index + 1])) {
					eucRemoveV.add(new Integer(index));
					index += 2;
				} else {
					index++;
					// System.out.println("d");
					errorEUC++;
				}
			} else {
				index++;
				// System.out.println("e:"+b+" "+buf[index]);
				errorEUC++;
			}
		}

		// System.out.println(errorEUC);

		return (errorEUC == 0);

	}

	/**
	 * Return detected Japanese encoding of the target input stream.
	 * 
	 * @return detected encoding
	 * @throws IOException
	 */
	public String detect() throws IOException {
		length = 0;
		errorJIS = 0;
		errorUTF8 = 0;
		errorLATIN1 = 0;
		errorSJIS = 0;
		errorEUC = 0;

		for (int len = is.read(buf, length, buf.length - length); len > 0;) {
			length += len;
			if (length == buf.length) {
				byte newBuf[] = new byte[length + 8192];
				System.arraycopy(buf, 0, newBuf, 0, length);
				buf = newBuf;
			}
			len = is.read(buf, length, buf.length - length);
		}
		// byte c, d, e;
		int ret = J_SJIS;

		// Check Kanji Character set SJIS/JIS/EUC
		// boolean isJis = false;
		@SuppressWarnings("unused")
		int nSJisError = 0;
		@SuppressWarnings("unused")
		int nEucError = 0;

		// int nSJis = 0;
		// int nEuc = 0;
		// int count = 0;
		//
		// int index = 0;
		// int tmp_length = length - 2;
		// System.out.println("AutoDetect: "+length);

		// TODO
		// for (c = buf[index++]; index < tmp_length; c = buf[index++]) {
		//
		// if ((c == JIS_ESCAPE)
		// && ((d = buf[index]) == JIS_DOUBLE)
		// && ((e = buf[index + 1]) == JIS_B)) {
		// isJis = true;
		// ret = J_JIS;
		// break;
		// }
		// }

		if (isJIS(buf, length)) {
			ret = J_JIS;
			return (toString(ret));
		}

		if (isUTF8(buf, length)) {
			ret = J_UTF8;
			return (toString(ret));
		}

		if (isSJIS(buf, length)) {
			ret = J_SJIS;
			return (toString(ret));
		} else if (isEUC(buf, length)) {
			ret = J_EUC;

			removeKisyuIzonEUC();

			return (toString(ret));
		} else if (isLATIN1(buf, length)) {
			ret = LATIN1;
			return (toString(ret));
		}

		// System.out.println("SJIS: " + errorSJIS + "(" + nSJisError + ") EUC:
		// "
		// + errorEUC + "(" + nEucError + ") UTF-8: " + errorUTF8
		// + " LATIN1: " + errorLATIN1 + " JIS: " + errorJIS);

		ret = J_UTF8;
		if (errorSJIS < 100 && errorSJIS < errorEUC && errorSJIS < errorUTF8
				&& errorSJIS < errorLATIN1 && errorSJIS < errorJIS) {
			ret = J_SJIS;
		} else if (errorEUC < 100 && errorEUC < errorSJIS
				&& errorEUC < errorUTF8 && errorEUC < errorLATIN1
				&& errorEUC < errorJIS) {

			removeKisyuIzonEUC();

			ret = J_EUC;
		} else if (errorLATIN1 < 100 && errorLATIN1 < errorSJIS
				&& errorLATIN1 < errorEUC && errorLATIN1 < errorUTF8
				&& errorLATIN1 < errorJIS) {
			ret = LATIN1;
		} else if (errorJIS < 100 && errorJIS < errorSJIS
				&& errorJIS < errorEUC && errorJIS < errorUTF8
				&& errorJIS < errorLATIN1) {
			ret = J_JIS;
		}
		// System.out.println("estimation: " + toString(ret));
		return (toString(ret));
	}

	private boolean isPayloadJISkanji(byte c) {
		return (JIS_D_PAYLOAD_BEGIN <= c && c <= JIS_D_PAYLOAD_END);
	}

	private boolean isPayloadJISkana(byte c) {
		return (JIS_KANA_PAYLOAD_BEGIN <= c && c <= JIS_KANA_PAYLOAD_END);
	}

	private boolean isEUCkana(byte c) {
		return (c == E_KANA_1ST);
	}

	private boolean isEUCkanji(byte c) {
		return (E_KANJI_BEGIN <= c && c <= E_KANJI_END);
	}

	private boolean isEUChojo(byte c) {
		return (c == E_HOJO_1ST);
	}

	private boolean isEUCKisyu(byte c) {
		return (c == E_KISYU_1ST);
	}

	@SuppressWarnings("unused")
	private boolean isEUC1st(byte c) {
		return (c == E_KANA_1ST || c == E_HOJO_1ST || (E_KANJI_BEGIN <= c && c <= E_KANJI_END));
	}

	private boolean isPayloadEUC(byte c) {
		return (E_PAYLOAD_BEGIN <= c && c <= E_PAYLOAD_END);
	}

	private boolean isKanaPayloadEUC(byte c) {
		return (E_KANA_BEGIN <= c && c <= E_KANA_END);
	}

	private boolean isSJIS1st(byte c) {
		return ((S1st1begin <= c && c <= S1st1end) || (S1st2begin <= c && c <= S1st2end));
	}

	private boolean isSJISkana(byte c) {
		return (Skanabegin <= c && c <= Skanaend);
	}

	private boolean isSJIS2nd(byte c) {
		return ((S2nd1begin <= c && c <= S2nd1end) || (S2nd2begin <= c && c <= S2nd2end));
	}

	private boolean isASCII(byte c) {
		return (ASCII_BEGIN <= c && c <= ASCII_END);
	}

	private boolean isLATIN1(byte c) {
		return (LATIN_BEGIN <= c && c <= LATIN_END);
	}

	private boolean is2byteUTF8(byte c) {
		return (U_2BYTE_BEGIN <= c && c <= U_2BYTE_END);
	}

	private boolean is3byteUTF8(byte c) {
		return (U_3BYTE_BEGIN <= c && c <= U_3BYTE_END);
	}

	private boolean is4byteUTF8(byte c) {
		return (U_4BYTE_BEGIN <= c && c <= U_4BYTE_END);
	}

	private boolean isPayloadUTF8(byte c) {
		return (U_PAYLOAD_BEGIN <= c && c <= U_PAYLOAD_END);
	}

	// for test
	// public static void main(String args[]) {
	// try {
	//			InputStream is = new FileInputStream("tmp/jed.txt"); //$NON-NLS-1$
	// JapaneseEncodingDetector JED = new JapaneseEncodingDetector(is);
	// System.out.println(JED.detect());
	// // JED.detect2();
	// } catch (Exception e2) {
	// e2.printStackTrace();
	// }
	// }

}
