package com.xten.cluster.common.inject;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Description:
 * User: kongqingyu
 * Date: 2017/10/25
 */
public class ModulesBuilder implements Iterable<Module> {

    private final List<Module> modules = new ArrayList<Module>();

    public ModulesBuilder add(Module... newModules){
        Collections.addAll(this.modules,newModules);
        return this;
    }

    public Iterator<Module> iterator() {
        return modules.iterator();
    }

    public Injector createInjector() {
       return Guice.createInjector(modules);
    }
}
