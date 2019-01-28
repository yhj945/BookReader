package cn.itcast.bookreader.model;

import java.util.List;
/**
 * Created by Administrator on 2018/7/3.
 */

public class PageModel {
    private List<LineModel> lineModels;
    private int lineDiff;

    public PageModel() {
    }

    public PageModel(List<LineModel> lineModels, int lineDiff) {
        this.lineModels = lineModels;
        this.lineDiff = lineDiff;
    }

    public List<LineModel> getLineModels() {
        return lineModels;
    }

    public void setLineModels(List<LineModel> lineModels) {
        this.lineModels = lineModels;
    }

    public int getLineDiff() {
        return lineDiff;
    }

    public void setLineDiff(int lineDiff) {
        this.lineDiff = lineDiff;
    }
}
