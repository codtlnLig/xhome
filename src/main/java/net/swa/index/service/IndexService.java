
package net.swa.index.service;

import java.util.Map;
public interface IndexService
{

    Map<String, Object> login(String email , String mobi , String password , String mac);

    Map<String, Object> saveRegister(String mobi,String code , String email , String nickname , String password , String mac);

    Map<String, Object> saveCode(String mobi , String mac);

    Map<String, Object> updatePassword(Long uid , String password, String oldpassword);

    Map<String, Object> updateNick(Long uid , String nickname);

    Map<String, Object> updatePhoto(Long uid, String imgUrl);

 
}
