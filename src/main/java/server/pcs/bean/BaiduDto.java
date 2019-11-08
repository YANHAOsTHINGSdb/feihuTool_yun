package server.pcs.bean;

import lombok.Data;

@Data
public class BaiduDto {
	String UID;      // 百度ID对应的uid
	String name;     // 真实ID
	String nameShow; // 显示的用户名(昵称)
	String sex;      // 性别
	int age;         // 帐号年龄
	String bduss;    // 百度BDUSS
	String ptoken;
	String stoken;
	String workdir = "/";


	@Override
	public String toString() {
		return "BaiduDto [UID=" + UID + ", name=" + name + ", nameShow=" + nameShow + ", sex=" + sex + ", age=" + age
				+ ", bduss=" + bduss + ", ptoken=" + ptoken + ", stoken=" + stoken + ", workdir=" + workdir + "]";
	}
}
