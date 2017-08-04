package com.niit.csvparse.queryParser;

import com.niit.csvparse.dataretreivals.ResultSet;
import com.niit.csvparse.queryprocessordao.QueryProcessor;
import com.niit.csvparse.queryprocessorimpl.AggregateQueryProcessor;
import com.niit.csvparse.queryprocessorimpl.GroupByQueryProcessor;
import com.niit.csvparse.queryprocessorimpl.SimpleQueryProcessor;

//class to receive input and send back dataset of the query
public class Query {
	public ResultSet executeQuery(String queryString) throws Exception 
	{
			ResultSet resultSetObject=new ResultSet();
			QueryParser queryParserObject = new QueryParser(queryString);
			QueryParameter queryParameter = new QueryParameter();
			queryParameter = queryParserObject.parseQuery(queryString);
			if(queryParameter.isHasAggregate())
			{
				QueryProcessor aggregateQueryProcessor=new AggregateQueryProcessor();
				resultSetObject=aggregateQueryProcessor.executeQuery(queryParameter);
			}
			else if(queryParameter.isHasGroupBy()||queryParameter.isHasOrderBy())
			{
				QueryProcessor groupByQueryProcessor=new GroupByQueryProcessor();
				resultSetObject=groupByQueryProcessor.executeQuery(queryParameter);
			}
			else
			{
				QueryProcessor simpleQueryProcessor=new SimpleQueryProcessor();
				resultSetObject=simpleQueryProcessor.executeQuery(queryParameter);
			}		
			
			return resultSetObject;
		}
	//return resultSetObject;
	}


//no need to check the query is validate or not
	/*private boolean isValidQueryString(String queryString) {
		if (queryString.contains("select") && queryString.contains("from") || (queryString.contains("where")
				|| queryString.contains("order by") || queryString.contains("group by"))) {
			return true;
		} else {
			return false;
		}
	}
*/
//}
