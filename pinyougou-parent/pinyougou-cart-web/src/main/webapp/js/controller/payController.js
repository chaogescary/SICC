app.controller('payController',function($scope,payService){
	
	//生成二维码
	$scope.createNative = function(){
		payService.createNative().success(
				function(response){
					$scope.out_trade_no = response.out_trade_no;
					$scope.total_fee = response.total_fee;
					
					var qr = new QRious({
				 		   element:document.getElementById('qrious'),
				 		   size:250,
				 		   level:'H',
				 		   value:response.code_url
				 		});	
					
					queryPayStatus($scope.out_trade_no);
				}
		)
	}
	
	//查询支付单状态
	queryPayStatus = function(out_trade_no){
		payService.queryPayStatus(out_trade_no).success(
				function(response){
					if(response.success){
						location.href = "paysuccess.html";
					}else{
						if(response.message == 'timeout'){//超时的逻辑，如果超时，重新生成二维码
							$scope.createNative();
						}else{
							location.href="payfail.html";
						}
					}
				}
		)
		
	}
	
})