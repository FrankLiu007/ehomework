package name.wanghwx.ehomework.common.pojo.algebra;

public class Bezier{

    private RowVector<ComplexNumber> begin;
    private RowVector<ComplexNumber> c1;
    private RowVector<ComplexNumber> c2;
    private RowVector<ComplexNumber> end;

    public Bezier(RowVector<ComplexNumber> begin,RowVector<ComplexNumber> c1,RowVector<ComplexNumber> c2,RowVector<ComplexNumber> end){
        this.begin = begin;
        this.c1 = c1;
        this.c2 = c2;
        this.end = end;
    }

    public RowVector<ComplexNumber> getBegin() {
        return begin;
    }

    public RowVector<ComplexNumber> getC1() {
        return c1;
    }

    public RowVector<ComplexNumber> getC2() {
        return c2;
    }

    public RowVector<ComplexNumber> getEnd() {
        return end;
    }

    public RowVector<ComplexNumber> getBeginDerivative(){
        return c1.subtract(begin);
    }

    public RowVector<ComplexNumber> getC1Derivative(){
        return begin.multiply(new RealNumber(-4.0/3)).add(c2).add(end.multiply(new RealNumber(1.0/3)));
    }

    public RowVector<ComplexNumber> getC2Derivative(){
        return begin.multiply(new RealNumber(-1.0/3)).subtract(c1).add(end.multiply(new RealNumber(4.0/3)));
    }

    public RowVector<ComplexNumber> getEndDerivative(){
        return end.subtract(c2);
    }

}