package com.yun.note.service;

import com.yun.note.exception.ValidateParamException;

public interface IUserTokenService {

    /**
     * 验证userToken
     * @param token
     * @return  用户的编号
     * @throws ValidateParamException
     */
    int validDator(String token) throws ValidateParamException;
}
