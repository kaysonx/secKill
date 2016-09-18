<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- 引入jstl -->
<%@include file="common/tag.jsp"%>

<!DOCTYPE html>
<html>
<head>
    <title>秒杀列表</title>
    <%@include file="common/head.jsp"%>
</head>
<body>
    <div class="container">
    	<div class="panel panel-default">
    		<div class="panel-heading text-center">
    			<h2>秒杀列表</h2>
    		</div>
    		
    		<div class="panel-body">
    			<table class="table table-hover">
    				<thead>
    					<tr>
    						<th>名称</th>
    						<th>库存</th>
    						<th>开始时间</th>
    						<th>结束时间</th>
    						<th>创建时间</th>
    						<th>详情页</th>
    					</tr>
    				</thead>
    				
    				<tbody>
    				
    					<c:forEach var="sk" items="${list }">
    					<tr>
    						<td>${sk.name }</td>
    						<td>${sk.number }</td>
    						<td>
    							<fmt:formatDate value="${sk.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
    						</td>
    						<td>
    							<fmt:formatDate value="${sk.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
    						</td>
    						<td>
    							<fmt:formatDate value="${sk.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
    						</td>
    						<td>
    							<a class="btn btn-info" href="/seckill/${sk.seckillId}/detail" target="_blank">详情</a>
    						</td>
    					</tr>
    					</c:forEach>
    				
    				</tbody>
    			</table>
    			
    			 <ul id='pagintor' style="margin:0 auto; margin-left:30%;"></ul>
    		</div>
    	</div>
    </div>


  </body>
  	<!-- 使用CDN加速 -->
	<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
	<script src="http://apps.bdimg.com/libs/jquery/2.0.0/jquery.min.js"></script>
	<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
	<script src="http://apps.bdimg.com/libs/bootstrap/3.3.0/js/bootstrap.min.js"></script>
	
	<!-- bootstrap-paginator  -->
	<script src="/resources/script/bootstrap-paginator.min.js"></script>
	
	<script type="text/javascript">
			var options = {
			    bootstrapMajorVersion:3,
			    alignment:"center",//居中显示
			    size:"normal",
			    
			    currentPage: "${pager.pageIndex}",//当前页数
			    totalPages: "${pager.totalPages}",//总页数 注意不是总条数
	            numberOfPages: "${pager.totalPages}",//可以改变显示的页码数
			    
			    pageUrl: function(type, page, current){
			            if (page==current) {
			                return "javascript:void(0)";
			            } else {
			            	return "/seckill/list?pageSize=3&pageIndex="+page;
			            }
			        }
			}
			$("#pagintor").bootstrapPaginator(options);// $("#pagintor") Bootstrap 是2.X 使用div元素，3.X使用ul元素
	</script>
</html>