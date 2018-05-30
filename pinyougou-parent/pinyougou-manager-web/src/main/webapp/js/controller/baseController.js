//品牌控制层 
app.controller('baseController', function($scope) {

	// 重新加载列表数据,方式涉及到展示对象的修改,最终都会被调用用于页面的刷新
	$scope.reloadList = function() {
		// 切换页码
		$scope.search($scope.paginationConf.currentPage,
				$scope.paginationConf.itemsPerPage);
	}

	// 分页控件配置
	$scope.paginationConf = {
		currentPage : 1,
		totalItems : 10,
		itemsPerPage : 10,
		perPageOptions : [ 10, 20, 30, 40, 50 ],
		onChange : function() {
			$scope.reloadList();// 重新加载
		}
	};

	$scope.selectIds = [];// 选中的ID集合

	// 更新复选
	$scope.updateSelection = function($event, id) {
		if ($event.target.checked) {// 如果是被选中,则增加到数组
			$scope.selectIds.push(id);
		} else {
			var idx = $scope.selectIds.indexOf(id);
			$scope.selectIds.splice(idx, 1);// 删除
		}
	}

	// 提取json字符串数据中某个属性，返回拼接字符串 逗号分隔
	$scope.jsonToString = function(jsonString, key) {
		var json = JSON.parse(jsonString);// 将json字符串转换为json对象
		var value = "";
		for (var i = 0; i < json.length; i++) {
			if (i > 0) {
				value += ","
			}
			value += json[i][key];
		}
		return value;
	}

	// 修改勾选后选取下一页然后返回失效的bug
	$scope.hasCheck = function(id) {
		if ($scope.selectIds.indexOf(id) != -1) {
			return true;
		} else {
			return false;
		}
	}

});