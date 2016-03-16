package org.gaaroth.devlib.renderer;

import com.vaadin.ui.renderers.ButtonRenderer;

@SuppressWarnings("serial")
public class HtmlButtonRenderer extends ButtonRenderer {
	
    public HtmlButtonRenderer() {
        super();
    }
    
    public HtmlButtonRenderer(RendererClickListener listener) {
        this();
        addClickListener(listener);
    }
}
