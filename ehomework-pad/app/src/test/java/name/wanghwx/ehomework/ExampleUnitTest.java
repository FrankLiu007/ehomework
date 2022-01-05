package name.wanghwx.ehomework;

import org.junit.Test;

import name.wanghwx.ehomework.common.pojo.algebra.ComplexNumber;
import name.wanghwx.ehomework.common.pojo.algebra.Matrix;
import name.wanghwx.ehomework.common.pojo.algebra.Operable;

import static org.junit.Assert.*;

/**
 * Example local unit autoLogin, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect(){
        Matrix<ComplexNumber> m1 = Matrix.getInstance(2, 2);
        for(int i=0;i<2;i++){
            for(int j=0;j<2;j++){
                m1.setElement(i,j,i==j?new ComplexNumber(1,0):new ComplexNumber(2,0));
            }
        }
        Matrix<ComplexNumber> m2 = Matrix.getInstance(2, 2);
        for(int i=0;i<2;i++){
            for(int j=0;j<2;j++){
                m2.setElement(i,j,i==j?new ComplexNumber(2,0):new ComplexNumber(1,0));
            }
        }
        System.err.println(m1.add(m2).toString());
    }
}