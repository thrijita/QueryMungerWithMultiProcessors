package com.niit.csvparse.queryprocessorimpl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import com.niit.csvparse.dataretreivals.ResultSet;
import com.niit.csvparse.dataretreivals.RestrictionCondition;
import com.niit.csvparse.dataretreivals.RowData;
import com.niit.csvparse.dataretreivals.RowHeader;
import com.niit.csvparse.queryParser.QueryEvaluatingConditions;
import com.niit.csvparse.queryParser.QueryParameter;
import com.niit.csvparse.queryprocessordao.QueryProcessor;

public class SimpleQueryProcessor implements QueryProcessor {

	@Override
	public ResultSet executeQuery(QueryParameter queryParameterObject) throws IOException {
		QueryEvaluatingConditions queryEvaluator = new QueryEvaluatingConditions();
		ResultSet dataSet = new ResultSet();
		RowHeader headerRow = queryParameterObject.getHeaderRow();
		BufferedReader bufferedReader = new BufferedReader(new FileReader(queryParameterObject.getFilePath()));
		RowData rowData;
		bufferedReader.readLine();
		String rowFetched;
		while ((rowFetched = bufferedReader.readLine()) != null) {
			int count = 0;
			rowData = new RowData();

			String rowValues[] = rowFetched.trim().split(",");
			int columnCount = rowValues.length;

			if (!queryParameterObject.isHasAllColumn()) {
				Set<String> columnNames = headerRow.keySet();
				for (String columnName : queryParameterObject.getColumNames().getColumns()) {
					for (String originalColumnName : columnNames) {
						if (originalColumnName.equals(columnName)) {
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

				}
			} else {
				dataSet.getResultSet().add(rowData);

			}

		}
		return dataSet;
	}
	// return dataSet;
}
