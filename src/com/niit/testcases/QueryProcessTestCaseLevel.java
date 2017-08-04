package com.niit.testcases;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import com.niit.csvparse.*;
import com.niit.csvparse.dataretreivals.ResultSet;
import com.niit.csvparse.dataretreivals.RowData;
import com.niit.csvparse.queryParser.Query;


public class QueryProcessTestCaseLevel 
{
	static Query query;

	@BeforeClass
	public static void initialize()
	{
		query=new Query();
	}

	@Test
	public void selectAllWithoutWhereTestCase()throws Exception //Test Case 1
	{
		ResultSet dataSet=query.executeQuery("select * from E:/Emp.csv");
		assertNotNull(dataSet);
		System.out.println("select * from E:/Emp.csv");
		display("selectAllWithoutWhereTestCase",dataSet);
	}
	
	@Test
	public void selectColumnsWithoutWhereTestCase()throws Exception //Test Case 2
	{
		ResultSet dataSet=query.executeQuery("select City,Dept,Name from E:/Emp.csv");
		assertNotNull(dataSet);
		System.out.println("select City,Dept,Name from E:/Emp.csv");
		display("selectColumnsWithoutWhereTestCase",dataSet);	
	}
	
	@Test
	public void selectCountColumnsWithoutWhereTestCase()throws Exception //Test Case 9
	{
		ResultSet dataSet=query.executeQuery("select count(Name) from E:/Emp.csv");
		assertNotNull(dataSet);
		System.out.println("select count(Name) from E:/Emp.csv");
		display("selectCountColumnsWithoutWhereTestCase",dataSet);
	}
	
	@Test
	public void selectSumColumnsWithoutWhereTestCase()throws Exception //Test Case 10
	{
		ResultSet dataSet=query.executeQuery("select sum(Salary) from E:/Emp.csv");
		assertNotNull(dataSet);
		System.out.println("select sum(Salary) from E:/Emp.csv");
		display("selectSumColumnsWithoutWhereTestCase",dataSet);
	}
	
	@Test
	public void selectSumColumnsWithWhereTestCase()throws Exception //Test Case 11
	{
	    ResultSet dataSet=query.executeQuery("select sum(Salary) from E:/Emp.csv where City=Bangalore");
		assertNotNull(dataSet);
		System.out.println("select sum(Salary) from E:/Emp.csv where City=Bangalore");
		display("selectSumColumnsWithWhereTestCase",dataSet);
	}
	
	@Test
	public void selectColumnsWithoutWhereWithOrderByTestCase()throws Exception //Test Case 12
	{
		ResultSet dataSet=query.executeQuery("select City,Name,Salary from E:/Emp.csv order by Salary");
		assertNotNull(dataSet);
		System.out.println("select City,Name,Salary from E:/Emp.csv order by Salary");
		display("selectColumnsWithoutWhereWithOrderByTestCase",dataSet);
	}
	
	@Test
	public void selectColumnsWithWhereWithOrderByTestCase()throws Exception //Test Case 13
	{
		ResultSet dataSet=query.executeQuery("select City,Name,Salary from E:/Emp.csv where City=Bangalore order by Salary");
		assertNotNull(dataSet);
		System.out.println("select City,Name,Salary from E:/Emp.csv where City=Bangalore order by Salary");
		display("selectColumnsWith-WhereWithOrderByTestCase",dataSet);
	}
	
	@Test
	public void selectColumnsWithoutWhereWithGroupByCountTestCase()throws Exception //Test Case 14
	{
		ResultSet dataSet=query.executeQuery("select City,count(*) from E:/Emp.csv group by City");
		assertNotNull(dataSet);
		System.out.println("select City,count(*) from E:/Emp.csv group by City");
		displayGroupByRecords("selectColumnsWithoutWhereWithGroupByTestCase",dataSet);
	}
	
	@Test 
	public void selectColumnsWithoutWhereWithGroupBySumTestCase()throws Exception //Test Case 15
	{
		ResultSet dataSet=query.executeQuery("select City,sum(Salary) from E:/Emp.csv group by City");
		assertNotNull(dataSet);
		System.out.println("select City,sum(Salary) from E:/Emp.csv group by City");
		displayGroupByRecords("selectColumnsWithoutWhereWithGroupByTestCase",dataSet);
	}
	
	public void display(String str,ResultSet dataSet)
	{
		System.out.println();
		System.out.println(str);
		System.out.println();
		
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
				System.out.print(dataSet.getAggregateRow().get(columnName)/2+"\t");
			}
		}
	}
	
	public void displayGroupByRecords(String str,ResultSet dataSet4)
	{
		System.out.println();
		System.out.println(str);
		System.out.println();
		
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
}
