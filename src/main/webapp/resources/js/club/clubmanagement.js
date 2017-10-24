$(function() {
	var clubId = getQueryString('clubId');
	var clubInfoUrl = '/CC/clubadmin/getclubmanagementinfo?clubId=' + clubId;
	$.getJSON(clubInfoUrl, function(data) {
		if (data.redirect) {
			window.location.href = data.url;
		} else {
			if (data.clubId != undefined && data.clubId != null) {
				clubId = data.clubId;
			}
			$('#clubInfo').attr('href', '/CC/clubadmin/cluboperation?clubId=' + clubId);
		}
	});
});