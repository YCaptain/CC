$(function() {
	getlist();
	function getlist(e) {
		$.ajax({
			url : "/CC/clubadmin/getclublist",
			type : "get",
			dataType : "json",
			success : function(data) {
				if (data.success) {
					handleList(data.clubList);
					handleUser(data.user);
				}
			}
		});
	}
	
	function handleUser(data) {
		$('#user-name').text(data.name);
	}

	function handleList(data) {
		var html = '';
		data.map(function(item, index) {
			html += '<div class="row row-club"><div class="col-40">'
					+ item.clubName + '</div><div class="col-40">'
					+ clubStatus(item.enableStatus)
					+ '</div><div class="col-20">'
					+ goClub(item.enableStatus, item.clubId) + '</div></div>';
		});
		$('.club-wrap').html(html);
	}

	function clubStatus(status) {
		if (status == 0) {
			return '审核中';
		} else if (status == -1) {
			return '社团非法';
		} else if (status == 1) {
			return '审核通过';
		}
	}
	
	function goClub(status, id) {
		if (status == 1) {
			return '<a href="/CC/clubadmin/clubmanagement?clubId=' + id + '">进入</a>';
		} else {
			return '';
		}
	}
})