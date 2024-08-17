package mydb.backend.im;

import com.kevin.minidb.backend.im.BPlusTree;
import mydb.backend.tm.MockTransactionManager;
import org.junit.Test;
import com.kevin.minidb.backend.dm.DataManager;
import com.kevin.minidb.backend.dm.pageCache.PageCache;
import com.kevin.minidb.backend.tm.TransactionManager;

import java.io.File;
import java.util.List;

public class BPlusTreeTest {
    @Test
    public void testTreeSingle() throws Exception {
        TransactionManager tm = new MockTransactionManager();
        DataManager dm = DataManager.create("D://TestTreeSingle", PageCache.PAGE_SIZE*10, tm);

        long root = BPlusTree.create(dm);
        BPlusTree tree = BPlusTree.load(root, dm);

        int lim = 1000;
        for(int i = lim-1; i >= 0; i --) {
            tree.insert(i, i);
        }

        for(int i = 0; i < lim; i ++) {
            List<Long> uids = tree.search(i);
            assert uids.size() == 1;
            assert uids.get(0) == i;
        }
            tm.close();
            dm.close();
        assert new File("D://TestTreeSingle.db").delete();
        assert new File("D://TestTreeSingle.log").delete();
    }
}
