$(function() {
	var loading = false;
	// 分页允许返回的最大条数，超过此数则禁止访问后台
	var maxItems = 999;
	// 一页返回的最大条数
	var pageSize = 3;
	// 获取社团列表的URL
	var listUrl = '/CC/frontend/listclubs';
	// 获取社团类别列表以及区域列表的URL
	var searchDivUrl = '/CC/frontend/listclubspageinfo';
	// 页码
	var pageNum = 1;
	// 从地址栏URL里尝试获取parent club category id.
	var parentId = getQueryString('parentId');
	var areaId = '';
	var clubCategoryId = '';
	var clubName = '';
	// 渲染出社团类别列表以及区域列表以供搜索
	getSearchDivData();
	// 预先加载10条社团信息
	addItems(pageSize, pageNum);
	/**
	 * 获取社团类别列表以及区域列表信息
	 * 
	 * @returns
	 */
	function getSearchDivData() {
		// 如果传入了parentId，则取出此一级类别下面的所有二级类别
		var url = searchDivUrl + '?' + 'parentId=' + parentId;
		$
				.getJSON(
						url,
						function(data) {
							if (data.success) {
								// 获取后台返回过来的社团类别列表
								var clubCategoryList = data.clubCategoryList;
								var html = '';
								html += '<a href="#" class="button" data-category-id=""> 全部类别  </a>';
								// 遍历社团类别列表，拼接出a标签集
								clubCategoryList
										.map(function(item, index) {
											html += '<a href="#" class="button" data-category-id='
													+ item.clubCategoryId
													+ '>'
													+ item.clubCategoryName
													+ '</a>';
										});
								// 将拼接好的类别标签嵌入前台的html组件里
								$('#clublist-search-div').html(html);
								var selectOptions = '<option value="">全部校区</option>';
								// 获取后台返回过来的区域信息列表
								var areaList = data.areaList;
								// 遍历区域信息列表，拼接出option标签集
								areaList.map(function(item, index) {
									selectOptions += '<option value="'
											+ item.areaId + '">'
											+ item.areaName + '</option>';
								});
								// 将标签集添加进area列表里
								$('#area-search').html(selectOptions);
							}
						});
	}

	/**
	 * 获取分页展示的社团列表信息
	 * 
	 * @param pageSize
	 * @param pageIndex
	 * @returns
	 */
	function addItems(pageSize, pageIndex) {
		// 拼接出查询的URL，赋空值默认就去掉这个条件的限制，有值就代表按这个条件去查询
		var url = listUrl + '?' + 'pageIndex=' + pageIndex + '&pageSize='
				+ pageSize + '&parentId=' + parentId + '&areaId=' + areaId
				+ '&clubCategoryId=' + clubCategoryId + '&clubName=' + clubName;
		// 设定加载符，若还在后台取数据则不能再次访问后台，避免多次重复加载
		loading = true;
		// 访问后台获取相应查询条件下的社团列表
		$.getJSON(url, function(data) {
			if (data.success) {
				// 获取当前查询条件下社团的总数
				maxItems = data.count;
				var html = '';
				// 遍历社团列表，拼接出卡片集合
				data.clubList.map(function(item, index) {
					html += '' + '<div class="card" data-club-id="'
							+ item.clubId + '">' + '<div class="card-header">'
							+ item.clubName + '</div>'
							+ '<div class="card-content">'
							+ '<div class="list-block media-list">' + '<ul>'
							+ '<li class="item-content">'
							+ '<div class="item-media">' + '<img src="'
							+ item.clubImg + '" width="44">' + '</div>'
							+ '<div class="item-inner">'
							+ '<div class="item-subtitle">' + item.clubDesc
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

	// 点击社团的卡片进入该社团的详情页
	$('.club-list').on('click', '.card', function(e) {
		var clubId = e.currentTarget.dataset.clubId;
		window.location.href = '/CC/frontend/clubdetail?clubId=' + clubId;
	});

	// 选择新的社团类别之后，重置页码，清空原先的社团列表，按照新的类别去查询
	$('#clublist-search-div').on(
			'click',
			'.button',
			function(e) {
				if (parentId) {// 如果传递过来的是一个父类下的子类
					clubCategoryId = e.target.dataset.categoryId;
					// 若之前已选定了别的category,则移除其选定效果，改成选定新的
					if ($(e.target).hasClass('button-fill')) {
						$(e.target).removeClass('button-fill');
						clubCategoryId = '';
					} else {
						$(e.target).addClass('button-fill').siblings()
								.removeClass('button-fill');
					}
					// 由于查询条件改变，清空社团列表再进行查询
					$('.list-div').empty();
					// 重置页码
					pageNum = 1;
					addItems(pageSize, pageNum);
				} else {// 如果传递过来的父类为空，则按照父类查询
					parentId = e.target.dataset.categoryId;
					if ($(e.target).hasClass('button-fill')) {
						$(e.target).removeClass('button-fill');
						parentId = '';
					} else {
						$(e.target).addClass('button-fill').siblings()
								.removeClass('button-fill');
					}
					// 由于查询条件改变，清空社团列表再进行查询
					$('.list-div').empty();
					// 重置页码
					pageNum = 1;
					addItems(pageSize, pageNum);
					parentId = '';
				}

			});

	// 需要查询的社团名字发生变化后，重置页码，清空原先的社团列表，按照新的名字去查询
	$('#search').on('change', function(e) {
		clubName = e.target.value;
		$('.list-div').empty();
		pageNum = 1;
		addItems(pageSize, pageNum);
	});

	// 区域信息发生变化后，重置页码，清空原先的社团列表，按照新的区域去查询
	$('#area-search').on('change', function() {
		areaId = $('#area-search').val();
		$('.list-div').empty();
		pageNum = 1;
		addItems(pageSize, pageNum);
	});

	// 点击后打开右侧栏
	$('#me').click(function() {
		$.openPanel('#panel-right-demo');
	});

	// 初始化页面
	$.init();
});
