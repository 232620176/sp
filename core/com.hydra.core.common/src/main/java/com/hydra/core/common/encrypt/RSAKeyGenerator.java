package com.hydra.core.common.encrypt;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import com.hydra.core.common.util.MapUtil;

@Slf4j
public class RSAKeyGenerator {
	public static PrivateKey pkey;
	public static PublicKey pubkey;
	public final static String RSA_ALGORITHM = "RSA";
	
	public static Map<String, Object> genKey() throws Exception {
		KeyPairGenerator kpg = KeyPairGenerator.getInstance(RSA_ALGORITHM);
		kpg.initialize(2048);
		KeyPair kep = kpg.generateKeyPair();
		Provider p = kpg.getProvider();
		log.debug(p.getName());
		pkey = kep.getPrivate();
		pubkey = kep.getPublic();
		Map<String, Object> param = MapUtil.getMap();
		param.put("PublicKey", new String(Base64.encode(pubkey.getEncoded())));
		param.put("PrivateKey", new String(Base64.encode(pkey.getEncoded())));
		return param;
	}
}
