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

import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;
@Data
@Entity						// 表明该类 (UserEntity) 为一个实体类
@Table(name = "财务信息")		// 它默认对应数据库中的表名是[除权信息]
							// @Table 当实体类与其映射的数据库表名不同名时需要使用
public class 财务Entity extends AbstractEntity implements IEntity {

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
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "seq2")
	@SequenceGenerator(name = "seq2", sequenceName = "caiwu_seq")
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
	@Column(name="流通股本", nullable=true)
	private String 流通股本;

	@NotEmpty
	@Column(name="所属省份", nullable=true)
	private String 所属省份;

	@NotEmpty
	@Column(name="所属行业", nullable=true)
	private String 所属行业;

	@NotEmpty
	@Column(name="账务更新日期", nullable=true)
	private String 账务更新日期;

	@NotEmpty
	@Column(name="上市日期", nullable=true)
	private String 上市日期;

	@NotEmpty
	@Column(name="总股本", nullable=true)
	private String 总股本;

	@NotEmpty
	@Column(name="国家股", nullable=true)
	private String 国家股;

	@NotEmpty
	@Column(name="发起人法人股", nullable=true)
	private String 发起人法人股;

	@NotEmpty
	@Column(name="法人股", nullable=true)
	private String 法人股;

	@NotEmpty
	@Column(name="B股", nullable=true)
	private String B股;

	@NotEmpty
	@Column(name="H股", nullable=true)
	private String H股;

	@NotEmpty
	@Column(name="职工股", nullable=true)
	private String 职工股;

	@NotEmpty
	@Column(name="总资产", nullable=true)
	private String 总资产;

	@NotEmpty
	@Column(name="流动资产", nullable=true)
	private String 流动资产;

	@NotEmpty
	@Column(name="固定资产", nullable=true)
	private String 固定资产;

	@NotEmpty
	@Column(name="无形资产", nullable=true)
	private String 无形资产;

	@NotEmpty
	@Column(name="股东人数", nullable=true)
	private String 股东人数;

	@NotEmpty
	@Column(name="流动负债", nullable=true)
	private String 流动负债;

	@NotEmpty
	@Column(name="长期负债", nullable=true)
	private String 长期负债;

	@NotEmpty
	@Column(name="资本公积金", nullable=true)
	private String 资本公积金;

	@NotEmpty
	@Column(name="净资产", nullable=true)
	private String 净资产;

	@NotEmpty
	@Column(name="主营收入", nullable=true)
	private String 主营收入;

	@NotEmpty
	@Column(name="主营利润", nullable=true)
	private String 主营利润;

	@NotEmpty
	@Column(name="应收帐款", nullable=true)
	private String 应收帐款;

	@NotEmpty
	@Column(name="营业利润", nullable=true)
	private String 营业利润;

	@NotEmpty
	@Column(name="投资收益", nullable=true)
	private String 投资收益;

	@NotEmpty
	@Column(name="经营现金流", nullable=true)
	private String 经营现金流;

	@NotEmpty
	@Column(name="总现金流", nullable=true)
	private String 总现金流;

	@NotEmpty
	@Column(name="存贷", nullable=true)
	private String 存贷;

	@NotEmpty
	@Column(name="利润总额", nullable=true)
	private String 利润总额;

	@NotEmpty
	@Column(name="税后利润", nullable=true)
	private String 税后利润;

	@NotEmpty
	@Column(name="净利润", nullable=true)
	private String 净利润;

	@NotEmpty
	@Column(name="未分利润", nullable=true)
	private String 未分利润;

	@NotEmpty
	@Column(name="保留", nullable=true)
	private String 保留;




}
