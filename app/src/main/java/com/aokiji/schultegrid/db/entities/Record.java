package com.aokiji.schultegrid.db.entities;

import org.litepal.crud.LitePalSupport;

public class Record extends LitePalSupport {

    /**
     * 耗时
     */
    private String timeConsuming;

    /**
     * 创建时间
     */
    private long createTime;

    /**
     * 创建人
     */
    private String creator;

    public String getTimeConsuming()
    {
        return timeConsuming;
    }

    public void setTimeConsuming(String timeConsuming)
    {
        this.timeConsuming = timeConsuming;
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
