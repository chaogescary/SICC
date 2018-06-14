	//选择地址
	$scope.selectAddress=function(address){
		$scope.address=address;		
	}
	
	//判断是否是当前选中的地址
	$scope.isSelectedAddress=function(address){
		if(address==$scope.address){
			return true;
		}else{
			return false;
		}		
	}
	
	//查询当前登录人的地址列表
	$scope.findAddressList=function(){
		addressService.findListByLoginUser().success(
			function(response){
				$scope.addressList=response;
				//设置默认地址
				for(var i=0;i< $scope.addressList.length;i++){
					if($scope.addressList[i].isDefault=='1'){
						$scope.address=$scope.addressList[i];
						break;
					}					
				}					
			}
		);		
	}