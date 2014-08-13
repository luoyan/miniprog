<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<div class="navbar navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container">
            <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </a>
            <a class="brand" href="#">广告统计平台</a>

            <div class="nav-collapse">
                <ul class="nav">
                    <li class="${navType=='searchLogStatistics'?'active':'nonsense'}"><a
                            href="demo">指标分析</a></li>
                </ul>
            </div>
        </div>
    </div>
</div>