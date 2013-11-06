package com.ticketmaster.example.commons.persistence.paginators;


import java.util.List;

/**
 * 
 * An abstract paginator, independent from the underlying database implementation.
 * It provides pagination functionality such as getting page data for next page,previous page and current page.
 * @param <T> type of object being paginated by Paginator
 */
public abstract class AbstractPaginator<T> {

	public static final int DISPLAY_ALL		= -100;
	/**
	 * Defines the page size
	 */
	private int pageSize = -1;
	/**
	 * Defines the query count
	 */
	private int queryCount = -1;
	/**
	 * Defines current start offset 
	 */
	private int currentStartOffset = 0;
	/**
	 * Defines current end offset
	 */
	private int currentEndOffset = 0;
	/**
	 * Defines current page size
	 */
	private int currentPageSize = 0;
	/**
	 * true if current page is already queried
	 */
	private boolean currentPageQueried = false;
	
	/**
	 * Creates an instance with page size
	 * @param pageSize 
	 */
	protected AbstractPaginator(int pageSize) {
		this.pageSize = pageSize;
		currentStartOffset = 0;
		currentEndOffset = 0;
		currentPageSize = 0;
		currentPageQueried = false;
	}
	
	/**
	 * Concrete paginator responsible for loading query count
	 * @return int represents total count
	 */
	public abstract int loadQueryCount();
	
	/**
	 * Concrete paginator responsible for queying current page
	 * @return List<T> represents paginator contents
	 */
	protected abstract List<T> queryCurrentPage();
	
	/**
	 * Getting the query count
	 * @param int represents total query count
	 */
	public int getQueryCount() {
		if(queryCount == -1) {
			queryCount = loadQueryCount();
		}
		return queryCount;
	}
	
	/**
	 * Getting current start offset
	 * @return currentStartOffset
	 */
	public int getCurrentStartOffset() {
		return currentStartOffset;
	}
	
	/**
	 * Getting current end offset
	 * @return currentEndOffset
	 */
	public int getCurrentEndOffset() {
		return currentEndOffset;
	}
	
	/**
	 * Determines if paginator has any more before pages
	 * @returns true if current start offset greater then zero
	 */
	public boolean hasMoreBefore()  {
		return currentStartOffset > 0;
	}
	
	/**
	 * Determines if paginator has any more next pages
	 * @returns true if current end offset less then total query count
	 */
	public boolean hasMoreAfter()  {
		return currentEndOffset < getQueryCount();
	}
	
	/**
	 * Adjusting end offset 
	 * It also sets currentEndOffset / current page size to zero if page is null OR empty
	 * @param page represents input data being part of page
	 */
	protected void adjustEndOffset(List<T> page) {
		if(page == null || page.isEmpty()) {
			currentEndOffset = 0;
			currentPageSize = 0;
		} else {
			currentPageSize = page.size();
			currentEndOffset = currentStartOffset + currentPageSize;	
		}
		currentPageQueried = true;
	}
	
	
	/**
	 * Getting current page size
	 * @return int represents current page size
	 */
	protected int getCurrentPageSize() {
		return currentPageSize;
	}
	
	/**
	 * Get the current page data being paginated
	 * @return List<T> represents page data
	 */
	public List<T> getCurrentPage()  {
		return queryCurrentPage();
	}
	
	
	/**
	 * Getting first page data
	 * @return List<T> represents current page data
	 */
	public List<T> getFirstPage() {
		currentStartOffset = 0;
		return queryCurrentPage();
	}
	
	/**
	 * Getting previous page information
	 * It also checks If It has any previous page 
	 * It also adjust current start offset by subtracting page size 
	 * @param List<T> data by querying current page
	 * @see #hasMoreBefore()
	 */
	public List<T> getPreviousPage()  {
		if(!hasMoreBefore()) {
			return null;
		}
		currentStartOffset -= pageSize;
		if(currentStartOffset < 0) {
			currentStartOffset = 0;
		}
		return queryCurrentPage();
	}
	
	/**
	 * Getting next page information
	 * It also check If it has any next page otherwise returns null 
	 * It also adjust current start offset by incrementing page size 
	 * @param List<T> data by querying current page
	 * @see #hasMoreAfter()
	 */
	public List<T> getNextPage()  {
		if(!currentPageQueried) {
			// First call
			return queryCurrentPage();
		} else {
			if(!hasMoreAfter()) {
				return null;
			}
			currentStartOffset += pageSize;
			return queryCurrentPage();
		}
	}
	
	/**
	 * Getting last page information 
	 * It also adjust current start offset result from subtraction of current end offset and page size 
	 * @param List<T> data by querying current page
	 * @see #getFirstPage()
	 */
	public List<T> getLastPage()  {
		currentEndOffset = getQueryCount();
		currentStartOffset = currentEndOffset - pageSize;
		if(currentStartOffset < 0) {
			return getFirstPage();
		}
		return queryCurrentPage();
	}
}
