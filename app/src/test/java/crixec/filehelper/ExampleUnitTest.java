package crixec.filehelper;

import org.junit.Test;

import java.io.File;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void bigBand() throws Exception {
        System.out.print("===Starting===\n");
        String src = "/home/crixec/tmp/aaa/BAP";
        int num = 20;
        String[] parts = Utils.splitFileName(src, num);
        long len = new File(src).length();
        long[] sizes = Utils.splitLength(len, num);
        System.out.println(Utils.splitFilesBySize(src, sizes, parts));
        System.out.print("=== Ending ===\n");
    }

}