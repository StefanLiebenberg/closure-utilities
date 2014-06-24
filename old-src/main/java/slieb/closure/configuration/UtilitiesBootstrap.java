package slieb.closure.configuration;


import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import static com.google.common.collect.Lists.newArrayList;

public class UtilitiesBootstrap {

    public static Iterable<Module> getModules() {
        return newArrayList();
    }

    public static Injector getInjector() {
        return Guice.createInjector(getModules());
    }
}
