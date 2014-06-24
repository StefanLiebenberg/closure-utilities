package slieb.closureutils.build;


public abstract class AbstractBuildResult implements BuildResult {

    private final BuildStatus buildStatus;

    protected AbstractBuildResult(BuildStatus buildStatus) {
        this.buildStatus = buildStatus;
    }

    @Override
    public BuildStatus getBuildStatus() {
        return buildStatus;
    }

    public static abstract class Builder {
        private BuildStatus buildStatus;

        public abstract BuildResult build();

    }
}
