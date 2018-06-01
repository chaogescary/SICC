//将uploadService服务注入到goodsController中
//文件上传服务层
app.service("uploadService",function($http){
	this.uploadFile=function(){
		var formData=new FormData();
	    formData.append("file",file.files[0]);   
		return $http({
            method:'POST',
            url:"../upload.do",
            data: formData,
            headers: {'Content-Type':undefined},	//把浏览器的Content-Type设置为 multipart/form-data
            transformRequest: angular.identity	//anjularjs transformRequest function 将序列化formdata object
        });		
	}	
});

