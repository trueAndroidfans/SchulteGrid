package com.aokiji.schultegrid.db.entities;

import org.litepal.crud.LitePalSupport;

public class Times extends LitePalSupport {

    /**
     * 完成次数
     */
    private int times;

    /**
     * 完成时间
     */
    private String completionTime;

    /**
     * 创建人
     */
    private String creator;

    public int getTimes()
    {
        return times;
    }

    public void setTimes(int times)
    {
        this.times = times;
    }

    public String getCompletionTime()
    {
        return completionTime;
    }

    public void setCompletionTime(String completionTime)
    {
        this.completionTime = completionTime;
    }

    public String getCreator()
    {
        return creator;
    }

    public void setCreator(String creator)
    {
        this.creator = creator;
    }
}
