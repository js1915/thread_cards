import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.internal.TextListener;

public class RunTestSuite {
    public static void main(String[] args) {
        JUnitCore junit = new JUnitCore();
        junit.addListener(new TextListener(System.out));
        Result result = junit.run(TestSuite.class);
        System.out.println(result);
    }
}
