package cn.luoweiying.service.impl;

import cn.luoweiying.dao.AdUserRepository;
import cn.luoweiying.entity.AdUser;
import cn.luoweiying.exception.AdException;
import cn.luoweiying.service.IUserService;
import cn.luoweiying.utils.CommonUtils;
import cn.luoweiying.vo.CodeMsg;
import cn.luoweiying.vo.CreateUserRequest;
import cn.luoweiying.vo.CreateUserResponse;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Service
public class UserServiceImpl implements IUserService {


    private final AdUserRepository userRepository;
    @Autowired
    public UserServiceImpl(AdUserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    @Transactional
    public CreateUserResponse createUser(CreateUserRequest request) throws AdException {
        if (!request.validate()) {
            throw new AdException(CodeMsg.REQUEST_PARAM_ERROR);
        }

        AdUser oldUser = userRepository.findByUsername(request.getUsername());
        System.err.println(JSON.toJSONString(oldUser)+"根据用户名查询");
        if (oldUser != null) {
            throw new AdException(CodeMsg.SAME_NAME_ERROR);
        }

        AdUser newUser = userRepository.save(new AdUser(request.getUsername(),
                CommonUtils.inputPassToDbPass(request.getUsername(),new Date().toString())));

        return new CreateUserResponse(
                newUser.getId(), newUser.getUsername(), newUser.getToken(),
                newUser.getCreateTime(), newUser.getUpdateTime()
        );
    }
}
