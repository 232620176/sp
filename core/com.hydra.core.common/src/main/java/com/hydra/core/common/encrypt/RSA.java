package com.hydra.core.common.encrypt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RSA {
	public static String ALGORITHM = "RSA";
	public static String SIGN_ALGORITHMS = "SHA1WithRSA";// 摘要加密算法
	public static String CHAR_SET = "UTF-8";
	public static String PRIVATE_KEY = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCAF/P0eQF/d6viYe/uCtIXNzfb+hyS0SLE0ddHVjnoBZQRR1hCXqQF/FsgUkLBa0LRlEfE/w/sVZ6J9GjyQNwMFhHtVH53DOXTv14HZhgVyVi3gTyyU9V3ppKSNY97YPRRN90TmsBxPLtnD+MMxJT4IXReBBHpX16h//wQeQN+NOARpL9DvGF2FzCImPpusOiZjRmp8UbB6clUitb9eDTIdaXfiyGzY+xsd5DoeFynTqMtkctA2dZ+4zcpHW5eOANtLicgq+YmuYR+58iw+QN1Dljsbu7u3QD0LdpEpuCjQG71GBCz8PC2TyyQ0UdVe6R9UVC/yZ4jC5BwGurBFDktAgMBAAECggEAUjyOj8bcMlTHUljkK+Rgiy0VPLIvhAamlsvNtWvjU1W5gPpX7IxMK9efPXnyh7DwPhM/nHbQZz90wEbc1aLUYyIMMl1Lr+zk5HP4zzE5h0s0HOPo7ugppV6YvSuLyX8ue7IsyfhRUqHWAs3NTBYtObxotC9SvIxQ+hqZXo4CsxQguMywn0TCoeRi2tbqc/LbaJyRkxr4vDm/2gWNWsNOSea6A4lEe+M2J9IGfc7YnSFKjfilJXLWtbvfQI08F6BCdZJo1GKziDBrBsoBBMWPeyDrTwr/JzSM4IDD4+uwmnj52oEwuMiq6Zea+oK4UkKrDiD57xujSZN4beuLNHBz1QKBgQC3gWnIvZQbs2xbX/o8AvBGX7+nQP+Ws45ZtSz0D9SBWjQwPwAzP5OPMiHa71NDchHpTdQ/KPOY0J9y28ZIHBOYZUXK1E5NrGXHaOX1RWaoqJPqWlPwckHOz72gswEz9DuO1WFR3+AyIJuzTsl2LqvGXnZ15BlhBXwcvey5649vlwKBgQCysoaClJr5x+flnyIGSlRBtN8MQVsYKJPvXxU37l8kUaJ8zIsNkrGn9Lju0g6NpQ5wJeXqlUYo/YjTFBbv6FKV9pei//3Hh2o2hdA6KxqviaaxbRFo9kiId5PSeJzOceLFRUdL7Nau4rCIDF/Q+awNmR7Er6odjfVdesa7N5a12wKBgGJhv5UgDofLagyTKFWEPc19CANjlaP2IBt4RuGSmxu+gLxdCn5vV20uM3htVvaR8YhjcadrDS6gqtupeGSwqNDLet+Dc3exNd/zHVaiHqfNuX+rbdU7bIy0U7YUQlDYYBXz0HlUzZ4SXWeElf4Gh5GSk3AhGnoptqCyMnh7EiuBAoGAeyvs5VLzI5p0MGUoubdALm4ylCr0VEmb9pHOeVf4Anu2iVkfu8JSbFeVpR/q4h1UP18QBErSKmzfg8bELKcxTabETJkw3vhq8Jv5tC6D3Vz/JirspjGkO1RY8ukqBG6hhaJEC4R7Ud51GcGVxeBihJj3iVEf6/EuoDmBhAhD/KMCgYA4iF/PUi9ADYQ1OPKOHVKCyhnWdG1f9sFuq3/hIUJKsFSdGhhsVV7nqlrr+81rzaJoFZTTVj/7r175JXSzMNs8kQU1zVqxIxxX9ZktSH1XKZ+kKFB6EwViy6nNNdnc86SjiJpq2AHiMxG+MvZzyNeeedOY/l2d/RsJ4T3yZ2FHng==";
	public static String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgBfz9HkBf3er4mHv7grSFzc32/ocktEixNHXR1Y56AWUEUdYQl6kBfxbIFJCwWtC0ZRHxP8P7FWeifRo8kDcDBYR7VR+dwzl079eB2YYFclYt4E8slPVd6aSkjWPe2D0UTfdE5rAcTy7Zw/jDMSU+CF0XgQR6V9eof/8EHkDfjTgEaS/Q7xhdhcwiJj6brDomY0ZqfFGwenJVIrW/Xg0yHWl34shs2PsbHeQ6Hhcp06jLZHLQNnWfuM3KR1uXjgDbS4nIKvmJrmEfufIsPkDdQ5Y7G7u7t0A9C3aRKbgo0Bu9RgQs/Dwtk8skNFHVXukfVFQv8meIwuQcBrqwRQ5LQIDAQAB";
	
	/**
	 * 数据签名
	 * @param content
	 *			签名内容
	 * @param privateKey
	 *			私钥
	 * @return 返回签名数据
	 */
	public static String sign(String content, String privateKey) {
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
					Base64.decode(privateKey));
			KeyFactory keyf = KeyFactory.getInstance(ALGORITHM);
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);
			java.security.Signature signature = java.security.Signature
					.getInstance(SIGN_ALGORITHMS);
			signature.initSign(priKey);
			signature.update(content.getBytes(CHAR_SET));
			byte[] signed = signature.sign();
			return Base64.encode(signed);
		} catch (Exception e) {
			log.error("{}", e);
		}
		return null;
	}
	
	/**
	 * 签名验证
	 * @param content
	 * @param sign
	 * @param lakala_public_key
	 * @return
	 */
	public static boolean verify(String content, String sign,
			String lakala_public_key) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
			byte[] encodedKey = Base64.decode(lakala_public_key);
			PublicKey pubKey = keyFactory
					.generatePublic(new X509EncodedKeySpec(encodedKey));
			java.security.Signature signature = java.security.Signature
					.getInstance(SIGN_ALGORITHMS);
			signature.initVerify(pubKey);
			signature.update(content.getBytes(CHAR_SET));
			boolean bverify = signature.verify(Base64.decode(sign));
			return bverify;
		} catch (Exception e) {
			log.error("{}", e);
		}
		return false;
	}
	
	/**
	 * 通过公钥解密
	 * @param content待解密数据
	 * @param pk公钥
	 * @return 返回 解密后的数据
	 */
	protected static byte[] decryptByPublicKey(String content, PublicKey pk) {
		try {
			Cipher ch = Cipher.getInstance(ALGORITHM);
			ch.init(Cipher.DECRYPT_MODE, pk);
			InputStream ins = new ByteArrayInputStream(Base64.decode(content));
			ByteArrayOutputStream writer = new ByteArrayOutputStream();
			// rsa解密的字节大小最多是128，将需要解密的内容，按128位拆开解密
			byte[] buf = new byte[128];
			int bufl;
			while ((bufl = ins.read(buf)) != -1) {
				byte[] block = null;
				if (buf.length == bufl) {
					block = buf;
				} else {
					block = new byte[bufl];
					for (int i = 0; i < bufl; i++) {
						block[i] = buf[i];
					}
				}
				writer.write(ch.doFinal(block));
			}
			return writer.toByteArray();
		} catch (Exception e) {
			log.error("{}", e);
		}
		return null;
	}
	
	/**
	 * 通过私钥加密
	 * @param content
	 * @param pk
	 * @return,加密数据，未进行base64进行加密
	 */
	protected static byte[] encryptByPrivateKey(String content, PrivateKey pk) {
		try {
			Cipher ch = Cipher.getInstance(ALGORITHM);
			ch.init(Cipher.ENCRYPT_MODE, pk);
			return ch.doFinal(content.getBytes(CHAR_SET));
		} catch (Exception e) {
			log.error("{}", e);
		}
		return null;
	}
	
	/**
	* 得到私钥对象
	* @param key 密钥字符串（经过base64编码的秘钥字节）
	* @throws Exception
	*/
	public static PrivateKey getPrivateKey(String privateKey) {
		try {
			byte[] keyBytes;
			keyBytes = Base64.decode(privateKey);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
			PrivateKey privatekey = keyFactory.generatePrivate(keySpec);
			return privatekey;
		}catch(Exception e){
			log.error("{}", e);
		}
		return null;
	}
	
	/**
	* 获取公钥对象
	* @param key 密钥字符串（经过base64编码秘钥字节）
	* @throws Exception
	*/
	public static PublicKey getPublicKey(String publicKey) {
		try {
			byte[] keyBytes;
			keyBytes = Base64.decode(publicKey);
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
			PublicKey publickey = keyFactory.generatePublic(keySpec);
			return publickey;
		} catch (Exception e) {
			log.error("{}", e);
		}
		return null;
	}
	
	/**
	 * 解密数据，接收端接收到数据直接解密
	 * @param content
	 * @param key
	 * @return
	 */
	public static String decrypt(String content, String pubKey) {
		if (null == pubKey || "".equals(pubKey)) {
			log.debug("RSAUtil： decrypt方法中key=" + pubKey);
			return null;
		}
		PublicKey pk = getPublicKey(pubKey);
		byte[] data = decryptByPublicKey(content, pk);
		String res = null;
		try {
			res = new String(data, CHAR_SET);
		} catch (UnsupportedEncodingException e) {
			log.error("{}", e);
		}
		return res;
	}
	
	/**
	 * 对内容进行加密
	 * @param content
	 * @param key私钥
	 * @return
	 */
	public static String encrypt(String content, String priKey) {
		PrivateKey pk = getPrivateKey(priKey);
		byte[] data = encryptByPrivateKey(content, pk);
		String res = null;
		try {
			res = Base64.encode(data);
		} catch (Exception e) {
			log.error("{}", e);
		}
		return res;
	}
}
