## Introduction

There are some errors that are just too important to miss. [Amazon SNS](http://aws.amazon.com/sns/)
is a very easy-to-use PubSub service that can send you e-mails or texts (or be
added to an SQS queue so you can do your own processing on it). This log4j
appender lets you easily set a threshold past which logged errors will be sent
to an SNS topic.


## Usage
log4j.xml or log4j.properties

	    <!-- SNS events -->
		<appender name="SNS" class="org.log.SNSAppender">
			<param name="Threshold" value="ERROR" />
			<param name="ProxyHost" value="" /> <!-- option -->
			<param name="ProxyPort" value="" />                 <!-- option -->
			<param name="Region" value="us-west-2" />               <!-- option -->
			<param name="Subject" value="[Example] Test" />         <!-- option -->
		    <param name="TopicName" value="example" />
			<layout class="org.log.StackTraceLayout"/>
		</appender>
	
		<!-- ======================= -->
		<!-- Setup the Root category -->
		<!-- ======================= -->
		
		<root>
			<appender-ref ref="SNS"/>
		</root>

3. example code

        private Logger log = LoggerFactory.getLogger(Example.class);
    	log.error("API錯誤", e);

4. example message

		Subject: [Example] Test
		
		
		API錯誤
		
		class java.lang.NullPointerException:null
			 at org.example.getId(Example.java:52)
			 at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
			 at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:57)
			 at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
			 at java.lang.reflect.Method.invoke(Method.java:606)
			 at org.ApiResponseProxy.invoke(ApiResponseProxy.java:31)
			 at com.sun.proxy.$Proxy22.getEndorseTopList(Unknown Source)
			 at Test.main(Test.java:11)


