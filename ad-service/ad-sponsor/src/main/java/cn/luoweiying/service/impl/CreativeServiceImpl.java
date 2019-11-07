package cn.luoweiying.service.impl;

import cn.luoweiying.exception.AdException;
import cn.luoweiying.vo.CodeMsg;
import cn.luoweiying.dao.CreativeRepository;
import cn.luoweiying.entity.Creative;
import cn.luoweiying.service.ICreativeService;
import cn.luoweiying.vo.CreativeRequest;
import cn.luoweiying.vo.CreativeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreativeServiceImpl implements ICreativeService {
    private final CreativeRepository creativeRepository;
@Autowired
    public CreativeServiceImpl(CreativeRepository creativeRepository) {
        this.creativeRepository = creativeRepository;
    }


    @Override
    public CreativeResponse createCreative(CreativeRequest request) throws AdException {
        if (!request.validate()) {
            throw new AdException(CodeMsg.REQUEST_PARAM_ERROR);
        }
        Creative newcreative = creativeRepository.save(request.convertToEntity());
        return new CreativeResponse(newcreative.getName(), newcreative.getId());
    }
}
