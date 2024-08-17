package com.kevin.minidb.backend.dm.page;

/**
 * @author Kevin Liu
 * @description
 * @create 2024/7/27 13:36
 */
public interface Page {
    void lock();

    void unlock();

    void release();

    void setDirty(boolean dirty);

    boolean isDirty();

    int getPageNumber();

    byte[] getData();
}
