package server.pcs.bean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;



public class BaiduClientStore {
	public static Map<String, BaiduDto> bdClients = new ConcurrentHashMap<String, BaiduDto>();
	public static String currentActiveUid = "";
	public static BaiduDto currentActiveBaiduDto = null;
}
