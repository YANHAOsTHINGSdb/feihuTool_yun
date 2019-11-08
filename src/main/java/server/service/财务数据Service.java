package server.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import ConvertTool.impl.StockData财务ConvertTool;
import server.dao.财务信息Dao;
import server.entity.财务Entity;

@Service
// @Transactional
public class 财务数据Service  extends 爸爸数据Service{

	// @Autowired
	财务信息Dao 财务信息dao;

	public 财务数据Service() {
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ

	}

	/**
	 * 下载除权导入数据
	 * @return
	 */
	public String 下载财务导入数据() {

		String[] s财务原始文件全路径 = new String[1];
		//s财务原始文件全路径[0]="D:\\FeihuTool_Convert\\财务数据.data";

		String s财务导出文件全路径 = null;

		s财务导出文件全路径 = new StockData财务ConvertTool().输出到财务文件(s财务原始文件全路径);

		if(StringUtils.isEmpty(s财务导出文件全路径)) {
			return s财务导出文件全路径;
		}
		// 存储财务数据（解析原始文件、存入数据库）
		// List<String> 财务数据list = InputDataUtil财务.getFileContext2(s财务原始文件全路径[0]);
		// 存储财务数据_by财务数据(财务数据list);

		String s本地上传文件全路径 = s财务原始文件全路径[0];
		String s网盘存储文件全路径 = "/apps/财务数据" + 取得系统时间_文件名() +".data";
		存储到百度网盘(s本地上传文件全路径, s网盘存储文件全路径);

		return s财务导出文件全路径;
	}



	/**
	 * 存储除权数据_by除权数据
	 * @param 除权数据list
	 */
	private void 存储财务数据_by财务数据(List<String> 财务数据list) {
		List<财务Entity> 财务EntityList = new ArrayList<财务Entity>();
		for(String in每股财务Data : 财务数据list) {
	           System.out.println(in每股财务Data);

				List<财务Entity> 财务DataInputBean飞狐list = 解析每一行的财务数据2(
						in每股财务Data);
				if(财务DataInputBean飞狐list !=null)
				财务EntityList.addAll(财务DataInputBean飞狐list);
		}

		// 将【out除权DataInputBean飞狐list】写入数据库
		财务信息dao.save(财务EntityList);
	}

	/**
	 * 解析每一行的除权数据2
	 * @param in每股除权Data
	 * @return
	 *
	 *  为何在此另写一个解析函数呢
	 *  因为，需要返回<除权Entity>这个类型
	 *  这个类型是数据库专用的
	 *  与解析函数的bean不同
	 *  所以，需要另写函数
	 */
	public List<财务Entity> 解析每一行的财务数据2(String in每股财务Data) {

		// 把数据分解成数组
		byte[] resultByte深沪个股财务输出数据 = new byte[0];
		String[] s每条财务数据 = in每股财务Data.split("\\n");
		int iIndex = 0;
		int i财务数据个数 = 0;
		List<财务Entity> 财务Entitylist = new ArrayList<财务Entity>();

		for(String s每项财务数据  : s每条财务数据) {
//			if(iIndex == 0) {
//				iIndex++;
//				continue;
//			}

			if(s每项财务数据.equals("市场	证券代码	流通股本	所属省份	所属行业	财务更新日期	上市日期	总股本	国家股	发起人法人股	法人股	B股	H股	职工股	总资产	流动资产	固定资产	无形资产	股东人数	流动负债	长期负债	资本公积金	净资产	主营收入	主营利润	应收帐款	营业利润	投资收益	经营现金流	总现金流	存货	利润总额	税后利润	净利润	未分利润	保留")) {
				continue;
			}

			财务Entity 财务entity = new 财务Entity();

			// 从第二条开始分析
			String[] s = s每项财务数据.split("\\t");
			if (s.length != 37) return null;
			// 判断是不是实际的数据
			if(new StockData财务ConvertTool().判断是不是有效的财务数据ForDLLData(s)) {
				// 只有实际的数据才进行后续处理
				// 先取得股票代号，做成文件名
				// 再取得入力的值
				// 输出实体文件

				// 先取得股票代号，做成文件名
				// 市场 证券代码 日期 权息数据类别 派息金额 配股价 送股数 配股数

				// 先取得股票代号，做成文件名
				String s市场代码      = s[0];
				String s股票代码 = s[1];
				String s流通股本     = s[2];
				String s所属行业 = s[3];
			    String s所属省份   = s[4];
				String sUTCtime   = s[5];
				String s账务更新日期   = s[6];
				String s上市日期   = s[7];
				String s总股本   = s[8];
				String s国家股   = s[9];
				String s发起人法人股   = s[10];
				String s法人股   = s[11];
				String sB股   = s[12];
				String sH股   = s[13];
				String s职工股   = s[14];
				String s总资产   = s[15];
				String s流动资产   = s[16];
				String s固定资产   = s[17];
				String s无形资产   = s[18];
				String s股东人数   = s[19];
				String s流动负债   = s[20];
				String s长期负债   = s[21];
				String s资本公积金   = s[22];
				String s净资产   = s[23];
				String s主营收入   = s[24];
				String s主营利润   = s[25];
				String s应收帐款   = s[26];
				String s营业利润   = s[27];
				String s投资收益   = s[28];
				String s经营现金流   = s[29];
				String s总现金流   = s[30];
				String s存贷   = s[31];
				String s利润总额   = s[32];
				String s税后利润   = s[33];
				String s净利润   = s[34];
				String s未分利润   = s[35];
				String s保留   = s[36];







			//String[] sData = new String[] {s市场代码, s股票代码,sUTCtime,s流通股本,s所属省份,s所属行业,s账务更新日期 };

				财务entity.set市场代码(s市场代码);
				财务entity.set股票代码(s股票代码);
				财务entity.setUTCtime(sUTCtime);
				财务entity.set流通股本(s流通股本);
				财务entity.set所属省份(s所属省份);
				财务entity.set所属行业(s所属行业);
				财务entity.set账务更新日期(s账务更新日期);
				财务entity.set上市日期(s上市日期);
				财务entity.set总股本(s总股本);
				财务entity.set国家股(s国家股);
				财务entity.set发起人法人股(s发起人法人股);
				财务entity.set法人股(s法人股);
				财务entity.setB股(sB股);
				财务entity.setH股(sH股);
				财务entity.set职工股(s职工股);
				财务entity.set总资产(s总资产);
				财务entity.set流动资产(s流动资产);
				财务entity.set固定资产(s固定资产);
				财务entity.set无形资产 (s无形资产 );
				财务entity.set股东人数(s股东人数);
				财务entity.set流动负债(s流动负债);
				财务entity.set长期负债(s长期负债);
				财务entity.set资本公积金(s资本公积金);
				财务entity.set净资产(s净资产);
				财务entity.set主营收入(s主营收入);
				财务entity.set主营利润 (s主营利润 );
				财务entity.set应收帐款(s应收帐款);
				财务entity.set营业利润 (s营业利润 );
				财务entity.set投资收益(s投资收益);
				财务entity.set经营现金流(s经营现金流);
				财务entity.set总现金流(s总现金流);
				财务entity.set存贷(s存贷);
				财务entity.set利润总额 (s利润总额 );
				财务entity.set税后利润(s税后利润);
				财务entity.set净利润(s净利润);
				财务entity.set未分利润(s未分利润);
				财务entity.set保留  (s保留  );


				财务Entitylist.add(财务entity);
			}

		}

		return 财务Entitylist;
	}

}
