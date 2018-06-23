app.controller('itemController',function($scope){

	$scope.num = 1;
	$scope.addNum = function(value){
		$scope.num += value;
		if($scope.num < 1){
			$scope.num = 1;
		}
	}

	$scope.specItems = {};
	$scope.selectSpec = function(key,value){
		$scope.specItems[key] = value;
		searchSku();//读取sku
	}

	$scope.isSelected = function(key,value){
		if($scope.specItems[key] == value){
			return true;
		}
		return false;
	}

	$scope.sku={};
	$scope.defaultSku = function(){
		$scope.sku = skuList[0];
		$scope.specItems = JSON.parse(JSON.stringify($scope.sku.spec));
	}

	searchSku=function(){
		for(var i=0;i< skuList.length;i++  ){
			if( matchObject (skuList[i].spec, $scope.specItems ) ){
				$scope.sku=skuList[i];				
			}		
		}		
	}

	matchObject=function(map1,map2){		
		for(var k in map1){
			if(map1[k]!=map2[k]	){
				return false;
			}				
		}
		for(var k in map2){
			if(map1[k]!=map2[k]	){
				return false;
			}				
		}
		return true;		
	}

})