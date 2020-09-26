import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class Test2 {


	String accessKey = "AWS_ACCESS_KEY";
	String secreteKey = "AWS_SECRET_KEY";

	String bucket = "nbcu-domo";
	String fileName = "%s-data000.gz";
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	public void checkFile(){   

		ClientConfiguration configuration = new ClientConfiguration();
		configuration.setProxyHost("proxy.inbcu.com");
		configuration.setProxyPort(80);

		AWSCredentials credentials = new BasicAWSCredentials(accessKey, secreteKey);
		AmazonS3 s3client = new AmazonS3Client(credentials, configuration);

		//boolean fileFound = s3client.doesObjectExist("nbcu-domo", "ListenFirst/NBCU_AdSales_ListenFirst_20171009.csv");
		//System.out.println(fileFound);
		//s3client.getObject(new GetObjectRequest("nbcu-domo", "ListenFirst/NBCU_AdSales_ListenFirst_20171009.csv"), new File("listenfirst.csv"));

		ListObjectsV2Request listObjectsV2Request = new ListObjectsV2Request()
			    .withBucketName(bucket)
			    .withPrefix("ListenFirst/")
			    .withMaxKeys(1000)
				;
			ListObjectsV2Result result = s3client.listObjectsV2(listObjectsV2Request);

			for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
			     System.out.println(objectSummary.getKey()+"\t"+objectSummary.getLastModified());
			}
		
		/*boolean fileFound = s3client.doesObjectExist(bucket, fileName);
		
		if(fileFound){
			System.out.println("File found!");
			System.exit(0);
		}else{
			System.out.println("File not found!");
			System.exit(1);
		}*/
	}

	public static void main(String[] args) throws ParseException, java.text.ParseException {

		try{
			new Test2().checkFile();
		}
		catch (Exception e){
			System.out.println("Failed!");
			System.out.println(Test2.getStackTrace(e));
			System.exit(2);
		}
	}
	
	synchronized public static String getStackTrace(Throwable aThrowable) {
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		aThrowable.printStackTrace(printWriter);
		return result.toString();
	}

}
