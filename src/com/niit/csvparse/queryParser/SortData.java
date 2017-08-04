package com.niit.csvparse.queryParser;

import java.util.Comparator;

import com.niit.csvparse.dataretreivals.RowData;


public class SortData implements Comparator<RowData> 
{
	private int sortingIndex;
	
	public int getSortingIndex() 
	{
		return sortingIndex;
	}

	public void setSortingIndex(int sortingIndex) 
	{
		this.sortingIndex = sortingIndex;
	}

	@Override
	public int compare(RowData arg0, RowData arg1) 
	{
		return arg0.get(sortingIndex).compareTo(arg1.get(sortingIndex));
	}

}
