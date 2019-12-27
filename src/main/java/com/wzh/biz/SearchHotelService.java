package com.wzh.biz;

import com.wzh.util.ItripHotelVO;
import com.wzh.util.Page;
import com.wzh.util.SearchHotelVO;

import java.util.List;

/*solr搜索酒店的Service*/
public interface SearchHotelService {
    /***
     * 搜索旅馆
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Page<ItripHotelVO> searchItripHotelPage(SearchHotelVO vo,Integer pageNo,Integer pageSize)throws Exception;
    /***
     * 根据热门城市查询酒店
     * @param count
     * @return
     */
    public List<ItripHotelVO> searchItripHotelListByHotCity(Integer cityId,Integer count)throws Exception;
}
