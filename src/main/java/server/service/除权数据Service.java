package server.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import ConvertTool.impl.StockData除权ConvertTool;
import server.dao.除权信息Dao;
import server.entity.除权Entity;
import server.pcs.PCSUploadUtils;

@Service
// @Transactional
public class 除权数据Service  extends 爸爸数据Service{

	// @Autowired
	除权信息Dao 除权信息dao;

	public 除权数据Service() {
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ

	}

	/**
	 * 下载除权导入数据
	 * @return
	 */
	public String 下载除权导入数据() {

		String[] s财务原始文件全路径 = new String[1];
		s财务原始文件全路径[0]="/Users/haoyan/Desktop/FeihuTool_Convert/除权数据.data";

		String s财务导出文件全路径 = null;
//
//		s财务导出文件全路径 = new StockData除权ConvertTool().输出到除权文件(s财务原始文件全路径);
//
//		if(StringUtils.isEmpty(s财务导出文件全路径)) {
//			return s财务导出文件全路径;
//		}

		// 存储除权数据（解析原始文件、存入百度网盘）

		// 取得系统时间
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		System.out.println(df.format(new Date()));// new Date()为获取当前系统时间

//        String mLocalPath = "/Users/haoyan/Desktop/FeihuTool_Convert/除权数据.data";
//        String mDestPath = "/apps/除权数据.data";
		String mLocalPath = s财务原始文件全路径[0];
		String mDestPath = "/apps/除权数据" + df.format(new Date()) +".data";

		new PCSUploadUtils().multiFileUpload(mLocalPath, mDestPath);

		return s财务导出文件全路径;
	}



	/**
	 * 存储除权数据_by除权数据
	 * @param 除权数据list
	 */

	private void 存储除权数据_by除权数据(List<String> 除权数据list) {
		List<除权Entity> 除权EntityList = new ArrayList<除权Entity>();
		for(String in每股除权Data : 除权数据list) {
	           System.out.println(in每股除权Data);

				List<除权Entity> 除权DataInputBean飞狐list = 解析每一行的除权数据2(
						in每股除权Data);

				除权EntityList.addAll(除权DataInputBean飞狐list);
		}

		// 将【out除权DataInputBean飞狐list】写入数据库
		除权信息dao.save(除权EntityList);
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
	public List<除权Entity> 解析每一行的除权数据2(String in每股除权Data) {

		// 把数据分解成数组
		byte[] resultByte深沪个股除权输出数据 = new byte[0];
		String[] s每条除权数据 = in每股除权Data.split("\\n");
		int iIndex = 0;
		int i除权数据个数 = 0;
		List<除权Entity> 除权Entitylist = new ArrayList<除权Entity>();

		for(String s每项除权数据  : s每条除权数据) {
			if(iIndex == 0) {
				iIndex++;
				continue;
			}

			除权Entity 除权entity = new 除权Entity();

			// 从第二条开始分析
			String[] s = s每项除权数据.split("\\t");
			if (s.length != 8) return null;
			// 判断是不是实际的数据
			if(new StockData除权ConvertTool().判断是不是有效的除权数据ForDLLData(s)) {
				// 只有实际的数据才进行后续处理
				// 先取得股票代号，做成文件名
				// 再取得入力的值
				// 输出实体文件

				// 先取得股票代号，做成文件名
				// 市场 证券代码 日期 权息数据类别 派息金额 配股价 送股数 配股数

				// 先取得股票代号，做成文件名
				String s市场       = s[0];
				String s股票代码 = s[1];
				String s日期      = s[2];
				String s权息数据类别= s[3];
				String s派息金额 = s[4];
				String s配股价   = s[5];
				String s送股数   = s[6];
				String s配股数   = s[7];
//				String[] sData = new String[] {s市场, s股票代码,s日期,s权息数据类别,s派息金额,s配股价,s送股数,s配股数};

				除权entity.set股票代码(s股票代码);
				除权entity.set市场代码(s市场);
				除权entity.setUTCtime(s日期);
				除权entity.set分红(s派息金额);
				除权entity.set送股(s送股数);
				除权entity.set配股(s配股数);
				除权entity.set配股价(s配股价);

				除权Entitylist.add(除权entity);
			}

		}

		return 除权Entitylist;
	}

}
