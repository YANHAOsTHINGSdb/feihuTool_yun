package server.pcs.bean;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import server.pcs.system.BaiduLoginSystem;
import server.pcs.system.OperateSystem;



public class CommandDict {
	static Map<String, OperateSystem> ops = new ConcurrentHashMap<String, OperateSystem>();
	static {
		ops.put("login", new BaiduLoginSystem());
	//	ops.put("ls", new PcsLsSystem());
	//	ops.put("download", new DownloadSystem());
	//	ops.put("cd", new PcsCdSystem());
	//	ops.put("path", new LocalPathSystem());
	}

	public static OperateSystem getOperateSystem(String key) {
		return ops.get(key);
	}

	public static Collection<String> getKeys(){
		return ops.keySet();
	}
}
