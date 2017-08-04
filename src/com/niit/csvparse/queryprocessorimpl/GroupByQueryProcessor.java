package com.niit.csvparse.queryprocessorimpl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.niit.csvparse.dataretreivals.ResultSet;
import com.niit.csvparse.dataretreivals.RowData;
import com.niit.csvparse.dataretreivals.RowHeader;
import com.niit.csvparse.queryParser.QueryEvaluatingConditions;
import com.niit.csvparse.queryParser.QueryParameter;
import com.niit.csvparse.queryParser.SortData;
import com.niit.csvparse.queryprocessordao.QueryProcessor;

public class GroupByQueryProcessor implements QueryProcessor{


	@Override
	public ResultSet executeQuery(QueryParameter queryParameterObject) throws IOException {
		
		QueryEvaluatingConditions queryEvaluator = new QueryEvaluatingConditions();
		ResultSet dataSet = new ResultSet();
		RowHeader headerRow = queryParameterObject.getHeaderRow();

		BufferedReader bufferedReader = new BufferedReader(new FileReader(queryParameterObject.getFilePath()));
		RowData rowData;
		bufferedReader.readLine();
		String rowFetched;
		
		Set<String> columnNames = headerRow.keySet();
		while ((rowFetched = bufferedReader.readLine()) != null) {
			int count = 0;
			rowData = new RowData();

			String rowValues[] = rowFetched.trim().split(",");
			int columnCount = rowValues.length;
		
			if (!queryParameterObject.isHasAllColumn()) {
				
				for (String columnName : queryParameterObject.getColumNames().getColumns()) {
					for (String actualColumnName : columnNames) {
						if (actualColumnName.equals(columnName)) {
							rowData.put(headerRow.get(columnName), rowValues[headerRow.get(columnName)].trim());
						}
					}
				}
			} else {
				while (count < columnCount) {
					rowData.put(count, rowValues[count].trim());
					count++;
				}
			}

			if (queryParameterObject.isHasWhere()) {
				if (queryEvaluator.evaluateWhereRelationalCondition(queryParameterObject, rowValues)) {
					dataSet.getResultSet().add(rowData);
					String groupByColumnValue = rowData.get(headerRow.get(queryParameterObject.getGroupByColumn()));
					List<RowData> dataValues = null;
					if (queryParameterObject.isHasGroupBy()) {
						
						if (dataSet.getGroupByData().containsKey(groupByColumnValue)) {
							dataValues = dataSet.getGroupByData().get(groupByColumnValue);
							dataValues.add(rowData);
						} else {
							dataValues = new ArrayList<RowData>();
							dataValues.add(rowData);
						}
						dataSet.getGroupByData().put(groupByColumnValue, dataValues);
					}
				}
			} else {
				dataSet.getResultSet().add(rowData);

				if (queryParameterObject.isHasGroupBy()) {
					String groupByColumnValue = rowData.get(headerRow.get(queryParameterObject.getGroupByColumn()));
					List<RowData> dataValues = null;
					if (dataSet.getGroupByData().containsKey(groupByColumnValue)) {
						dataValues = dataSet.getGroupByData().get(groupByColumnValue);
						dataValues.add(rowData);
					} else {
						dataValues = new ArrayList<RowData>();
						dataValues.add(rowData);
					}
					dataSet.getGroupByData().put(groupByColumnValue, dataValues);
				}
			}

		}

		if (queryParameterObject.isHasOrderBy()) {
			SortData sortData = new SortData();
			sortData.setSortingIndex(queryParameterObject.getHeaderRow().get(queryParameterObject.getOrderByColumn()));
			Collections.sort(dataSet.getResultSet(), sortData);
		}
		return dataSet;
	}
	//return dataSet;
}
