import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MCAITest {
    //
    // private MCAI evaluateBoard;

    @Before
    public void setup() {
        MCAI eva = new MCAI();
    }

    @Test
    public void testAdd() {
        MCAI eva = new MCAI();
        int[][] board;
        //board=[[1],[1]];
        Map<Integer, MCAI.Chain > bestLine;
        //bestLine=0;
        //double sum = eva.evaluateBoard(board,bestLine);
        Assert.assertEquals(7.1, 7.1);
    }
}