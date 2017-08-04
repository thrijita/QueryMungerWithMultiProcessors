package com.niit.csvparse.queryParser;

import java.util.LinkedHashMap;
import java.util.List;

import com.niit.csvparse.dataretreivals.RestrictionCondition;
import com.niit.csvparse.dataretreivals.RowData;
import com.niit.csvparse.dataretreivals.RowHeader;
import com.niit.csvparse.queryParser.QueryParameter;

//class to evaluate the query

public class QueryEvaluatingConditions {
	
	QueryParameter queryParamterObject;
	
	
	/*public QueryEvaluatingConditions(QueryParameter queryParamterObject) {
		super();
		this.queryParamterObject = queryParamterObject;
	}*/
	public LinkedHashMap<String,Float> evaluateAggregateColumns(List<RowData> groupRows,QueryParameter queryParamterObject)
	{
		this.queryParamterObject=queryParamterObject;	
		LinkedHashMap<String,Float> eachGroupRow=new LinkedHashMap<String,Float>();
		
		int numberOfAggregateColumns=queryParamterObject.getColumNames().getColumns().size();
		List<String> actualAggregateColumns=queryParamterObject.getColumNames().getColumns();
	    int countingValue=0,recordCount=0,columnCount=0;
		float sumValue=0,avgValue=0,minValue=0,maxValue=0,countValue=0,countColumnValue=0;

		int groupRowSize=groupRows.size();
		
		while(countingValue<groupRowSize)
		{
			columnCount=0;	
			while(columnCount<numberOfAggregateColumns)
			{
			String aggregateColumnString=actualAggregateColumns.get(columnCount);
			String aggregateColumnName,actualRowValue;
			
			if(!aggregateColumnString.equals(queryParamterObject.getGroupByColumn()))
			{
			aggregateColumnName=aggregateColumnString.substring(aggregateColumnString.indexOf('(')+1,aggregateColumnString.indexOf(')'));
			actualRowValue=groupRows.get(countingValue).get(queryParamterObject.getHeaderRow().get(aggregateColumnName));
			
				if(!evaluateDataType(actualRowValue))
				{
					float rowValue=Float.parseFloat(actualRowValue);
					
					if(countingValue==0)
					{
						minValue=Float.parseFloat(actualRowValue);
						maxValue=Float.parseFloat(actualRowValue);
					}
					
					if(aggregateColumnString.contains("sum("))
					{
						if(eachGroupRow.containsKey(aggregateColumnString))
						{
							sumValue=eachGroupRow.get(aggregateColumnString);
							sumValue=sumValue+rowValue;
							eachGroupRow.put(aggregateColumnString,sumValue);
						}
						else
						{
							eachGroupRow.put(aggregateColumnString,rowValue);
						}
					}
					else if(aggregateColumnString.contains("min("))
					{
							if(minValue>rowValue)
							{
								minValue=rowValue;
							}
							eachGroupRow.put(aggregateColumnString,minValue);
					}
					else if(aggregateColumnString.contains("max("))
					{
							if(maxValue<rowValue)
							{
								maxValue=rowValue;
							}
							eachGroupRow.put(aggregateColumnString,maxValue);
					}
				}
				else if(aggregateColumnString.contains("count(*)"))
				{
					countValue++;
					eachGroupRow.put(aggregateColumnString,countValue);
				}
				
				else if(aggregateColumnString.contains("count("))
				{
					countColumnValue++;
					eachGroupRow.put(aggregateColumnString,countColumnValue);
				}
			 }
				
			columnCount++;
			}
			countingValue++;
		}
		
		return eachGroupRow;
	}
	public boolean evaluateWhereRelationalCondition(QueryParameter queryParamterObject,String rowValues[])
	{
		List<com.niit.csvparse.dataretreivals.RestrictionCondition> listRelationalExpression=queryParamterObject.getRestrictions();
		RowHeader headerRow=queryParamterObject.getHeaderRow();
		List<String> logicalOperators=queryParamterObject.getLogicalOperator();
		
		boolean expression=evaluateEachCondition(listRelationalExpression.get(0),rowValues,headerRow);
		int logicalOperatorCount=logicalOperators.size();
		int count=0;
		
		if(logicalOperatorCount>0)
		{
			while(count<logicalOperatorCount)
			{
				if(logicalOperators.get(count).equals("and"))
				{
					expression=expression && evaluateEachCondition(listRelationalExpression.get(count+1),rowValues,headerRow);
				}
				else
				{
					expression=expression || evaluateEachCondition(listRelationalExpression.get(count+1),rowValues,headerRow);
				}
				
				count++;
			}
		}
		
		return expression;
	}
	
	private  boolean evaluateEachCondition(RestrictionCondition condition,String rowValues[],RowHeader headerRow)
	{
		
		boolean expression=false;
		
		String conditionColumnName=condition.getColumn().trim().toLowerCase();
		String conditionOperator=condition.getOperator();
		String conditionValue=condition.getValue();
			
			String columnValue=rowValues[headerRow.get(conditionColumnName)].trim();
			boolean isString=evaluateDataType(columnValue);
				
				if(isString)
				{
					if(conditionOperator.equals("="))
					{
						if(columnValue.equals(conditionValue))
						{
							expression=true;
						}
					}
					else if(conditionOperator.equals("!="))
					{
						if(!columnValue.equals(conditionValue))
						{
							expression=true;
						}
					}
				}
				else
				{
					float conditionParseValue=Float.parseFloat(conditionValue);
					float rowDataValue=Float.parseFloat(rowValues[queryParamterObject.getHeaderRow().get(conditionColumnName)]);
					switch(conditionOperator)
					{
						case ">=":
							expression=rowDataValue>=conditionParseValue;
							break;
						case "<=":
							expression=rowDataValue<=conditionParseValue;
							break;
						case ">":
							expression=rowDataValue>conditionParseValue;
							break;
						case "<":
							expression=rowDataValue<conditionParseValue;
							break;
						case "=":
							expression=rowDataValue==conditionParseValue;
							break;
						case "!=":
							expression=rowDataValue!=conditionParseValue;
							break;
					}
				}
		
		return expression;
	}
	
	private  boolean evaluateDataType(String value)
	{
		try
		{
			Float f=Float.parseFloat(value);
			return false;
		}
		catch(Exception e)
		{
			return true;
		}
	}
	

}
