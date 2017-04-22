package com.test.responsehandler;

import java.util.List;

public interface ResponseHandlerInterface {

	public List<String> processResponseAndExtractLinks(String filePath) throws Exception ;
}
