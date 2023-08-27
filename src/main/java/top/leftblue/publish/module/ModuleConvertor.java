package top.leftblue.publish.module;

import top.leftblue.publish.dto.MWACmd;

public abstract class ModuleConvertor<E> {

    public abstract E from(MWACmd mwaCmd);

}
