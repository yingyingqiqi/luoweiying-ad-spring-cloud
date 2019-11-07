package cn.luoweiying.service;

import cn.luoweiying.exception.AdException;
import cn.luoweiying.vo.CreateUserRequest;
import cn.luoweiying.vo.CreateUserResponse;

public interface IUserService {
    CreateUserResponse createUser(CreateUserRequest request) throws AdException;
}

