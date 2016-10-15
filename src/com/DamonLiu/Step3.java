package com.DamonLiu;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.fnlp.nlp.cn.CNFactory;
import org.fnlp.util.exception.LoadModelException;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;

import static com.DamonLiu.Step2.getNamesFromString;
import static com.DamonLiu.Step2.writer;

/**
 * used to extract relationships from info.xml
 * Created by DamonLiu on 2016/10/15.
 */
public class Step3 {
    public static void extractRelationship(){
        CNFactory factory = null;
        try {
            factory = CNFactory.getInstance("models");
        } catch (LoadModelException e) {
            e.printStackTrace();
        }
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(new File("info.xml"));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Element node = document.getRootElement();
        //遍历所有的元素节点
        // 当前节点下面子节点迭代器
        Iterator<Element> it = node.elementIterator();
        // 遍历
        Document namesDocument = DocumentHelper.createDocument();
        Element root = namesDocument.addElement("root");
        while (it.hasNext()) {
            // 获取某个子节点对象
            Element person = it.next();
            String mainName = person.elementText("name");
            String desc = person.elementText("description");


        }

        try {
            writer(namesDocument,"relationships.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
