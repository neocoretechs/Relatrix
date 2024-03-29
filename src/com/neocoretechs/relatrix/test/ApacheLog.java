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
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.neocoretechs.rocksack.session.DatabaseManager;
import com.neocoretechs.relatrix.DuplicateKeyException;
import com.neocoretechs.relatrix.Relatrix;

/**
 * Process the apache log files and place in a Relatrix database
 * @author jg
 *
 */
public class ApacheLog {
	private static final String dateForm = "dd/MMM/yyyy:HH:mm:ss Z";
	boolean DEBUG = false;

	/*Log file fields*/
	String remoteHost = null;
	String shouldBDash = null;
	String requestTime = null;
	String requestMethodUrl = null;
	String remoteUser = null;
	String clientRequest = null;
	String httpStatusCode = null;
	String numBytes = null;
	String referer = null;
	String userAgent = null;
	
	String Os = null;
	String OsVer = null;

	private long accessLogEntryEpoch;
	
	private int totalRecords = 0;

	SimpleDateFormat accesslogDateFormat = new SimpleDateFormat(dateForm);
	Pattern accessLogPattern = Pattern.compile(getAccessLogRegex(),Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	Matcher accessLogEntryMatcher;
	/**
	* Structures Apache combined access log
	*@return regex
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
	public void getFiles(String dir) throws IOException, ParseException, IllegalAccessException, ClassNotFoundException {
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
				processPayload(load);
			} catch (DuplicateKeyException e) {
				System.out.println("Attempt to store duplicate domain/map key "+e);
				e.printStackTrace();
			}
		}
		//Relatrix.transactionCommit();
		System.out.println("FINISHED! with "+totalRecords+" processed");
	}
	
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
	
	public void processPayload(byte[] pl) throws IOException, ParseException, DuplicateKeyException, ClassNotFoundException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new DataInputStream(new ByteArrayInputStream(pl))));
		String line = "";
		while((line = br.readLine()) != null) {
			try {
				readAndProcess(line);
				Comparable rel = Relatrix.store(accessLogEntryEpoch,"accessed by",remoteHost);
				Relatrix.store(rel, "remote user", remoteUser); // unreliable info field remoteUser
				//Relatrix.transactionalStore(rel, "access time",accessLogEntryEpoch);
				Relatrix.store(rel, "client request",clientRequest);
				Relatrix.store(rel, "http status",httpStatusCode);
				Relatrix.store(rel, "bytes returned",numBytes);
				Relatrix.store(rel, "referer",referer);
				Relatrix.store(rel, "user agent",userAgent);
				Relatrix.store(rel, "OS",Os);
				Relatrix.store(rel,"OS Ver.",OsVer);
			} catch(ParseException pe) { //its in chinese
				
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		br.close();
	}
	public void readAndProcess(String line) throws ParseException {
		//int index = 0;

		accessLogEntryMatcher = accessLogPattern.matcher(line);
		if(!accessLogEntryMatcher.matches()) {
			System.out.println("Can't parse:"+line );
			throw new ParseException(line +" : couldn't be parsed", 0);
			//continue;
		}
		else
		{
			remoteHost = accessLogEntryMatcher.group(1);
			remoteUser = accessLogEntryMatcher.group(2); // unreliable info in this field
			requestTime=accessLogEntryMatcher.group(4);
			accessLogEntryEpoch = (accesslogDateFormat.parse(requestTime)).getTime();
			//System.out.println("Got time:"+accessLogEntryEpoch);
			clientRequest = (String)accessLogEntryMatcher.group(5);
			httpStatusCode = accessLogEntryMatcher.group(6);
			numBytes = accessLogEntryMatcher.group(7);
			referer = accessLogEntryMatcher.group(9);
			String userAgents[] = accessLogEntryMatcher.group(11).split(" ");
			userAgent = userAgents[0];
			if( userAgents.length > 1)
				Os = userAgents[1].replace("(".subSequence(0,1), "".subSequence(0, 0));
			else
				Os = "N/A";
			if( userAgents.length > 2)
				OsVer = userAgents[2];
			else
				OsVer = "N/A";
			System.out.println(toString());
			++totalRecords;
			//	System.out.println("" + index + " : " +(remoteUser.split(" "))[1]);
			//	for(index = 0; index < accessLogEntryMatcher.groupCount(); index++) {
			//	System.out.println("Line num : " + index + " " +
			//			accessLogEntryMatcher.group(index) );
			//	}
		}

	}
	public String toString() {
		return "Remote host:"+remoteHost+" Remote user:"+remoteUser+" Request time:"+accessLogEntryEpoch+" Client Request:"+clientRequest+
				" Status:"+httpStatusCode+" Referer:"+referer+" User Agent:"+userAgent+" Os:"+Os+" ver:"+OsVer;
	}
	public static void main(String[] args) throws ParseException, IOException, IllegalAccessException, ClassNotFoundException {
		String lin = "203.106.155.51 www.neocoretechs.com - [21/Jul/2013:01:18:11 -0400] ";
		lin += "\"GET /favicon.ico HTTP/1.1\" 200 894 \"http://lizahanum.blogspot.com/2011/02/kebab-daging.html\" ";
		lin += "\"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.6 (KHTML, like Gecko) Chrome/16.0.899.0 Safari/535.6\" \"-\"";
		DatabaseManager.setTableSpaceDir("C:/users/jg/Relatrix/logs");
		ApacheLog alfoo = new ApacheLog();
		//alfoo.readAndProcess(lin);
		alfoo.getFiles(args[0]);

	}

}
