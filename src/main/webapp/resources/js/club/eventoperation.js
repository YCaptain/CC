$(function() {
	// 从URL里获取eventId参数的值
	var eventId = getQueryString('eventId');
	// 通过eventId获取活动信息的URL
	var infoUrl = '/CC/clubadmin/geteventbyid?eventId=' + eventId;
	// 获取当前社团设定的活动类别列表的URL
	var categoryUrl = '/CC/clubadmin/geteventcategorylist';
	// 更新活动信息的URL
	var eventPostUrl = '/CC/clubadmin/modifyevent';
	// 由于活动添加和编辑使用的是同一个页面，
	// 该标识符用来标明本次是添加还是编辑操作
	var isEdit = false;
	if (eventId) {
		// 若有eventId则为编辑操作
		getInfo(eventId);
		isEdit = true;
	} else {
		getCategory();
		eventPostUrl = '/CC/clubadmin/addevent';
	}

	// 获取需要编辑的活动的活动信息，并赋值给表单
	function getInfo(id) {
		$
				.getJSON(
						infoUrl,
						function(data) {
							if (data.success) {
								// 从返回的JSON当中获取event对象的信息，并赋值给表单
								var event = data.event;
								$('#event-name').val(event.eventName);
								$('#event-desc').val(event.eventDesc);
								$('#priority').val(event.priority);
								$('#num-person').val(event.numPerson);
								$('#capacity').val(
										event.capacity);
								// 获取原本的活动类别以及该社团的所有活动类别列表
								var optionHtml = '';
								var optionArr = data.eventCategoryList;
								var optionSelected = event.eventCategory.eventCategoryId;
								// 生成前端的HTML活动类别列表，并默认选择编辑前的活动类别
								optionArr
										.map(function(item, index) {
											var isSelect = optionSelected === item.eventCategoryId ? 'selected'
													: '';
											optionHtml += '<option data-value="'
													+ item.eventCategoryId
													+ '"'
													+ isSelect
													+ '>'
													+ item.eventCategoryName
													+ '</option>';
										});
								$('#category').html(optionHtml);
							}
						});
	}

	// 为活动添加操作提供该社团下的所有活动类别列表
	function getCategory() {
		$.getJSON(categoryUrl, function(data) {
			if (data.success) {
				var eventCategoryList = data.data;
				var optionHtml = '';
				eventCategoryList.map(function(item, index) {
					optionHtml += '<option data-value="'
							+ item.eventCategoryId + '">'
							+ item.eventCategoryName + '</option>';
				});
				$('#category').html(optionHtml);
			}
		});
	}

	// 针对活动详情图控件组，若该控件组的最后一个元素发生变化（即上传了图片），
	// 且控件总数未达到6个，则生成新的一个文件上传控件
	$('.detail-img-div').on('change', '.detail-img:last-child', function() {
		if ($('.detail-img').length < 6) {
			$('#detail-img').append('<input type="file" class="detail-img">');
		}
	});

	// 提交按钮的事件响应，分别对活动添加和编辑操作做不同响应
	$('#submit').click(
			function() {
				// 创建活动json对象，并从表单里面获取对应的属性值
				var event = {};
				event.eventName = $('#event-name').val();
				event.eventDesc = $('#event-desc').val();
				event.priority = $('#priority').val();
				event.numPerson = $('#num-person').val();
				event.capacity = $('#capacity').val();
				// 获取选定的活动类别值
				event.eventCategory = {
					eventCategoryId : $('#category').find('option').not(
							function() {
								return !this.selected;
							}).data('value')
				};
				event.eventId = eventId;

				// 获取缩略图文件流
				var thumbnail = $('#small-img')[0].files[0];
				// 生成表单对象，用于接收参数并传递给后台
				var formData = new FormData();
				formData.append('thumbnail', thumbnail);
				// 遍历活动详情图控件，获取里面的文件流
				$('.detail-img').map(
						function(index, item) {
							// 判断该控件是否已选择了文件
							if ($('.detail-img')[index].files.length > 0) {
								// 将第i个文件流赋值给key为eventImgi的表单键值对里
								formData.append('eventImg' + index,
										$('.detail-img')[index].files[0]);
							}
						});
				// 将event json对象转成字符流保存至表单对象key为eventStr的的键值对里
				formData.append('eventStr', JSON.stringify(event));
				// 获取表单里输入的验证码
				var verifyCodeActual = $('#j_captcha').val();
				if (!verifyCodeActual) {
					$.toast('请输入验证码！');
					return;
				}
				formData.append("verifyCodeActual", verifyCodeActual);
				// 将数据提交至后台处理相关操作
				$.ajax({
					url : eventPostUrl,
					type : 'POST',
					data : formData,
					contentType : false,
					processData : false,
					cache : false,
					success : function(data) {
						if (data.success) {
							$.toast('提交成功！');
							$('#captcha_img').click();
						} else {
							$.toast('提交失败！');
							$('#captcha_img').click();
						}
					}
				});
			});

});