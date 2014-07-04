package slieb.closureutils.rendering;

public class SoyHtmlRenderer extends DefaultHtmlRenderer {
//
//    private final SoyTofu.Renderer renderer;
//
//    public SoyHtmlRenderer(SoyTofu.Renderer renderer) {
//        this.renderer = renderer;
//    }
//
//
//    @Nonnull
//    @Override
//    public String render(HtmlRenderOptions options) throws RenderException {
//        renderer.setData(new RendererData(this));
//        return renderer.render();
//    }
//
//    private static class BaseData extends SoyMapData {
//
//        protected final SoyHtmlRenderer soyHtmlRenderer;
//
//        public BaseData(@Nonnull final SoyHtmlRenderer soyHtmlRenderer) {
//            this.soyHtmlRenderer = soyHtmlRenderer;
//        }
//    }
//
//    private static class RendererData extends BaseData {
//
//        private final ScriptsData scriptsData;
//        private final StyleSheetsData styleSheetsData;
//
//        private RendererData(@Nonnull SoyHtmlRenderer soyHtmlRenderer) {
//            super(soyHtmlRenderer);
//            scriptsData = new ScriptsData(soyHtmlRenderer);
//            styleSheetsData = new StyleSheetsData(soyHtmlRenderer);
//        }
//
//
//        public SoyData getSingle(String key) {
//
//            switch (key) {
//                case "Title":
//                    return SoyData.createFromExistingData(
//                            soyHtmlRenderer.renderTitleContent());
//                case "Scripts":
//                    return scriptsData;
//                case "Stylesheets":
//                    return styleSheetsData;
//                case "Content":
//                    return SoyData.createFromExistingData(soyHtmlRenderer
//                            .renderContent());
//            }
//
//            return super.getSingle(key);
//        }
//    }
//
//    private static class ScriptsData extends SoyListData {
//
//        private SoyHtmlRenderer soyHtmlRenderer;
//
//        @Override
//        public String toString() {
//            try {
//                return soyHtmlRenderer.renderScripts();
//            } catch (IOException ioException) {
//                throw new RuntimeException(ioException);
//            }
//        }
//
//        private ScriptsData(@Nonnull final SoyHtmlRenderer soyHtmlRenderer) {
//            super();
//            if (soyHtmlRenderer.scripts != null) {
//                add(Lists.transform(soyHtmlRenderer.scripts,
//                        new Function<Resource, ScriptData>() {
//                            @Nullable
//                            @Override
//                            public ScriptData apply(@Nullable Resource
// input) {
//                                if (input != null) {
//                                    return new ScriptData(input,
//                                            soyHtmlRenderer);
//                                } else {
//                                    return null;
//                                }
//                            }
//                        }));
//            }
//            this.soyHtmlRenderer = soyHtmlRenderer;
//        }
//    }
//
//    private static class StyleSheetsData extends SoyListData {
//
//        private SoyHtmlRenderer soyHtmlRenderer;
//
//        @Override
//        public String toString() {
//            try {
//                return soyHtmlRenderer.renderStylesheets();
//            } catch (IOException ioException) {
//                throw new RuntimeException(ioException);
//            }
//        }
//
//        private StyleSheetsData(
//                @Nonnull final SoyHtmlRenderer soyHtmlRenderer) {
//            super();
//            if (soyHtmlRenderer.stylesheets != null) {
//                add(Lists.transform(soyHtmlRenderer.stylesheets,
//                        new Function<Resource, StylesheetData>() {
//                            @Nullable
//                            @Override
//                            public StylesheetData apply(@Nullable
//                                                        Resource input) {
//                                if (input != null) {
//                                    return new StylesheetData(input,
//                                            soyHtmlRenderer);
//                                } else {
//                                    return null;
//                                }
//                            }
//                        }));
//            }
//            this.soyHtmlRenderer = soyHtmlRenderer;
//        }
//
//        @Override
//        public SoyData get(int index) {
//            Resource stylesheet = this.soyHtmlRenderer.stylesheets.get(index);
//            return new StylesheetData(stylesheet, soyHtmlRenderer);
//        }
//    }
//
//    public static class ScriptData extends BaseData {
//        private final Resource stylesheet;
//
//        public ScriptData(@Nonnull Resource stylesheet,
//                          @Nonnull SoyHtmlRenderer soyHtmlRenderer) {
//            super(soyHtmlRenderer);
//            this.stylesheet = stylesheet;
//        }
//
//        @Override
//        public String toString() {
//            return soyHtmlRenderer.renderScript(stylesheet);
//        }
//    }
//
//    public static class StylesheetData extends BaseData {
//        private final Resource stylesheet;
//
//        public StylesheetData(
//                @Nonnull Resource stylesheet,
//                @Nonnull SoyHtmlRenderer soyHtmlRenderer) {
//            super(soyHtmlRenderer);
//            this.stylesheet = stylesheet;
//        }
//
//        @Override
//        public String toString() {
//            return soyHtmlRenderer.renderStylesheet(stylesheet);
//        }
//    }
}
