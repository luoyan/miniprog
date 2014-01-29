/*!
 * myPagination Jquery Pagination Plug-in Library v6.0
 * 
 * http://linapex.blog.163.com/
 *
 * Date: 2013/3/24 21:20
 */
 define("js/lib/myPagination/jquery.myPagination6.0",['jquery', './page.css'],function(require) {var jQuery = require('jquery');require('./page.css');
(function ($) {
    function init(param, obj) {
        function getParam() {
            var a = "page=" + opts.currPage;
            return opts.ajax.param && (a += "&" + opts.ajax.param), a
        }

        function getPanelTipInfo() {
            var a = "";
            if (opts.panel.tipInfo_on) {
                var b = document.createElement("span");
                b = $(b);
                var c = b.text(opts.panel.tipInfo).text();
                if (-1 != c.indexOf("{input}")) {
                    var d = "<input type='text' value='" + opts.currPage + "' >";
                    c = c.replace("{input}", d), c = c.replace("{sumPage}", opts.pageCount), b.html(c), d = b.children(":text:first"), d.css(opts.panel.tipInfo_css), a = b.html()
                } else if (-1 != c.indexOf("{select}")) {
                    for (var e = document.createElement("select"), f = 1; parseInt(opts.pageCount) >= f; f++) {
                        var g = new Option(f, f);
                        e.options.add(g)
                    }
                    b.html("");
                    var h = c.substr(0, c.indexOf("{select}")),
                        i = c.substr(c.indexOf("{select}") + "{select}".length).replace("{sumPage}", opts.pageCount);
                    b.append(h), b.append(e), b.append(i), a = b.html()
                }
            }
            return a
        }

        function onRequest() {
            debug(opts.id), 
            debug("ajax\u8bf7\u6c42\u53c2\u6570\u5217\u8868:"), 
            debug(getParam()), 
            opts.ajax.on ? (opts.ajax.ajaxStart(), $.ajax({
                url: opts.ajax.url,
                type: opts.ajax.type,
                data: getParam(),
                contentType: "application/x-www-form-urlencoded;utf-8",
                async: !0,
                cache: !1,
                timeout: 6e4,
                error: function () {
                    alert("\u8bbf\u95ee\u670d\u52a1\u5668\u8d85\u65f6\uff0c\u8bf7\u91cd\u8bd5\uff0c\u8c22\u8c22\uff01")
                },
                success: function (a) {
                    opts.ajax.ajaxStop(), responseHandle(a), createPageBar()
                }
            })) : createPageBar()
        }

        function responseHandle(data) {
            var pageCountId = opts.ajax.pageCountId,
                resultPageCount = 1;
            switch (opts.ajax.dataType) {
            case "json":
                try {
                    data = eval("(" + data + ")")
                } catch (err) {} finally {
                    resultPageCount = eval("data." + pageCountId)
                }
                break;
            case "xml":
                try {
                    resultPageCount = $(data).find(pageCountId).text()
                } catch (e) {
                    debug("xml\u8fd4\u56de\u6570\u636e\u89e3\u6790\u9519\u8bef\uff0c\u4f7f\u7528\u9ed8\u8ba4\u7684pageCount=1"), resultPageCount = 1
                }
                break;
            default:
                try {
                    resultPageCount = $(data).find(":hidden[id='" + pageCountId + "']").val()
                } catch (e) {
                    debug("html\u8fd4\u56de\u6570\u636e\u89e3\u6790\u9519\u8bef\uff0c\u4f7f\u7528\u9ed8\u8ba4\u7684pageCount=1"), resultPageCount = 1
                }
            }
            debug(opts.id), debug("\u8fd4\u56de\u603b\u9875\u6570:" + resultPageCount), opts.pageCount = resultPageCount, opts.ajax.callback(data)
        }

        function createPageBar() {
            var a = opts.panel.links;
            opts.currPage = opts.currPage > opts.pageCount ? opts.pageCount : opts.currPage;
            var b = opts.currPage,
                c = parseInt(opts.pageCount),
                d = parseInt(opts.pageNumber / 2),
                e = opts.pageNumber,
                f = "";
            opts.panel.first_on && (f = "<a class='side' href='" + a + "' paged='1'>" + opts.panel.first + "</a>"), 
            opts.panel.prev_on && (f += 1 == b ? '<span class="disabled" paged="' + opts.panel.prev + '">' + opts.panel.prev + " </span>" : "<a class='side' href='" + a + "' paged='" + (b - 1) + "'>" + opts.panel.prev + " </a>");
            var g = lastPage = 1;
            for (g = b - d > 0 ? g = b - d : 1, g + e > c ? (lastPage = c + 1, g = lastPage - e) : lastPage = g + e, 0 >= g && (g = 1), g; lastPage > g; g++) 
                f += g == b ? '<span class="current" paged="' + g + '">' + g + "</span>" : "<a href='" + a + "' paged='" + g + "'>" + g + "</a>";
            opts.panel.next_on && (f += b == c ? '<span class="disabled" paged="' + opts.panel.next + '">' + opts.panel.next + " </span>" : "<a class='side' href='" + a + "' paged='" + (b + 1) + "'>" + opts.panel.next + " </a>"), 
            opts.panel.last_on && (f += "<a class='side' href='" + a + "' paged='" + c + "'>" + opts.panel.last + "</a>"), 
            f += getPanelTipInfo(), 
            debug(opts.id), 
            debug("\u6700\u7ec8\u751f\u6210\u83dc\u5355\uff1a"), 
            debug(f), 
            obj.html(f), 
            obj.children("select").val(opts.currPage), 
            obj.children("select").change(function () {
                var a = parseInt($(this).children("option:selected").val());
                opts.currPage = a, onRequest()
            }), 
            obj.children(":text").keyup(function () {
                var a = $(this),
                    b = $.trim($(this).val());
                if (0 != b.length) {
                    var c = /^\+?[0-9][0-9]*$/;
                    c.exec(b) || a.val(1)
                }
            }), 
            obj.children(":text").keypress(function (a) {
                var b = a.which;
                if (13 == b) {
                    var c = $.trim($(this).val());
                    obj.children("a").unbind("click"), obj.children("a").each(function () {
                        $(this).click(function () {
                            return !1
                        })
                    }), opts.currPage = c, onRequest()
                }
            }), 
            obj.children("a").each(function () {
                var c = $(this);
                c.click(function () {
                    var a = parseInt(c.attr("paged"));
                    return a = a > 0 ? a : 1, c.children("a").unbind("click"), c.children("a").each(function () {
                        $(this).click(function () {
                            return !1
                        })
                    }), opts.currPage = a, opts.ajax.onClick(a), onRequest(), $(this).focus(), !1
                })
            })
        }

        function debug(a) {
            opts.debug && $.fn.debug(a)
        }
        var defaults = {
            currPage: 1,
            pageCount: 10,
            pageNumber: 5,
            cssStyle: "badoo",
            debug: !1,
            ajax: {
                on: !1,
                type: "POST",
                pageCountId: "pageCount",
                url: "jsonTest.php",
                dataType: "json",
                param: !1,
                onClick: function () {
                    return !1
                },
                ajaxStart: function () {
                    return !1
                },
                ajaxStop: function () {
                    return !1
                },
                callback: function () {
                    return !1
                }
            },
            panel: {
                first: "\u9996\u9875",
                last: "\u5c3e\u9875",
                next: "\u4e0b\u4e00\u9875",
                prev: "\u4e0a\u4e00\u9875",
                first_on: !0,
                last_on: !0,
                next_on: !0,
                prev_on: !0,
                links: "#",
                tipInfo_on: !1,
                tipInfo: "<span>&nbsp;&nbsp;\u8df3{currText}/{sumPage}\u9875</span>",
                tipInfo_css: {
                    width: "22px"
                },
                tipSelect_on: !1,
                tipSelect: "\u8df3\u8f6c\u5230{select} \u5171{sumPage}\u9875"
            }
        }, opts = $.extend(!0, defaults, param);
        opts.id = obj.attr("id"), obj.addClass(opts.cssStyle), onRequest();
        var method = {};
        return method.id = opts.id, method.getPage = function () {
            return opts.currPage
        }, method.onReload = function () {
            debug("reload()"), onRequest()
        }, method.onLoad = function (a) {
            a && a instanceof Object && (debug(a), opts.currPage = 1, opts.ajax.param = a.param, onRequest())
        }, method.jumpPage = function (a) {
            debug("jumpPage(" + a + ")"), a = 1 > a ? 1 : a, a = a > opts.pageCount ? opts.pageCount : a, opts.currPage = a, onRequest()
        }, method
    }
    $.fn.myPagination = function (a) {
        return init(a, $(this))
    }, $.fn.debug = function (a) {
        window.console && window.console.log && console.log(a)
    }
})(jQuery);
});