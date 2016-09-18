package utlight.seckill.dto;

/**
 *  分页数据对象
 * @author liusha
 *
 */
public class Pager {

	 private int  pageIndex;//当前页数
	 private int  pageSize = 3;//每一页的记录数,默认为3
	 private int  totalPages;//有多少页
	 private double  totalCount;//总记录数
	 
	 
	 
	public Pager(int pageIndex, int pageSize, double totalCount) {
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
		this.totalCount = totalCount;
		this.totalPages = (int)Math.ceil(totalCount/pageSize);
		
	}



	public int getPageIndex() {
		return pageIndex;
	}



	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}



	public int getPageSize() {
		return pageSize;
	}



	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}



	public int getTotalPages() {
		return totalPages;
	}



	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}



	public double getTotalCount() {
		return totalCount;
	}



	public void setTotalCount(double totalCount) {
		this.totalCount = totalCount;
	}



	@Override
	public String toString() {
		return "Pager [pageIndex=" + pageIndex + ", pageSize=" + pageSize + ", totalPages=" + totalPages
				+ ", totalCount=" + totalCount + "]";
	}
	  
}
