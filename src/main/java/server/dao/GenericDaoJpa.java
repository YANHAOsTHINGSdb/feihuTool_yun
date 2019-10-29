/*
* 本資料には、東京電力株式会社またはその他の企業の秘密情報が含まれている可能性があります。
* 当社の許可なく本資料の複製物を作成すること、本資料の内容を本来の目的以外に使用すること、
* ならびに第三者に開示、公開する行為を禁止します。                        東京電力株式会社
*
* * システム名    ：第３世代託送業務システム
* * サブシステム名：地点(lpd),  契約(lpb),  申込(lpl),   検針(lpc),  営料(lpf）,  共通(lpa)
* * ファイル名    ：@(#) GenericDaoJpa.java
*
* * 会社名：東京電力株式会社
* * 作成日：2017/03/05
* * 作成者：
* * メールアドレス：
*/
package server.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import server.entity.AbstractEntity;


public class GenericDaoJpa<E extends AbstractEntity, PK extends Serializable> implements IGenericDao<E, PK> {

    private EntityManager em;

    private Class<E> clazz;

    public GenericDaoJpa(EntityManager em, Class<E> clazz) {

        this.em = em;
        this.clazz = clazz;
    }


    public E findByPrimaryKey(PK primaryKey) {

        return em.find(clazz, primaryKey);
    }


    public void merge(E entity) {

        em.merge(entity);
    }


    public void save(E entity) {

        em.persist(entity);
    }


    public void remove(E entity) {

        em.remove(entity);
    }


    public <T> T executeJpql(IJpaCallback callback) {

        return (T)callback.execute(em);
    }


    public void logicalRemove(E entity) {

        entity.setDeleteFlg(1);
        merge(entity);
    }


    public List<E> findAll() {

        return (List<E>) em.createQuery(" From " + clazz.getSimpleName() + " e WHERE e.deleteFlg = 0 ");
    }
}