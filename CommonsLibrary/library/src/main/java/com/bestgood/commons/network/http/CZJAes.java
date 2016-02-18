/**
 * LICENSE AND TRADEMARK NOTICES
 * <p/>
 * Except where noted, sample source code written by Motorola Mobility Inc. and
 * provided to you is licensed as described below.
 * <p/>
 * Copyright (c) 2012, Motorola, Inc.
 * All  rights reserved except as otherwise explicitly indicated.
 * <p/>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * <p/>
 * - Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * <p/>
 * - Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * <p/>
 * - Neither the name of Motorola, Inc. nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 * <p/>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER  CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * <p/>
 * Other source code displayed may be licensed under Apache License, Version
 * 2.
 * <p/>
 * Copyright ¬© 2012, Android Open Source Project. All rights reserved unless
 * otherwise explicitly indicated.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy
 * of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0.
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

// Please refer to the accompanying article at 
// http://developer.motorola.com/docs/using_the_advanced_encryption_standard_in_android/

package com.bestgood.commons.network.http;

// A tutorial guide to using AES encryption in Android
// First we generate a 256 bit secret key; then we use that secret key to AES encrypt a plaintext message.
// Finally we decrypt the ciphertext to get our original message back.
// We don't keep a copy of the secret key - we generate the secret key whenever it is needed, 
// so we must remember all the parameters needed to generate it -
// the salt, the IV, the human-friendly passphrase, all the algorithms and parameters to those algorithms.
// Peter van der Linden, April 15 2012


import com.bestgood.commons.util.log.Logger;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;


public class CZJAes {
    private final String KEY_GENERATION_ALG = "PBKDF2WithHmacSHA1";

    private final int HASH_ITERATIONS = 10000;
    private final int KEY_LENGTH = 128;

//	private char[] humanPassphrase = { 'P', 'e', 'r', ' ', 'v', 'a', 'l', 'l',
//			'u', 'm', ' ', 'd', 'u', 'c', 'e', 's', ' ', 'L', 'a', 'b', 'a',
//			'n', 't' };

    private byte[] salt = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0xA, 0xB, 0xC, 0xD,
            0xE, 0xF}; // must save this for next time we want the key

    private PBEKeySpec myKeyspec = null;
    //	private final String CIPHERMODEPADDING = "AES/CBC/PKCS7Padding";
    private final String CIPHERMODEPADDING = "AES/CBC/PKCS5Padding";

    private SecretKeyFactory keyfactory = null;
    private SecretKey sk = null;
    private SecretKeySpec skforAES = null;
    private byte[] iv = {0xA, 1, 0xB, 5, 4, 0xF, 7, 9, 0x17, 3, 1, 6, 8, 0xC, 0xD, 91};


    private IvParameterSpec IV;

    public CZJAes(String key) {

        this.setKey(key);
    }

    public void setKey(String key) {
        try {
            char[] chHumanPassphrase = key.toCharArray();
            this.myKeyspec = new PBEKeySpec(chHumanPassphrase, salt, HASH_ITERATIONS, KEY_LENGTH);
            this.keyfactory = SecretKeyFactory.getInstance(KEY_GENERATION_ALG);
            this.sk = keyfactory.generateSecret(myKeyspec);

        } catch (NoSuchAlgorithmException e) {
            Logger.e(e, "");
        } catch (InvalidKeySpecException e) {
            Logger.e(e, "");
        }
        // This is our secret key. We could just save this to a file instead of regenerating it each time it is needed.
        // But that file cannot be on the device (too insecure).
        // It could be secure if we kept it on a server accessible through https.
        byte[] skAsByteArray = sk.getEncoded();
        // Log.d("", "skAsByteArray=" + skAsByteArray.length + "," + Base64Encoder.encode(skAsByteArray));
        this.skforAES = new SecretKeySpec(skAsByteArray, "AES");

        IV = new IvParameterSpec(iv);
    }

    public String encrypt(String plaintext, String charset) {

        String base64_ciphertext = null;
        try {
            byte[] plaintextBytes = plaintext.getBytes(charset);
            byte[] ciphertextBytes = encrypt(CIPHERMODEPADDING, skforAES, IV, plaintextBytes);

            base64_ciphertext = parseByte2HexStr(ciphertextBytes);
        } catch (Exception e) {
            Logger.e(e, "");
        }

        return base64_ciphertext;
    }


    public String decrypt(String ciphertext_base64, String charset) {
        String decrypted = null;

        try {
            byte[] ciphertext_base64_bytes = parseHexStr2Byte(ciphertext_base64);
            byte[] plaintext_bytes = decrypt(CIPHERMODEPADDING, skforAES, IV, ciphertext_base64_bytes);

            if (plaintext_bytes != null) {
                decrypted = new String(plaintext_bytes, charset);
            }
        } catch (UnsupportedEncodingException e) {
            Logger.e(e, "");
        }
        return decrypted;
    }

    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return String
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return byte[]
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    // Use this method if you want to add the padding manually
    // AES deals with messages in blocks of 16 bytes.
    // This method looks at the length of the message, and adds bytes at the end
    // so that the entire message is a multiple of 16 bytes.
    // the padding is a series of bytes, each set to the total bytes added (a
    // number in range 1..16).
    private byte[] addPadding(byte[] plain) {
        byte plainpad[] = null;
        int shortage = 16 - (plain.length % 16);
        // if already an exact multiple of 16, need to add another block of 16
        // bytes
        if (shortage == 0) shortage = 16;

        // reallocate array bigger to be exact multiple, adding shortage bits.
        plainpad = new byte[plain.length + shortage];
        for (int i = 0; i < plain.length; i++) {
            plainpad[i] = plain[i];
        }
        for (int i = plain.length; i < plain.length + shortage; i++) {
            plainpad[i] = (byte) shortage;
        }
        return plainpad;
    }

    // Use this method if you want to remove the padding manually
    // This method removes the padding bytes
    private byte[] dropPadding(byte[] plainpad) {
        byte plain[] = null;
        int drop = plainpad[plainpad.length - 1]; // last byte gives number of
        // bytes to drop

        // reallocate array smaller, dropping the pad bytes.
        plain = new byte[plainpad.length - drop];
        for (int i = 0; i < plain.length; i++) {
            plain[i] = plainpad[i];
            plainpad[i] = 0; // don't keep a copy of the decrypt
        }
        return plain;
    }

    private byte[] encrypt(String cmp, SecretKey sk, IvParameterSpec IV, byte[] msg) {
        try {
            Cipher c = Cipher.getInstance(cmp);
            c.init(Cipher.ENCRYPT_MODE, sk, IV);
            return c.doFinal(msg);
        } catch (NoSuchAlgorithmException e) {
            Logger.e(e, "");
        } catch (NoSuchPaddingException e) {
            Logger.e(e, "");
        } catch (InvalidKeyException e) {
            Logger.e(e, "");
        } catch (InvalidAlgorithmParameterException e) {
            Logger.e(e, "");
        } catch (IllegalBlockSizeException e) {
            Logger.e(e, "");
        } catch (BadPaddingException e) {
            Logger.e(e, "");
        }
        return null;
    }

    private byte[] decrypt(String cmp, SecretKey sk, IvParameterSpec IV, byte[] ciphertext) {
        try {
            Cipher c = Cipher.getInstance(cmp);
            c.init(Cipher.DECRYPT_MODE, sk, IV);
            return c.doFinal(ciphertext);
        } catch (NoSuchAlgorithmException e) {
            Logger.e(e, "");
        } catch (NoSuchPaddingException e) {
            Logger.e(e, "");
        } catch (InvalidKeyException e) {
            Logger.e(e, "");
        } catch (InvalidAlgorithmParameterException e) {
            Logger.e(e, "");
        } catch (IllegalBlockSizeException e) {
            Logger.e(e, "");
        } catch (BadPaddingException e) {
            Logger.e(e, "");
        }
        return null;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub

//		String data = "点击违章信息，扩展显示处理（代缴）与分享按钮";
//		DE5A923EA3CC0684235E4BC8F05E64AFEBE05D4AB0C1B12267A2A2BB9B47D50C8F722FD64443CF4A1B630943F89B8CAD0AAD55E1FD068F4AFDF8F780235D9660A6A9CCA0DE8DA4ADBAE2F9CEAA4DF43D

//		String data = "hellow world!";
//		08EFBE4C3EE4A8DF58F8C80C59F4F535

        String data = "1234567890abcdefg01234567890ABCDEFG";
//		A772506167048CED9AFFDA2FEF54E21F5AEA09D1BA8C45E9305ADE37748571FD05CFF9409F0728A1FB7367D38DEE5A83

        String key = "18610557868";
        CZJAes aes = new CZJAes(key);
        String cryptStr = aes.encrypt(data, "utf-8");
        System.out.println(cryptStr);

        String plainText = aes.decrypt(cryptStr, "utf-8");
        System.out.println(plainText);
    }

}