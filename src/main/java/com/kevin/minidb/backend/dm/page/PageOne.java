package com.kevin.minidb.backend.dm.page;

import com.kevin.minidb.backend.dm.pageCache.PageCache;
import com.kevin.minidb.backend.utils.RandomUtil;

import java.util.Arrays;

/**
 * @author Kevin Liu
 * @description  特殊管理第一页
 *               ValidCheck
 *               db启动时给100~107字节处填入一个随机字节，db关闭时将其拷贝到108~115字节
 *               用于判断上一次数据库是否正常关闭
 * @create 2024/7/27 14:59
 */
public class PageOne {
    private static final int OF_VC = 100;//字段偏移量 //
    private static final int LEN_VC = 8;//长度
    //初始化raw，并且设置随机值
    public static byte[] InitRaw() {
        byte[] raw = new byte[PageCache.PAGE_SIZE];
        setVcOpen(raw);
        return raw;
    }

    public static void setVcOpen(Page pg) {
        pg.setDirty(true);
        setVcOpen(pg.getData());
    }

    private static void setVcOpen(byte[] raw) {
        System.arraycopy(RandomUtil.randomBytes(LEN_VC), 0, raw, OF_VC, LEN_VC);
    }

    public static void setVcClose(Page pg) {
        pg.setDirty(true);
        setVcClose(pg.getData());
    }
    //存老随机值
    private static void setVcClose(byte[] raw) {
        System.arraycopy(raw, OF_VC, raw, OF_VC+LEN_VC, LEN_VC);
    }

    public static boolean checkVc(Page pg) {
        return checkVc(pg.getData());
    }
    //验证是否正常关闭
    private static boolean checkVc(byte[] raw) {
        return Arrays.equals(Arrays.copyOfRange(raw, OF_VC, OF_VC+LEN_VC), Arrays.copyOfRange(raw, OF_VC+LEN_VC, OF_VC+2*LEN_VC));
    }
}
