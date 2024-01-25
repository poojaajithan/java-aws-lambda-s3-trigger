package com.awsprojects.poc.java_aws_lambda_s3_trigger;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class LambdaHandler implements RequestHandler<S3Event,String>{

	private static final String REGION = "ap-south-1";
	AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
												.withRegion(Regions.fromName(REGION))
									            .withCredentials(new DefaultAWSCredentialsProviderChain())
									            .build();
	
	@Override
	public String handleRequest(S3Event s3Event, Context context) {
		String bucketName = s3Event.getRecords().get(0).getS3().getBucket().getName();
		String fileName = s3Event.getRecords().get(0).getS3().getObject().getKey();
		context.getLogger().log("BucketName ::: " + bucketName );
        context.getLogger().log("fileName ::: " + fileName );
        InputStream stream = s3Client.getObject(bucketName,fileName).getObjectContent();
        String content;
		try {
			content = IOUtils.toString(stream, StandardCharsets.UTF_8);
		} 
		catch (IOException e) {
			return "Error while reading file from S3 :::" +e.getMessage();
		}
		
        context.getLogger().log("file content ::: " + content );
        return "successfully read file from s3 bucket";
	}

}
