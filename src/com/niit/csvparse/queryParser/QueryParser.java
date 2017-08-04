package com.niit.csvparse.queryParser;

import java.io.BufferedReader;

//class for parsing the query
import java.io.FileReader;
import java.util.ArrayList;

import com.niit.csvparse.dataretreivals.RestrictionCondition;
import com.niit.csvparse.dataretreivals.RowHeader;
import com.niit.csvparse.dataretreivals.SelectColumns;
import com.niit.csvparse.queryParser.QueryParameter;

public class QueryParser {
	QueryParameter queryParameterObject = new QueryParameter();

	public QueryParser(String queryString) throws Exception {
		queryParameterObject.setColumNames(new SelectColumns());
		queryParameterObject.setListrelexpr(new ArrayList<RestrictionCondition>());
		queryParameterObject.setLogicalOperator(new ArrayList<String>());
		this.parseQuery(queryString);
		RowHeader headerRow = this.rowHeaderReturn();
	}

	public QueryParameter parseQuery(String queryString) {
		String baseQuery = null, conditionQuery = null, selectcol = null;

		if (queryString.contains("order by")) {
			baseQuery = queryString.split("order by")[0].trim();
			queryParameterObject.setOrderByColumn(queryString.split("order by")[1].trim().toLowerCase());
			if (baseQuery.contains("where")) {
				conditionQuery = baseQuery.split("where")[1].trim();
				this.relationalExpressionProcessing(conditionQuery);
				baseQuery = baseQuery.split("where")[0].trim();
				queryParameterObject.setHasWhere(true);
			}
			queryParameterObject.setFilePath(baseQuery.split("from")[1].trim());
			baseQuery = baseQuery.split("from")[0].trim();
			selectcol = baseQuery.split("select")[1].trim();
			this.fieldsProcessing(selectcol);
			queryParameterObject.setHasOrderBy(true);

		} else if (queryString.contains("group by")) {
			baseQuery = queryString.split("group by")[0].trim();
			queryParameterObject.setGroupByColumn(queryString.split("group by")[1].trim().toLowerCase());
			if (baseQuery.contains("where")) {
				conditionQuery = baseQuery.split("where")[1].trim();
				this.relationalExpressionProcessing(conditionQuery);
				baseQuery = baseQuery.split("where")[0].trim();
				queryParameterObject.setHasWhere(true);
			}
			queryParameterObject.setFilePath(baseQuery.split("from")[1].trim());
			baseQuery = baseQuery.split("from")[0].trim();
			selectcol = baseQuery.split("select")[1].trim();
			this.fieldsProcessing(selectcol);
			queryParameterObject.setHasGroupBy(true);
		} else if (queryString.contains("where")) {
			baseQuery = queryString.split("where")[0];
			conditionQuery = queryString.split("where")[1];
			conditionQuery = conditionQuery.trim();
			queryParameterObject.setFilePath(baseQuery.split("from")[1].trim());
			baseQuery = baseQuery.split("from")[0].trim();
			this.relationalExpressionProcessing(conditionQuery);
			selectcol = baseQuery.split("select")[1].trim();
			this.fieldsProcessing(selectcol);
			queryParameterObject.setHasWhere(true);

		} else {
			baseQuery = queryString.split("from")[0].trim();
			queryParameterObject.setFilePath(queryString.split("from")[1].trim());
			selectcol = baseQuery.split("select")[1].trim();
			this.fieldsProcessing(selectcol);
			queryParameterObject.setHasSimpleQuery(true);
		}
		// return headerRow;
		return queryParameterObject;
		// return rowData;
		/*
		 * queryParameterObject.setHeaderRow(headerRow); return headerRow;
		 */
	}

	private void relationalExpressionProcessing(String conditionQuery) {
		String oper[] = { ">=", "<=", ">", "<", "!=", "=" };

		String relationalQueries[] = conditionQuery.split("\\s+and\\s+|\\s+or\\s+");

		for (String relationQuery : relationalQueries) {
			relationQuery = relationQuery.trim();
			for (String operator : oper) {
				if (relationQuery.contains(operator)) {
					RestrictionCondition restrictcond = new RestrictionCondition();
					restrictcond.setColumn(relationQuery.split(operator)[0].trim());
					restrictcond.setValue(relationQuery.split(operator)[1].trim());
					restrictcond.setOperator(operator);
					queryParameterObject.getRestrictions().add(restrictcond);
					break;
				}
			}
		}
		/*
		 * queryParameterObject.setHeaderRow(headerRow); return headerRow;
		 */
		queryParameterObject.setRestrictions(queryParameterObject.getRestrictions());
		if (queryParameterObject.getRestrictions().size() > 1)
			this.logicalOperatorStore(conditionQuery);
	}

	private void logicalOperatorStore(String conditionQuery) {
		String conditionQueryData[] = conditionQuery.split(" ");
		for (String queryData : conditionQueryData) {
			queryData = queryData.trim();
			if (queryData.equals("and") || queryData.equals("or")) {
				queryParameterObject.getLogicalOperator().add(queryData);
			}
		}
		queryParameterObject.setLogicalOperator(queryParameterObject.getLogicalOperator());
	}

	private void fieldsProcessing(String selectColumn) {
		if (selectColumn.trim().contains("*") && selectColumn.trim().length() == 1) {
			queryParameterObject.setHasAllColumn(true);
		} else {
			String columnList[] = selectColumn.trim().split(",");
			for (String column : columnList) {
				queryParameterObject.getColumNames().getColumns().add(column.trim().toLowerCase());
			}
			if (selectColumn.contains("sum(") || selectColumn.contains("count(") || selectColumn.contains("count(*)")) {
				queryParameterObject.setHasAggregate(true);
				queryParameterObject.setHasAllColumn(true);
			}
		}
	}

	// change the method name
	public RowHeader rowHeaderReturn() throws Exception {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(queryParameterObject.getFilePath()));
		RowHeader headerRow = new RowHeader();

		if (bufferedReader != null) {
			String rowData = bufferedReader.readLine();
			String rowValues[] = rowData.split(",");
			int columnIndex = 0;
			for (String rowvalue : rowValues) {
				headerRow.put(rowvalue.toLowerCase(), columnIndex);
				columnIndex++;
			}
		}
		queryParameterObject.setHeaderRow(headerRow);
		return headerRow;
	}
	/*
	 * return headerRow; return rowData;
	 */

}
