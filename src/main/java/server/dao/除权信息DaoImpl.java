package server.dao;

import java.util.List;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import server.entity.除权Entity;
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

@Repository("除权信息Dao")
public class 除权信息DaoImpl  extends AbstractDao<Integer, 除权Entity> implements 除权信息Dao {

	public 除权Entity findById(int id) {
		// 用标准函数取得指定ID的值
		除权Entity 除权entity = getByKey(id);
		if(除权entity!=null){
			// initializeCollection(user.getUserProfiles());
		}
		return 除权entity;
	}

	public 除权Entity findByStockCode(String stockCode) {
		System.out.println("stockCode : "+stockCode);
		try{
			// 取得单条结果
			除权Entity 除权entity = (除权Entity) getEntityManager()
					.createQuery("SELECT u FROM User u WHERE u.ssoId LIKE :ssoId")
					.setParameter("ssoId", stockCode)
					.getSingleResult();

			if(除权entity!=null){
				// initializeCollection(user.getUserProfiles());
			}
			return 除权entity;
		}catch(NoResultException ex){
			return null;
		}
	}

	public 除权Entity findByStockInfo(除权Entity 除权entityParam) {

		try{
			// 取得单条结果
			// 市场代码 + 股票代码 + 时间（到日）+ 送股 + 配股 + 配股价 + 分红

			// .createQuery("SELECT u FROM 除权信息 u WHERE u.市场代码 LIKE :市场代码 AND u.股票代码 LIKE :股票代码 AND u.UTCtime LIKE :UTCtime AND u.送股 LIKE :送股 AND u.配股 LIKE :配股 AND u.配股价 LIKE :配股价 AND u.分红 LIKE :分红")
			// .createQuery("SELECT u FROM 除权Entity u WHERE u.市场代码 LIKE :市场代码")
			除权Entity 除权entity = (除权Entity) getEntityManager()
					.createQuery("SELECT u FROM 除权Entity u WHERE u.市场代码 LIKE :市场代码 AND u.股票代码 LIKE :股票代码 AND u.UTCtime LIKE :UTCtime AND u.送股 LIKE :送股 AND u.配股 LIKE :配股 AND u.配股价 LIKE :配股价 AND u.分红 LIKE :分红")
					.setParameter("市场代码", 除权entityParam.get市场代码())
					.setParameter("股票代码", 除权entityParam.get股票代码())
					.setParameter("UTCtime", 除权entityParam.getUTCtime())
					.setParameter("送股", 除权entityParam.get送股())
					.setParameter("配股", 除权entityParam.get配股())
					.setParameter("配股价", 除权entityParam.get配股价())
					.setParameter("分红", 除权entityParam.get分红())

					.getSingleResult();

			if(除权entity!=null){
				return 除权entity;
			}
			return null;

		}catch(NoResultException ex){
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<除权Entity> findAll除权信息() {
		// 取得所有值
		List<除权Entity> 除权entityList= getEntityManager()
				.createQuery("SELECT u FROM User u ORDER BY u.firstName ASC")
				.getResultList();
		return 除权entityList;
	}

	public void save(除权Entity 除权entity) {
		// 用标准函数追加
		persist(除权entity);
	}

	public void deleteByStockCode(String stockCode) {
		除权Entity 除权entity = (除权Entity) getEntityManager()
				.createQuery("SELECT u FROM User u WHERE u.ssoId LIKE :ssoId")
				.setParameter("ssoId", stockCode)
				.getSingleResult();
		// 用标准函数删除
		delete(除权entity);
	}

	public void save(List<除权Entity> 除权Entitylist) {
		// TODO 自動生成されたメソッド・スタブ
		for(除权Entity 除权entity : 除权Entitylist) {
			// 用每条信息进行查询，如果DB中存在，就不追加了。
			// 市场代码 + 股票代码 + 时间（到日）+ 权息信息属性 + 送股 + 配股 + 配股价 + 分红
			除权Entity result = findByStockInfo(除权entity);
			if(result == null) {
				save(除权entity);
			}
		}
	}

//	//An alternative to Hibernate.initialize()
//	protected void initializeCollection(Collection<?> collection) {
//	    if(collection == null) {
//	        return;
//	    }
//	    collection.iterator().hasNext();
//	}
}
