package com.neocoretechs.relatrix.test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.neocoretechs.rocksack.session.DatabaseManager;
import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.Relatrix;
import com.neocoretechs.relatrix.Result;

/**
 * Process the apache log files and place in a Relatrix database.<p/>
 * Store the relationship (accessLogEntryEpoch,"accessed by",remoteHost) and use this relationship
 * as the domain of the remainder of the log entry data.<p/>
 * Comparable rel = Relatrix.store(accessLogEntryEpoch,"accessed by",remoteHost);<br/>
 * Relatrix.store(rel, "access time",accessLogEntryEpoch);<br/>
 * Relatrix.store(rel, "client request",clientRequest);<br/>
 * Relatrix.store(rel, "http status",httpStatusCode);<br/>
 * Relatrix.store(rel, "bytes returned",numBytes);<br/>
 * If we are using the more complex format, store additional relationships:<br/>
 * Relatrix.store(rel, "remote user", remoteUser); // unreliable info field remoteUser<br/>
 * Relatrix.store(rel, "referer",referer);<br/>
 * Relatrix.store(rel, "user agent",userAgent);<br/>
 * Relatrix.store(rel, "OS",Os);<br/>
 * Relatrix.store(rel,"OS Ver.",OsVer);<br/>
 * You could then use something like :<dd/>
 * Iterator it = Relatrix.findSet('*',"accessed by",'*');<br>
 * it.forEachRemaining(e->{<br/>
 *			Iterator it2 = null;<br/>
 *			try {<br/>
 *				it2 = Relatrix.findSet(((Result)e).get(),'?','?');<br/>
 *			} catch (Exception e1) {<br/>
 *				e1.printStackTrace();<br/>
 *			} <br/>
 *			it2.forEachRemaining(System.out::println);<br/>
 *		});<br/>
 * This allows us to select sets of primary relationships, then subsets of related data. Our
 * retrieval is robust despite differing log formats, thus showing the power of the relationship structure.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2020,2024
 *
 */
public class ApacheLog {
	private static final String dateForm = "dd/MMM/yyyy:HH:mm:ss Z";
	static boolean DEBUG = false;
	/*Log file fields*/
	static String remoteHost = null;
	String shouldBDash = null;
	String requestTime = null;
	static Long accessLogEntryEpoch;
	String requestMethodUrl = null;
	static String remoteUser = null;
	static String clientRequest = null;
	static Integer httpStatusCode = null;
	static Integer numBytes = null;
	static String referer = null;
	static String userAgent = null;
	static String Os = null;
	static String OsVer = null;
	
	private int totalRecords = 0;
	static int cnt, cnt2 = 0;
	static Result result;
	private static long tims;

	SimpleDateFormat accesslogDateFormat = new SimpleDateFormat(dateForm);
	Pattern accessLogPattern = Pattern.compile(getAccessLogRegex(),Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	Pattern accessLogPattern2 = Pattern.compile(getAccessLogRegex2(),Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	Matcher accessLogEntryMatcher;
	/**
	* 203.106.155.51 www.neocoretechs.com - [21/Jul/2013:01:18:11 -0400] 
	* "GET /favicon.ico HTTP/1.1" 
	* 200 894 "http://lizahanum.blogspot.com/2011/02/kebab-daging.html"
	* "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.6 (KHTML, like Gecko) Chrome/16.0.899.0 Safari/535.6" "-";
	*/
	private String getAccessLogRegex() {
		String clientHost = "^([\\d.]+)"; // Client IP, remote host
		String shouldBDash = " (\\S+)"; // -
		String clientRequest = " (\\S+)"; // Client request
		String requestTime = " \\[([\\w:/]+\\s[+\\-]\\d{4})\\]"; // Date
		String requestMethodUrl = " \"(.+?)\""; // request method and url
		String httpStatusCode = " (\\d{3})"; // HTTP code
		String numOfBytes = " (\\d+|(.+?))"; // Number of bytes
		String referer = " \"([^\"]+|(.+?))\""; // Referer
		String agent = " \"([^\"]+|(.+?))\""; // Agent
		return clientHost+shouldBDash+clientRequest+requestTime+requestMethodUrl+httpStatusCode+numOfBytes+referer+agent;
	}
	/**
	 * 199.72.81.55 - - [01/Jul/1995:00:00:01 -0400] "GET /history/apollo/ HTTP/1.0" 200 6245
	 */
	private String getAccessLogRegex2() {
		String clientHost = "^([\\S]+)"; // Client 
		String shouldBDash = " (\\S+)"; // -
		String shouldBDash2 = " (\\S+)"; // -
		String requestTime = " \\[([\\w:/]+\\s[+\\-]\\d{4})\\]"; // Date
		String requestMethodUrl = " \"(.+?)\""; // request method and url
		String httpStatusCode = " (\\d{3})"; // HTTP code
		String numOfBytes = " (\\d+|(.+?))"; // Number of bytes
		return clientHost+shouldBDash+shouldBDash2+requestTime+requestMethodUrl+httpStatusCode+numOfBytes;
	}
	/**
	 * Use the regex patterns to parse out specific fields in the data
	 * @param line
	 * @throws ParseException
	 */
	public void readAndProcess(String line) throws ParseException {
		//int index = 0;
		if(DEBUG)
			System.out.println("Line "+totalRecords+"="+line);
		
		accessLogEntryMatcher = accessLogPattern.matcher(line);
		
		if(!accessLogEntryMatcher.matches() && !accessLogEntryMatcher.lookingAt()) {
			System.out.println("Can't parse:"+line );
				throw new ParseException(line +" : couldn't be parsed", 0);
		} else {
			if(!accessLogEntryMatcher.matches())
				System.out.println("Only some can be parsed!!");
			try {
				remoteHost = accessLogEntryMatcher.group(1);
				if(DEBUG)
					System.out.println("Got host:"+remoteHost);
			} catch(IllegalStateException e) {
				System.out.println("Cant get host:");
			}
			try {
				remoteUser = accessLogEntryMatcher.group(2); // unreliable info in this field
				if(DEBUG)
					System.out.println("Got user:"+remoteUser);
			} catch(IllegalStateException e) {
				System.out.println("Cant get remote user!:");
			}
			try {
				requestTime= accessLogEntryMatcher.group(4);
				if(DEBUG)
					System.out.println("Got request time:"+requestTime);
			} catch(IllegalStateException e) {
				System.out.println("Cant get request time!:");
			}
			try {
				accessLogEntryEpoch = (accesslogDateFormat.parse(requestTime)).getTime();
				if(DEBUG)
					System.out.println("Got epoch:"+accessLogEntryEpoch);
			} catch(IllegalStateException e) {
				System.out.println("Cant get epoch!:");
			}
			try {
				clientRequest = (String)accessLogEntryMatcher.group(5);
				if(DEBUG)
					System.out.println("Got client:"+clientRequest);
			} catch(IllegalStateException e) {
				System.out.println("Cant get client!:");
			}
			try {
				httpStatusCode = Integer.parseInt(accessLogEntryMatcher.group(6));
				if(DEBUG)
					System.out.println("Got http stat:"+httpStatusCode);
			} catch(IllegalStateException e) {
				System.out.println("Cant get http status!:");
			}
			try {
				numBytes = Integer.parseInt(accessLogEntryMatcher.group(7));
				if(DEBUG)
					System.out.println("Got numBytes:"+numBytes);
			} catch(IllegalStateException e) {
				System.out.println("Cant get number bytes!:");
			}
			try {
				referer = accessLogEntryMatcher.group(9);
				if(DEBUG)
					System.out.println("Got referer:"+referer);
			} catch(IllegalStateException e) {
				System.out.println("Cant get referer!:");
			}
			try {
				String userAgents[] = accessLogEntryMatcher.group(11).split(" ");
				if(DEBUG)
					System.out.println("Got userAgents:"+Arrays.toString(userAgents));
				userAgent = userAgents[0];
				if( userAgents.length > 1)
					Os = userAgents[1].replace("(".subSequence(0,1), "".subSequence(0, 0));
				else
					Os = "N/A";
				if( userAgents.length > 2)
					OsVer = userAgents[2];
				else
					OsVer = "N/A";
			} catch(IllegalStateException e) {
				System.out.println("Cant get user agents!:");
			}

			++totalRecords;
			if(DEBUG)
				System.out.println(totalRecords+".) "+toString());
			//	System.out.println("" + index + " : " +(remoteUser.split(" "))[1]);
			//	for(index = 0; index < accessLogEntryMatcher.groupCount(); index++) {
			//	System.out.println("Line num : " + index + " " +
			//			accessLogEntryMatcher.group(index) );
			//	}
		}

	}
	/**
	 * Use the regex patterns to parse out specific fields in the data
	 * @param line
	 * @throws ParseException
	 */
	public void readAndProcess2(String line) throws ParseException {
		//int index = 0;
		if(DEBUG)
			System.out.println("Line "+totalRecords+"="+line);
		
		accessLogEntryMatcher = accessLogPattern2.matcher(line);
		
		if(!accessLogEntryMatcher.matches() && !accessLogEntryMatcher.lookingAt()) {
			System.out.println("Can't parse:"+line );
				throw new ParseException(line +" : couldn't be parsed", 0);
		} else {
			if(!accessLogEntryMatcher.matches())
				System.out.println("Only some can be parsed!!");
			try {
				remoteHost = accessLogEntryMatcher.group(1);
				if(DEBUG)
					System.out.println("Got host:"+remoteHost);
			} catch(IllegalStateException e) {
				System.out.println("Cant get host:");
			}
			try {
				requestTime= accessLogEntryMatcher.group(4);
				if(DEBUG)
					System.out.println("Got request time:"+requestTime);
			} catch(IllegalStateException e) {
				System.out.println("Cant get request time!:");
			}
			try {
				accessLogEntryEpoch = (accesslogDateFormat.parse(requestTime)).getTime();
				if(DEBUG)
					System.out.println("Got epoch:"+accessLogEntryEpoch);
			} catch(IllegalStateException e) {
				System.out.println("Cant get epoch!:");
			}
			try {
				clientRequest = (String)accessLogEntryMatcher.group(5);
				if(DEBUG)
					System.out.println("Got client:"+clientRequest);
			} catch(IllegalStateException e) {
				System.out.println("Cant get client!:");
			}
			try {
				httpStatusCode = Integer.parseInt(accessLogEntryMatcher.group(6));
				if(DEBUG)
					System.out.println("Got http stat:"+httpStatusCode);
			} catch(IllegalStateException e) {
				System.out.println("Cant get http status!:");
			}
			try {
				// a 404 status gives us a - for number bytes
				String snum = accessLogEntryMatcher.group(7);
				if(snum.equals("-"))
					numBytes = 0;
				else
					numBytes = Integer.parseInt(snum);
				if(DEBUG)
					System.out.println("Got numBytes:"+numBytes);
			} catch(IllegalStateException e) {
				System.out.println("Cant get number bytes!:");
			}
	
			++totalRecords;
			if(DEBUG)
				System.out.println(totalRecords+".) "+toString());
			//	System.out.println("" + index + " : " +(remoteUser.split(" "))[1]);
			//	for(index = 0; index < accessLogEntryMatcher.groupCount(); index++) {
			//	System.out.println("Line num : " + index + " " +
			//			accessLogEntryMatcher.group(index) );
			//	}
		}

	}
	/**
	 * Process all files in the given directory
	 * @param dir directory with log files
	 * @param simplified true if using simplified log format
	 * @throws IOException
	 * @throws ParseException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public void getFiles(String dir, boolean simplified) throws IOException, ParseException, IllegalAccessException, ClassNotFoundException {
		Path path = FileSystems.getDefault().getPath(dir);
		DirectoryStream<Path> files = Files.newDirectoryStream(path); 
		Iterator<Path> it = files.iterator();
		while(it.hasNext()) {
			Path targFile = it.next();
			if( targFile.toFile().isDirectory() )
				continue;
			System.out.print("Processing "+targFile);
			FileInputStream fis = new FileInputStream(targFile.toFile());
			byte[] load = null;
			if( targFile.toString().endsWith(".zip")) {
				//System.out.println("Processing Zip..");
				load = unzipFile(targFile.toString());
			} else {
				//System.out.println("Processing non-zip");
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				while(fis.available() > 0) baos.write(fis.read());
				baos.flush();
				load = baos.toByteArray();
				baos.close();
				fis.close();
			}
			System.out.println(" payload: "+load.length);
			try {
				if(simplified)
					processPayload2(load);
				else
					processPayload(load);
			} catch (DuplicateKeyException e) {
				System.out.println("Attempt to store duplicate domain/map key "+e);
				e.printStackTrace();
			}
		}
		//Relatrix.transactionCommit();
		System.out.println("FINISHED! with "+totalRecords+" processed");
	}
	/**
	 * Unzip the specified file
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public byte[] unzipFile(String filePath) throws IOException{
	         
	        FileInputStream fis = null;
	        ZipInputStream zipIs = null;
	        ZipEntry zEntry = null;
	        fis = new FileInputStream(filePath);
	        zipIs = new ZipInputStream(new BufferedInputStream(fis));
	        ByteArrayOutputStream baos = null;
	        while((zEntry = zipIs.getNextEntry()) != null){
	                    byte[] tmp = new byte[4096];
	                    baos = new ByteArrayOutputStream();
	                    int size = 0;
	                    while((size = zipIs.read(tmp)) != -1){
	                        baos.write(tmp, 0 , size);
	                    }
	                    baos.flush();
	                    baos.close();
	        }
	        zipIs.close();
	        return baos.toByteArray();
	}
	/**
	 * Parse and store a line of data
	 * @param pl
	 * @throws IOException
	 * @throws ParseException
	 * @throws DuplicateKeyException
	 * @throws ClassNotFoundException
	 */
	public void processPayload(byte[] pl) throws IOException, ParseException, DuplicateKeyException, ClassNotFoundException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new DataInputStream(new ByteArrayInputStream(pl))));
		String line = "";
		tims = System.currentTimeMillis();
		while((line = br.readLine()) != null) {
			try {
				readAndProcess(line);
				storeRelatrix();
				if((System.currentTimeMillis()-tims) > 5000) {
					System.out.println("Processed "+totalRecords+" current:"+toString());
					tims = System.currentTimeMillis();
				}
			} catch(ParseException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		br.close();
	}
	/**
	 * Parse and store a line of simplified log data
	 * @param pl
	 * @throws IOException
	 * @throws ParseException
	 * @throws DuplicateKeyException
	 * @throws ClassNotFoundException
	 */
	public void processPayload2(byte[] pl) throws IOException, ParseException, DuplicateKeyException, ClassNotFoundException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new DataInputStream(new ByteArrayInputStream(pl))));
		String line = "";
		tims = System.currentTimeMillis();
		while((line = br.readLine()) != null) {
			try {
				readAndProcess2(line);
				storeRelatrix2();
				if((System.currentTimeMillis()-tims) > 5000) {
					System.out.println("Processed "+totalRecords+" current:"+toString());
					tims = System.currentTimeMillis();
				}
			} catch(ParseException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		br.close();
	}
	/**
	 * Store the parsed data as a relationship, then as a series of relationships that contain
	 * the primary relationship as the domain of the subsequent relationships. This allows us to select sets
	 * of primary relationships, then subsets of related data.
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private static void storeRelatrix() throws IllegalAccessException, ClassNotFoundException, IOException {
		try {
			Comparable<?> rel = Relatrix.store(accessLogEntryEpoch,"accessed by",remoteHost);
			Relatrix.store(rel, "remote user", remoteUser);
			// unreliable info field remoteUser
			Relatrix.store(rel, "access time",accessLogEntryEpoch);
			Relatrix.store(rel, "client request",clientRequest);
			Relatrix.store(rel, "http status",httpStatusCode);
			Relatrix.store(rel, "bytes returned",numBytes);
			Relatrix.store(rel, "referer",referer);
			Relatrix.store(rel, "user agent",userAgent);
			Relatrix.store(rel, "OS",Os);
			Relatrix.store(rel,"OS Ver.",OsVer);
		} catch (DuplicateKeyException e) {}
	}
	
	/**
	 * Store the simplified parsed data as a relationship, then as a series of relationships that contain
	 * the primary relationship as the domain of the subsequent relationships. This allows us to select sets
	 * of primary relationships, then subsets of related data.
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private static void storeRelatrix2() throws IllegalAccessException, ClassNotFoundException, IOException {
		try {
			Comparable<?> rel = Relatrix.store(accessLogEntryEpoch,"accessed by",remoteHost);
			Relatrix.store(rel, "access time",accessLogEntryEpoch);
			Relatrix.store(rel, "client request",clientRequest);
			Relatrix.store(rel, "http status",httpStatusCode);
			Relatrix.store(rel, "bytes returned",numBytes);
		} catch (DuplicateKeyException e) {}
	}

	public String toString() {
		return "Remote host:"+remoteHost+" Remote user:"+remoteUser+" Request time:"+accessLogEntryEpoch+" Client Request:"+clientRequest+
				" Status:"+httpStatusCode+" Referer:"+referer+" User Agent:"+userAgent+" Os:"+Os+" ver:"+OsVer;
	}
	
	/**
	 * usage java com.neocoretechs.relatrix.test.ApacheLog <tablespace dir> [log file dir] [true]
	 * Option is to use a directory of log files or a sample test set of canned hardcoded lines. Omit directory to use test lines.
	 * argument 3 indicates use expanded, more complex log file format similar to synthetic test data
	 * @param args
	 * @throws ParseException
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws DuplicateKeyException 
	 */
	public static void main(String[] args) throws ParseException, IOException, IllegalAccessException, ClassNotFoundException {
		String lin = "203.106.155.51 www.neocoretechs.com - [21/Jul/2013:01:18:11 -0400] ";
		lin += "\"GET /favicon.ico HTTP/1.1\" 200 894 \"http://lizahanum.blogspot.com/2011/02/kebab-daging.html\" ";
		lin += "\"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.6 (KHTML, like Gecko) Chrome/16.0.899.0 Safari/535.6\" \"-\"";
		
		String lin2 = "203.106.155.51 www.neocoretechs.com - [21/Jul/2013:01:20:01 -0400] ";
		lin2 += "\"GET /faticon.ico HTTP/1.1\" 200 894 \"http://lizahanum.blogspot.com/2011/02/hummus.html\" ";
		lin2 += "\"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.6 (KHTML, like Gecko) Chrome/16.0.899.0 Safari/535.6\" \"-\"";
		
		String lin3 = "203.106.155.51 www.neocoretechs.com - [21/Jul/2013:01:25:21 -0400] ";
		lin3 += "\"GET /flabicon.ico HTTP/1.1\" 200 894 \"http://lizahanum.blogspot.com/2011/02/falafel.html\" ";
		lin3 += "\"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.6 (KHTML, like Gecko) Chrome/16.0.899.0 Safari/535.6\" \"-\"";
		
		// get either the test line or a directory of log files, assume simple log format unless we have extra cmdl arg
		ApacheLog alfoo = new ApacheLog();
		if(args.length == 2 || args.length == 3) {
			DatabaseManager.setTableSpaceDir(args[0]);
			if(args.length == 3)
				alfoo.getFiles(args[1], false);
			else
				alfoo.getFiles(args[1], true);
		} else {
			if(args.length == 1) {
				DatabaseManager.setTableSpaceDir(args[0]);
				alfoo.readAndProcess(lin);
				storeRelatrix();
				alfoo.readAndProcess(lin2);
				storeRelatrix();
				alfoo.readAndProcess(lin3);
				storeRelatrix();
			} else {
				System.out.println("usage java com.neocoretechs.relatrix.test.ApacheLog <tablespace dir> [log file dir]");
			}	
		}
		System.out.println("Stored..now retrieving stored data as a series of relations:");
		tims = System.currentTimeMillis();
		// now display the results processed by the input
		// If we provide ranges for wildcard qualifiers, we can obtain a set sorted in order of those qualifiers
		// in the case of tailSet, we provide lower bounds and elements will be retrieved in order starting from the lower bounds
		// retrieve all identity relationships that contain the concrete object specified
		Iterator<?> it = Relatrix.findTailSet('*',"accessed by",'*', new Long(0),"");
		// If the order does not matter, we can merely specify findSet to retrieve randomly ordered elements
		// Iterator it = Relatrix.findSet("*","accessed by","*");
		// Iterate all the retrieved identity relationships
		it.forEachRemaining(e->{
			//System.out.println(++cnt+".) Primary relation:"+e);
			Iterator<?> it2 = null;
			// findSet returns Result as the lambda, which contains components of the relationships
			result = (Result) e;
			// use the identity as the first element to retrieve related elements
			try {
				it2 = Relatrix.findSet(result.get(),'?','?');
			} catch (Exception e1) {
				e1.printStackTrace();
			} 
			// If there are any elements related to the identity, display them
			cnt2 = 0;
			// break the identity relationship object out into a list of its components
			List<?> l = Relatrix.resolve(result.get());
			// display the primary relationship and each element it is related to
			it2.forEachRemaining(/*System.out::println*/ e2->{
				++cnt2;
				if(DEBUG)
					System.out.println(cnt2+".) "+Arrays.toString(l.toArray())+" has "+e2);
				else
					if((System.currentTimeMillis()-tims) > 5000) {
						System.out.println("Processed "+cnt2+" current:"+cnt2+".) "+Arrays.toString(l.toArray())+" has "+e2);
						tims = System.currentTimeMillis();
					}
			});
			if(DEBUG)
				System.out.println("-----------------");
		});
		System.out.println("End of stored data.");
		System.exit(0);
	}

}
