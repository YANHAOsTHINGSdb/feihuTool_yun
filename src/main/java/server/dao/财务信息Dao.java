package server.dao;

import java.util.List;

import server.entity.财务Entity;

public interface 财务信息Dao {

	财务Entity findById(int id);

	财务Entity findByStockCode(String stockCode);

	void save(财务Entity 财务entity);

	void deleteByStockCode(String stockCode);

	List<财务Entity> findAll财务信息();

	void save(List<财务Entity> out财务DataInputBean飞狐list);

}
