package com.taotao.search.listener;

import com.taotao.common.pojo.SearchItem;
import com.taotao.search.mapper.SearchItemMapper;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.IOException;

public class ItemAddMessageListener implements MessageListener {

    @Autowired
    private SearchItemMapper searchItemMapper;

    @Autowired
    private SolrServer solrServer;

    @Override
    public void onMessage(Message message) {
        try {
            //从消息中取出商品id
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();
            long itemId = Long.valueOf(text);
            //根据商品id,查询商品详情,这里注意商品插入方法,可能事务并没有提交,则查不到商品信息,
            //在这里采用尝试三次,每次尝试休眠一秒
            SearchItem searchItem = null;
            while (true){
                try {
                    //休眠一秒
                    Thread.sleep(1000);
                    searchItem = searchItemMapper.getItemById(itemId);
                    //获取到信息,退出循环
                    if (searchItem != null) {
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //创建文档对象
            SolrInputDocument document = new SolrInputDocument();
            //向文档对象添加域信息
            document.setField("id", searchItem.getId());
            document.setField("item_title", searchItem.getTitle());
            document.setField("item_sell_point", searchItem.getSell_point());
            document.setField("item_price", searchItem.getPrice());
            document.setField("item_image", searchItem.getImage());
            document.setField("item_category_name", searchItem.getItem_category_name());
            document.setField("item_desc", searchItem.getItem_desc());
            //把文档对象写到索引库
            solrServer.add(document);
            //提交
            solrServer.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
