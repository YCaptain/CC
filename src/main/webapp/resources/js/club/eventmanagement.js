$(function() {
	// 获取此社团下的活动列表的URL
	var listUrl = '/CC/clubadmin/geteventlistbyclub?pageIndex=1&pageSize=999';
	// 活动下架URL
	var statusUrl = '/CC/clubadmin/modifyevent';
	getList();
	/**
	 * 获取此社团下的活动列表
	 * 
	 * @returns
	 */
	function getList() {
		// 从后台获取此社团的活动列表
		$.getJSON(listUrl, function(data) {
			if (data.success) {
				var eventList = data.eventList;
				var tempHtml = '';
				// 遍历每条活动信息，拼接成一行显示，列信息包括：
				// 活动名称，优先级，上架\下架(含eventId)，编辑按钮(含eventId)
				// 预览(含eventId)
				eventList.map(function(item, index) {
					var textOp = "下架";
					var contraryStatus = 0;
					if (item.enableStatus == 0) {
						// 若状态值为0，表明是已下架的活动，操作变为上架(即点击上架按钮上架相关活动)
						textOp = "上架";
						contraryStatus = 1;
					} else {
						contraryStatus = 0;
					}
					// 拼接每件活动的行信息
					tempHtml += '' + '<div class="row row-event">'
							+ '<div class="col-33">'
							+ item.eventName
							+ '</div>'
							+ '<div class="col-20">'
							+ item.priority
							+ '</div>'
							+ '<div class="col-40">'
							+ '<a href="#" class="edit" data-id="'
							+ item.eventId
							+ '" data-status="'
							+ item.enableStatus
							+ '">编辑</a>'
							+ '<a href="#" class="status" data-id="'
							+ item.eventId
							+ '" data-status="'
							+ contraryStatus
							+ '">'
							+ textOp
							+ '</a>'
							+ '<a href="#" class="preview" data-id="'
							+ item.eventId
							+ '" data-status="'
							+ item.enableStatus
							+ '">预览</a>'
							+ '</div>'
							+ '</div>';
				});
				// 将拼接好的信息赋值进html控件中
				$('.event-wrap').html(tempHtml);
			}
		});
	}
	// 将class为event-wrap里面的a标签绑定上点击的事件
	$('.event-wrap')
			.on(
					'click',
					'a',
					function(e) {
						var target = $(e.currentTarget);
						if (target.hasClass('edit')) {
							// 如果有class edit则点击就进入社团信息编辑页面，并带有eventId参数
							window.location.href = '/CC/clubadmin/eventoperation?eventId='
									+ e.currentTarget.dataset.id;
						} else if (target.hasClass('status')) {
							// 如果有class status则调用后台功能上/下架相关活动，并带有eventId参数
							changeItemStatus(e.currentTarget.dataset.id,
									e.currentTarget.dataset.status);
						} else if (target.hasClass('preview')) {
							// 如果有class preview则去前台展示系统该活动详情页预览活动情况
							window.location.href = '/CC/frontend/eventdetail?eventId='
									+ e.currentTarget.dataset.id;
						}
					});
	function changeItemStatus(id, enableStatus) {
		// 定义event json对象并添加eventId以及状态(上架/下架)
		var event = {};
		event.eventId = id;
		event.enableStatus = enableStatus;
		$.confirm('确定么?', function() {
			// 上下架相关活动
			$.ajax({
				url : statusUrl,
				type : 'POST',
				data : {
					eventStr : JSON.stringify(event),
					statusChange : true
				},
				dataType : 'json',
				success : function(data) {
					if (data.success) {
						$.toast('操作成功！');
						getList();
					} else {
						$.toast('操作失败！');
					}
				}
			});
		});
	}
});