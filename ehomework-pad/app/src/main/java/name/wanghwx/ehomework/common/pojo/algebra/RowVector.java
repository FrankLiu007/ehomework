package name.wanghwx.ehomework.common.pojo.algebra;

public class RowVector<E extends Operable> extends Matrix<E>{

    public RowVector(E... elements){
        super(1,elements.length);
        for(int j=0;j<elements.length;j++){
            setElement(j,elements[j]);
        }
    }

    public int getDimension(){
        return getColumnNum();
    }

    public E getElement(int index){
        return getElement(0,index);
    }

    public E setElement(int index,E e){
        return super.setElement(0,index,e);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder("行向量：(");
        for(int j=0;j<getColumnNum();j++){
            sb.append(getElement(0,j));
            if(j<getColumnNum()-1) sb.append(",");
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public <T extends Operable> T add(Operable o){
        if(!(o instanceof RowVector)) throw new IllegalArgumentException("只能和行向量进行加法运算");
        RowVector<? extends Operable> rowVector = (RowVector<? extends Operable>)o;
        if(getDimension() != rowVector.getDimension()) throw new IllegalArgumentException("只能和同维行向量进行加法运算");
        Operable[] elements = new Operable[getDimension()];
        for(int j=0;j<getDimension();j++){
            elements[j] = getElement(j).add(rowVector.getElement(j));
        }
        return (T)new RowVector<>(elements);
    }

    @Override
    public <T extends Operable> T subtract(Operable o){
        if(!(o instanceof RowVector)) throw new IllegalArgumentException("只能和行向量进行减法运算");
        RowVector<? extends Operable> rowVector = (RowVector<? extends Operable>)o;
        if(getDimension() != rowVector.getDimension()) throw new IllegalArgumentException("只能和同维行向量进行减法运算");
        Operable[] elements = new Operable[getDimension()];
        for(int j=0;j<getDimension();j++){
            elements[j] = getElement(j).subtract(rowVector.getElement(j));
        }
        return (T)new RowVector<>(elements);
    }

    @Override
    public <T extends Operable> T multiply(Operable o){
        if(o instanceof ComplexNumber){
            ComplexNumber c = (ComplexNumber)o;
            Operable[] elements = new Operable[getDimension()];
            for(int j=0;j<getDimension();j++){
                elements[j] = getElement(j).multiply(c);
            }
            return (T)new RowVector<>(elements);
        }
//        if(o instanceof Matrix){
//            Matrix<? extends Operable> m = (Matrix<?>)o;
//            if(columnNum != m.columnNum) throw new IllegalArgumentException("乘法运算要求第二个矩阵行数等于第一个矩阵列数");
//            result = getInstance(rowNum,m.columnNum);
//            for(int i=0;i<rowNum;i++){
//                for(int j=0;j<columnNum;j++){
//                    Operable r = null;
//                    for(int k=0;k<columnNum;k++){
//                        Operable ok = getElement(i,k).multiply(m.getElement(k,j));
//                        if(k == 0){
//                            r = ok;
//                        }else{
//                            r = r.add(ok);
//                        }
//                    }
//                    result.setElement(i,j,r);
//                }
//            }
//            return (T)result;
//        }
        throw new IllegalArgumentException("暂不支持此种运算");
    }

    @Override
    public Matrix<? extends Operable> divide(Operable o) {
        return super.divide(o);
    }
}