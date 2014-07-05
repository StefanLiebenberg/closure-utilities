package slieb.closureutils.commandline.options;


import org.kohsuke.args4j.Option;

import java.util.HashSet;
import java.util.Set;

public class BuildOptions extends BaseOptions {


    private Set<String> modules = new HashSet<>();

    public Set<String> getModules() {
        return modules;
    }

    @Option(name = "--module", aliases = {"-M"}, usage = "specifies specific modules to build. values are soy,css,js,html")
    public void setModules(String module) {
        this.modules.add(module);
    }

    @Option(name = "--forced", aliases = {"-F"}, usage = "forces the builders to run all parts")
    protected Boolean forced;

    public Boolean getForced() {
        return forced;
    }


}
