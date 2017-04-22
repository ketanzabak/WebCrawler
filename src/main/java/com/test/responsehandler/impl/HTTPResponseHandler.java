package com.test.responsehandler.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.Link;
import org.apache.tika.sax.LinkContentHandler;

import com.test.responsehandler.ResponseHandlerInterface;

public class HTTPResponseHandler implements ResponseHandlerInterface{

	/*
	 * This method is used to process response file and extract links from it using Apache Tika .
	 * It return list of links 
	 */
	@Override
	public List<String> processResponseAndExtractLinks(String filePath) throws Exception {
		List<String> result = new ArrayList<>();
		BufferedWriter bufferWriter = null;

		try {

			FileInputStream fileInputStream = new FileInputStream(new File(filePath));
			InputStream inputStream = TikaInputStream.get(fileInputStream);

			bufferWriter = new BufferedWriter(new FileWriter("Input/test_input",true));

			Metadata metadata = new Metadata();

			LinkContentHandler linkhandler = new LinkContentHandler();

			HtmlParser parser = new HtmlParser();

			ParseContext context = new ParseContext();
			parser.parse(inputStream, linkhandler, metadata,context);
			

			List<Link> links = linkhandler.getLinks();
			Iterator<Link> iterator2 = links.iterator();
			bufferWriter.newLine();
			while (iterator2.hasNext()) {
				bufferWriter.write(iterator2.next().getUri());
				bufferWriter.newLine();
			}
			
			fileInputStream.close();
			inputStream.close();
		} catch (Exception exception) {
			System.out.println("Exception in reducer : "+exception);
		} finally {
			/*
			 * close buffer writer .
			 */
			bufferWriter.flush();
			bufferWriter.close();
			
		}
		
		return result ;
	}

}
