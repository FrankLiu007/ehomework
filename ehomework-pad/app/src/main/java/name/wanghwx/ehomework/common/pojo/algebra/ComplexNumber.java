package name.wanghwx.ehomework.common.pojo.algebra;

public class ComplexNumber implements MathNumber{

    private final double real;

    private final double imaginary;

    private double precision = MathNumber.DEFAULT_PRECISION;

    public ComplexNumber(Number real,Number imaginary){
        if(real == null || imaginary == null) throw new IllegalArgumentException("数值不能为null");
        this.real = real.doubleValue();
        this.imaginary = imaginary.doubleValue();
    }

    public double getReal(){
        return real;
    }

    public double getImaginary(){
        return imaginary;
    }

    public void setPrecision(Number precision){
        if(precision == null) throw new IllegalArgumentException("数值不能为null");
        this.precision = precision.doubleValue();
    }

    public double getModulus(){
        return Math.sqrt(real*real+imaginary*imaginary);
    }

    public double getArgument(){
        return Math.atan2(imaginary,real);
    }

    @Override
    public String toString(){
        double absReal = Math.abs(real);
        double absImaginary = Math.abs(imaginary);
        boolean noReal = absReal < MathNumber.DEFAULT_PRECISION;
        boolean noImaginary = absImaginary < MathNumber.DEFAULT_PRECISION;
        boolean omitImaginary = Math.abs(absImaginary-1) < MathNumber.DEFAULT_PRECISION;
        boolean intReal = Math.abs(absReal - Math.round(absReal)) < MathNumber.DEFAULT_PRECISION;
        boolean intImaginary = Math.abs(absImaginary - Math.round(absImaginary)) < MathNumber.DEFAULT_PRECISION;
        boolean positiveImaginary = imaginary > 0;
        boolean showReal = false;
        boolean showImaginary = false;
        StringBuilder format = new StringBuilder();
        if(!noReal || noImaginary){
            format.append("%.").append(intReal?0:2).append("f");
            showReal = true;
        }
        if(!noImaginary){
            if(!omitImaginary){
                format.append("%").append(noReal?"":"+").append(".").append(intImaginary?0:2).append("f");
                showImaginary = true;
            }else{
                format.append(positiveImaginary?noReal?"":"+":"-");
            }
            format.append("i");
        }
        if(showReal){
            return showImaginary?String.format(format.toString(),real,imaginary):String.format(format.toString(),real);
        }else{
            return showImaginary?String.format(format.toString(),imaginary):String.format(format.toString());
        }
    }

    @Override
    public Operable add(Operable o){
        if(!(o instanceof ComplexNumber)) throw new IllegalArgumentException("暂不支持此类运算");
        ComplexNumber c = (ComplexNumber)o;
        return new ComplexNumber(real+c.getReal(),imaginary+c.getImaginary());
    }

    @Override
    public Operable subtract(Operable o){
        if(!(o instanceof ComplexNumber)) throw new IllegalArgumentException("暂不支持此类运算");
        ComplexNumber c = (ComplexNumber)o;
        return new ComplexNumber(real-c.getReal(),imaginary-c.getImaginary());
    }

    @Override
    public Operable multiply(Operable o){
        if(!(o instanceof ComplexNumber)) throw new IllegalArgumentException("暂不支持此类运算");
        ComplexNumber c = (ComplexNumber)o;
        double re = c.getReal();
        double im = c.getImaginary();
        return new ComplexNumber(real*re-imaginary*im,real*im+imaginary*re);
    }

    @Override
    public Operable divide(Operable o){
        if(!(o instanceof ComplexNumber)) throw new IllegalArgumentException("暂不支持此类运算");
        ComplexNumber c = (ComplexNumber)o;
        double re = c.getReal();
        double im = c.getImaginary();
        double m2 = re*re+im*im;
        return new ComplexNumber((real*re+imaginary*im)/m2,(imaginary*re-real*im)/m2);
    }

}