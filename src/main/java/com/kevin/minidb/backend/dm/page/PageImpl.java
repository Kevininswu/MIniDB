package com.kevin.minidb.backend.dm.page;


import com.kevin.minidb.backend.dm.pageCache.PageCache;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Kevin Liu
 * @description 数据页
 * @create 2024/7/27 13:38
 */
public class PageImpl implements Page {
    private int pageNumber; //Page编号
    private byte[] data; //数据
    private boolean dirty; //是否脏页面
    private Lock lock; //锁

    private PageCache pc;

    public PageImpl(int pageNumber, byte[] data, PageCache pc) {
        this.pageNumber = pageNumber;
        this.data = data;
        this.pc = pc;
        lock = new ReentrantLock();
    }

    @Override
    public void lock() {
        lock.lock();
    }

    @Override
    public void unlock() {
        lock.unlock();
    }

    @Override
    public void release() {
        pc.release(this);
    }

    @Override
    public void setDirty(boolean dirty) {
        this.dirty = dirty;

    }

    @Override
    public boolean isDirty() {
        return this.dirty;
    }

    @Override
    public int getPageNumber() {
        return this.pageNumber;
    }

    @Override
    public byte[] getData() {
        return this.data;
    }
}
