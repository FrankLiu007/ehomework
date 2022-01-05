package name.wanghwx.ehomework.common.pojo.algebra;

public class RealNumber extends ComplexNumber{

    public RealNumber(Number real){
        super(real,0);
    }

    @Override
    public Operable add(Operable o){
        return o instanceof RealNumber?add((RealNumber)o):super.add(o);
    }

    @Override
    public Operable subtract(Operable o){
        return o instanceof RealNumber?subtract((RealNumber)o):super.subtract(o);
    }

    @Override
    public Operable multiply(Operable o){
        return o instanceof RealNumber?multiply((RealNumber)o):super.multiply(o);
    }

    @Override
    public Operable divide(Operable o){
        return o instanceof RealNumber?divide((RealNumber)o):super.divide(o);
    }

    public RealNumber add(RealNumber realNumber){
        return new RealNumber(getReal()+realNumber.getReal());
    }

    public RealNumber subtract(RealNumber realNumber){
        return new RealNumber(getReal()-realNumber.getReal());
    }

    public RealNumber multiply(RealNumber realNumber){
        return new RealNumber(getReal()*realNumber.getReal());
    }

    public RealNumber divide(RealNumber realNumber){
        return new RealNumber(getReal()/realNumber.getReal());
    }

}