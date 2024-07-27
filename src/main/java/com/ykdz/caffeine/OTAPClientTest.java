package com.ykdz.caffeine;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.otapclient.*;
import com.otapclient.EBaseClientFunc;
import com.otapclient.LogFunc;
import com.otapclient.OTAPClientNative;

import java.util.Map;
import java.util.HashMap;

import java.lang.StringBuffer;

/**
 * @ClassName OTAPClientTest
 * @Description TODO
 * @Author huangpei8
 * @Date 2023/3/1
 * @Version 2.0
 **/
public class OTAPClientTest {
    private static Logger logger = LoggerFactory.getLogger(OTAPClientTest.class);
    public static boolean m_isAllEvent = false;
    public static Map m_EventTypeMap = new HashMap();

    public static void main(String[] args) {
		logger.info("demo start");
        //初始化 创建客户端
        OTAPClientNative.init();
        int dwHandle = OTAPClientNative.createIOTClient();
        logger.info("句柄：" + dwHandle);

        //接收到的数据回调
        EBaseClientFunc eBaseClientFunc = new EBaseClientFunc() {
            @Override
            public int funcCallBack(int dwHandle, int dwType, int dwLen, String pData, long pUserData) {
            	logger.info("客户端句柄:" + dwHandle + "\r\n请求数据类型:" + dwType + "\r\npData:" + pData);
            	if (dwType == OTAPClientNative.DATATYPE_IOT_KERNEL_READY)
				{
                    logger.info("SDK内核准备完成 ");
                }
				/**
        		 * 在实际开发过程中，业务操作需要新开线程处理，在回调中做业务会造成死锁
        		 * 此处只做示例使用 ，默认都是处理的服务端下发到设备自身的请求
        		 * 本部分代码由开发者自身根据需求定义
        		 * */
            	final int cur_dwHandle = dwHandle;
            	final int cur_dwType = dwType;
            	final String cur_pData = pData;

				Thread reportThread = new Thread(){
            		public void run(){
            			switch(cur_dwType)
            			{
            				case OTAPClientNative.DATATYPE_CONN_SUCC:
            				{
            					// 表示平台服务（客户端）连接成功
            					// 具体需求由开发者自定义   我想这里可以进行数据的传输
            					ReportHandle(cur_dwHandle);
            					break;
            				}
            				case OTAPClientNative.DATATYPE_RECONN_SUCC:
            				{
            					// 表示平台服务（客户端）重连成功
            					// 此时只做示例， 展示网关子设备全量上报(挂载了子设备的网关设备重连后需要重新进行子设备全量上报)
            					// 具体需求由开发者自定义
            					fullchildReport(cur_dwHandle);
            					break;
            				}
            				default:
            				{
            					// 暂不处理
            	            	//logger.info("客户端句柄:" + cur_dwHandle + "\r\n请求数据类型:" + cur_dwType + "\r\npData:" + cur_pData);
            					break;
            				}
            			}
            		}
            	};
            	reportThread.start();

				Thread reponseThread = new Thread(){
            		/**
            		 *  平台服务（客户端）端响应物联网平台（服务端）请求线程
            		 *
            		 *  客户端收到来自服务端的请求后，解析请求进行业务操作
            		 *  响应操作调用SDK接口：boolean reponse(int iHandle, int iSeq, String strTopic, String lpClientData);
            		 *
            		 *  此处只做示例，具体实现由开发者自身根据需求定义
            		 * */
            		public void run(){
            			switch(cur_dwType)
            			{
            				case OTAPClientNative.DATATYPE_IOT:
            				{
            					// 收到来自服务端的请求
                        		//logger.info("接收到服务端请求 :" + cur_pData);
                        		ResponseHandle(cur_dwHandle, cur_pData);
                        		break;
            				}
            				default:
            				{
            					// 暂不处理
            	            	//logger.info("客户端句柄:" + cur_dwHandle + "\r\n请求数据类型:" + cur_dwType + "\r\npData:" + cur_pData);
            					break;
            				}
            			}
            		}
            	};
            	reponseThread.start();
                return 0;
            }
        };

        // 平台服务注册上线
		// strIOTRegAddr: 服务端的ip, iPort: 服务端的端口,ip根据实际修改，端口一般不变
        String strIOTRegAddr = "10.42.1.55";
        int iPort = 17262;
        String strDevInfoFile = "/opt/DeviceSDK/dev_info";
        logger.info("connectToIOTServer start");
        int iRet = OTAPClientNative.DEV_JNI_ERROR_FAIL;
        while(iRet != OTAPClientNative.DEV_JNI_ERROR_SUCC)
        {
			/**
        	* 接口返回true后，需要在回调中收到DATATYPE_CONN_SUCC才表示设备与平台连接成功
        	* */
        	iRet = OTAPClientNative.connectToIOTServer(dwHandle,strIOTRegAddr, iPort, strDevInfoFile, eBaseClientFunc);
            logger.info("connectToIOTServer, iRet_connect: " + iRet + ", errorCode: " + OTAPClientNative.getLastError());
        }
        LogFunc logFunc = new LogFunc() {
            @Override
            public int logCallBack(int dwLevel, String pBuffer, long pUserData) {
                logger.info("callback data is " + pBuffer);
                return 0;
            }
        };
        String strLogInfo = "{\n" +
        		"\"dwLevel\": 0,\n" +
        		"\"pUserData\": 12345\n" +
        		"}";
        logger.info("test1, strLogInfo：" + strLogInfo);
        iRet = OTAPClientNative.setLogInfo(strLogInfo,logFunc);
        logger.info("test1, iRet_setLog：" + iRet);

        while(true) {

        }

       // 销毁及反初始化, 进程退出时需要执行
       //OTAPClientNative.deystoryClient(dwHandle);
       //OTAPClientNative.fini();

    }

    public static void ReportHandle(int dwHandle)
    {
    	// 此处为示例  具体业务实现由开发者自定义
		boolean bRet = false;
		{
			/**
             * 注意：平台对接不需要关注，直接设备对接需要关注该接口
			 * 设备自身为网关设备，挂载了子设备，需要进行子设备全量上报
			 * 在重连后需要再次进行子设备全量上报
			 * 子设备变更后需要进行变更上报
			 * 这里以子设备全量上报为示例
			 * */
			bRet = fullchildReport(dwHandle);
			logger.info("fullchildReport bRet: " + bRet);
		}

		{
    		/**
    		 * 属性上报
    		 * 以属性温度为例  假如现在设备温度为39°C
    		 * */
			bRet = attributeReport(dwHandle);
			logger.info("attributeReport bRet: " + bRet);
		}
    }

    public static void ResponseHandle(int dwHandle, String pData)
    {
    	/**
		 * 此处为示例  具体业务实现由开发者自定义
		 *
         * 数据格式为字符串   由seq值和功能点URL和data组成,用\r\n隔开
         * 平台请求数据示例：7\r\n/iot/{$DEVID}/global/0-global/model/attribute/get/DynamicResource/Resources\r\n{"data":{"Value":{}}}
         * 示例说明：seqValue\r\b/iot/{$DEVID}/{$bySubSerial}/($byResourceID)-{$byResourceType}/{$byModule}/{$byMethod}/{$byMsgType}/{$byDomainID}/{$byIdentifier}\r\n{$dataValue}
         * */
		// 根据"\r\n"切割pData
        String[] strDatas = pData.split("\r\n");
        String strSeq = strDatas[0];
        String strTopic = strDatas[1];

        if (strTopic.contains("/model/service/operate/SearchDeviceCatalog/SearchDeviceCatalogList"))
        {
            /**
             * 此处表示收到了服务端下发的目录查询的请求解析（包括： region组织目录，device设备资源或resource通道资源）
             * */
            GetDeviceCatalogList(dwHandle, strSeq, strTopic, strDatas[2]);

        }
        else if (strTopic.contains("/model/service/operate/EventSubscription/AddEventSubscribeCfg"))
        {
        	/**
        	 * 此处表示收到了服务端下发的订阅设备自身事件的请求
        	 * 设备端需要保持事件订阅的信息，保存方式由开发者自定义，此处只做示例
        	 * */
        	AddEventSubscribeCfgHandle(dwHandle, strSeq, strTopic, strDatas[2]);
        }
        else if (strTopic.contains("/model/service/operate/DynamicCapability/GetDomains"))
        {
        	/**
        	 * 此处表示收到服务端下发的获取设备自身支持的功能领域的请求
        	 * 设备自身支持哪些功能领域由开发者自定义(在设备的物模型文件中会有描述)，此处只做示例
        	 * */
        	GetDomainsHandle(dwHandle, strSeq, strTopic, strDatas[2]);
        }
        else if (strTopic.contains("/model/attribute/get/InfoMgr/DeviceDescription"))
        {
        	/**
        	 * 此处表示收到服务端下发的获取设备自身描述信息的请求
        	 * 设备自身支持哪些功能领域由开发者自定义(在设备的物模型文件中会有描述)，此处只做示例
        	 * */
        	DeviceDescriptionHandle(dwHandle, strSeq, strTopic, strDatas[2]);
        }
        else if(strTopic.contains("/childmanage/searchchild/operate"))
        {
        	/**
        	 * 此处表示收到服务端下发的查询设备挂载的子设备列表的请求
        	 * 具体业务处理由开发者自定义，此处只做示例
        	 * */
        	searchchildHandle(dwHandle, strSeq, strTopic, strDatas[2]);
        }
		else
		{
			// 暂不支持
			UnSupport(dwHandle, strSeq, strTopic, strDatas[2]);
		}
    }

    public static boolean fullchildReport(int dwHandle)
    {
	    // 此处只做示例，具体网关设备全量上报流程和业务实现由开发者按照需求自定义
    	/**
    	 * 设备自身为网关设备，挂载了子设备，需要进行子设备全量上报
    	 * 子设备全量上报的功能点URL为/iot/{$DEVID}/{$CHILDID}/{$RESOURCEID-$RESOURCETYPE}/childmanage/fullchild/report
    	 * 设备唯一编号为hhhhhhhhh，所以{$DEVID} = hhhhhhhhh
    	 * 是主设备自身在上报子设备，所以{$CHILDID}填默认值 为global
    	 * 是设备自身资源，所以{$RESOURCEID-$RESOURCETYPE} = 0-global
    	 * */
		try{
			String strTopic = "/iot/hhhhhhhhh/global/0-global/childmanage/fullchild/report";
			String lpClientData = "{\"data\":{\"vesion\":0,\"childList\":["
					+ "{\"connected\":true,\"childDevID\":\"CWM_001\",\"serialNumber\":\"CWM_001\",\"version\":\"V5.1.0 build 150401\",\"model\":\"CS-C2C-1A1WFR\",\"firmwareIDCode\":\"test\",\"childStatus\":\"connect\",\"tags\":[{\"Key\":\"childDescription\",\"Value\":\"1111\"}]},"
					+ "{\"connected\":false,\"childDevID\":\"CWM_002\",\"serialNumber\":\"CWM_002\",\"version\":\"V5.1.0 build 150401\",\"model\":\"CS-C2C-1A1WFR\",\"firmwareIDCode\":\"test\",\"childStatus\":\"connect\",\"tags\":[{\"Key\":\"childDescription\",\"Value\":\"1111\"}]},"
					+ "{\"connected\":false,\"childDevID\":\"CWM_003\",\"serialNumber\":\"CWM_003\",\"version\":\"V5.1.0 build 150401\",\"model\":\"CS-C2C-1A1WFR\",\"firmwareIDCode\":\"test\",\"childStatus\":\"connect\",\"tags\":[{\"Key\":\"childDescription\",\"Value\":\"1111\"}]}]}}";

			StringBuffer strAnsData = new StringBuffer();
			int iRet = OTAPClientNative.sendPublish(dwHandle, strTopic, lpClientData, strAnsData);
			logger.info("test: " + strTopic + ", iRet：" + iRet);
			logger.info("strAnsData: " + strAnsData);
			// 解析服务端回复的数据
			if((iRet == OTAPClientNative.DEV_JNI_ERROR_SUCC) && (strAnsData != null) && (strAnsData.length() > 0))
			{
				// 根据"\r\n"分割strAnsData
				String[] strDatas = (strAnsData.toString()).split("\r\n");
				// strDatas[2]为服务端回复的协议响应内容，可以用JSON解析
				JSONObject jsonAnsData = JSONObject.parseObject(strDatas[2]);
				String strCode = jsonAnsData.getString("code");
				if(strCode.equals("0x00000000"))
				{
					// 表示属性正确上报到服务端
					return true;
				}
			}
			return false;

		}
        catch(Exception e)
    	{
    	    logger.info("fullchildReport function error");
    	}
    	return false;
    }
    public static boolean attributeReport(int dwHandle)
    {
    	// 此处只做示例，具体属性上报流程和业务实现由开发者按照需求自定义
    	/**
    	 * 功能点URL为/iot/${DEVID}/${CHILDID}/0-global/model/attribute/report/TemperatureSense/RoomTemp
    	 * 设备唯一编号为hhhhhhhhh，所以{$DEVID} = hhhhhhhhh
    	 * 设备自身的属性温度， 所以{$CHILDID}填默认值 为global
    	 * 是设备自身资源，所以{$RESOURCEID-$RESOURCETYPE} = 0-global
    	 * */
		try{
			String strTopic = "/iot/hhhhhhhhh/global/0-global/model/attribute/report/TemperatureSense/RoomTemp";
			// 对于属性，响应报文必须封装为data["Value"]格式
			String lpClientData = "{\"data\":{\"Value\":39}}";
			StringBuffer strAnsData = new StringBuffer();
			int iRet = OTAPClientNative.sendPublish(dwHandle, strTopic, lpClientData, strAnsData);
			logger.info("test: " + strTopic + ", iRet：" + iRet);
			logger.info("strAnsData: " + strAnsData);
			// 解析服务端回复的数据
			if((iRet == OTAPClientNative.DEV_JNI_ERROR_SUCC) && (strAnsData != null) && (strAnsData.length() > 0))
			{
				// 根据"\r\n"分割strAnsData
				String[] strDatas = (strAnsData.toString()).split("\r\n");
				// strDatas[2]为服务端回复的协议响应内容，可以用JSON解析
				JSONObject jsonAnsData = JSONObject.parseObject(strDatas[2]);
				String strCode = jsonAnsData.getString("code");
				if(strCode.equals("0x00000000"))
				{
					//表示属性正确上报到服务端
					return true;
				}
			}
			return false;
		}
        catch(Exception e)
    	{
    	    logger.info("attributeReport function error");
    	}
    	return false;
    }
    public static int eventReport(int dwHandle)
    {
    	// 此处只做示例，具体事件上报流程和业务实现由开发者按照需求自定义
    	/**
    	 * 功能点URL为/iot/${DEVID}/${CHILDID}/0-global/model/event/report/BoxDoorMgr/BoxDoorStatusReport
    	 * 设备唯一编号为hhhhhhhhh，所以{$DEVID} = hhhhhhhhh
    	 * 设备自身的属性温度， 所以{$CHILDID}填默认值 为global
    	 * 是设备自身资源，所以{$RESOURCEID-$RESOURCETYPE} = 0-global
    	 * */
    	int iRet = OTAPClientNative.DEV_JNI_ERROR_FAIL;
		try{
			String strTopic = "/iot/hhhhhhhhh/global/0-global/model/event/report/BoxDoorMgr/BoxDoorStatusReport";
			String strEventData = "{\"basic\":{\"ipV4Address\": \"10.65.101.171\",\"ipV6Address\": \"fe80::4883:315d:e4c:75bc\","
					+ "\"macAddress\": \"0C-9D-92-99-DD-31\",\"dateTime\": \"2021-01-28T02:00:00+08:00\","
					+ "\"UUID\": \"079f23cd-0988-459f-96f5-fa1c507dd07c\"},"
					+ "\"payload\":{\"doorStatusList\":[{\"doorID\": 1,\"doorStatus\": \"closed\"}]}}";
			StringBuffer strAnsData = new StringBuffer();
			// 平台服务（客户端）主动上报事件数据用sendPublish
			iRet = OTAPClientNative.sendPublish(dwHandle, strTopic, strEventData, strAnsData);
			logger.info("test: " + strTopic + ", iRet：" + iRet);
		}
        catch(Exception e)
    	{
    	   logger.info("eventReport function error");
    	}
    	return iRet;
    }
    public static int eventMReport(int dwHandle)
    {
    	// 此处只做示例，具体事件批量上报流程和业务实现由开发者按照需求自定义
    	int iRet = OTAPClientNative.DEV_JNI_ERROR_FAIL;
		try{
			/**
	    	 * 批量事件上报功能点URL为/iot/{$DEVID}/{$CHILDID}/{$RESOURCEID-$RESOURCETYPE}/event/mreport
	    	 * 设备唯一编号为hhhhhhhhh，所以{$DEVID} = hhhhhhhhh
	    	 * 设备自身上报批量事件， 所以{$CHILDID}填默认值 为global
	    	 * 是设备自身资源，所以{$RESOURCEID-$RESOURCETYPE} = 0-global
	    	 * */
			String strTopic = "/iot/hhhhhhhhh/global/0-global/event/mreport";

			/**
			 * 上报湿度过高和温度过低事件，组装批量事件报文
			 * 湿度过高事件功能点URL为/iot/${DEVID}/${CHILDID}/0-global/model/event/report/Humiture/HumidityTooHigh
			 * 所以湿度过高事件的method为/0-global/Humiture/HumidityTooHigh
			 * 温度过低事件功能点URL为/iot/${DEVID}/${CHILDID}/0-global/model/event/report/Humiture/TemperatureTooLow
			 * 所以温度过低事件的method为/0-global/Humiture/TemperatureTooLow
			 * */
			String strMEventData = "{\"event\":["
						+ "{\"child\":\"global\","
						+ "\"method\":\"/0-global/Humiture/HumidityTooHigh\","
						+ "\"data\":{"
							+ "\"basic\":{\"dateTime\":\"2021-01-28T02:00:00+08:00\","
								+ "\"UUID\":\"079f23cd-0988-459f-96f5-fa1c507dd07c\"},"
							+ "\"payload\":{\"humidity\":90}}},"
						+ "{\"child\":\"global\","
						+ "\"method\":\"/0-global/Humiture/TemperatureTooLow\","
						+ "\"data\":{"
							+ "\"basic\":{\"dateTime\":\"2021-01-28T02:00:00+08:00\","
								+ "\"UUID\":\"079f23cd-0988-459f-96f5-fa1c507dd07d\"},"
							+ "\"payload\":{\"temperature\":60}}}]}";

			StringBuffer strAnsData = new StringBuffer();
			iRet = OTAPClientNative.sendPublish(dwHandle, strTopic, strMEventData, strAnsData);
			logger.info("test: " + strTopic + ", iRet：" + iRet);
		}
        catch(Exception e)
    	{

    	}
    	return iRet;
    }
    public static boolean attributeMReport(int dwHandle)
    {
    	// 此处只做示例，具体属性批量上报流程和业务实现由开发者按照需求自定义
		try{
			/**
			 * 批量属性上报功能点URL为/iot/{$DEVID}/{$CHILDID}/{$LOCALINDEX-$RESOURCETYPE}/model/attribute/mreport
			 * 设备唯一编号为hhhhhhhhh，所以{$DEVID} = hhhhhhhhh
			 * 设备自身上报批量事件， 所以{$CHILDID}填默认值 为global
			 * 是设备自身资源，所以{$RESOURCEID-$RESOURCETYPE} = 0-global
			 * */
			String strTopic = "/iot/hhhhhhhhh/global/0-global/model/attribute/mreport";
			// 上报湿度检测状态和温度检测状态两个属性，组装批量属性报文
			// 湿度检测状态URL为/iot/${DEVID}/${CHILDID}/${LOCALINDEX}-${RESOURCETYPE}/model/attribute/report/Humiture/HumidityDetectionStatus
			// 所以湿度检测状态的method为/0-global/Humiture/HumidityDetectionStatus
			// 温度检测状态URL为/iot/${DEVID}/${CHILDID}/${LOCALINDEX}-${RESOURCETYPE}/model/attribute/report/Humiture/TemperatureDetectionStatus
			// 所以温度检测状态的method为/0-global/Humiture/TemperatureDetectionStatus
			String lpClientData = "{\"props\":["
						+ "{\"child\":\"global\","
						+ "\"method\":\"/0-global/Humiture/HumidityTooHigh\","
						+ "\"data\":{\"Value\":{\"status\":\"normal\"}}},"
						+ "{\"child\":\"global\","
						+ "\"method\":\"/0-global/Humiture/TemperatureTooLow\","
						+ "\"data\":{\"Value\":{\"status\":\"normal\"}}}]}";

			StringBuffer strAnsData = new StringBuffer();
			int iRet = OTAPClientNative.sendPublish(dwHandle, strTopic, lpClientData, strAnsData);
			logger.info("test: " + strTopic + ", iRet：" + iRet);
			logger.info("strAnsData: " + strAnsData);
			// 解析服务端回复的数据
			if((iRet == OTAPClientNative.DEV_JNI_ERROR_SUCC) && (strAnsData != null) && (strAnsData.length() > 0))
			{
				// 根据"\r\n"分割strAnsData
				String[] strDatas = (strAnsData.toString()).split("\r\n");
				// strDatas[2]为服务端回复的协议响应内容，可以用JSON解析
				JSONObject jsonAnsData = JSONObject.parseObject(strDatas[2]);
				String strCode = jsonAnsData.getString("code");
				if(strCode.equals("0x00000000"))
				{
					// 表示属性正确上报到服务端
					return true;
				}
			}
			return false;

		}
        catch(Exception e)
    	{

    	}
    	return false;
    }
    public static int AddEventSubscribeCfgHandle(int dwHandle, String strSeq, String strTopic, String strReqBody)
    {
    	// 此处只做示例，具体业务实现由开发者按照需求自定义
    	// 收到服务端下发的事件订阅请求
        String[] strs = strTopic.split("/");
        JSONObject jsonReqBody = JSONObject.parseObject(strReqBody);
        JSONObject jsonData = jsonReqBody.getJSONObject("data");
        String eventMode = jsonData.getString("eventMode");
        logger.info("eventMode：" + eventMode);
        // 判断服务端下发的是全量订阅还是按照事件类型订阅 保存相关订阅信息
        if(eventMode.equals("all"))
        {
        	// 表示订阅全部事件
        	m_isAllEvent = true;
        }
        else if(eventMode.equals("list"))
        {
        	// 表示按照事件类型订阅事件
        	// 保存订阅的事件
        	JSONArray jsonEventList = jsonData.getJSONArray("EventList");
        	for (Object object : jsonEventList)
        	{
        		JSONObject jsonOne = (JSONObject) object;
        		String eventType = jsonOne.getString("eventType");
        		// 保存时以子设备ID加事件类型为key, 事件类型为Value值(具体保存方式由开发者自定义)
        		m_EventTypeMap.putIfAbsent(strs[3] + "_" + eventType, strs[3]);
        	}
        }
        // 回复服务端
        int iSeq = Integer.parseInt(strSeq);
        // 将服务端下发的功能点URL中的{$byMsgType}在原有基础上加上"_reply"，其余保持不变，表示回复
        String strTopic_reply = "/iot";
        strs[7] = strs[7] + "_reply";
        for(int i = 2; i < strs.length; i++)
        {
        	strTopic_reply += "/" + strs[i];
        }
        // subscribeEventID是随机生成的订阅ID，range:[1,64]
        String lpClientData = "\"data\":{\"subscribeEventID\":\"98765\"}";
        int iRet = OTAPClientNative.reponse(dwHandle, iSeq, strTopic_reply, lpClientData);
        logger.info("test: " + strTopic_reply + ", iRet：" + iRet);

		{
    		/**
    		 * 此处只做示例使用
			 * 若此时事件已被订阅且已经触发需要上报事件
    		 * 事件上报的前提是事件已被订阅
    		 * 以箱门状态事件为例  假如现在设备的箱门是关闭的，需要上报箱门状态事件到服务端
    		 * */
			if(m_isAllEvent)
			{
				final int iHandle = dwHandle;
				Thread eventThread = new Thread(){
            		public void run(){
						// 主动上报事件
        				eventReport(iHandle);
            		}
				};
				eventThread.start();
			}
		}
        return iRet;
    }
    public static int GetDeviceCatalogList(int dwHandle, String strSeq, String strTopic, String strReqBody)
    {
        // 用"/"分割服务端下发的功能点URL
        String[] strs = strTopic.split("/");
        // 回复服务端
        int iSeq = Integer.parseInt(strSeq);
        // 将服务端下发的功能点URL中的{$byMsgType}在原有基础上加上"_reply"，其余保持不变，表示回复
        String strTopic_reply = "/iot";
        strs[7] = strs[7] + "_reply";
        for(int i = 2; i < strs.length; i++)
        {
            strTopic_reply += "/" + strs[i];
        }
        // lpClientData回复报文，需要应用自己根据文档要求定义
        String lpClientData = "{\"data\": {\"searchMatchItemList\": [\"device\", \"region\", \"resource\"]}}";
        int iRet = OTAPClientNative.reponse(dwHandle, iSeq, strTopic_reply, lpClientData);
        logger.info("test: " + strTopic_reply + ", iRet_send：" + iRet);
        return iRet;
    }
    public static int GetDomainsHandle(int dwHandle, String strSeq, String strTopic, String strReqBody)
    {
    	// 此处只做示例，具体业务实现由开发者按照需求自定义

    	// 用"/"分割服务端下发的功能点URL
        String[] strs = strTopic.split("/");
        // 回复服务端
        int iSeq = Integer.parseInt(strSeq);
        // 将服务端下发的功能点URL中的{$byMsgType}在原有基础上加上"_reply"，其余保持不变，表示回复
        String strTopic_reply = "/iot";
        strs[7] = strs[7] + "_reply";
        for(int i = 2; i < strs.length; i++)
        {
        	strTopic_reply += "/" + strs[i];
        }
        String lpClientData = "{\"data\": {\"domains\": [\"DynamicCapability\", \"childmanage\", \"EventSubscription\"]}}";
        int iRet = OTAPClientNative.reponse(dwHandle, iSeq, strTopic_reply, lpClientData);
        logger.info("test: " + strTopic_reply + ", iRet_send：" + iRet);
    	return iRet;
    }
    public static int DeviceDescriptionHandle(int dwHandle, String strSeq, String strTopic, String strReqBody)
    {
    	// 此处只做示例，具体业务实现由开发者按照需求自定义

    	// 用"/"分割服务端下发的功能点URL
        String[] strs = strTopic.split("/");
        // 回复服务端
        int iSeq = Integer.parseInt(strSeq);
        // 将服务端下发的功能点URL中的{$byMsgType}在原有基础上加上"_reply"，其余保持不变，表示回复
        String strTopic_reply = "/iot";
        strs[7] = strs[7] + "_reply";
        for(int i = 2; i < strs.length; i++)
        {
        	strTopic_reply += "/" + strs[i];
        }
        String lpClientData = "{\"data\":{\"Value\":{\"deviceName\":\"OTAPTestDev\",\"serialNumber\":\"hhhhhhhhh\"}}}";
        int iRet = OTAPClientNative.reponse(dwHandle, iSeq, strTopic_reply, lpClientData);
        logger.info("test: " + strTopic_reply + ", iRet_send：" + iRet);
    	return iRet;
    }
    public static int searchchildHandle(int dwHandle, String strSeq, String strTopic, String strReqBody)
    {
    	// 此处只做示例，具体业务实现由开发者按照需求自定义

    	// 用"/"分割服务端下发的功能点URL
        String[] strs = strTopic.split("/");
        // 回复服务端
        int iSeq = Integer.parseInt(strSeq);
        // 将服务端下发的功能点URL中的{$byMsgType}在原有基础上加上"_reply"，其余保持不变，表示回复
        String strTopic_reply = "/iot";
        strs[7] = strs[7] + "_reply";
        for(int i = 2; i < strs.length; i++)
        {
        	strTopic_reply += "/" + strs[i];
        }
        String lpClientData = "{\"status\":200,\"code\":\"0x00000000\",\"errorMsg\":\"Succeeded.\",\"data\":{\"pageCount\":1,\"childList\":[{\"connected\":true,\"childDevID\":\"3434645611\",\"serialNumber\":\"3434645611\",\"version\":\"V5.1.0 build 150401\",\"model\":\"CS-C2C-1A1WFR\",\"firmwareIDCode\":\"test\",\"childStatus\":\"connect\",\"tags\":[{\"Key\":\"aaaa\",\"Value\":\"1111\"}]}]}}{\"status\":200,\"code\":\"0x00000000\",\"errorMsg\":\"Succeeded.\",\"data\":{\"pageCount\":1,\"childList\":[{\"connected\":true,\"childDevID\":\"3434645611\",\"serialNumber\":\"3434645611\",\"version\":\"V5.1.0 build 150401\",\"model\":\"CS-C2C-1A1WFR\",\"firmwareIDCode\":\"test\",\"childStatus\":\"connect\",\"tags\":[{\"Key\":\"aaaa\",\"Value\":\"1111\"}]}]}}";
        int iRet = OTAPClientNative.reponse(dwHandle, iSeq, strTopic_reply, lpClientData);
        logger.info("test: " + strTopic_reply + ", iRet_send：" + iRet);
    	return iRet;
    }
    public static int UnSupport(int dwHandle, String strSeq, String strTopic, String strReqBody)
    {
    	// 用"/"分割服务端下发的功能点URL
        String[] strs = strTopic.split("/");
        // 回复服务端
        int iSeq = Integer.parseInt(strSeq);
        // 将服务端下发的功能点URL中的{$byMsgType}在原有基础上加上"_reply"，其余保持不变，表示回复
        String strTopic_reply = "/iot";
        strs[7] = strs[7] + "_reply";
        for(int i = 2; i < strs.length; i++)
        {
        	strTopic_reply += "/" + strs[i];
        }
        // 若设备自身不支持  可以回复服务端不支持
        String lpClientData = "{\"status\":200,\"code\":\"0x00100003\",\"data\":{\"Value\":{}}}";
        int iRet = OTAPClientNative.reponse(dwHandle, iSeq, strTopic_reply, lpClientData);
        logger.info("test: " + strTopic_reply + ", iRet_send：" + iRet);
    	return iRet;
    }
}
