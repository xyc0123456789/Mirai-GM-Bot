/**
 *
 * Licensed Property to China UnionPay Co., Ltd.
 *
 * (C) Copyright of China UnionPay Co., Ltd. 2010
 *     All Rights Reserved.
 *
 *
 * Modification History:
 * =============================================================================
 *   Author         Date          Description
 *   ------------ ---------- ---------------------------------------------------
 *   xshu       2014-05-28       HTTP通信工具类
 * =============================================================================
 */
package com.king.util;

import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


@Slf4j
public class HttpClient {
	/**
	 * 目标地址
	 */
	private URL url;

	/**
	 * 通信连接超时时间
	 */
	private int connectionTimeout;

	/**
	 * 通信读超时时间
	 */
	private int readTimeOut;

	/**
	 * 通信结果
	 */
	private String result;

	private ByteArrayOutputStream outputStream;

	/**
	 * 是否开启https
	 */
	private boolean isIfValidateRemoteCert;


	private  Map<String,String> requestHead;

	/**
	 * 获取通信结果
	 * @return
	 */
	public String getResult() {
		return result;
	}

	public ByteArrayOutputStream getOutputStream() {
		return outputStream;
	}

	/**
	 * 设置通信结果
	 * @param result
	 */
	public void setResult(String result) {
		this.result = result;
	}

	public boolean isIfValidateRemoteCert() {
		return isIfValidateRemoteCert;
	}

	public void setIfValidateRemoteCert(boolean ifValidateRemoteCert) {
		isIfValidateRemoteCert = ifValidateRemoteCert;
	}



	/**
	 * 构造函数
	 * @param url 目标地址
	 * @param connectionTimeout HTTP连接超时时间
	 * @param readTimeOut HTTP读写超时时间
	 */
	public HttpClient(String url, int connectionTimeout, int readTimeOut, boolean isIfValidateRemoteCert) {
		try {
			this.url = new URL(url);
			this.connectionTimeout = connectionTimeout;
			this.readTimeOut = readTimeOut;
			this.isIfValidateRemoteCert = isIfValidateRemoteCert;
		} catch (MalformedURLException e) {
			log.error(e.toString());
		}
	}

	public HttpClient(String url, Map<String,String> head) {
		this(url,60000,60000, false);
		this.requestHead = head;
	}

	public HttpClient(String url, int connectionTimeout, int readTimeOut, boolean isIfValidateRemoteCert,Map<String,String> head) {
		this(url,connectionTimeout,readTimeOut,isIfValidateRemoteCert);
		this.requestHead = head;
	}

	/**
	 * 发送信息到服务端
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public int sendPost(Map<String, String> data) throws Exception {
		return sendPost(data, StandardCharsets.UTF_8.toString(),null);
	}
	public int sendPost(Map<String, String> data, String proxy) throws Exception {
		return sendPost(data, StandardCharsets.UTF_8.toString(), proxy);
	}
	public int sendPost(Map<String, String> data, String encoding, String proxy) throws Exception {
		try {
			HttpURLConnection httpURLConnection = createConnectionPost(encoding, proxy);
			if (null == httpURLConnection) {
				throw new Exception("Create httpURLConnection Failure");
			}
			String sendData = this.getRequestParamString(data, encoding);
//			log.info("请求报文(对每个报文域的值均已做url编码):[" + sendData + "]");
			this.requestServer(httpURLConnection, sendData,
					encoding);
			this.result = this.response(httpURLConnection, encoding);
			log.info("Response message:[" + result + "]");
			return httpURLConnection.getResponseCode();
		} catch (Exception e) {
			throw e;
		}
	}

	public int sendJson(Map<String,Object> data, String proxy) throws Exception {
		return sendJson(JsonUtil.toJson(data), proxy);
	}

	public int sendJson(String data, String proxy) throws Exception {
		String encoding = StandardCharsets.UTF_8.toString();
		try {
			HttpURLConnection httpURLConnection = createConnectionPost(encoding, proxy, "application/json");
			if (null == httpURLConnection) {
				throw new Exception("Create httpURLConnection Failure");
			}
//			log.info("请求报文(对每个报文域的值均已做url编码):[" + sendData + "]");
			this.requestServer(httpURLConnection, data,
					encoding);
			this.result = this.response(httpURLConnection, encoding);
			log.info("Response message:[" + result + "]");
			return httpURLConnection.getResponseCode();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 发送信息到服务端 GET方式
	 * @return
	 * @throws Exception
	 */
	public int sendGet() throws Exception {
		return sendGet(StandardCharsets.UTF_8.toString(), null);
	}
	public int sendGet(String proxy) throws Exception {
		return sendGet(StandardCharsets.UTF_8.toString(), proxy);
	}
	public int sendGet(String encoding, String proxy) throws Exception {
		try {
			HttpURLConnection httpURLConnection = createConnectionGet(encoding, proxy);
			if(null == httpURLConnection){
				throw new Exception("创建联接失败");
			}
			this.result = this.response(httpURLConnection, encoding);
			log.info("同步返回报文:[" + result + "]");
			return httpURLConnection.getResponseCode();
		} catch (Exception e) {
			throw e;
		}
	}

	public int sendGetGetOutPutStream(String encoding, String proxy) throws Exception {
		try {
			HttpURLConnection httpURLConnection = createConnectionGet(encoding, proxy);
			if(null == httpURLConnection){
				throw new Exception("创建联接失败");
			}
			this.responseOutPutStream(httpURLConnection, encoding);
			return httpURLConnection.getResponseCode();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * HTTP Post发送消息
	 *
	 * @param connection
	 * @param message
	 * @throws IOException
	 */
	private void requestServer(final URLConnection connection, String message, String encoder)
			throws Exception {
		PrintStream out = null;
		try {
			connection.connect();
			out = new PrintStream(connection.getOutputStream(), false, encoder);
			out.print(message);
			out.flush();
		} catch (Exception e) {
			throw e;
		} finally {
			if (null != out) {
				out.close();
			}
		}
	}

	/**
	 * 显示Response消息
	 *
	 * @param connection
	 * @return
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	private String response(final HttpURLConnection connection, String encoding)
			throws URISyntaxException, IOException, Exception {
		InputStream in = null;
		StringBuilder sb = new StringBuilder(1024);
		BufferedReader br = null;
		try {
			if (200 == connection.getResponseCode()) {
				in = connection.getInputStream();
				sb.append(new String(read(in), encoding));
			} else {
				in = connection.getErrorStream();
				sb.append(new String(read(in), encoding));
			}
//			log.info("HTTP Return Status-Code:[" + connection.getResponseCode() + "]");
			return sb.toString();
		} catch (Exception e) {
			throw e;
		} finally {
			if (null != br) {
				br.close();
			}
			if (null != in) {
				in.close();
			}
			if (null != connection) {
				connection.disconnect();
			}
		}
	}

	/**
	 * 显示Response消息
	 *
	 * @param connection
	 * @return
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	private ByteArrayOutputStream responseOutPutStream(final HttpURLConnection connection, String encoding)
			throws URISyntaxException, IOException, Exception {
		InputStream in = null;

		BufferedReader br = null;
		try {
			if (200 == connection.getResponseCode()) {
//				Map<String, List<String>> headerFields = connection.getHeaderFields();
//				for (String key :headerFields.keySet()){
//					log.info("{},{}",key,headerFields.get(key));
//				}
				in = connection.getInputStream();
				outputStream = StreamUtil.inputStreamToOutputStream(in);
			} else {
				in = connection.getErrorStream();
				outputStream = StreamUtil.inputStreamToOutputStream(in);
			}
//			log.info("HTTP Return Status-Code:[" + connection.getResponseCode() + "]");
			return outputStream;
		} catch (Exception e) {
			throw e;
		} finally {
			if (null != br) {
				br.close();
			}
			if (null != in) {
				in.close();
			}
			if (null != connection) {
				connection.disconnect();
			}
		}
	}

	public static byte[] read(InputStream in) throws IOException {
		byte[] buf = new byte[1024];
		int length = 0;
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		while ((length = in.read(buf, 0, buf.length)) > 0) {
			bout.write(buf, 0, length);
		}
		bout.flush();
		return bout.toByteArray();
	}

	private Proxy generateProxy(String proxyStr){
		if (MyStringUtil.isEmpty(proxyStr)){
			return null;
		}
		int index = proxyStr.indexOf(":");
		if (index!=-1){
			try {
				String host = proxyStr.substring(0,index);
				int port = Integer.parseInt(proxyStr.substring(index + 1));
				return new Proxy(Proxy.Type.HTTP,new InetSocketAddress(host, port));
			}catch (Exception e){
				log.error("generateProxy err", e);
			}
		}
		return null;
	}

	/**
	 * 创建连接
	 *
	 * @return
	 * @throws ProtocolException
	 */
	private HttpURLConnection createConnectionPost(String encoding, String proxyStr) throws ProtocolException {
		return createConnectionPost(encoding, proxyStr, "application/x-www-form-urlencoded");
	}
	private HttpURLConnection createConnectionPost(String encoding, String proxyStr, String contentType) throws ProtocolException {
		if (MyStringUtil.isEmpty(encoding)){
			encoding=StandardCharsets.UTF_8.toString();
		}
		HttpURLConnection httpURLConnection = null;
		try {
			Proxy proxy = generateProxy(proxyStr);
			if (proxy == null){
				httpURLConnection = (HttpURLConnection) url.openConnection();
			}else {
				httpURLConnection = (HttpURLConnection) url.openConnection(proxy);
			}
		} catch (IOException e) {
			log.error(e.getMessage());
			return null;
		}
		httpURLConnection.setConnectTimeout(this.connectionTimeout);// 连接超时时间
		httpURLConnection.setReadTimeout(this.readTimeOut);// 读取结果超时时间
		httpURLConnection.setDoInput(true); // 可读
		httpURLConnection.setDoOutput(true); // 可写
		httpURLConnection.setUseCaches(false);// 取消缓存
		httpURLConnection.setRequestProperty("Content-type",
				contentType+";charset=" + encoding);
		if (requestHead!=null&&requestHead.size()!=0){
			for (String key:requestHead.keySet()){
				httpURLConnection.setRequestProperty(key,
						requestHead.get(key));
			}
		}
		httpURLConnection.setRequestMethod("POST");
		if ("https".equalsIgnoreCase(url.getProtocol())) {
			HttpsURLConnection husn = (HttpsURLConnection) httpURLConnection;
			//是否验证https证书，测试环境请设置false，生产环境建议优先尝试true，不行再false
			if(!this.isIfValidateRemoteCert){
				husn.setSSLSocketFactory(new BaseHttpSSLSocketFactory());
				husn.setHostnameVerifier(new TrustAnyHostnameVerifier());//解决由于服务器证书问题导致HTTPS无法访问的情况
			}
			return husn;
		}
		return httpURLConnection;
	}

	/**
	 * 创建连接
	 *
	 * @return
	 * @throws ProtocolException
	 */
	private HttpURLConnection createConnectionGet(String encoding, String proxyStr) throws ProtocolException {
		if (MyStringUtil.isEmpty(encoding)){
			encoding=StandardCharsets.UTF_8.toString();
		}
		HttpURLConnection httpURLConnection = null;
		try {
			Proxy proxy = generateProxy(proxyStr);
			if (proxy == null){
				httpURLConnection = (HttpURLConnection) url.openConnection();
			}else {
				httpURLConnection = (HttpURLConnection) url.openConnection(proxy);
			}
		} catch (IOException e) {
			log.error(e.toString());
			return null;
		}
		httpURLConnection.setConnectTimeout(this.connectionTimeout);// 连接超时时间
		httpURLConnection.setReadTimeout(this.readTimeOut);// 读取结果超时时间
		httpURLConnection.setUseCaches(false);// 取消缓存
		httpURLConnection.setRequestProperty("Content-type",
				"application/x-www-form-urlencoded;charset=" + encoding);
		if (requestHead!=null&&requestHead.size()!=0){
			for (String key:requestHead.keySet()){
				httpURLConnection.setRequestProperty(key,
						requestHead.get(key));
			}
		}
		httpURLConnection.setRequestMethod("GET");
		if ("https".equalsIgnoreCase(url.getProtocol())) {
			HttpsURLConnection husn = (HttpsURLConnection) httpURLConnection;
			//是否验证https证书，测试环境请设置false，生产环境建议优先尝试true，不行再false
			if(!this.isIfValidateRemoteCert){
				husn.setSSLSocketFactory(new BaseHttpSSLSocketFactory());
				husn.setHostnameVerifier(new TrustAnyHostnameVerifier());//解决由于服务器证书问题导致HTTPS无法访问的情况
			}
			return husn;
		}
		return httpURLConnection;
	}

	/**
	 * 将Map存储的对象，转换为key=value&key=value的字符
	 *
	 * @param requestParam
	 * @param coder
	 * @return
	 */
	private String getRequestParamString(Map<String, String> requestParam, String coder) {
		if (null == coder || "".equals(coder)) {
			coder = "UTF-8";
		}
		StringBuffer sf = new StringBuffer("");
		String reqstr = "";
		if (null != requestParam && 0 != requestParam.size()) {
			for (Entry<String, String> en : requestParam.entrySet()) {
				try {
					sf.append(en.getKey()
							+ "="
							+ (null == en.getValue() || "".equals(en.getValue()) ? "" : URLEncoder
									.encode(en.getValue(), coder)) + "&");
				} catch (UnsupportedEncodingException e) {
					log.error(e.toString());
					return "";
				}
			}
			reqstr = sf.substring(0, sf.length() - 1);
		}
//		log.info("Request Message:[" + reqstr + "]");
		return reqstr;
	}

	/**
	 * 发送信息到服务端
	 * @param sendData 发送的内容
	 * @param msgTp 报文编号
	 * @param iniTrxTp 初始交易类型，网关交易必选
	 * @param oriIssrId 初始交易的发起方机构标识
	 * @param encoding 编码方式
	 * @return 服务器的响应码
	 * @throws Exception
	 */
	public int sendPostXml(String sendData, String msgTp, String iniTrxTp, String oriIssrId, String encoding) throws Exception {
		//			获取连接
		int msgLenth = sendData.length();
		HttpURLConnection httpURLConnection = createXmlConnection(msgLenth, msgTp,iniTrxTp,oriIssrId,encoding);
		if (null == httpURLConnection) {
			throw new Exception("Create httpURLConnection Failure");
		}
//			发出请求
		this.requestServer(httpURLConnection, sendData,
				encoding);
//			获取响应信息
		this.result = this.response(httpURLConnection, encoding);
//		log.info("Response message:[" + result + "]");
		return httpURLConnection.getResponseCode();
	}

	/**
	 * 创建post方式的连接
	 * @param msgTp 报文编号
	 * @param iniTrxTp 初始交易类型，网关交易必选
	 * @param oriIssrId 初始交易的发起方机构标识
	 * @param encoding 编码方式
	 * @return
	 * @throws ProtocolException
	 */
	private HttpURLConnection createXmlConnection(int msgLenth,String msgTp,String iniTrxTp,String oriIssrId,String encoding) throws ProtocolException {
		HttpURLConnection httpURLConnection = null;
		try {
			httpURLConnection = (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			log.error(e.toString());
			return null;
		}
		httpURLConnection.setConnectTimeout(this.connectionTimeout);// 连接超时时间
		httpURLConnection.setReadTimeout(this.readTimeOut);// 读取结果超时时间
		httpURLConnection.setDoInput(true); // 可读
		httpURLConnection.setDoOutput(true); // 可写
		httpURLConnection.setUseCaches(false);// 取消缓存
		httpURLConnection.setRequestProperty("Content-Type","application/xml;charset=" + encoding);
		httpURLConnection.setRequestProperty("Content-Length", String.valueOf(msgLenth)+12);
		httpURLConnection.setRequestProperty("MsgTp",msgTp);
		if (iniTrxTp!=null&&!iniTrxTp.isEmpty()) {
			httpURLConnection.setRequestProperty("IniTrxTp", iniTrxTp);
		}
		httpURLConnection.setRequestProperty("OriIssrId",oriIssrId);
		log.info("请求头：{}", JsonUtil.toJson(httpURLConnection.getRequestProperties()));
		httpURLConnection.setRequestMethod("POST");
		if ("https".equalsIgnoreCase(url.getProtocol())) {
			HttpsURLConnection husn = (HttpsURLConnection) httpURLConnection;
			//是否验证https证书，测试环境请设置false，生产环境建议优先尝试true，不行再false
			if(!this.isIfValidateRemoteCert){
				husn.setSSLSocketFactory(new BaseHttpSSLSocketFactory());
				husn.setHostnameVerifier(new TrustAnyHostnameVerifier());//解决由于服务器证书问题导致HTTPS无法访问的情况
			}
			return husn;
		}
		return httpURLConnection;
	}

}

@Slf4j
class BaseHttpSSLSocketFactory extends SSLSocketFactory {
	private SSLContext getSSLContext() {
		return createEasySSLContext();
	}

	@Override
	public Socket createSocket(InetAddress arg0, int arg1, InetAddress arg2,
							   int arg3) throws IOException {
		return getSSLContext().getSocketFactory().createSocket(arg0, arg1,
				arg2, arg3);
	}

	@Override
	public Socket createSocket(String arg0, int arg1, InetAddress arg2, int arg3)
			throws IOException, UnknownHostException {
		return getSSLContext().getSocketFactory().createSocket(arg0, arg1,
				arg2, arg3);
	}

	@Override
	public Socket createSocket(InetAddress arg0, int arg1) throws IOException {
		return getSSLContext().getSocketFactory().createSocket(arg0, arg1);
	}

	@Override
	public Socket createSocket(String arg0, int arg1) throws IOException,
			UnknownHostException {
		return getSSLContext().getSocketFactory().createSocket(arg0, arg1);
	}

	@Override
	public String[] getSupportedCipherSuites() {
		return null;
	}

	@Override
	public String[] getDefaultCipherSuites() {
		return null;
	}

	@Override
	public Socket createSocket(Socket arg0, String arg1, int arg2, boolean arg3)
			throws IOException {
		return getSSLContext().getSocketFactory().createSocket(arg0, arg1,
				arg2, arg3);
	}

	private SSLContext createEasySSLContext() {
		try {
			SSLContext context = SSLContext.getInstance("SSL");
			context.init(null,
					new TrustManager[] { MyX509TrustManager.manger }, null);
			return context;
		} catch (Exception e) {
			log.error(e.toString());
			return null;
		}
	}

	public static class MyX509TrustManager implements X509TrustManager {

		static MyX509TrustManager manger = new MyX509TrustManager();

		public MyX509TrustManager() {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public void checkClientTrusted(X509Certificate[] chain, String authType) {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType) {
		}
	}


}
/**
 * 解决由于服务器证书问题导致HTTPS无法访问的情况 PS:HTTPS hostname wrong: should be <localhost>
 */
class TrustAnyHostnameVerifier implements HostnameVerifier {
	public boolean verify(String hostname, SSLSession session) {
		//直接返回true
		return true;
	}
}