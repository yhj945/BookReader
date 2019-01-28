package cn.itcast.bookreader.utils;

import cn.itcast.bookreader.model.LineModel;
import cn.itcast.bookreader.model.PageModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/3.
 */

public class ReadTool {
    private List<PageModel> pageModels;
    private List<LineModel> lineModels;
    private List<String> stringList;
    private List<Integer> strWidths;
    private List<Integer> strXs;
    private List<Integer> strColors;
    private int lineNum;

    public void init(){
        pageModels = new ArrayList<>();
        lineModels = new ArrayList<>();
        stringList = new ArrayList<>();
        strWidths = new ArrayList<>();
        strXs = new ArrayList<>();
        strColors = new ArrayList<>();
        lineNum = 0;
    }

    public void addStrArr(String subStr,int strWidth,int strX,int strColor){
        stringList.add(subStr);
        strWidths.add(strWidth);
        strXs.add(strX);
        strColors.add(strColor);
    }

    public void setStrCaptal(int strWidth,int strColor){
        for(int i=0;i<2;i++){
            stringList.add("");
            strWidths.add(strWidth);
            strXs.add(i*strWidth);
            strColors.add(strColor);
        }
    }
    //添加行
    public void addLine(int strDiff){
        LineModel lineModel =new LineModel(stringList,strWidths,strXs,strDiff,strColors);
        lineModels.add(lineModel);
        stringList = new ArrayList<>();
        strWidths = new ArrayList<>();
        strXs = new ArrayList<>();
        strColors = new ArrayList<>();
    }
    //添加页
    public void addPage(int readHeight,int fontSize){
        lineNum++;
        if(lineNum*fontSize*1.5 > readHeight){ //如果显示的行高度大于阅读的高度，就增加一页
            PageModel pageModel =new PageModel(lineModels,0);
            pageModels.add(pageModel);
            lineModels = new ArrayList<>();
            lineNum = 1;
        }
    }

    public void addEnd(int readHeight,int fontSize){
        addPage(readHeight,fontSize);
        addLine(0);
        PageModel pageModel =new PageModel(lineModels,0);
        pageModels.add(pageModel);
        lineNum = 1;
    }

    public List<PageModel> getPageModels() {
        return pageModels;
    }

    public void setPageModels(List<PageModel> pageModels) {
        this.pageModels = pageModels;
    }

    public List<LineModel> getLineModels() {
        return lineModels;
    }

    public void setLineModels(List<LineModel> lineModels) {
        this.lineModels = lineModels;
    }

    public List<String> getStringList() {
        return stringList;
    }

    public void setStringList(List<String> stringList) {
        this.stringList = stringList;
    }

    public List<Integer> getStrWidths() {
        return strWidths;
    }

    public void setStrWidths(List<Integer> strWidths) {
        this.strWidths = strWidths;
    }

    public List<Integer> getStrXs() {
        return strXs;
    }

    public void setStrXs(List<Integer> strXs) {
        this.strXs = strXs;
    }

    public List<Integer> getStrColors() {
        return strColors;
    }

    public void setStrColors(List<Integer> strColors) {
        this.strColors = strColors;
    }

    public int getLineNum() {
        return lineNum;
    }

    public void setLineNum(int lineNum) {
        this.lineNum = lineNum;
    }
}
