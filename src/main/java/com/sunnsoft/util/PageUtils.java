package com.sunnsoft.util;

import com.sunnsoft.util.constants.PageConstants;

public class PageUtils {

	public static int defaultPageNumber(Integer pageNumber) {
		if (pageNumber == null) {
			return PageConstants.DEFAULT_PAGE_NUMBER;
		}
		return pageNumber;
	}

	public static int defaultPageSize(Integer pageSize) {
		if (pageSize == null) {
			return PageConstants.DEFAULT_PAGE_SEZE;
		}
		return pageSize;
	}

}
