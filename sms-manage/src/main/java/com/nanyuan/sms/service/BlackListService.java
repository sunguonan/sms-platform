package com.nanyuan.sms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nanyuan.sms.entity.BlackListEntity;
import com.ydl.base.R;
import org.springframework.web.multipart.MultipartFile;

/**
 * 黑名单
 *
 * @author IT李老师
 */
public interface BlackListService extends IService<BlackListEntity> {

    R<Boolean> upload(MultipartFile file);
}
