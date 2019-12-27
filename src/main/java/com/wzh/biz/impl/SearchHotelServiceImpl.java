package com.wzh.biz.impl;

import com.wzh.biz.SearchHotelService;
import com.wzh.mapper.BaseQuery;
import com.wzh.util.EmptyUtils;
import com.wzh.util.ItripHotelVO;
import com.wzh.util.Page;
import com.wzh.util.SearchHotelVO;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.stereotype.Service;
import java.util.List;

/*solr搜索酒店的Service 实现类*/
@Service
public class SearchHotelServiceImpl implements SearchHotelService {
    //利用封装好的sorl查询工具类获得sorl查询对象（相当于SolrClient注入）
    private BaseQuery<ItripHotelVO> itripHotelVOBaseQuery=new BaseQuery<>("http://localhost:8080/solr/hotel");
    //private BaseQuery<ItripHotelVO> itripHotelVOBaseQuery = new BaseQuery();
    /***
     * 根据多个条件查询酒店分页
     * @param vo
     * @param pageNo
     * @param pageSize
     * @return
     * @throws Exception
     */
    @Override
    public Page<ItripHotelVO> searchItripHotelPage(SearchHotelVO vo, Integer pageNo, Integer pageSize) throws Exception {
        SolrQuery query=new SolrQuery("*:*");
        int flag=0;
        StringBuffer stringBuffer=new StringBuffer();
        if (EmptyUtils.isNotEmpty(vo)){
            //目的地不为空
            if(EmptyUtils.isNotEmpty(vo.getDestination())){
                stringBuffer.append("destination:"+vo.getDestination());
                flag=1;
            }
            //酒店级别
            if(EmptyUtils.isNotEmpty(vo.getHotelLevel())){
                query.addFilterQuery("hotelLevel:"+vo.getHotelLevel());
            }
            //酒店查询关键字
            if(EmptyUtils.isNotEmpty(vo.getKeywords())){
                if(flag==1){
                    stringBuffer.append("AND keyword:"+vo.getKeywords());
                }else{
                    stringBuffer.append("keyword:"+vo.getKeywords());
                }
            }
            //酒店特色
            if(EmptyUtils.isNotEmpty(vo.getFeatureIds())){
                StringBuffer buffer=new StringBuffer("(");
                int flags=0;
                String featureIdArray[]=vo.getFeatureIds().split(",");
                for (String featureId:featureIdArray){
                    if (flags==0){
                        buffer.append("featureIds:"+"*,"+featureId+",*");
                    }else {
                        buffer.append("OR featureIds:"+"*,"+featureId+",*");
                    }
                    flags++;
                }
                buffer.append(")");
                query.addFilterQuery(buffer.toString());
            }
            //酒店商圈
            if (EmptyUtils.isNotEmpty(vo.getTradeAreaIds())){
                StringBuffer buffer=new StringBuffer("(");
                int flags=0;
                String tradeAreaIdsArray[]=vo.getTradeAreaIds().split(",");
                for (String TradeAreaId:tradeAreaIdsArray){
                    if (flags==0){
                        buffer.append("tradingAreaIds:"+"*,"+TradeAreaId+",*");
                    }else {
                        buffer.append("OR tradingAreaIds:"+"*,"+TradeAreaId+",*");
                    }
                    flags++;
                }
                buffer.append(")");
                query.addFilterQuery(buffer.toString());
            }
            //最高价
            if (EmptyUtils.isNotEmpty(vo.getMaxPrice())){
                query.addFilterQuery("minPrice:"+"[* TO "+vo.getMaxPrice()+"]");
            }
            //最低价
            if (EmptyUtils.isNotEmpty(vo.getMinPrice())){
                query.addFilterQuery("minPrice:"+"["+vo.getMinPrice()+" TO *]");
            }
            //升序
            if (EmptyUtils.isNotEmpty(vo.getAscSort())){
                query.addSort(vo.getAscSort(),SolrQuery.ORDER.asc);
            }
            //降序
            if (EmptyUtils.isNotEmpty(vo.getDescSort())){
                query.addSort(vo.getDescSort(),SolrQuery.ORDER.desc);
            }
        }
        //条件查询传入solr
        if (EmptyUtils.isNotEmpty(stringBuffer.toString())){
            query.setQuery(stringBuffer.toString());
        }
        Page<ItripHotelVO> page=itripHotelVOBaseQuery.queryPage(query,pageNo,pageSize,ItripHotelVO.class);
        return page;
    }

    @Override
    public List<ItripHotelVO> searchItripHotelListByHotCity(Integer cityId, Integer count) throws Exception {
        SolrQuery query=new SolrQuery("*:*");
        //城市id不为空
        if (EmptyUtils.isNotEmpty(cityId)){
            query.addFilterQuery("cityId:"+cityId);
        }else {
            return null;
        }
        List<ItripHotelVO> hotelVOList=itripHotelVOBaseQuery.queryList(query,count,ItripHotelVO.class);
        return hotelVOList;
    }
}
