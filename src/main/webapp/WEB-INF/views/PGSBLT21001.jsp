<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding = "UTF-8" %>
<!-- <%@ include file = "/jsp/incTop.jsp" %> -->
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-Type" content="text/html; charset=UTF-8">
<title><parts:message code="title.screnName" /></title>
<script type="text/javascript">

	function click_rndParameterCalculate(){
		// 放置2次按下
		if(isSubmitted){
			return false;
		}
		isSubmitted = true;
		$.ajax([
			data 		: $(BLT21001Form').serialize(),
			type 		: 'post',
			dataType 	: 'html',
			url  		: '../PGSBLT21001/PGSBLT21001RndmParameterCalculate.form',
			success 	: function(data){
				if(   $(data).find('.np-err-input-item').length == 0
				   && $(data).find('#PGSBLT22001\\.errors').length == 0
				){
					// 打开子画面
					openModalDialogInherit(
						"../PGSBLT22001/PGSBLT22001Init.form",
						false,
						false,
						"dialogwidth:1000px",
						"dialogheight:650px"
						);

				}else{
					// 画面错误啦
					$('#PGSBLT22001Form').html(data);
					executeAction('../PGSBLT22001/PGSBLT22001RndmParameterCalculate.form');
				}


			}, // success完结
			complete:function(){
				isSubmitted = false;
			}
		]);
	}

	<%-- TotoOrBig按钮 按下--%>
	function click_editTotoOrBig(index){
		// 放置2次按下
		if(isSubmitted){
			return false;
		}
		isSubmitted = true;

		// 取得所选行
		$("input [name='hiddenSelectRow']").val(index);

		// 打开子画面
		$.ajax({
			url		:'../PGSBLT21001/PGSBLT21001EditTotoOrBig.form',
			data    :$('#PGSBLT21001Form').serialize()，
			type    :'post'
			dataType:'html',
			success : function(data){
				var refreshForm = $(data).find(‘#PGSBLT21501Form’);
				if(refreshForm.length != 0 ){
					// 移除主画面countryId的'data-tooltip'
					$('#countryId').removeAttr('data-tooltip');
					// 拉上大黑幕
					$('.l-np-dialog-bk').show();
					// 显示子画面
					$('#subPage').html(refreshForm);
					// 将子画面的countryName置成无效
					$('#subPage').find('#countryName').attr("disabled",true);
					// 将子画面的applicatipnholdCnt置成无效
					$('#subPage').find('#applicatipnholdCnt').attr("disabled",true);
					// 清除错误
					comFunc.clearError();
					// 设置光标初始位置
					comFunc.setFirstFocus('subPage');
				}else{
					$('#footer').html($(data).find().html());
				}

			}, // success完结
			complete:function(){
				isSubmitted = false;
			}

		});
	}

	<%-- edit按钮 按下--%>
	function click_editGoal(index){
		// 放置2次按下
		if(isSubmitted){
			return false;
		}
		isSubmitted = true;

		// 取得所选行
		$("input [name='hiddenSelectRow']").val(index);

		// 打开子画面
		$.ajax({
			url		:'../PGSBLT21001/PGSBLT21001EditGoal.form',
			data    :$('#PGSBLT21001Form').serialize()，
			type    :'post'
			dataType:'html',
			success : function(data){
				// 在返回data中找PGSBLT21501Form的代码
				var refreshForm = $(data).find(‘#PGSBLT21501Form’);

				if(refreshForm.length != 0 ){
					// 移除主画面countryId的'data-tooltip'
					$('#countryId').removeAttr('data-tooltip');
					// 拉上大黑幕
					$('.l-np-dialog-bk').show();
					// 显示子画面
					$('#subPage').html(refreshForm);
					// 将子画面的countryName置成无效
					$('#subPage').find('#countryName').attr("disabled",true);
					// 将子画面的applicatipnholdCnt置成无效
					$('#subPage').find('#applicatipnholdCnt').attr("disabled",true);
					// 清除错误
					comFunc.clearError();
					// 设置光标初始位置
					comFunc.setFirstFocus('subPage');
				}else{
					$('#footer').html($(data).find().html());
				}

			}, // success完结
			complete:function(){
				isSubmitted = false;
			}

		});
	}

	<%-- 追加按钮 按下--%>
	function click_addTotoOrBig(index){
		// 放置2次按下
		if(isSubmitted){
			return false;
		}
		isSubmitted = true;


		// 打开子画面
		$.ajax({
			url		:'../PGSBLT21001/PGSBLT21001AddTotoOrBig.form',
			// serialize()=输出序列化表单值的结果、参照https://www.w3school.com.cn/jquery/ajax_serialize.asp
			data    :$('#PGSBLT21001Form').serialize()，
			type    :'post',
			dataType:'html',
			success : function(data){
				if(   $(data).find('.np-err-input-item').length == 0
				   && $(data).find('#PGSBLT22001\\.errors').length == 0
				){
					// 打开子画面
					var refreshFrom = $(data).find("#PGSBLT21501Form");
					$('#countryId').removeAttr('data-tooltip');
					$('.l-np-dialog-bk').show();
					// 显示子画面：将取到的画面html值放入subPage中
					$('#subPage').html(refreshForm);
					// 将子画面的countryName置成无效
					$('#subPage').find('#countryName').attr("disabled",true);
					// 清除错误
					comFunc.clearError();
					// 设置光标初始位置
					comFunc.setFirstFocus('subPage');
				}else{
					// 画面错误啦
					$('#PGSBLT22001Form').html(data);
					executeAction('../PGSBLT22001/PGSBLT22001RndmParameterCalculate.form');
				}

			}, // success完结
			complete:function(){
				isSubmitted = false;
			}

		});
	}

</script>

<body class="l-np-base">
	<parts:form modelAttribute="PGSBLT21001Form">

		<article class="l-np-contents" style="padding-top: 60px; z-index: 2">
			<parts:button id="addBtn" onclick="click_addTotoOrBig()" >
				登陆百度网盘
			</parts:button>
		</article>
	</parts:form>
	<!-- 这里是子画面-->
	<div id="subPage"></div>
</body>


