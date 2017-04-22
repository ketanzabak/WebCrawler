package com.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import com.test.responsehandler.ResponseHandlerInterface;
import com.test.responsehandler.impl.HTTPResponseHandler;

public class LinkReducer extends Reducer<Text, Text, Text, Text> {

	private static List<String> visitedLinks = new ArrayList<String>();
	
	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		
		String link = key.toString();
		String filePath = "";
		Iterator<Text> iterator = values.iterator();
		if (iterator.hasNext()) {
			filePath = iterator.next().toString();
		}
		
		/*
		 * If the link is already visited then do nothing with it .
		 */
		if(visitedLinks.contains(link)){
			return ;
		}else{
			visitedLinks.add(link);
			ResponseHandlerInterface responseHandlerInterface = new HTTPResponseHandler();
			try {
				responseHandlerInterface.processResponseAndExtractLinks(filePath);
			} catch (Exception exception) {
				System.out.println("Exception in reducer : "+exception);
			}
		}
		
	
	}
}
