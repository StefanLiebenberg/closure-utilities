package slieb.closureutils.commandline.options;


import org.kohsuke.args4j.Option;

import java.util.Set;

public class BuildOptions extends BaseOptions {

    private Set<String> modules;

    public Set<String> getModules() {
        return modules;
    }

    @Option(name = "--module", aliases = {"-M"}, usage = "specifies specific modules to build. values are soy,css,js,html")
    public void setModules(String modules) {
        this.modules.add(modules);
    }

    private Boolean forced;

    public Boolean getForced() {
        return forced;
    }

    @Option(name = "--forced", aliases = {"-F"}, usage = "forces the builders to run all parts")
    public void setForced(Boolean forced) {
        this.forced = forced;
    }

}
