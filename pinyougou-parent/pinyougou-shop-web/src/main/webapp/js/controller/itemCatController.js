 //控制层 
app.controller('itemCatController' ,function($scope,$controller   ,itemCatService){	
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		itemCatService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		itemCatService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		itemCatService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=itemCatService.update( $scope.entity ); //修改  
		}else{
			serviceObject=itemCatService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
		        	$scope.reloadList();//重新加载
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		itemCatService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		itemCatService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
    
	//根据父iD查询所有分类，下面的代码块会用到该函数
	$scope.findByParentId = function(parentId){
		itemCatService.findByParentId(parentId).success(
				function(response){
					$scope.list = response;
				}
		)
	}
	
	
	
	
	$scope.grade = 1;  //默认的顶级分类
	
	$scope.setGrade = function(value){ //给等级进行赋值
		$scope.grade = value;
	}
	//{id:0}
	$scope.selectType = function(p_entity){
		if($scope.grade == 1){  //选择顶级分类
			$scope.entity_1 = null;//顶级分类面包屑不可点，或者说自身不可点
			$scope.entity_2 = null;//自然一级分类面包屑也点不了
		}
		if($scope.grade == 2){  //选择一级分类
			$scope.entity_1 = p_entity;//可以点顶级分类，即父分类面包屑
			$scope.entity_2 = null;//自身不可点
		}
		if($scope.grade == 3){	//选择二级分类
			$scope.entity_2 = p_entity;//同上，同时进入了上面一个分支的递归，所以下面就省略不写了
		}
		$scope.findByParentId(p_entity.id);
	}
});	
