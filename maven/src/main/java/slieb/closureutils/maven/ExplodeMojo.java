package slieb.closureutils.maven;


import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.List;

@Mojo(name = "explode", requiresProject = true, requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class ExplodeMojo extends AbstractMojo {

    @Parameter(required = true, defaultValue = "${project}", readonly = true)
    private MavenProject project;

    @Parameter()
    private List<String> explodeFilters;

    private File explodeDirectory;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            for (String element : project.getRuntimeClasspathElements()) {
                System.out.println(element);
            }
        } catch (DependencyResolutionRequiredException e) {
            throw new MojoExecutionException("Failed to explode dependencies", e);
        }
    }
}
