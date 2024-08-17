package com.kevin.minidb.backend.vm;

import com.kevin.minidb.backend.dm.DataManager;
import com.kevin.minidb.backend.tm.TransactionManager;

/**
 * @author Kevin Liu
 * @description
 * @create 2024/8/10 15:57
 */
public interface VersionManager {
    byte[] read(long xid, long uid) throws Exception;
    long insert(long xid, byte[] data) throws Exception;
    boolean delete(long xid, long uid) throws Exception;

    long begin(int level);
    void commit(long xid) throws Exception;
    void abort(long xid);

    public static VersionManager newVersionManager(TransactionManager tm, DataManager dm) {
        return new VersionManagerImpl(tm, dm);
    }
}
