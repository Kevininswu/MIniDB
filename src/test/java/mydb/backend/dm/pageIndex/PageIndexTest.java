package mydb.backend.dm.pageIndex;

import com.kevin.minidb.backend.dm.pageIndx.PageIndex;
import com.kevin.minidb.backend.dm.pageIndx.PageInfo;
import org.junit.Test;
import com.kevin.minidb.backend.dm.pageCache.PageCache;

public class PageIndexTest {
    @Test
    public void testPageIndex() {
        PageIndex pIndex = new PageIndex();
        int threshold = PageCache.PAGE_SIZE / 20;
        for(int i = 0; i < 20; i ++) {
            pIndex.add(i, i*threshold);
            pIndex.add(i, i*threshold);
            pIndex.add(i, i*threshold);
        }

        for(int k = 0; k < 3; k ++) {
            for(int i = 0; i < 19; i ++) {
                PageInfo pi = pIndex.select(i * threshold);
                assert pi != null;
                assert pi.pgno == i+1;
            }
        }
    }
}
