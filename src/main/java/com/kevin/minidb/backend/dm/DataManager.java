package com.kevin.minidb.backend.dm;

import com.kevin.minidb.backend.dm.pageCache.PageCache;
import com.kevin.minidb.backend.dm.dataItem.DataItem;
import com.kevin.minidb.backend.dm.logger.Logger;
import com.kevin.minidb.backend.dm.page.PageOne;
import com.kevin.minidb.backend.tm.TransactionManager;

/**
 * @author Kevin Liu
 * @description
 * @create 2024/7/28 15:17
 */
public interface DataManager {
    DataItem read(long uid) throws Exception;
    long insert(long xid, byte[] data) throws Exception;
    void close();

    public static DataManager create(String path, long mem, TransactionManager tm) {
        PageCache pc = PageCache.create(path, mem);
        Logger lg = Logger.create(path);

        DataManagerImpl dm = new DataManagerImpl(pc, lg, tm);
        dm.initPageOne();
        return dm;
    }

    public static DataManager open(String path, long mem, TransactionManager tm) {
        PageCache pc = PageCache.open(path, mem);
        Logger lg = Logger.open(path);
        DataManagerImpl dm = new DataManagerImpl(pc, lg, tm);
        if(!dm.loadCheckPageOne()) {
            Recover.recover(tm, lg, pc);
        }
        dm.fillPageIndex();
        PageOne.setVcOpen(dm.pageOne);
        dm.pc.flushPage(dm.pageOne);

        return dm;
    }
}