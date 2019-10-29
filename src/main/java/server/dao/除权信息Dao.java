package server.dao;

import java.util.List;

import server.entity.除权Entity;

public interface 除权信息Dao {

	除权Entity findById(int id);

	除权Entity findByStockCode(String stockCode);

	void save(除权Entity 除权entity);

	void deleteByStockCode(String stockCode);

	List<除权Entity> findAll除权信息();

	void save(List<除权Entity> out除权DataInputBean飞狐list);

}
