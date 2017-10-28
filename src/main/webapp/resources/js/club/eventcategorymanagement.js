$(function() {
	var listUrl = '/CC/clubadmin/geteventcategorylist';
	var addUrl = '/CC/clubadmin/addeventcategory';
	var deleteUrl = '/CC/clubadmin/removeeventcategory';
	getList();
	function getList() {
		$
				.getJSON(
						listUrl,
						function(data) {
							if (data.success) {
								var dataList = data.data;
								$('.category-wrap').html('');
								var tempHtml = '';
								dataList
										.map(function(item, index) {
											tempHtml += ''
													+ '<div class="row row-event-category now">'
													+ '<div class="col-33 event-category-name">'
													+ item.eventCategoryName
													+ '</div>'
													+ '<div class="col-33">'
													+ item.priority
													+ '</div>'
													+ '<div class="col-33"><a href="#" class="button delete" data-id="'
													+ item.eventCategoryId
													+ '">删除</a></div>'
													+ '</div>';
										});
								$('.category-wrap').append(tempHtml);
							}
						});
	}
	$('#new')
			.click(
					function() {
						var tempHtml = '<div class="row row-event-category temp">'
								+ '<div class="col-33"><input class="category-input category" type="text" placeholder="分类名"></div>'
								+ '<div class="col-33"><input class="category-input priority" type="number" placeholder="优先级"></div>'
								+ '<div class="col-33"><a href="#" class="button delete">删除</a></div>'
								+ '</div>';
						$('.category-wrap').append(tempHtml);
					});
	$('#submit').click(function() {
		var tempArr = $('.temp');
		var eventCategoryList = [];
		tempArr.map(function(index, item) {
			var tempObj = {};
			tempObj.eventCategoryName = $(item).find('.category').val();
			tempObj.priority = $(item).find('.priority').val();
			if (tempObj.eventCategoryName && tempObj.priority) {
				eventCategoryList.push(tempObj);
			}
		});
		$.ajax({
			url : addUrl,
			type : 'POST',
			data : JSON.stringify(eventCategoryList),
			contentType : 'application/json',
			success : function(data) {
				if (data.success) {
					$.toast('提交成功! ');
					getList();
				} else {
					$.toast('提交失败! ');
				}
			}
		});
	});
	$('.category-wrap').on('click', '.row-event-category.temp .delete',
			function(e) {
				console.log($(this).parent().parent());
				$(this).parent().parent().remove();
			});
	$('.category-wrap').on('click', '.row-event-category.now .delete',
			function(e) {
				var target = e.currentTarget;
				$.confirm('确定删除？', function() {
					$.ajax({
						url : deleteUrl,
						type : 'POST',
						data : {
							eventCategoryId : target.dataset.id
						},
						dataType : 'json',
						success : function(data) {
							if (data.success) {
								$.toast('删除成功! ');
								getList();
							} else {
								$.toast('删除失败! ');
							}
						}
					});
				});
			});
});