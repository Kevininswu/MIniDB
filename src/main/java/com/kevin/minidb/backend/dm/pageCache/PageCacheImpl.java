package com.kevin.minidb.backend.dm.pageCache;

import com.kevin.minidb.backend.common.AbstractCache;
import com.kevin.minidb.backend.dm.page.Page;
import com.kevin.minidb.backend.dm.page.PageImpl;
import com.kevin.minidb.backend.utils.Panic;
import com.kevin.minidb.common.Error;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * @author Kevin Liu
 * @description
 * @create 2024/7/27 13:40
 */
public class PageCacheImpl extends AbstractCache<Page> implements PageCache {
    private static final int MEM_MIN_LIM = 10; //最小内存限制
    public static final String DB_SUFFIX = ".db";//后缀

    private RandomAccessFile file;
    private FileChannel fc;
    private Lock fileLock;

    private AtomicInteger pageNumbers;//page的总页数

    public PageCacheImpl(RandomAccessFile file, FileChannel fileChannel, int maxResource) {
        super(maxResource);
        if (maxResource < MEM_MIN_LIM) {
            Panic.panic(Error.MemTooSmallException);
        }
        long length = 0;
        try {
            length = file.length();
        } catch (IOException e) {
            Panic.panic(e);
        }
        this.file = file;
        this.fc = fileChannel;
        this.fileLock = new ReentrantLock();
        this.pageNumbers = new AtomicInteger((int) length / PAGE_SIZE);

    }

    //创建一个新page并写入db 返回pgno
    @Override
    public int newPage(byte[] initData) {
        int pgno = pageNumbers.incrementAndGet();
        Page pg = new PageImpl(pgno, initData, null);
        flush(pg);
        return pgno;
    }

    //写入一个page的数据
    private void flush(Page pg) {
        int pgno = pg.getPageNumber();
        long offset = pageOffset(pgno);

        fileLock.lock();
        try {
            ByteBuffer buf = ByteBuffer.wrap(pg.getData());
            fc.position(offset);
            fc.write(buf);
            fc.force(false);
        } catch (IOException e) {
            Panic.panic(e);
        } finally {
            fileLock.unlock();
        }
    }

    //根据pgno获取页
    @Override
    public Page getPage(int pgno) throws Exception {

        return get((long) pgno);
    }

    //关闭缓存将所有数据写回数据库 然后关闭通道
    @Override
    public void close() {
        super.close();
        try {
            fc.close();
            file.close();
        } catch (IOException e) {
            Panic.panic(e);
        }

    }

    //查页数据然后存到缓存
    @Override
    protected Page getForCache(long key) throws Exception {
        int pgno = (int) key;
        long offset = PageCacheImpl.pageOffset(pgno);
        ByteBuffer buf = ByteBuffer.allocate(PAGE_SIZE);
        fileLock.lock();
        try {
            fc.position(offset);
            fc.read(buf);
        } catch (IOException e) {
            Panic.panic(e);
        }
        fileLock.unlock();
        return new PageImpl(pgno, buf.array(), this);
    }

    //从缓存回写脏页面
    @Override
    protected void releaseForCache(Page pg) {
        if (pg.isDirty()) {
            flush(pg);
            pg.setDirty(false);
        }

    }

    //释放缓存
    @Override
    public void release(Page page) {
        release((long) page.getPageNumber());
    }

    //调整文件长度并生成新的page总数
    @Override
    public void truncateByBgno(int maxPgno) {
        long size = pageOffset(maxPgno + 1);
        try {
            file.setLength(size);
        } catch (IOException e) {
            Panic.panic(e);
        }
        pageNumbers.set(maxPgno);
    }

    @Override
    public int getPageNumber() {
        return pageNumbers.intValue();
    }

    //把缓存中的页存入数据库
    @Override
    public void flushPage(Page pg) {
        flush(pg);
    }

    //获取页面偏移量
    private static long pageOffset(int pgno) {
        return (pgno - 1) * PAGE_SIZE;
    }
}
