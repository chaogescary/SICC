package com.pinyougou.search.service;

import java.util.Map;

public interface ItemSearchService {
	/**
	 * @param searchMap
	 * 搜索
	 */
	public Map<String,Object> search(Map searchMap);
}
