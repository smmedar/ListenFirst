import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.util.List;


import java.util.*;

import java.text.*;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cloudfront.model.Paths;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import freemarker.core.ParseException;

public class S3Object {

	//String accessKey = "AKIAI6HWRI57PCWXW7MQ";
	//String secreteKey = "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";
	String accessKey = "AKIAJDDOGHFPJGNGDLMQ";
	String secreteKey = "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";
	static String start_date;


	String bucket = "nbcu-domo";
	static String saveDirectory;
	String filePath;

	void downloadS3Object() throws IOException{   

		filePath = "ListenFirst/NBCU_AdSales_ListenFirst_"+start_date+".csv";

		ClientConfiguration configuration = new ClientConfiguration();
		configuration.setProxyHost("proxy.inbcu.com");
		configuration.setProxyPort(80);

		AWSCredentials credentials = new BasicAWSCredentials(accessKey, secreteKey);
		
		AmazonS3 s3client = new AmazonS3Client(credentials, configuration);
		File file = new File(saveDirectory+filePath.split("/")[1]);
		s3client.getObject(new GetObjectRequest(bucket, filePath), file);
		
		List<String> lines = Files.readAllLines(java.nio.file.Paths.get(file.getAbsolutePath()));
		
		lines.remove(0);
		
		Files.write(java.nio.file.Paths.get(file.getAbsolutePath().replace(".csv", "_no_header.csv")), lines);
		
		Files.delete(java.nio.file.Paths.get(file.getAbsolutePath()));
		
	}

	public static void main(String[] args) throws ParseException, java.text.ParseException {

		try{

			String line = System.getProperty("arguments");
			if(line != null) {
				Map<String, String> arguments;
				arguments = new HashMap<>();

				String str[] = line.split(",");
				for(int i=0;i<str.length;i++){
					String arr[] = str[i].split("=");
					arguments.put(arr[0].trim(), arr[1].trim());
				}


				if(arguments.get("start_date")==null)
					System.out.println( " start_date is missing from the arguments list");
				else
				{ 
					
					start_date = arguments.get("start_date");
					/*SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
					Date date=sdf.parse(arguments.get("start_date"));

					Calendar c = Calendar.getInstance();
					c.setTime(date);
					c.add(Calendar.DATE,-1);
					start_date = sdf.format(c.getTime());
*/
					if(arguments.get("saveDirectory")==null)
						System.out.println("saveDirectory is missing in the argument list");

					else{
						saveDirectory=(arguments.get("saveDirectory"));
						S3Object s3Object=  new S3Object();
						s3Object.downloadS3Object();
						System.out.println(s3Object.filePath+" is successfully pulled from \""+s3Object.bucket+"\" bucket and saved at "+saveDirectory);
					}

				}
			}
		}
		catch (Exception e){
			System.out.println("Process failed!");
			System.out.println(Test2.getStackTrace(e));
		}
		finally{
		}
	}
}