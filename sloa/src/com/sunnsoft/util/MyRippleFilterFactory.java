package com.sunnsoft.util;

import org.patchca.color.ColorFactory;
import org.patchca.filter.library.WobbleImageOp;
import org.patchca.filter.predefined.CurvesRippleFilterFactory;

import java.awt.image.BufferedImageOp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 林宇民 Andy (llade)
 *
 */
public class MyRippleFilterFactory extends CurvesRippleFilterFactory {

	protected WobbleImageOp wobble = new WobbleImageOp();

	public MyRippleFilterFactory() {
	}

	public MyRippleFilterFactory(ColorFactory colorFactory) {
		setColorFactory(colorFactory);
	}

	protected List<BufferedImageOp> getPreRippleFilters() {
		List<BufferedImageOp> list = new ArrayList<BufferedImageOp>();
		list.add(this.curves);
		list.add(this.wobble);
		return list;
	}


}
