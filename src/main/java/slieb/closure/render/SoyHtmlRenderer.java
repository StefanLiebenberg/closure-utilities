package slieb.closure.render;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.template.soy.SoyFileSet;
import com.google.template.soy.data.SoyData;
import com.google.template.soy.data.SoyListData;
import com.google.template.soy.data.SoyMapData;
import com.google.template.soy.tofu.SoyTofu;
import slieb.closure.tools.FS;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

public class SoyHtmlRenderer extends DefaultHtmlRenderer {

    private final SoyTofu.Renderer renderer;

    public SoyHtmlRenderer(@Nonnull final Collection<File> sourceDirectories,
                           @Nonnull final String templateName) {
        SoyFileSet.Builder soyFileSetBuilder = new SoyFileSet.Builder();
        for (File sourceFile : FS.find(sourceDirectories, "soy")) {
            soyFileSetBuilder.add(sourceFile);
        }
        final SoyFileSet fileSet = soyFileSetBuilder.build();
        final SoyTofu soyTofu = fileSet.compileToTofu();
        this.renderer = soyTofu.newRenderer(templateName);
    }


    @Nonnull
    @Override
    public String render() throws RenderException {
        renderer.setData(new RendererData(this));
        return renderer.render();
    }

    private static class BaseData extends SoyMapData {

        protected final SoyHtmlRenderer soyHtmlRenderer;

        public BaseData(@Nonnull final SoyHtmlRenderer soyHtmlRenderer) {
            this.soyHtmlRenderer = soyHtmlRenderer;
        }
    }

    private static class RendererData extends BaseData {

        private final ScriptsData scriptsData;
        private final StyleSheetsData styleSheetsData;

        private RendererData(@Nonnull SoyHtmlRenderer soyHtmlRenderer) {
            super(soyHtmlRenderer);
            scriptsData = new ScriptsData(soyHtmlRenderer);
            styleSheetsData = new StyleSheetsData(soyHtmlRenderer);
        }


        public SoyData getSingle(String key) {

            switch (key) {
                case "Title":
                    return SoyData.createFromExistingData(
                            soyHtmlRenderer.renderTitleContent());
                case "Scripts":
                    return scriptsData;
                case "Stylesheets":
                    return styleSheetsData;
                case "Content":
                    return SoyData.createFromExistingData(soyHtmlRenderer
                            .renderContent());
            }

            return super.getSingle(key);
        }
    }

    private static class ScriptsData extends SoyListData {

        private SoyHtmlRenderer soyHtmlRenderer;

        @Override
        public String toString() {
            try {
                return soyHtmlRenderer.renderScripts();
            } catch (IOException ioException) {
                throw new RuntimeException(ioException);
            }
        }

        private ScriptsData(@Nonnull final SoyHtmlRenderer soyHtmlRenderer) {
            super();
            if (soyHtmlRenderer.scripts != null) {
                add(Lists.transform(soyHtmlRenderer.scripts,
                        new Function<File, ScriptData>() {
                            @Nullable
                            @Override
                            public ScriptData apply(@Nullable File input) {
                                if (input != null) {
                                    return new ScriptData(input,
                                            soyHtmlRenderer);
                                } else {
                                    return null;
                                }
                            }
                        }));
            }
            this.soyHtmlRenderer = soyHtmlRenderer;
        }
    }

    private static class StyleSheetsData extends SoyListData {

        private SoyHtmlRenderer soyHtmlRenderer;

        @Override
        public String toString() {
            try {
                return soyHtmlRenderer.renderStylesheets();
            } catch (IOException ioException) {
                throw new RuntimeException(ioException);
            }
        }

        private StyleSheetsData(
                @Nonnull final SoyHtmlRenderer soyHtmlRenderer) {
            super();
            if (soyHtmlRenderer.stylesheets != null) {
                add(Lists.transform(soyHtmlRenderer.stylesheets,
                        new Function<File, StylesheetData>() {
                            @Nullable
                            @Override
                            public StylesheetData apply(@Nullable
                                                        File input) {
                                if (input != null) {
                                    return new StylesheetData(input,
                                            soyHtmlRenderer);
                                } else {
                                    return null;
                                }
                            }
                        }));
            }
            this.soyHtmlRenderer = soyHtmlRenderer;
        }

        @Override
        public SoyData get(int index) {
            File stylesheet = this.soyHtmlRenderer.stylesheets.get(index);
            return new StylesheetData(stylesheet, soyHtmlRenderer);
        }
    }

    public static class ScriptData extends BaseData {
        private final File stylesheet;

        public ScriptData(@Nonnull File stylesheet,
                          @Nonnull SoyHtmlRenderer soyHtmlRenderer) {
            super(soyHtmlRenderer);
            this.stylesheet = stylesheet;
        }

        @Override
        public String toString() {
            return soyHtmlRenderer.renderScript(stylesheet);
        }
    }

    public static class StylesheetData extends BaseData {
        private final File stylesheet;

        public StylesheetData(
                @Nonnull File stylesheet,
                @Nonnull SoyHtmlRenderer soyHtmlRenderer) {
            super(soyHtmlRenderer);
            this.stylesheet = stylesheet;
        }

        @Override
        public String toString() {
            return soyHtmlRenderer.renderStylesheet(stylesheet);
        }
    }
}
