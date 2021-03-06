/*
 * Java
 *
 * Copyright 2019 Sony Corp. All rights reserved.
 * This Software has been designed by MicroEJ Corp and all rights have been transferred to Sony Corp.
 * Sony Corp. has granted MicroEJ the right to sub-licensed this Software under the enclosed license terms.
 */
package com.microej.spresense.demo.widget;

import ej.microui.display.GraphicsContext;
import ej.style.background.Background;
import ej.style.container.Rectangle;
import ej.style.outline.Outline;

/**
 * A background avoiding the padding.
 */
public class PaddedBackground implements Background {

	private final Outline outline;

	/**
	 * Instantiates a {@link PaddedBackground}.
	 * 
	 * @param outline
	 *            the outline to use for the padding.
	 */
	public PaddedBackground(Outline outline) {
		this.outline = outline;
	}

	@Override
	public boolean isTransparent() {
		return false;
	}

	@Override
	public void apply(GraphicsContext g, Rectangle bounds, int color) {
		g.setColor(color);
		g.setBackgroundColor(color);
		outline.unwrap(bounds);
		g.fillRect(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
		outline.wrap(bounds);
	}

}
