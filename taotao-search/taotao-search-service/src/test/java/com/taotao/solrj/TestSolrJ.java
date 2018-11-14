package com.taotao.solrj;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.io.IOException;

public class TestSolrJ {

    @Test
    public void testAddDocument() throws IOException, SolrServerException {
        //创建一个SolrServer对象
        //如果有多个collection,需要制定collectio.如果只有一个,可以不指定
        HttpSolrServer solrServer = new HttpSolrServer("http://192.168.119.138:8080/solr/collection1");
        //创建一个文档对象
        SolrInputDocument document = new SolrInputDocument();
        document.setField("id", "333");
        document.setField("item_title", "格力空调");
        document.setField("item_sell_point", "价格便宜,优惠大,送充电宝一个");
        document.addField("item_price", 25000);
        document.addField("item_image", "http://www.123.jpg");
        document.addField("item_category_name", "电器");
        document.addField("item_desc", "这是一款最新的空调，质量好，值得信赖！！");
        //将document添加到索引库
        UpdateResponse add = solrServer.add(document);
        System.out.println(add);
        //提交
        UpdateResponse commit = solrServer.commit();
        System.out.println(commit);
    }

    @Test
    public void testDeleteDocument() throws IOException, SolrServerException {
        HttpSolrServer solrServer = new HttpSolrServer("http://192.168.119.138:8080/solr/collection1");
        //通过 id 删除文档
        solrServer.deleteById("333");
        //提交
        solrServer.commit();
    }

    @Test
    public void deleteDocumentByQuery() throws IOException, SolrServerException {
        HttpSolrServer solrServer = new HttpSolrServer("http://192.168.119.138:8080/solr/collection1");
        //根据查询结果删除
        solrServer.deleteByQuery("item_price:20000");
        solrServer.commit();
    }

    @Test
    public void queryDocument() throws SolrServerException {
        //查询测试
        HttpSolrServer solrServer = new HttpSolrServer("http://192.168.119.138:8080/solr/collection1");
        SolrQuery query = new SolrQuery();
        query.setQuery("id:333");
        QueryResponse response = solrServer.query(query);
        SolrDocumentList list = response.getResults();
        for (SolrDocument document : list
        ) {
            String id = document.getFieldValue("id").toString();
            String item_title = document.getFieldValue("item_title").toString();
            System.out.println("id:" + id + "------" + "item_title:" + item_title);
        }
    }
}
