package com.niit.testcases;


import static org.junit.Assert.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import com.niit.csvparse.*;
import com.niit.csvparse.dataretreivals.ResultSet;
import com.niit.csvparse.dataretreivals.RowData;
import com.niit.csvparse.queryParser.Query;

public class QueryProcessTestCase 
{
	static Query query;

	@BeforeClass
	public static void initialize()
	{
		query=new Query();
	}
	
	@Test
	public void allColumnsWithoutWhereClause()throws Exception
	{
		String queryString="select * from E:\\Emp.csv";
		assertNotNull(query.executeQuery(queryString));
		System.out.println(queryString);
		displayRecords(query.executeQuery(queryString));
	}
	
	@Test
	public void selectedColumnsWithoutWhereClause()throws Exception
	{
		String queryString="select Name,EmpID,Salary from E:\\Emp.csv";
		assertNotNull(query.executeQuery(queryString));
		System.out.println(queryString);
		displayRecords(query.executeQuery(queryString));
	}
	
	@Test
	public void allColumnsWithWhereClauseWithString()throws Exception
	{
		String queryString="select * from E:\\Emp.csv where Name=Will";
		ResultSet dataSet=query.executeQuery(queryString);
		assertNotNull(dataSet.getResultSet().size());
		System.out.println(queryString);
		displayRecords(dataSet);
	}
	
	@Test
	public void allColumnsWithOrderByClause()throws Exception
	{
		String queryString2="select * from E:\\Emp.csv order by Salary";
		ResultSet dataSet2=query.executeQuery(queryString2);
		assertNotNull(dataSet2.getResultSet().get(0));
		System.out.println(queryString2);
		displayRecords(dataSet2);
	}
	
	@Test
	public void aggregateDataDisplay()throws Exception
	{
		String queryString3="select sum(Salary),count(City) from E:\\Emp.csv";
		ResultSet dataSet3=query.executeQuery(queryString3);
		assertNotNull(dataSet3.getAggregateRow());
		System.out.println(queryString3);
		displayRecords(dataSet3);
	}
	
	@Test
	public void aggregateDataDisplayWithWhereClause()throws Exception
	{
		String queryString4="select sum(Salary),count(City) from E:\\Emp.csv where City=Bangalore";
		ResultSet dataSet4=query.executeQuery(queryString4);
		assertNotNull(dataSet4.getAggregateRow());
		System.out.println(queryString4);
		displayRecords(dataSet4);
	}
	
	@Test
	public void groupByDataDisplayWithWhereClause()throws Exception
	{
		String queryString4="select Dept,sum(Salary),count(City) from E:\\Emp.csv where Dept=Sales or Dept=IT group by Dept";
		ResultSet dataSet4=query.executeQuery(queryString4);
		assertNotNull(dataSet4.getAggregateRow());
		System.out.println(queryString4);
		displayGroupByRecords(dataSet4);
	}
	
	public void displayGroupByRecords(ResultSet dataSet4)
	{
		LinkedHashMap<String,LinkedHashMap<String,Float>> groupRows=dataSet4.getTotalGroupedData();
		
		Set<String> groupByColumnValues=groupRows.keySet();
		
		for(String groupByColumnValue:groupByColumnValues)
		{
			LinkedHashMap<String,Float> eachGroupRow=dataSet4.getTotalGroupedData().get(groupByColumnValue);
			System.out.print(groupByColumnValue+"\t");
			Set<String> aggregateColumnNames=eachGroupRow.keySet();
			for(String eachAggregateColumnName:aggregateColumnNames)
			{
				System.out.print(eachGroupRow.get(eachAggregateColumnName)/2+"\t");
			}
			
			System.out.println();
		}
	}
	public void displayRecords(ResultSet dataSet)
	{
		if(dataSet.getAggregateRow().isEmpty())
		{
			for(RowData rowData:dataSet.getResultSet())
			{
				Set<Integer> rowIndex=rowData.keySet();
				for(int index:rowIndex)
				{
					System.out.print(rowData.get(index)+"\t");
				}
				System.out.println();
			}
		}
		else
		{
			System.out.println();
			Set<String> columnNames=dataSet.getAggregateRow().keySet();
			for(String columnName:columnNames)
			{
				System.out.print(columnName+"\t");
			}
			System.out.println();
			for(String columnName:columnNames)
			{
				System.out.print(dataSet.getAggregateRow().get(columnName)/2+"\t\t");
			}
			System.out.println();
		}
	}

}
