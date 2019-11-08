package server.controller;

import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class 问题对策Controller extends 爸爸Controller{

	public 问题对策Controller() {
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@RequestMapping(value = "wenti", method = RequestMethod.GET)
	public String 问题对策(Locale locale, Model model) {
		return "问题及解决对策";

	}
}
