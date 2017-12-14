package org.log;

import org.apache.log4j.Appender;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

import com.amazonaws.AmazonClientException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSAsync;
import com.amazonaws.services.sns.AmazonSNSAsyncClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.util.StringUtils;

/**
 * @author paul.chen
 *
 */
public class SNSAppender extends AppenderSkeleton implements Appender {

	private String proxyHost;

	private int proxyPort;

	private String region;

	private String topicName;

	private String topicArn;

	private String subject;

	private AmazonSNSAsync snsAsyncClient;

	public void close() {
		snsAsyncClient.shutdown();
		this.closed = true;
	}

	public boolean requiresLayout() {
		return true;
	}

	public void activateOptions() {
		String region = this.getRegion();
		AmazonSNSAsyncClientBuilder builder = AmazonSNSAsyncClientBuilder.standard().withRegion(region)
				.withCredentials(new ProfileCredentialsProvider());

		if (proxyHost != null && proxyHost != "") {
			ClientConfiguration clientConfiguration = new ClientConfiguration();
			clientConfiguration.setProxyHost(proxyHost);
			clientConfiguration.setProxyPort(proxyPort);
			builder.withClientConfiguration(clientConfiguration);
		}

		snsAsyncClient = builder.build();
		if(!StringUtils.isNullOrEmpty(topicName)) topicArn = snsAsyncClient.createTopic(topicName).getTopicArn();
	}

	@Override
	protected void append(LoggingEvent event) {

		String logMessage;
		if (layout != null) {
			logMessage = layout.format(event);
		} else {
			logMessage = event.getRenderedMessage();
		}

		PublishRequest publishRequest = new PublishRequest(topicArn, logMessage, subject);

		try {
			snsAsyncClient.publish(publishRequest);
		} catch (AmazonClientException ase) {
			LogLog.error("Could not log to SNS", ase);
		}
	}

	public String getProxyHost() {
		return proxyHost;
	}

	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	public int getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}

	public String getRegion() {
		if (StringUtils.isNullOrEmpty(region)) {
			return Regions.DEFAULT_REGION.getName();
		} else {
			return region;
		}
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

}
