package com.kevin.minidb.backend.dm.pageIndx;

/**
 * @author Kevin Liu
 * @description
 * @create 2024/7/28 15:00
 */
public class PageInfo {
    public int pgno;
    public int freeSpace;

    public PageInfo(int pgno, int freeSpace) {
        this.pgno = pgno;
        this.freeSpace = freeSpace;
    }
}
