//package bgu.spl.mics;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import java.util.concurrent.TimeUnit;
//
//import static junit.framework.TestCase.*;
//
//
//public class FutureTest {
//    private Future f;
//
//    @Before
//    public void setUp() throws Exception {
//        f = new Future();
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        f = null;
//    }
//
//    @Test
//    //@post: future has been resolved.
//    public void testGet() {
//        assertFalse(f.get()==null);
//    }
//
//    @Test
//    public void testResolve() {
//        f.resolve(true);
//        assertEquals(true, f.isDone());
//        assertEquals(f.get(), true);
//    }
//    @Test
//    public void testGet1() {
//        assertEquals(null, f.get(100, TimeUnit.MILLISECONDS));
//        f.resolve(true);
//
//        assertEquals(true, f.get(100, TimeUnit.MILLISECONDS));
//    }
//}