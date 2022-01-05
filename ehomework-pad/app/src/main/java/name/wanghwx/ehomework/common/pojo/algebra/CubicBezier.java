package name.wanghwx.ehomework.common.pojo.algebra;

public class CubicBezier{

    private RowVector<RealNumber> begin;
    private RowVector<RealNumber> c1;
    private RowVector<RealNumber> c2;
    private RowVector<RealNumber> end;

    private RowVector<RealNumber> dBegin;
    private RowVector<RealNumber> dC1;
    private RowVector<RealNumber> dC2;
    private RowVector<RealNumber> dEnd;

    public CubicBezier(RowVector<RealNumber> begin,RowVector<RealNumber> c1,RowVector<RealNumber> c2,RowVector<RealNumber> end){
        this.begin = begin;
        this.c1 = c1;
        this.c2 = c2;
        this.end = end;
        dBegin = c1.subtract(begin);
        dC1 = end.multiply(new RealNumber(1.0/3)).add(c2).subtract(begin.multiply(new RealNumber(4.0/3)));
        dC2 = end.multiply(new RealNumber(4.0/3)).subtract(c1).subtract(begin.multiply(new RealNumber(1.0/3)));
        dEnd = end.subtract(c2);
    }

    public RowVector<RealNumber> getBegin(){
        return begin;
    }

    public RowVector<RealNumber> getC1(){
        return c1;
    }

    public RowVector<RealNumber> getC2(){
        return c2;
    }

    public RowVector<RealNumber> getEnd(){
        return end;
    }

    public RowVector<RealNumber> getDBegin(){
        return dBegin;
    }

    public RowVector<RealNumber> getDC1(){
        return dC1;
    }

    public RowVector<RealNumber> getDC2(){
        return dC2;
    }

    public RowVector<RealNumber> getDEnd(){
        return dEnd;
    }

}