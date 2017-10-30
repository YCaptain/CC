$(function() {
	// 从地址栏的URL里获取eventId
	var eventId = getQueryString('eventId');
	// 获取活动信息的URL
	var eventUrl = '/CC/frontend/listeventdetailpageinfo?eventId='
			+ eventId;
	// 访问后台获取该活动的信息并渲染
	$.getJSON(eventUrl, function(data) {
		if (data.success) {
			// 获取活动信息
			var event = data.event;
			// 给活动信息相关HTML控件赋值

			// 活动缩略图
			$('#event-img').attr('src', event.imgAddr);
			// 活动更新时间
			$('#event-time').text(
					new Date(event.lastEditTime).Format("yyyy-MM-dd"));
			// 活动名称
			$('#event-name').text(event.eventName);
			// 活动简介
			$('#event-desc').text(event.eventDesc);
			$('#person').show();
			$('#numPerson').text('参与人数: ' + event.numPerson);
			$('#capacity').text('可参与总数: ' + event.capacity);
			var imgListHtml = '';
			// 遍历活动详情图列表，并生成批量img标签
			event.eventImgList.map(function(item, index) {
				imgListHtml += '<div> <img src="' + item.imgAddr
						+ '" width="100%" /></div>';
			});
			$('#imgList').html(imgListHtml);
		}
	});
	// 点击后打开右侧栏
	$('#me').click(function() {
		$.openPanel('#panel-right-demo');
	});
	$.init();
});
