package server.dao;

import java.util.List;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import server.entity.财务Entity;
/**
 * 参照：https://blog.csdn.net/qq_37464248/article/details/82769868
 *
	// 1.创建[除权信息]表
	CREATE TABLE 除权信息(
		id int primary key,
		市场代码 VARCHAR(1),
		股票代码 VARCHAR(6),
		UTCtime VARCHAR(10),
		送股 VARCHAR(8),
		配股 VARCHAR(8),
		配股价 VARCHAR(8),
		分红 VARCHAR(8)
	);


	// 2.创建自动增长序列
	CREATE SEQUENCE chuquan_id_seq
	  START WITH 1
	  INCREMENT BY 1
	  NO MINVALUE
	  NO MAXVALUE
	  CACHE 1;

	// 3.为[除权信息]表添加自动增长序列
	ALTER TABLE 除权信息 ALTER COLUMN id SET DEFAULT nextval('chuquan_id_seq');

	// 4.向[除权信息]表Insert数据
	INSERT INTO 除权信息 Values(nextval('chuquan_id_seq'), '0', '','','');

	// 5.查询[除权信息]表中数据
	SELECT * FROM public.book;
 */

@Repository("财务信息dao")
public class 财务信息DaoImpl  extends AbstractDao<Integer, 财务Entity> implements 财务信息Dao {

	public 财务Entity findById(int id) {
		// 用标准函数取得指定ID的值
		财务Entity 财务entity = getByKey(id);
		if(财务entity!=null){
			// initializeCollection(user.getUserProfiles());
		}
		return 财务entity;
	}

	public 财务Entity findByStockCode(String stockCode) {
		System.out.println("stockCode : "+stockCode);
		try{
			// 取得单条结果
			财务Entity 财务entity = (财务Entity) getEntityManager()
					.createQuery("SELECT u FROM User u WHERE u.ssoId LIKE :ssoId")
					.setParameter("ssoId", stockCode)
					.getSingleResult();

			if(财务entity!=null){
				// initializeCollection(user.getUserProfiles());
			}
			return 财务entity;
		}catch(NoResultException ex){
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<财务Entity> findAll财务信息() {
		// 取得所有值
		List<财务Entity> 财务entityList= getEntityManager()
				.createQuery("SELECT u FROM User u ORDER BY u.firstName ASC")
				.getResultList();
		return 财务entityList;
	}

	public void save(财务Entity 财务entity) {
		// 用标准函数追加
		persist(财务entity);
	}

	public void deleteByStockCode(String stockCode) {
		财务Entity 财务entity = (财务Entity) getEntityManager()
				.createQuery("SELECT u FROM User u WHERE u.ssoId LIKE :ssoId")
				.setParameter("ssoId", stockCode)
				.getSingleResult();
		// 用标准函数删除
		delete(财务entity);
	}


	public void save(List<财务Entity> 财务Entitylist) {
		// TODO 自動生成されたメソッド・スタブ
		for(财务Entity 财务entity : 财务Entitylist) {
			财务Entity result= findByStockInfo(财务entity);
			if(result==null)
			save(财务entity);
		}
	}

//	//An alternative to Hibernate.initialize()
//	protected void initializeCollection(Collection<?> collection) {
//	    if(collection == null) {
//	        return;
//	    }
//	    collection.iterator().hasNext();
//	}
	public 财务Entity findByStockInfo(财务Entity 财务entityParam) {

		  try{
		   // 取得单条结果
		   // 市场代码 + 股票代码 + 时间（到日）+ 送股 + 配股 + 配股价 + 分红

		   // .createQuery("SELECT u FROM 除权信息 u WHERE u.市场代码 LIKE :市场代码 AND u.股票代码 LIKE :股票代码 AND u.UTCtime LIKE :UTCtime AND u.送股 LIKE :送股 AND u.配股 LIKE :配股 AND u.配股价 LIKE :配股价 AND u.分红 LIKE :分红")
		   // .createQuery("SELECT u FROM 除权Entity u WHERE u.市场代码 LIKE :市场代码")
		   财务Entity 财务entity = (财务Entity) getEntityManager()
		     .createQuery("SELECT u FROM 财务Entity u WHERE u.市场代码 LIKE :市场代码 AND u.股票代码 LIKE :股票代码 AND u.UTCtime LIKE :UTCtime AND u.账务更新日期 LIKE :账务更新日期")
		     .setParameter("市场代码", 财务entityParam.get市场代码())
		     .setParameter("股票代码", 财务entityParam.get股票代码())
		     .setParameter("UTCtime", 财务entityParam.getUTCtime())
//		     .setParameter("流通股本", 财务entityParam.get流通股本())
//		     .setParameter("所属省份", 财务entityParam.get所属省份())
//		     .setParameter("所属行业", 财务entityParam.get所属行业())
		     .setParameter("账务更新日期", 财务entityParam.get账务更新日期())
//		     .setParameter("上市日期", 财务entityParam.get上市日期())
//		     .setParameter("总股本", 财务entityParam.get总股本())
//		     .setParameter("国家股", 财务entityParam.get国家股())
//		     .setParameter("发起人法人股", 财务entityParam.get发起人法人股())
//		     .setParameter("法人股", 财务entityParam.get法人股())
//		     .setParameter("B股", 财务entityParam.getB股())
//		     .setParameter("H股", 财务entityParam.getH股())
//		     .setParameter("职工股", 财务entityParam.get职工股())
//		     .setParameter("总资产", 财务entityParam.get总资产())
//		     .setParameter("流动资产", 财务entityParam.get流动资产())
//		     .setParameter("固定资产", 财务entityParam.get固定资产())
//		     .setParameter("无形资产", 财务entityParam.get无形资产())
//		     .setParameter("股东人数", 财务entityParam.get股东人数())
//		     .setParameter("流动负债", 财务entityParam.get流动负债())
//		     .setParameter("长期负债", 财务entityParam.get长期负债())
//		     .setParameter("资本公积金", 财务entityParam.get资本公积金())
//		     .setParameter("净资产", 财务entityParam.get净资产())
//		     .setParameter("主营收入", 财务entityParam.get主营收入())
//		     .setParameter("主营利润", 财务entityParam.get主营利润())
//		     .setParameter("应收帐款", 财务entityParam.get应收帐款())
//		     .setParameter("营业利润", 财务entityParam.get营业利润())
//		     .setParameter("投资收益", 财务entityParam.get投资收益())
//		     .setParameter("经营现金流", 财务entityParam.get经营现金流())
//		     .setParameter("总现金流", 财务entityParam.get总现金流())
//		     .setParameter("存贷", 财务entityParam.get存贷())
//		     .setParameter("利润总额", 财务entityParam.get利润总额())
//		     .setParameter("税后利润", 财务entityParam.get税后利润())
//		     .setParameter("净利润 ", 财务entityParam.get净利润 ())
//		     .setParameter("未分利润", 财务entityParam.get未分利润())
//		     .setParameter("保留", 财务entityParam.get保留())

		     .getSingleResult();

		   if(财务entity!=null){
		    return 财务entity;
		   }
		   return null;

		  }catch(NoResultException ex){
		   return null;
		  }
		 }
}
