/**
 * @name: checkAll && downloadAll
 * @overview: 一键下载功能
 * @author: yangweixian
 */
var download = function() {
	function DownloadUrls(urls) {
		if (urls.length != 0) {
			for (var i=0; i < urls.length; i++) {
				var npXLPlugin = navigator.mimeTypes["application/np_xunlei_plugin"];
				if (npXLPlugin) {
					var xlPlugin = document.createElement("embed");
					xlPlugin.style.visibility = "hidden";
					xlPlugin.type = "application/np_xunlei_plugin";
					xlPlugin.width = 0;
					xlPlugin.height = 0;
					document.body.appendChild(xlPlugin);
					xlPlugin.DownLoadByThunderPlugin(urls[i]);
				}
				else {
					downloadDirect(urls);
				}
			}
		}
	}
	function downloadDirect(urls) {
		for(var i = 0, ii = urls.length; i < ii; i++) {
			window.open(urls[i]);
		}
	}
	return {
		checkAll: function(btn, checkboxStr) {
			//全选
			var checkboxes = $(checkboxStr);
			$(btn).on('click', function() {
				if (this.checked) {
					checkboxes.attr('checked', 'checked');
				} else {
					checkboxes.removeAttr('checked')
				}
			});
		},
		downloadAllByThunder: function(btn, checkbox) {
			//一键下载
			$(btn).click(function(e) {
				var urls = [];
				$(checkbox + ':checked').each(function(i, elem) {
					urls.push($(this).attr('data-url'));
				});
				DownloadUrls(urls);
			});
		},
		downloadAllDirect: function(btn, checkbox) {
			$(btn).click(function(e) {
				var urls = [];
				$(checkbox + ':checked').each(function(i, elem) {
					urls.push($(this).attr('data-url'));
				});
				downloadDirect(urls);
			});
		}
	}
}()