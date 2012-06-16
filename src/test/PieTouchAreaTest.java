package test;

import com.bluecar.PieTouchArea;
import com.bluecar.Point;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * Date: 6/16/12
 * Time: 12:55 PM
 */
public class PieTouchAreaTest extends TestCase {

    @Test
    public void test_getArea(){
        //rotated, position changed
        assertEquals(0, new PieTouchArea(150, 8, new Point(145,145), -30).getArea(new Point(125,34)));
        assertEquals(1, new PieTouchArea(150, 8, new Point(145,145), -30).getArea(new Point(228,46)));
        assertEquals(2, new PieTouchArea(150, 8, new Point(145,145), -30).getArea(new Point(267,147)));
        assertEquals(3, new PieTouchArea(150, 8, new Point(145,145), -30).getArea(new Point(228,234)));
        assertEquals(4, new PieTouchArea(150, 8, new Point(145,145), -30).getArea(new Point(130,264)));
        assertEquals(5, new PieTouchArea(150, 8, new Point(145,145), -30).getArea(new Point(48,235)));
        assertEquals(6, new PieTouchArea(150, 8, new Point(145,145), -30).getArea(new Point(10,139)));
        assertEquals(7, new PieTouchArea(150, 8, new Point(145,145), -30).getArea(new Point(58,51)));


        //out of range
        assertEquals(-1, new PieTouchArea(3, 6, null, 0).getArea(new Point(4, 4)));

        //point in correct area
        assertEquals(2, new PieTouchArea(3, 6, null, 0).getArea(new Point(1, 1)));

        //rotated
        assertEquals(1, new PieTouchArea(3, 6, null, 60).getArea(new Point(1, 1)));
    }
}
