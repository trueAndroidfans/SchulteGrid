package com.aokiji.schultegrid.db.entities;

import org.litepal.crud.LitePalSupport;

public class Times extends LitePalSupport {

    /**
     * 完成次数
     */
    private int count;

    /**
     * 完成时间
     */
    private String completionTime;

    /**
     * 创建时间
     */
    private long createTime;

    /**
     * 创建人
     */
    private String creator;

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }

    public String getCompletionTime()
    {
        return completionTime;
    }

    public void setCompletionTime(String completionTime)
    {
        this.completionTime = completionTime;
    }

    public long getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(long createTime)
    {
        this.createTime = createTime;
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
