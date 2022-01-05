package name.wanghwx.ehomework.common.util;

import java.util.UUID;

import name.wanghwx.ehomework.common.constant.Default;

public class UUIDUtils{

    public static String generate(){
        return UUID.randomUUID().toString().replace(Default.HYPHEN,Default.EMPTY);
    }

}