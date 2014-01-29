/*map start*/
seajs.production = true;
if(seajs.production){
    seajs.config({
        map: [
	[
		"dev/js/page/base.js",
		"dev/js/page/base.js?8135245470a5da2be6484d9d662089ed"
	],
	[
		"dev/js/page/goldmall.js",
		"dev/js/page/goldmall.js?43c7e34ff473ed8eb334e969982fe08d"
	],
	[
		"dev/js/page/index.js",
		"dev/js/page/index.js?8ff54865c1d614b25cafc69cedb0470d"
	],
	[
		"dev/js/page/jianbao.js",
		"dev/js/page/jianbao.js?66112e989b3074c22016064089fadb8f"
	],
	[
		"dev/js/page/search.js",
		"dev/js/page/search.js?586d84351188072bac0c5ef71ada0419"
	],
	[
		"dev/js/page/seckill.js",
		"dev/js/page/seckill.js?e4f98bdb5cafff4c3588af0f05f82dea"
	],
	[
		"dev/js/page/youhui.js",
		"dev/js/page/youhui.js?084be9ade26e4b435c4a06ca6cf71b1d"
	]
]
    });
}
/*map end*/
seajs.config({
  // Configure alias
  alias: {
    'es5-safe': 'js/lib/es5-safe.js',
    'json': 'js/lib/json2.js',
    'jquery': 'js/lib/jquery.min',
    'jqueryplugins': 'js/lib/jquery.plugin',
    'myPagination': 'js/lib/myPagination/jquery.myPagination6.0',
    'Boxy': 'js/lib/jquery.boxy'
  },

  preload: [
    Function.prototype.bind ? '' : 'es5-safe',
    this.JSON ? '' : 'json',
    'jquery'
  ]
});