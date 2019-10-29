package server.entity;

//CREATE TABLE 除权信息(
//		id int,
//		市场代码 VARCHAR(1),
//		股票代码 VARCHAR(6),
//		UTCtime VARCHAR(10),
//		送股 VARCHAR(8),
//		配股 VARCHAR(8),
//		配股价 VARCHAR(8),
//		分红 VARCHAR(8)
//	);

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import lombok.Data;
@Data
@Entity						// 表明该类 (UserEntity) 为一个实体类
@Table(name = "除权信息")		// 它默认对应数据库中的表名是[除权信息]
							// @Table 当实体类与其映射的数据库表名不同名时需要使用
public class 除权Entity extends AbstractEntity implements IEntity {

	/**
	 @Column注释定义了将成员属性映射到关系表中的哪一列和该列的结构信息，属性如下：
	　　1）name：映射的列名。如：映射tbl_user表的name列，可以在name属性的上面或getName方法上面加入；
	　　2）unique：是否唯一；
	　　3）nullable：是否允许为空；
	　　4）length：对于字符型列，length属性指定列的最大字符长度；
	　　5）insertable：是否允许插入；
	　　6）updatetable：是否允许更新；
	　　7）columnDefinition：定义建表时创建此列的DDL；
	　　8）secondaryTable：从表名。如果此列不建在主表上（默认是主表），该属性定义该列所在从表的名字
	 */

	/**
	 @Id注释指定表的主键，它可以有多种生成方式：
	　　1）TABLE：容器指定用底层的数据表确保唯一；
	　　2）SEQUENCE：使用数据库德SEQUENCE列莱保证唯一（Oracle数据库通过序列来生成唯一ID）；
	　　3）IDENTITY：使用数据库的IDENTITY列莱保证唯一；
	　　4）AUTO：由容器挑选一个合适的方式来保证唯一；
	　　5）NONE：容器不负责主键的生成，由程序来完成。
	 */

	/**
	 * GeneratorValue属于一个JPA接口，其接口下包含了两个抽象的参数，GenerationType类型的strategy和String类型的generator，并且两个参数都有相应的默认值。
		1.generator  : String  //JPA 持续性提供程序为它选择的主键生成器分配一个名称,如果该名称难于处理、是一个保留字、与事先存在的数据模型不兼容或作为数据库中的主键生成器名称无效，则将 generator 设置为要使用的 String 生成器名称。
	 	2.strategy   : String  // 指定生成策略
	 		JPA提供的四种标准用法为TABLE, SEQUENCE, IDENTITY, AUTO. 

				TABLE   ：使用一个特定的数据库表格来保存主键。 
				SEQUENCE：根据底层数据库的序列来生成主键，条件是数据库支持序列。 
				IDENTITY：主键由数据库自动生成（主要是自动增长型） 
				AUTO    ：主键由程序控制。
	 */

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "seq1")
	@SequenceGenerator(name = "seq1", sequenceName = "chuquan_seq")
	@Column(name = "id", unique = true, nullable = false)
	private long id;

	@NotEmpty
	@Column(name="市场代码", nullable=false)
	private String 市场代码;

	@NotEmpty
	@Column(name="股票代码", nullable=false)
	private String 股票代码;

	@NotEmpty
	@Column(name="UTCtime", nullable=false)
	private String UTCtime;

	@NotEmpty
	@Column(name="送股", nullable=true)
	private String 送股;			// 送股   :

	@NotEmpty
	@Column(name="配股", nullable=true)
	private String 配股;			// 配股   :

	@NotEmpty
	@Column(name="配股价", nullable=true)
	private String 配股价;		// 配股价 :

	@NotEmpty
	@Column(name="分红", nullable=true)
	private String 分红;			// 分红   :

}
