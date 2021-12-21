package cn.com.changan.baselib.utils;

import android.content.Context;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import okhttp3.Call;

/**
 * @author Bobi Zhou
 * @version 1.0
 * @description RSA公钥/私钥/签名工具包
 * @date 2015年11月6日 上午11:28:42
 */
public class RSAUtils {
	/** */
	/**
	 * 加密算法RSA
	 */
	public static final String KEY_ALGORITHM = "RSA";

	//	public static final  String KEY_ALGORITHM_PADDING="RSA/ECB/PKCS1Padding";
	public static final String KEY_ALGORITHM_PADDING = "RSA/None/PKCS1Padding";
	public static final String KEY_ALGORITHM_PADDING_SAFE = "RSA/ECB/OAEPWithSHA256AndMGF1Padding";
	public static final int RSA_PUBLIC_KEY_FAIL_CODE=-1000;

	/** */
	/**
	 * 签名算法
	 */
	public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

	/** */
	/**
	 * 获取公钥的key
	 */
	private static final String PUBLIC_KEY = "RSAPublicKey";

	/** */
	/**
	 * 获取私钥的key
	 */
	private static final String PRIVATE_KEY = "RSAPrivateKey";

	/** */
	/**
	 * RAS网络回调
	 */
	public interface RSACallBackResult {
		void onFailure(Call call, String exception);

		void onResponse(String pubKey);
	}
	private static String TAG = RSAUtils.class.getSimpleName();

	public static final String LOCAL="USE_LOCAL_PUB_KEY";

	/** */
	/**
	 * <p>
	 * 生成密钥对(公钥和私钥)
	 * </p>
	 *
	 * @param length 密钥长度
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> genKeyPair(int length) throws Exception {
		KeyPairGenerator keyPairGen = KeyPairGenerator
				.getInstance(KEY_ALGORITHM);
		keyPairGen.initialize(1024);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		Map<String, Object> keyMap = new HashMap<String, Object>(2);
		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);
		return keyMap;
	}

	/** */
	/**
	 * <p>
	 * 用私钥对信息生成数字签名
	 * </p>
	 *
	 * @param data
	 *            已加密数据
	 * @param privateKey
	 *            私钥
	 *
	 * @return
	 * @throws Exception
	 */
//	public static String sign(byte[] data, byte[] privateKey) throws Exception {
//		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
//		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
//		PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
//		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
//		signature.initSign(privateK);
//		signature.update(data);
//		return Encodes.encodeBase64(signature.sign());
//	}

	/** */
	/**
	 * <p>
	 * 校验数字签名
	 * </p>
	 *
	 * @param data
	 *            已加密数据
	 * @param publicKey
	 *            公钥
	 * @param sign
	 *            数字签名
	 *
	 * @return
	 * @throws Exception
	 *
	 */
//	public static boolean verify(byte[] data, byte[] publicKey, String sign)
//			throws Exception {
//		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
//		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
//		PublicKey publicK = keyFactory.generatePublic(keySpec);
//		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
//		signature.initVerify(publicK);
//		signature.update(data);
//		return signature.verify(Encodes.decodeBase64(sign));
//	}

	/** */
	/**
	 * <P>
	 * 私钥解密
	 * </p>
	 *
	 * @param encryptedData 已加密数据
	 * @param privateKey    私钥
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPrivateKey(byte[] encryptedData,
											 byte[] privateKey) throws Exception {
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_PADDING);
		cipher.init(Cipher.DECRYPT_MODE, privateK);
		int inputLen = encryptedData.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;

		final int MAX_DECRYPT_BLOCK = getKeySize(privateK) / 8;

		// 对数据分段解密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
				cache = cipher
						.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
			} else {
				cache = cipher
						.doFinal(encryptedData, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_DECRYPT_BLOCK;
		}
		byte[] decryptedData = out.toByteArray();
		out.close();
		return decryptedData;
	}

	/** */
	/**
	 * <p>
	 * 公钥解密
	 * </p>
	 *
	 * @param encryptedData 已加密数据
	 * @param publicKey     公钥
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPublicKey(byte[] encryptedData,
											byte[] publicKey) throws Exception {
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicK = keyFactory.generatePublic(x509KeySpec);
		Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_PADDING);
		cipher.init(Cipher.DECRYPT_MODE, publicK);
		int inputLen = encryptedData.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		final int MAX_DECRYPT_BLOCK = getKeySize(publicK) / 8;
		// 对数据分段解密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
				cache = cipher
						.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
			} else {
				cache = cipher
						.doFinal(encryptedData, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_DECRYPT_BLOCK;
		}
		byte[] decryptedData = out.toByteArray();
		out.close();
		return decryptedData;
	}

	/** */
	/**
	 * <p>
	 * 公钥加密
	 * </p>
	 *
	 * @param data      源数据
	 * @param publicKey 公钥
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByPublicKey(byte[] data, byte[] publicKey)
			throws Exception {
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicK = keyFactory.generatePublic(x509KeySpec);

		final int MAX_ENCRYPT_BLOCK = getKeySize(publicK) / 8 - 11;

		// 对数据加密
		Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_PADDING);
		cipher.init(Cipher.ENCRYPT_MODE, publicK);

		int inputLen = data.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段加密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
				cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(data, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_ENCRYPT_BLOCK;
		}
		byte[] encryptedData = out.toByteArray();
		out.close();
		return encryptedData;
	}

	/** */
	/**
	 * <p>
	 * 私钥加密
	 * </p>
	 *
	 * @param data       源数据
	 * @param privateKey 私钥
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByPrivateKey(byte[] data, byte[] privateKey)
			throws Exception {
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_PADDING);
		cipher.init(Cipher.ENCRYPT_MODE, privateK);
		int inputLen = data.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		final int MAX_ENCRYPT_BLOCK = getKeySize(privateK) / 8 - 11;
		// 对数据分段加密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
				cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(data, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_ENCRYPT_BLOCK;
		}
		byte[] encryptedData = out.toByteArray();
		out.close();
		return encryptedData;
	}

	/** */
	/**
	 * <p>
	 * 获取私钥
	 * </p>
	 *
	 * @param keyMap 密钥对
	 * @return
	 * @throws Exception
	 */
	public static byte[] getPrivateKey(Map<String, Object> keyMap)
			throws Exception {
		Key key = (Key) keyMap.get(PRIVATE_KEY);
		return key.getEncoded();
	}

	/** */
	/**
	 * <p>
	 * 获取公钥
	 * </p>
	 *
	 * @param keyMap 密钥对
	 * @return
	 * @throws Exception
	 */
	public static byte[] getPublicKey(Map<String, Object> keyMap)
			throws Exception {
		Key key = (Key) keyMap.get(PUBLIC_KEY);
		return key.getEncoded();
	}

	/**
	 * 获取密钥长度
	 *
	 * @param key 密钥
	 * @return
	 * @throws Exception
	 */
	private static int getKeySize(Key key) throws Exception {
		String algorithm = key.getAlgorithm(); // 获取算法
		KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
		if ("RSA".equals(algorithm)) { // 如果是RSA加密
			if (key instanceof RSAPublicKey) {
				RSAPublicKeySpec keySpec = (RSAPublicKeySpec) keyFactory
						.getKeySpec(key, RSAPublicKeySpec.class);
				return keySpec.getModulus().toString(2).length();
			} else if (key instanceof RSAPrivateKey) {
				RSAPrivateKeySpec keySpec = (RSAPrivateKeySpec) keyFactory
						.getKeySpec(key, RSAPrivateKeySpec.class);
				return keySpec.getModulus().toString(2).length();
			}
		}
		return 0;
	}


	/**
	 * 新版公钥加密(RSA填充方式变化)
	 *
	 * @param data      源数据
	 * @param publicKey 公钥
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptSafeByPublicKey(byte[] data, byte[] publicKey)
			throws Exception {
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicK = keyFactory.generatePublic(x509KeySpec);

		final int MAX_ENCRYPT_BLOCK = getKeySize(publicK) / 8 - 11;

		// 对数据加密
		Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_PADDING_SAFE);
		cipher.init(Cipher.ENCRYPT_MODE, publicK);

		int inputLen = data.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段加密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
				cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(data, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_ENCRYPT_BLOCK;
		}
		byte[] encryptedData = out.toByteArray();
		out.close();
		return encryptedData;
	}

	//判断本地是否有动态公钥
	public static boolean isEmptySecurityKey(Context context){
		return SharedPreferencesUtil.instance(context).getString("public_key","").isEmpty();
	}

	//替换\r\n
	public static String replaceAllSlash(String content){
		return content.replaceAll("\r|\n","");
	}

	//通过String公钥获取加密字符串
	public static String encryptSafeByPublicKeyByString(String strData,String strPublicKey){
		if(strPublicKey.equals(LOCAL)){
			//使用本地KEY
			strPublicKey=SharedPreferencesUtil.instance(ContextProvider.getContext())
					.getString("public_key","");
		}
		byte[] publicKey = Base64.decode(strPublicKey, Base64.CRLF);
		//byte[] data =Base64.decode(strData, Base64.CRLF);
		String str = null;
		try {
			str = Base64.encodeToString(encryptSafeByPublicKey(strData.getBytes(), publicKey), Base64.CRLF);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return replaceAllSlash(str);
	}

	//通过String公钥获取加密字符串 回传byte
	public static byte[] encryptSafeByPublicKeyByByte(String strData,String strPublicKey){
		if(strPublicKey.equals(LOCAL)){
			//使用本地KEY
			strPublicKey=SharedPreferencesUtil.instance(ContextProvider.getContext())
					.getString("public_key","");
		}
		byte[] publicKey = Base64.decode(strPublicKey, Base64.CRLF);
		byte[] date=null;
		try {
			date=encryptSafeByPublicKey(strData.getBytes(), publicKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}


	//通过String公钥获取解密密字符串
	public static String decryptSafeByPublicKeyByString(String data,String strPublicKey){
		if(strPublicKey.equals(LOCAL)){
			//使用本地KEY
			strPublicKey=SharedPreferencesUtil.instance(ContextProvider.getContext())
					.getString("public_key","");
		}
		byte[] publicKey = Base64.decode(strPublicKey, Base64.CRLF);
		String str = null;
		try {
			str =  new String(decryptSafeByPublicKey(Base64Utils.decode(data), publicKey));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return replaceAllSlash(str);
	}

	/** */
	/**
	 * <p>
	 * 新版公钥解密(RSA填充方式变化)
	 * </p>
	 *
	 * @param encryptedData 已加密数据
	 * @param publicKey     公钥
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptSafeByPublicKey(byte[] encryptedData,
												byte[] publicKey) throws Exception {
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicK = keyFactory.generatePublic(x509KeySpec);
		Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_PADDING_SAFE);
		cipher.init(Cipher.DECRYPT_MODE, publicK);
		int inputLen = encryptedData.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		final int MAX_DECRYPT_BLOCK = getKeySize(publicK) / 8;
		// 对数据分段解密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
				cache = cipher
						.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
			} else {
				cache = cipher
						.doFinal(encryptedData, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_DECRYPT_BLOCK;
		}
		byte[] decryptedData = out.toByteArray();
		out.close();
		return decryptedData;
	}

}
