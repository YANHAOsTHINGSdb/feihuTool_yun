package server.controller;

import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class 行情数据Controller {

	public 行情数据Controller() {

	}

	// 行情数据下载
	public static void main(String[] args) {

	}

	@RequestMapping(value = "HangQing", method = RequestMethod.GET)
	public String 除权信息(Locale locale, Model model) {
		return "行情补全";

	}
}
