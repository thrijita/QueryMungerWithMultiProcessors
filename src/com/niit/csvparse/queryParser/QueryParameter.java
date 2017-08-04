package com.niit.csvparse.queryParser;

import java.util.List;

import com.niit.csvparse.dataretreivals.RestrictionCondition;
import com.niit.csvparse.dataretreivals.RowHeader;
import com.niit.csvparse.dataretreivals.SelectColumns;


//Class for only fields ,properties and getters&setters
public class QueryParameter {
	private String filePath;
	private String orderByColumn,groupByColumn;

	 private SelectColumns columNames;
	 private List<RestrictionCondition> restrictions;
	 private boolean hasGroupBy=false,hasOrderBy=false,hasWhere=false,hasAllColumn=false,hasColumn=false,hasSimpleQuery,hasAggregate=false;
     private List<String> logicalOperator;
 
	public List<RestrictionCondition> getRestrictions() {
		return restrictions;
	}

	public void setRestrictions(List<RestrictionCondition> restrictions) {
		this.restrictions = restrictions;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public void setOrderByColumn(String orderByColumn) {
		this.orderByColumn = orderByColumn;
	}

	public void setGroupByColumn(String groupByColumn) {
		this.groupByColumn = groupByColumn;
	}

	public void setHasGroupBy(boolean hasGroupBy) {
		this.hasGroupBy = hasGroupBy;
	}

	public void setHasOrderBy(boolean hasOrderBy) {
		this.hasOrderBy = hasOrderBy;
	}

	public void setHasWhere(boolean hasWhere) {
		this.hasWhere = hasWhere;
	}

	public void setHasAllColumn(boolean hasAllColumn) {
		this.hasAllColumn = hasAllColumn;
	}

	public void setHasColumn(boolean hasColumn) {
		this.hasColumn = hasColumn;
	}

	public void setHasSimpleQuery(boolean hasSimpleQuery) {
		this.hasSimpleQuery = hasSimpleQuery;
	}

	public void setHasAggregate(boolean hasAggregate) {
		this.hasAggregate = hasAggregate;
	}

	public void setHeaderRow(RowHeader headerRow) {
		this.headerRow = headerRow;
	}

	public void setLogicalOperator(List<String> logicalOperator) {
		this.logicalOperator = logicalOperator;
	}

	private RowHeader headerRow;
	
	
	public String getFilePath() {
		return filePath;
	}

	public String getGroupByColumn() {
		return groupByColumn;
	}

	public boolean isHasGroupBy() {
		return hasGroupBy;
	}

	public boolean isHasOrderBy() {
		return hasOrderBy;
	}

	public boolean isHasWhere() {
		return hasWhere;
	}

	public boolean isHasAllColumn() {
		return hasAllColumn;
	}

	public boolean isHasColumn() {
		return hasColumn;
	}

	public boolean isHasSimpleQuery() {
		return hasSimpleQuery;
	}

	public boolean isHasAggregate() {
		return hasAggregate;
	}
	
	public RowHeader getHeaderRow() 
	{
		return headerRow;
	}

	public List<String> getLogicalOperator() {
		return logicalOperator;
	}

	public SelectColumns getColumNames() {
		return columNames;
	}

	public void setColumNames(SelectColumns columnNames) {
		this.columNames = columnNames;
	}

	public List<RestrictionCondition> getListrelexpr() {
		return restrictions;
	}

	public void setListrelexpr(List<RestrictionCondition> listrelexpr) {
		this.restrictions = listrelexpr;
	}

	public String getOrderByColumn() 
	{
		return orderByColumn;
	}

}
