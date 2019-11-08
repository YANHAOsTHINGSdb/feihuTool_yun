package server.pcs.system;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import server.pcs.BaiduHttpService百度服务器登陆服务;
import server.pcs.Constant;
import server.pcs.PcsClientService个人电脑客户端网盘服务;
import server.pcs.bean.BaiduDto;
import server.pcs.bean.OpsParamDto;
import server.pcs.util.SystemUtil;



public class BaiduLoginSystem implements OperateSystem {
	static Map<String, OpsParamDto> opsParams = new ConcurrentHashMap<String, OpsParamDto>();
	static {
		opsParams.put("-test", new OpsParamDto(1, true, false));
		opsParams.put("-loc", new OpsParamDto(2, true, false));
		opsParams.put("-rk", new OpsParamDto(4, true, false));
		opsParams.put("-rs", new OpsParamDto(8, true, false));
	}
	public List<Integer> allow = Arrays.asList(0, 1, 2, 4, 8);

	@Override
	public void ops(String[] command) throws Exception {
		Map<String, String> opsParamsTmp = new HashMap<String, String>();
		int value = 0;
		for (int i = 1; i < command.length; i++) {
			OpsParamDto tmp = opsParams.get(command[i]);
			if (tmp == null){
				i++;
				continue;
			}
			if (tmp.getIsValue()) {
				if (i == command.length - 1) {
					SystemUtil.logError("参数错误！");
					return;
				}
				opsParamsTmp.put(command[i], command[i + 1]);
				i++;
			} else {
				opsParamsTmp.put(command[i], "");
			}
			value += tmp.getNo();
		}
		if (!checkAllow(value)) {
			SystemUtil.logError("参数不是这样用的！");
		}
		if (opsParamsTmp.size() < 1) {
			Constant.userName = SystemUtil.getJlineIn("-> 请输入用户名：");
			Constant.passwd = SystemUtil.getJlineIn("-> 请输入密码： ");
			BaiduHttpService百度服务器登陆服务 baiduHttpService = new BaiduHttpService百度服务器登陆服务();
			BaiduDto baiduDto = baiduHttpService.登陆();
			SystemUtil.logLeft("百度账号登陆成功！");
			PcsClientService个人电脑客户端网盘服务 pcsClientService = new PcsClientService个人电脑客户端网盘服务();
			pcsClientService.init(baiduDto);
		} else {
			for (String key : opsParamsTmp.keySet()) {
				switch (key) {
				case "-test":
					loadUser();
					BaiduHttpService百度服务器登陆服务 baiduHttpService = new BaiduHttpService百度服务器登陆服务();
					BaiduDto baiduDto = baiduHttpService.登陆();
					SystemUtil.logLeft("百度账号登陆成功！");
					PcsClientService个人电脑客户端网盘服务 pcsClientService = new PcsClientService个人电脑客户端网盘服务();
					pcsClientService.init(baiduDto);
					break;
				case "-loc":
					BaiduDto baiduDtoFromFile = loadPcsInfo();
					SystemUtil.logLeft("百度账号登陆成功！");
					PcsClientService个人电脑客户端网盘服务 pcsClientServiceLoc = new PcsClientService个人电脑客户端网盘服务();
					pcsClientServiceLoc.做成OkHttpClient_byBDUSS(baiduDtoFromFile);
					break;
				case "-rk":
					Constant.localPath = "";
					loadUser();
					BaiduHttpService百度服务器登陆服务 rkbaiduHttpService = new BaiduHttpService百度服务器登陆服务();
					BaiduDto rkbaiduDto = rkbaiduHttpService.登陆();
					SystemUtil.logLeft("百度账号登陆成功！");
					PcsClientService个人电脑客户端网盘服务 rkpcsClientService = new PcsClientService个人电脑客户端网盘服务();
					rkpcsClientService.init(rkbaiduDto);
					break;
				case "-rs":
					Constant.localPath = "";
					BaiduDto rsbaiduDtoFromFile = loadPcsInfo();
					SystemUtil.logLeft("百度账号登陆成功！");
					PcsClientService个人电脑客户端网盘服务 rspcsClientServiceLoc = new PcsClientService个人电脑客户端网盘服务();
					rspcsClientServiceLoc.做成OkHttpClient_byBDUSS(rsbaiduDtoFromFile);
					break;
				default:
					break;
				}
			}
		}

	}

	public Boolean checkAllow(int value) {
		return allow.contains(value);
	}

	public BaiduDto loadPcsInfo() throws IOException {
		Properties properties = new Properties();
		InputStream in = new FileInputStream(Constant.localPath + "PcsLogin.txt");
		properties.load(in);
		BaiduDto baiduDto = new BaiduDto();
		baiduDto.setBduss(properties.getProperty("bduss"));
		baiduDto.setName(properties.getProperty("name"));
		baiduDto.setPtoken(properties.getProperty("ptoken"));
		baiduDto.setStoken(properties.getProperty("stoken"));
		baiduDto.setWorkdir(properties.getProperty("workdir"));
		baiduDto.setUID(properties.getProperty("uid"));
		baiduDto.setNameShow(properties.getProperty("nameshow"));
		in.close();
		return baiduDto;
	}

	public void loadUser() throws IOException {
		Properties properties = new Properties();
		InputStream in = new FileInputStream(Constant.localPath + "BDLogin.txt");
		properties.load(in);
		Constant.userName = properties.getProperty("userName");
		Constant.passwd = properties.getProperty("passwd");
		SystemUtil.logDebug(Constant.userName);
		SystemUtil.logDebug(Constant.passwd);
		in.close();
	}
}
