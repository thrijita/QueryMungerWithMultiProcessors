package com.niit.csvparse.queryprocessordao;

import java.io.IOException;

import com.niit.csvparse.dataretreivals.ResultSet;
import com.niit.csvparse.queryParser.QueryParameter;

public interface QueryProcessor {
public ResultSet executeQuery(QueryParameter queryParameterObject) throws IOException;
}
