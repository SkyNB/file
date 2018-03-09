package com.mobanker.selenium;

import org.apache.poi.POIDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.model.PicturesTable;
import org.apache.poi.hwpf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.eclipse.jetty.util.Fields;

import java.io.*;
import java.util.Map;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.model.FieldsDocumentPart;

/**
 * Created by lixiaofeng on 2017-7-23.
 *  读取word文档
 */
public class ReadWord {

    public static void main(String[] args) throws Exception{
        readWord();
    }
    /**
     *
     */
    public static String readWord() throws Exception{
        //File文件
        File file = new File("F:"+File.separator+"2003.doc");
        //从硬盘读取一个doc文档
       InputStream in = new FileInputStream(file);

        //类从word文档中提取文本,非特殊情况下,都将使用getParagraphText()与getText()
        WordExtractor word = new WordExtractor(in);

        //获取段文本
        String [] strArray = word.getParagraphText();
        String str = word.getText();

        for(int i=0 ; i<strArray.length ; i++){
            System.out.println(strArray[i]+"\ti循环:"+i);
        }
        System.out.println(str +"\t --");



        return str;
    }


    /**
     * 读图片
     * @return
     */
    public static String readPicture() throws  Exception{
        //这个构造函数从InputStream中加载Word文档。
        HWPFDocument doc = new HWPFDocument((InputStream)new FileInputStream("F:\\2003.doc"));

        //这个类为HWPF对象模型,对文档范围段操作
        Range range = doc.getRange(); //

        //看看此文档有多少个段落
        int num = range.numParagraphs();
        System.out.println(num+"段");

        //得到word数据流
        byte [] dataStream = doc.getDataStream();
        System.out.println("数据流长度:"+dataStream.length);

        //用于在一段范围内获得段落数
        int numChar = range.numCharacterRuns();
        System.out.println("CharacterRuns 数:"+numChar);

        //负责图像提取和确定一些文件某块是否包含嵌入的图像。
        PicturesTable table = new PicturesTable(doc, dataStream, null);

        for(int j=0 ; j<numChar ; j++){
            //这个类表示一个文本运行，有着共同的属性。
            CharacterRun run = range.getCharacterRun(j);
            //是否存在图片
            boolean bool = table.hasPicture(run);
            System.out.println("是否存在图片:"+bool);
            if(bool){
                //返回图片对象绑定到指定的CharacterRun
                Picture pic = table.extractPicture(run, true);
                //图片的内容字节写入到指定的输出流。
                pic.writeImageContent(new FileOutputStream("F:\\"+j+".bmp"));
                System.out.println("成功提取图片"+j+":");
            }
        }
        return null;
    }




}
