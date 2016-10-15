package com.DamonLiu;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.fnlp.nlp.cn.CNFactory;
import org.fnlp.util.exception.LoadModelException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;

/**
 * Created by DamonLiu on 2016/10/15.
 */
public class Step2 {

    public static void extractNamesForEachRecord(){
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

            String[] namesInMainName = getNamesFromString(mainName,factory);
            String[] namesInDescription = getNamesFromString(desc,factory);
            System.out.println(" ");

            Element personElement = root.addElement("person");
            Element mainNameElement = personElement.addElement("mainName");
            Element namesInDesctiption = personElement.addElement("names");

            mainNameElement.setText(namesInMainName.length>=1?namesInMainName[0]:mainName);
            namesInDesctiption.setText(Arrays.toString(namesInDescription));
        }

        try {
            writer(namesDocument,"names.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void writer(Document document,String fileName) throws Exception {
        // 紧凑的格式
        // OutputFormat format = OutputFormat.createCompactFormat();
        // 排版缩进的格式
        OutputFormat format = OutputFormat.createPrettyPrint();
        // 设置编码
        format.setEncoding("UTF-8");
        // 创建XMLWriter对象,指定了写出文件及编码格式
        // XMLWriter writer = new XMLWriter(new FileWriter(new
        // File("src//a.xml")),format);
        XMLWriter writer = new XMLWriter(new OutputStreamWriter(
                new FileOutputStream(new File(fileName)), "UTF-8"), format);
        // 写入
        writer.write(document);
        // 立即写入
        writer.flush();
        // 关闭操作
        writer.close();
    }

    private static String[]  getNamesFromString(String input,CNFactory factory){
        // 使用标注器对包含实体名的句子进行标注，得到结果
        HashMap<String, String> result = factory.ner(input);
        ArrayList<String> names = new ArrayList<>();
        for (Map.Entry<String,String> entry : result.entrySet()){
            if (entry.getValue().compareToIgnoreCase("人名")==0){
                names.add(entry.getKey());
            }
        }
        return names.toArray(new String[names.size()]);
    }

    public static void main(String[] args) throws Exception {
        Step2.extractNamesForEachRecord();
    }
}
