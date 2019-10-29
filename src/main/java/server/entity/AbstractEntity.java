/*
* 本資料には、東京電力株式会社またはその他の企業の秘密情報が含まれている可能性があります。
* 当社の許可なく本資料の複製物を作成すること、本資料の内容を本来の目的以外に使用すること、
* ならびに第三者に開示、公開する行為を禁止します。                        東京電力株式会社
*
* * システム名    ：第３世代託送業務システム
* * サブシステム名：地点(lpd),  契約(lpb),  申込(lpl),   検針(lpc),  営料(lpf）,  共通(lpa)
* * ファイル名    ：@(#) AbstractEntity.java
*
* * 会社名：東京電力株式会社
* * 作成日：2017/03/05
* * 作成者：
* * メールアドレス：
*/
package server.entity;

import javax.persistence.MappedSuperclass;

/*
 * @MappedSuperclassのアノテーションを付けていること。
 * これでこのクラスを実装していれば各エンティティクラスに
 * わざわざdeleteFlgの永続化フィールドを設定する必要が無くなります。
 */
@MappedSuperclass
public abstract class AbstractEntity implements IEntity{

//  @Column(name = "DELETE_FLG", nullable = false)
  private int deleteFlg;

  public void setDeleteFlg(int deleteFlg){
     this.deleteFlg = deleteFlg;
  }

  public int getDeleteFlg(){
     return this.deleteFlg;
  }
}