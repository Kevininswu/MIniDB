package com.kevin.minidb.backend.dm.page;

import com.kevin.minidb.backend.dm.pageCache.PageCache;
import com.kevin.minidb.backend.utils.Parser;

import java.util.Arrays;

/**
 * @author Kevin Liu
 * @description   PageX管理普通页
                 普通页结构
                 [FreeSpaceOffset] [Data]
                FreeSpaceOffset: 2字节 空闲位置开始偏移
 * @create 2024/7/27 15:44
 */
public class PageX {
    private static final short OF_FREE = 0; //整体的偏移量
    private static final short OF_DATA = 2;//存FSO需要两个字节，就是上面那个值
    public static final int MAX_FREE_SPACE = PageCache.PAGE_SIZE - OF_DATA;
    public static byte[] initRaw() {
        byte[] raw = new byte[PageCache.PAGE_SIZE];
        setFSO(raw, OF_DATA);
        return raw;
    }
    private static void setFSO(byte[] raw, short ofData) {
        //从Parser.short2Byte(ofData[0]开始复制到raw[of_free] 之后 复制 OF_DATA
        System.arraycopy(Parser.short2Byte(ofData), 0, raw, OF_FREE, OF_DATA);
    }
    // 获取pg的FSO
    public static short getFSO(Page pg) {
        return getFSO(pg.getData());
    }

    private static short getFSO(byte[] raw) {
        return Parser.parseShort(Arrays.copyOfRange(raw, 0, 2));
    }
    // 将raw插入pg中，返回插入位置
    public static short insert(Page pg, byte[] raw) {
        pg.setDirty(true);
        short offset = getFSO(pg.getData());
        //从raw到pg.getData
        System.arraycopy(raw, 0, pg.getData(), offset, raw.length);
        setFSO(pg.getData(), (short)(offset + raw.length));
        return offset;
    }

    // 获取页面的空闲空间大小
    public static int getFreeSpace(Page pg) {
        return PageCache.PAGE_SIZE - (int)getFSO(pg.getData());
    }

    // 将raw插入pg中的指定offset位置，并将pg的offset设置为较大的offset offset>FSO才更新
    public static void recoverInsert(Page pg, byte[] raw, short offset) {
        pg.setDirty(true);
        System.arraycopy(raw, 0, pg.getData(), offset, raw.length);

        short rawFSO = getFSO(pg.getData());
        if(rawFSO < offset + raw.length) {
            setFSO(pg.getData(), (short)(offset+raw.length));
        }
    }

    // 将raw插入pg中的offset位置，redo update
    public static void recoverUpdate(Page pg, byte[] raw, short offset) {
        pg.setDirty(true);
        System.arraycopy(raw, 0, pg.getData(), offset, raw.length);
    }

}
