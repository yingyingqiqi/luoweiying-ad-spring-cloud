package cn.luoweiying.service;

import cn.luoweiying.exception.AdException;
import cn.luoweiying.vo.CreativeRequest;
import cn.luoweiying.vo.CreativeResponse;

public interface ICreativeService {
    CreativeResponse createCreative(CreativeRequest request) throws AdException;
}
