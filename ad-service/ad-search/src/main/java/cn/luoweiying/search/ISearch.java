package cn.luoweiying.search;

import cn.luoweiying.search.vo.SearchRequest;
import cn.luoweiying.search.vo.SearchResponse;

//广告位检索请求
public interface ISearch {
    public SearchResponse fetchAds(SearchRequest searchRequest);
}
