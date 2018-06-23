app.controller('itemController',function($scope,$http){

	$scope.init = function(){
		$scope.specificationItems={};  //初始化选择规格后存入的对象
		$scope.num = 1;  //默认够买数量是1
		$scope.sku = {};
		$scope.loadSku();
	}

	//保存用户选择的规格
	$scope.selectSpecificationItems = function(name,value){
		$scope.specificationItems[name] = value;
		searchSku(); //判断当前选择的规格和skuList中的对象是否相同
	}

	//是否格式是选择的
	$scope.isSelected=function(key,value){
		 if($scope.specificationItems[key] == value){
		 	return true;
		 }
		 return false;
	}

	//页面点击加减改变num
	$scope.addNum=function(x){
		$scope.num += x;
		if($scope.num < 1){
			$scope.num = 1;
		}
	}

	//设置默认规格
	$scope.loadSku = function(){
		$scope.sku = skuList[0]; 
		//设置默认选中的规格对象
		$scope.specificationItems = JSON.parse(JSON.stringify($scope.sku.spec));
	}

	//查询skuList中的spec和选择的specificationItems是否一致
	searchSku=function(){
		for (var i = 0; i < skuList.length; i++) {
			if(matchObject(skuList[i].spec,$scope.specificationItems)){
				$scope.sku = skuList[i];
			}
		}
		
	}


	//匹配两个map是否相等
	matchObject=function(map1,map2){
		for (var k in map1) {
			  if(map1[k]!=map2[k]){
			  	 return false;
			  }
		}
		for (var k in map2) {
			if(map1[k]!=map2[k]){
			  	 return false;
			  }
		}
		return true;
	}
	
	//添加商品到购物车
	$scope.addToCart=function(id,num){
		alert("添加到购物车");									//withCredentials发送跨域请求
		$http.get('http://localhost:9107/cart/addItemToCartList.do?itemId='+id+'&num='+num,{'withCredentials':true}).success(
				function(response){
					if(response.success){
						 location.href="http://localhost:9107/cart.html";
					}else{
						alert(response.message);
					}
				}
		)
	}
})