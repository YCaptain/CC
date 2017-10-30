$(function() {
	var loading = false;
	// 分页允许返回的最大条数，超过此数则禁止访问后台
	var maxItems = 20;
	// 默认一页返回的活动数
	var pageSize = 3;
	// 列出活动列表的URL
	var listUrl = '/CC/frontend/listeventsbyclub';
	// 默认的页码
	var pageNum = 1;
	// 从地址栏里获取ShopId
	var clubId = getQueryString('clubId');
	var eventCategoryId = '';
	var eventName = '';
	// 获取本社团信息以及活动类别信息列表的URL
	var searchDivUrl = '/CC/frontend/listclubdetailpageinfo?clubId=' + clubId;
	// 渲染出社团基本信息以及活动类别列表以供搜索
	getSearchDivData();
	// 预先加载10条活动信息
	addItems(pageSize, pageNum);

	// 获取本社团信息以及活动类别信息列表
	function getSearchDivData() {
		var url = searchDivUrl;
		$
				.getJSON(
						url,
						function(data) {
							if (data.success) {
								var club = data.club;
								$('#club-cover-pic').attr('src', club.clubImg);
								$('#club-update-time').html(
										new Date(club.lastEditTime)
												.Format("yyyy-MM-dd"));
								$('#club-name').html(club.clubName);
								$('#club-desc').html(club.clubDesc);
								$('#club-addr').html(club.clubAddr);
								$('#club-phone').html(club.phone);
								// 获取后台返回的该社团的活动类别列表
								var eventCategoryList = data.eventCategoryList;
								var html = '';
								// 遍历活动列表，生成可以点击搜索相应活动类别下的活动的a标签
								eventCategoryList
										.map(function(item, index) {
											html += '<a href="#" class="button" data-event-search-id='
													+ item.eventCategoryId
													+ '>'
													+ item.eventCategoryName
													+ '</a>';
										});
								// 将活动类别a标签绑定到相应的HTML组件中
								$('#clubdetail-button-div').html(html);
							}
						});
	}
	/**
	 * 获取分页展示的活动列表信息
	 * 
	 * @param pageSize
	 * @param pageIndex
	 * @returns
	 */
	function addItems(pageSize, pageIndex) {
		// 拼接出查询的URL，赋空值默认就去掉这个条件的限制，有值就代表按这个条件去查询
		var url = listUrl + '?' + 'pageIndex=' + pageIndex + '&pageSize='
				+ pageSize + '&eventCategoryId=' + eventCategoryId
				+ '&eventName=' + eventName + '&clubId=' + clubId;
		// 设定加载符，若还在后台取数据则不能再次访问后台，避免多次重复加载
		loading = true;
		// 访问后台获取相应查询条件下的活动列表
		$.getJSON(url, function(data) {
			if (data.success) {
				// 获取当前查询条件下活动的总数
				maxItems = data.count;
				var html = '';
				// 遍历活动列表，拼接出卡片集合
				data.eventList.map(function(item, index) {
					html += '' + '<div class="card" data-event-id='
							+ item.eventId + '>'
							+ '<div class="card-header">' + item.eventName
							+ '</div>' + '<div class="card-content">'
							+ '<div class="list-block media-list">' + '<ul>'
							+ '<li class="item-content">'
							+ '<div class="item-media">' + '<img src="'
							+ item.imgAddr + '" width="44">' + '</div>'
							+ '<div class="item-inner">'
							+ '<div class="item-subtitle">' + item.eventDesc
							+ '</div>' + '</div>' + '</li>' + '</ul>'
							+ '</div>' + '</div>' + '<div class="card-footer">'
							+ '<p class="color-gray">'
							+ new Date(item.lastEditTime).Format("yyyy-MM-dd")
							+ '更新</p>' + '<span>点击查看</span>' + '</div>'
							+ '</div>';
				});
				// 将卡片集合添加到目标HTML组件里
				$('.list-div').append(html);
				// 获取目前为止已显示的卡片总数，包含之前已经加载的
				var total = $('.list-div .card').length;
				// 若总数达到跟按照此查询条件列出来的总数一致，则停止后台的加载
				if (total >= maxItems) {
					// 隐藏提示符
					$('.infinite-scroll-preloader').hide();
				} else {
					$('.infinite-scroll-preloader').show();
				}
				// 否则页码加1，继续load出新的社团
				pageNum += 1;
				// 加载结束，可以再次加载了
				loading = false;
				// 刷新页面，显示新加载的社团
				$.refreshScroller();
			}
		});
	}

	// 下滑屏幕自动进行分页搜索
	$(document).on('infinite', '.infinite-scroll-bottom', function() {
		if (loading)
			return;
		addItems(pageSize, pageNum);
	});
	// 选择新的活动类别之后，重置页码，清空原先的活动列表，按照新的类别去查询
	$('#clubdetail-button-div').on(
			'click',
			'.button',
			function(e) {
				// 获取活动类别Id
				eventCategoryId = e.target.dataset.eventSearchId;
				if (eventCategoryId) {
					// 若之前已选定了别的category,则移除其选定效果，改成选定新的
					if ($(e.target).hasClass('button-fill')) {
						$(e.target).removeClass('button-fill');
						eventCategoryId = '';
					} else {
						$(e.target).addClass('button-fill').siblings()
								.removeClass('button-fill');
					}
					$('.list-div').empty();
					pageNum = 1;
					addItems(pageSize, pageNum);
				}
			});
	// 点击活动的卡片进入该活动的详情页
	$('.list-div').on(
			'click',
			'.card',
			function(e) {
				var eventId = e.currentTarget.dataset.eventId;
				window.location.href = '/CC/frontend/eventdetail?eventId='
						+ eventId;
			});
	// 需要查询的活动名字发生变化后，重置页码，清空原先的活动列表，按照新的名字去查询
	$('#search').on('change', function(e) {
		eventName = e.target.value;
		$('.list-div').empty();
		pageNum = 1;
		addItems(pageSize, pageNum);
	});
	// 点击后打开右侧栏
	$('#me').click(function() {
		$.openPanel('#panel-right-demo');
	});
	$.init();
});
