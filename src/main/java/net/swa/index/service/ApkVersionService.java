package net.swa.index.service;

import java.util.Map;

import net.swa.index.beans.entity.ApkVersion;
import net.swa.util.JsonResult;

public interface ApkVersionService
{

    JsonResult<ApkVersion> queryPage(String name , int currentPage , int pageSize , String orderBy , String orderType);

    ApkVersion queryLastVersion();

    ApkVersion queryById(Long id);

    Map<String, Object> add(ApkVersion model);

    Map<String, Object> update(ApkVersion model);

    Map<String, Object> save(ApkVersion model);

}
