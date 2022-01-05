package name.wanghwx.ehomework.common.pojo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Step<T>{

    private List<T> extra;

    private List<T> absence;

    public Step(Collection<T> extra,Collection<T> absence){
        if(extra != null) this.extra = new ArrayList<>(extra);
        if(absence != null) this.absence = new ArrayList<>(absence);
    }

    public List<T> getExtra() {
        return extra;
    }

    public List<T> getAbsence() {
        return absence;
    }
}