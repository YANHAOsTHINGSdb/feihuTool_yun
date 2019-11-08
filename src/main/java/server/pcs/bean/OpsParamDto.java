package server.pcs.bean;

import lombok.Data;

/**
 * 开关型参数和传值型参数
 * @author fufei
 *
 */
@Data
public class OpsParamDto {
	/**
	 * 参数编号
	 */
	int no;
	/**
	 * 开关型参数的开关值
	 */
	Boolean flag = false;
	/**
	 * 传值型参数值
	 */
	String value;
	/**
	 * 参数类型 true:传值型,false:开关型
	 */
	Boolean isValue = false;

	/**
	 * 传值型参数初始化
	 * @param no 参数编号
	 * @param value 参数值
	 * @param isValue 参数类型
	 */
	public OpsParamDto(int no, String value, Boolean isValue) {
		this.no = no;
		this.value = value;
		this.isValue = isValue;
	}

	/**
	 * 开关型参数的初始化
	 * @param no 参数编号
	 * @param flag 开关值
	 * @param isValue 参数类型
	 */
	public OpsParamDto(int no, Boolean flag, Boolean isValue) {
		this.no = no;
		this.flag = flag;
		this.isValue = isValue;
	}
}
