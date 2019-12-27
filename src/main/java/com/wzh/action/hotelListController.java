package com.wzh.action;

import com.wzh.biz.SearchHotelService;
import com.wzh.po.Dto;
import com.wzh.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*查询酒店的controller*/
@RestController
/*@Api(value = "API", basePath = "/http://api.itrap.com/api")*/
@RequestMapping(value = "/api/hotellist")
public class hotelListController {
    @Autowired
    private SearchHotelService searchHotelService;

    public SearchHotelService getSearchHotelService() {
        return searchHotelService;
    }

    public void setSearchHotelService(SearchHotelService searchHotelService) {
        this.searchHotelService = searchHotelService;
    }
    /***
     * 搜索酒店分页
     *
     * @param vo
     * @return
     * @throws Exception
     */
   /* @ApiOperation(value = "查询酒店分页", httpMethod = "POST",
            protocols = "HTTP", produces = "application/json",
            response = Dto.class, notes = "查询酒店分页(用于查询酒店列表)" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>错误码: </p>" +
            "<p>20001: 系统异常,获取失败</p>" +
            "<p>20002: 目的地不能为空</p>")*/
    @RequestMapping(value = "/searchItripHotelPage")
    @ResponseBody
    public Dto searchItripHotelPage(@RequestBody SearchHotelVO vo) {
        System.out.println("查询酒店条件搜索模块");
        Page page=null;
        //传入数据判空
        if (EmptyUtils.isEmpty(vo)||EmptyUtils.isEmpty(vo.getDestination())){
            return DtoUtil.returnFail("目的地不能为空","20002");
        }
        try {
            //调用solr搜索引擎业务层
            page=searchHotelService.searchItripHotelPage(vo,vo.getPageNo(),vo.getPageSize());
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常,获取失败","20001");
        }
        if (page.getRows().size()==0){
            return DtoUtil.returnFail("城市为空","20004");
        }
        return DtoUtil.returnDataSuccess(page);
    }
    /* @ApiOperation(value = "根据热门城市查询酒店", httpMethod = "POST",
          protocols = "HTTP", produces = "application/json",
          response = Dto.class, notes = "根据热门城市查询酒店" +
          "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
          "<p>错误码: </p>" +
          "<p>20003: 系统异常,获取失败</p>" +
          "<p>20004: 城市id不能为空</p>")*/
    @RequestMapping(value = "/searchItripHotelListByHotCity")
    @ResponseBody
    public Dto<Page<ItripHotelVO>> searchItripHotelListByHotCity(@RequestBody SearchHotCityVO vo) throws Exception {
        System.out.println("根据热门城市搜索.......");
        //获取前台数据判空判null
        if(EmptyUtils.isEmpty(vo)||EmptyUtils.isEmpty(vo.getCityId())){
            return DtoUtil.returnFail("城市id不能为空","20004");
        }
        Map<String,Object> map=new HashMap<>();
        map.put("cityId",vo.getCityId());
        List list=searchHotelService.searchItripHotelListByHotCity(vo.getCityId(),vo.getCount());
        if(list.size()==0){
            return DtoUtil.returnFail("城市为空", "20004");
        }
        return DtoUtil.returnDataSuccess(list);
    }
}
