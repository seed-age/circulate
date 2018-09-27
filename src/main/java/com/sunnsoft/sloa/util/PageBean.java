package com.sunnsoft.sloa.util;

import java.util.List;

/**
 * 分页工具类
 * @author chenjian
 *
 * @param <T>
 */
public class PageBean {

	private int pageNum;  //当前页
	private int pageSize;  //每页显示记录数
	private int totalSize;  //总的记录数
	
	
	private int totalPage;  //总页数, 需要通过 pageSize 和 totalSize 计算可以得出
	
	private int startIndex;  //开始索引
	
	private List<?> list;  //每页要显示的数据放在集合中
	
	
	private int start;  //分页显示的开始数
	private int end;  //分页显示的结束数
	
	public PageBean(int pageNum, int pageSize, int totalSize) {
		super();
		this.pageNum = pageNum;
		this.pageSize = pageSize;
		this.totalSize = totalSize;
		
		//计算总页数
		if(totalSize % pageSize == 0) {
			//如果整除, 说明只有一页
			this.totalPage = totalSize / pageSize;
		}else {
			//如果不能整除, 证明有多余的数
			this.totalPage = totalSize / pageSize + 1;
		}
		
		//开始索引
		this.startIndex = (pageNum - 1) * pageSize;
		this.start = 1; //开始页
		this.end = 10;	//结束页
		
		//显示5页
		if(totalPage <= 10) {
			//总页数都小于5, 那么end 就为总页数的值了
			this.end = this.totalPage;
		}else {
			//总页数大于 5, 那么就要根据当前是第几页, 来判断 start 和 end 为多少了
			this.start = pageNum - 2;
			this.end = pageNum + 2;
			
			if(start < 0) {
				this.start = 1;
				this.end = 10;
			}
			
			if(end > this.totalPage) {
				this.end = totalPage;
				this.start = end - 10 ;
			}
		}
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}
}
