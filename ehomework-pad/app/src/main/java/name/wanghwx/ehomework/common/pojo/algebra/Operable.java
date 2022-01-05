package name.wanghwx.ehomework.common.pojo.algebra;

public interface Operable{

    <T extends Operable> T add(Operable o);

    <T extends Operable> T subtract(Operable o);

    <T extends Operable> T multiply(Operable o);

    <T extends Operable> T divide(Operable o);

}