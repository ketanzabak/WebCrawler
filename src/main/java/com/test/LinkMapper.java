package com.test;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.test.communicator.CommunicatorInterface;
import com.test.communicator.impl.HTTPCommunicator;


public class LinkMapper extends Mapper<Object, Text, Text, Text> {

	private static int maxRecords = 0;

	@Override
	protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {

		/*
		 * This statement will ensure that only first 100 records are processed
		 * .
		 */
		if (LinkMapper.maxRecords > 100) {
			return;
		}

		String linkString = key.toString().trim();
		try {
			
			Text link = new Text();
			link.set(linkString);

			String output = null;

			CommunicatorInterface communicator = new HTTPCommunicator();
			output = communicator.sendMessage(linkString);
			if (output != null) {
				Text outputText = new Text(output);
				LinkMapper.maxRecords += 1;
				context.write(link, outputText);
			}
		} catch (Exception e) {
			/* Use logger instead of System.out.println() */
			System.out.println("Exception while mapping URL : " + linkString + "\nException : " + e);
		}

	}

}
