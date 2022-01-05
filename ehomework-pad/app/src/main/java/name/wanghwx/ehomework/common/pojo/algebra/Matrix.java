package name.wanghwx.ehomework.common.pojo.algebra;

public class Matrix<E extends Operable> implements Operable{

    private final int rowNum;

    private final int columnNum;

    private final Operable[][] elements;

    public static final Matrix<? extends Operable> EMPTY = new Matrix<>(0,0);

    protected Matrix(int rowNum,int columnNum){
        this.rowNum = rowNum;
        this.columnNum = columnNum;
        elements = new Operable[rowNum][columnNum];
    }

    @SuppressWarnings("unchecked")
    public static <T> T getInstance(int rowNum,int columnNum){
        if(rowNum < 0 || columnNum < 0) throw new IllegalArgumentException("矩阵的行数和列数不能为负");
        if(rowNum == 0 || columnNum == 0) return (T)EMPTY;
        return (T)new Matrix(rowNum,columnNum);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getSquare141(int order){
        if(order < 0) throw new IllegalArgumentException("矩阵的行数和列数不能为负");
        if(order == 0) return (T)EMPTY;
        Matrix<RealNumber> matrix = new Matrix<>(order,order);
        for(int i=0;i<order;i++){
            for(int j=0;j<order;j++){
                if(i == j){
                    matrix.setElement(i,j,new RealNumber(4));
                }else if(Math.abs(i-j) == 1){
                    matrix.setElement(i,j,new RealNumber(1));
                }else{
                    matrix.setElement(i,j,new RealNumber(0));
                }
            }
        }
        return (T)matrix;
    }

    public int getRowNum(){
        return rowNum;
    }

    public int getColumnNum(){
        return columnNum;
    }

    @SuppressWarnings("unchecked")
    public E getElement(int i,int j){
        if(i >= rowNum || j >= columnNum) throw new IndexOutOfBoundsException("行数或列数越界");
        return (E)elements[i][j];
    }

    public E setElement(int i,int j,E e){
        E element = getElement(i,j);
        elements[i][j] = e;
        return element;
    }

    public boolean isotype(Matrix<? extends Operable> matrix){
        return rowNum == matrix.rowNum && columnNum == matrix.columnNum;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder(String.format("%d行%d列矩阵：\r\n",rowNum,columnNum));
        for(int i=0;i<rowNum;i++){
            sb.append("[");
            for(int j=0;j<columnNum;j++){
                sb.append(getElement(i,j));
                if(j<columnNum-1) sb.append("|");
            }
            sb.append("]");
            if(i<rowNum-1) sb.append("\r\n");
        }
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Operable> T add(Operable o){
        if(!(o instanceof Matrix)) throw new IllegalArgumentException("只能和矩阵进行加法运算");
        Matrix<? extends Operable> matrix = (Matrix<? extends Operable>)o;
        if(!isotype(matrix)) throw new IllegalArgumentException("只能和同型矩阵进行加法运算");
        Matrix<Operable> result = getInstance(rowNum,columnNum);
        for(int i=0;i<rowNum;i++){
            for(int j=0;j<columnNum;j++){
                result.setElement(i,j,getElement(i,j).add(matrix.getElement(i,j)));
            }
        }
        return (T)result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Operable> T subtract(Operable o){
        if(!(o instanceof Matrix)) throw new IllegalArgumentException("只能和矩阵进行减法运算");
        Matrix<? extends Operable> matrix = (Matrix<? extends Operable>)o;
        if(!isotype(matrix)) throw new IllegalArgumentException("只能和同型矩阵进行减法运算");
        Matrix<Operable> result = getInstance(rowNum,columnNum);
        for(int i=0;i<rowNum;i++){
            for(int j=0;j<columnNum;j++){
                result.setElement(i,j,getElement(i,j).subtract(matrix.getElement(i,j)));
            }
        }
        return (T)result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Operable> T multiply(Operable o){
        Matrix<Operable> result;
        if(o instanceof ComplexNumber){
            result = getInstance(rowNum,columnNum);
            ComplexNumber c = (ComplexNumber)o;
            for(int i=0;i<rowNum;i++){
                for(int j=0;j<columnNum;j++){
                    result.setElement(i,j,getElement(i,j).multiply(c));
                }
            }
            return (T)result;
        }
        if(o instanceof Matrix){
            Matrix<? extends Operable> m = (Matrix<?>)o;
            if(columnNum != m.columnNum) throw new IllegalArgumentException("乘法运算要求第二个矩阵行数等于第一个矩阵列数");
            result = getInstance(rowNum,m.columnNum);
            for(int i=0;i<rowNum;i++){
                for(int j=0;j<columnNum;j++){
                    Operable r = null;
                    for(int k=0;k<columnNum;k++){
                        Operable ok = getElement(i,k).multiply(m.getElement(k,j));
                        if(k == 0){
                            r = ok;
                        }else{
                            r = r.add(ok);
                        }
                    }
                    result.setElement(i,j,r);
                }
            }
            return (T)result;
        }
        throw new IllegalArgumentException("暂不支持此种运算");
    }

    @Override
    public Matrix<? extends Operable> divide(Operable o){
        return null;
    }

    public void rowTransform(Integer r1,RealNumber m1,Integer r2,RealNumber m2){
        for(int j=0;j<columnNum;j++){
            setElement(r1,j,r2==null?(E)getElement(r1,j).multiply(m1):(E)getElement(r1,j).add(getElement(r2,j).multiply(m2)));
        }
    }

}