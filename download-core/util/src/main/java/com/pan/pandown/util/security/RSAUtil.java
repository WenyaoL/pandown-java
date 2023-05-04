package com.pan.pandown.util.security;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class RSAUtil {
    // 密钥算法
    private static final String KEY_ALGORITHM = "RSA";

    /**
     * 生成密钥对
     */
    public static Map initkeyPair() throws Exception {

        // 实例化密钥对生成器
        KeyPairGenerator kg = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        // 初始化密钥对生成器
        kg.initialize(512);
        // 生成密钥对
        KeyPair kp = kg.generateKeyPair();

        PublicKey publicKey = kp.getPublic();
        PrivateKey privateKey = kp.getPrivate();
        System.out.println(publicKey);

        //将公钥和私钥转换为base64编码便于存储和返回至前端
        byte[] publicKeyByte = publicKey.getEncoded();

        byte[] privateKeyByte = privateKey.getEncoded();
        String publicKeyStr = Base64.encodeBase64String(publicKeyByte);
        String privateKeyStr = Base64.encodeBase64String(privateKeyByte);
        Map map = new HashMap();
        map.put("publicKeyStr", publicKeyStr);
        map.put("privateKeyStr", privateKeyStr);
        return map;
    }

    /**
     * 解密数据
     *
     * @param passwordBase64 待解密数据
     * @param privateKeyStr 私密钥
     * @return 解密后的数据
     * */
    public static String decrypt(String passwordBase64, String privateKeyStr) throws GeneralSecurityException {

        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);

        //将Base64编码的私钥转为Private类型
        byte[] privateKeyByte = Base64.decodeBase64(privateKeyStr);
        KeyFactory kf = KeyFactory.getInstance(KEY_ALGORITHM); // or "EC" or whatever
        PrivateKey privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(privateKeyByte));

        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        //将base64编码的加密信息转为byte[]数据
        byte[] passwordByte = Base64.decodeBase64(passwordBase64);
        //获取解密后的byte[]数据
        byte[] resByte = cipher.doFinal(passwordByte);
        //将byte[]转为字符串，即为解密后的信息
        String resStr = new String(resByte, StandardCharsets.UTF_8);
        return resStr;
    }

    public static PrivateKey base64strToPrivateKey(KeySpec keySpec) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //将Base64编码的私钥转为Private类型
        KeyFactory kf = KeyFactory.getInstance(KEY_ALGORITHM); // or "EC" or whatever
        PrivateKey privateKey = kf.generatePrivate(keySpec);
        return privateKey;
    }

    public static PrivateKey base64strToPrivateKey(String privateKeyStr) throws NoSuchAlgorithmException, InvalidKeySpecException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return base64strToPrivateKey(privateKeyStr,PKCS8EncodedKeySpec.class);
    }

    public static PrivateKey base64strToPrivateKey(String privateKeyStr,Class cls) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        //将Base64编码的私钥转为Private类型
        byte[] privateKeyByte = Base64.decodeBase64(privateKeyStr);
        KeySpec keySpec = (KeySpec) cls.getConstructor(byte[].class).newInstance(privateKeyByte);
        return base64strToPrivateKey(keySpec);
    }

    public static PublicKey base64strToPublicKey(KeySpec keySpec) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory kf = KeyFactory.getInstance(KEY_ALGORITHM); // or "EC" or whatever
        PublicKey publicKey = kf.generatePublic(keySpec);
        return publicKey;
    }

    public static PublicKey base64strToPublicKey(String publicKeyStr) throws NoSuchAlgorithmException, InvalidKeySpecException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        //将Base64编码的私钥转为Private类型
        return base64strToPublicKey(publicKeyStr, X509EncodedKeySpec.class);
    }

    public static PublicKey base64strToPublicKey(String publicKeyStr,Class cls) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        //将Base64编码的私钥转为Private类型
        byte[] publicKeyByte = Base64.decodeBase64(publicKeyStr);
        KeySpec keySpec = (KeySpec) cls.getConstructor(byte[].class).newInstance(publicKeyByte);
        return base64strToPublicKey(keySpec);
    }

    public static void main(String[] args) throws Exception {
        Map pair = RSAUtil.initkeyPair();
        System.out.println("publicKeyStr:"+pair.get("publicKeyStr"));
        System.out.println("privateKeyStr:"+pair.get("privateKeyStr"));
    }
}
