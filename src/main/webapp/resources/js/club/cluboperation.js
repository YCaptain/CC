/**
 * 
 */
$(function() {
	var clubId = getQueryString('clubId');
	var isEdit = clubId ? true : false;
	var initUrl = '/CC/clubadmin/getclubinitinfo';
	var registerClubUrl = '/CC/clubadmin/registerclub';
	var clubInfoUrl = '/CC/clubadmin/getclubbyid?clubId=' + clubId;
	var editClubUrl = '/CC/clubadmin/modifyclub';
	if (!isEdit) {
		getClubInitInfo();
	} else {
		getClubInfo(clubId);
	}

	function getClubInfo(clubId) {
		$.getJSON(clubInfoUrl, function(data) {
			if (data.success) {
				var club = data.club;
				$('#club-name').val(club.clubName);
				$('#club-addr').val(club.clubAddr);
				$('#club-phone').val(club.phone);
				$('#club-desc').val(club.clubDesc);
				var clubCategory = '<option data-id="'
						+ club.clubCategory.clubCategoryId + '" selected>'
						+ club.clubCategory.clubCategoryName + '</option>';
				var tempAreaHtml = '';
				data.areaList.map(function(item, index) {
					tempAreaHtml += '<option data-id="' + item.areaId + '">'
							+ item.areaName + '</option>';
				});
				$('#club-category').html(clubCategory);
				$('#club-category').attr('disabled', 'disabled');
				$('#area').html(tempAreaHtml);
				$("#area option[data-id='" + club.area.areaId + "']").attr(
						"selected", "selected");
			}
		});
	}

	function getClubInitInfo() {
		$.getJSON(initUrl, function(data) {
			if (data.success) {
				var tempHtml = '';
				var tempAreaHtml = '';
				data.clubCategoryList.map(function(item, index) {
					tempHtml += '<option data-id="' + item.clubCategoryId
							+ '">' + item.clubCategoryName + '</option>';
				});
				data.areaList.map(function(item, index) {
					tempAreaHtml += '<option data-id="' + item.areaId + '">'
							+ item.areaName + '</option>';
				});
				$('#club-category').html(tempHtml);
				$('#area').html(tempAreaHtml);
			}
		});
	}
	$('#submit').click(
			function() {
				var club = {};
				if (isEdit) {
					club.clubId = clubId;
				}
				club.clubName = $('#club-name').val();
				club.clubAddr = $('#club-addr').val();
				club.phone = $('#club-phone').val();
				club.clubDesc = $('#club-desc').val();
				club.clubCategory = {
					clubCategoryId : $('#club-category').find('option')
							.not(function() {
								return !this.selected;
							}).data('id')
				};
				club.area = {
					areaId : $('#area').find('option').not(function() {
						return !this.selected;
					}).data('id')
				};
				var clubImg = $('#club-img')[0].files[0];
				var formData = new FormData();
				formData.append('clubImg', clubImg);
				formData.append('clubStr', JSON.stringify(club));
				var verifyCodeActual = $('#j_captcha').val();
				if (!verifyCodeActual) {
					$.toast('请输入验证码！');
					return;
				}
				formData.append('verifyCodeActual', verifyCodeActual);
				$.ajax({
					url : (isEdit ? editClubUrl : registerClubUrl),
					type : 'POST',
					data : formData,
					contentType : false,
					processData : false,
					cache : false,
					success : function(data) {
						if (data.success) {
							$.toast('提交成功!');
						} else {
							$.toast('提交失败!' + data.errMsg);
						}
						$('#captcha_img').click();
					}
				});
			})
})
