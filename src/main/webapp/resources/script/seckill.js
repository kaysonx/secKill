//模块化js代码
var seckill = {
		URL:{
			now:function(){
				return '/seckill/time/now';
			},
			exposer:function(seckillId){
				return '/seckill/' + seckillId + '/exposer';
			},
			execution:function(seckillId,md5){
				return '/seckill/' + seckillId + '/' + md5 + '/execution';
			}
			
		},
		//暴露秒杀接口并处理秒杀
		handleSeckill:function(seckillId,node){
			
			//获取秒杀地址-控制显示逻辑-执行秒杀
			node.hide().html('<button class="btn btn=primary btn-lg" id="killBtn">开始秒杀</button>');
			$.post(seckill.URL.exposer(seckillId),{},function(result){
				
				if(result && result['success']){
					var exposer = result['data'];
					if(exposer['exposed']){
						//开启秒杀
						//获取秒杀地址
						var md5 = exposer['md5'];
						var killUrl = seckill.URL.execution(seckillId,md5);
						console.log("killUrl:"+killUrl);
						
						//绑定一次点击事件
						$('#killBtn').one('click',function(){
							//执行秒杀请求
							//1.禁用btn 避免用户一直点击
							$(this).addClass('disabled');
							//2.发送请求
							$.post(killUrl,{},function(result){
								if(result && result['success']){
									var killResult = result['data'];
									var status = killResult['status'];
									var statusInfo = killResult['statusInfo'];
									//3.显示秒杀结果
									node.html('<span class="label label-success">' + statusInfo + '</span>');
								}
									
							});
						});
						node.show();
					}else{
						//未开启秒杀
						var now = exposer['now'];
						var start = exposer['start'];
						var end = exposer['end'];
						
						//重新计算计时逻辑
						seckill.countdown(seckillId, now, start, end);
					}
				}else{
					console.log('result:'+result);
				}
			});
		},
		
		//验证手机号
		validatePhone:function(phone){
			if(phone && phone.length == 11 && !isNaN(phone)){
				return true;
			}else{
				return false;
			}
		},
		
		//计时器
		countdown:function(seckillId, nowTime, startTime, endTime){
			var seckillBox = $('#seckill-box');
			
			//时间判断
			if(nowTime > endTime){
				//秒杀结束
				seckillBox.html('本次秒杀已结束!');
			}else if(nowTime < startTime){
				//秒杀未开始
				var killTime = new Date(startTime - 0 + 1000);
				seckillBox.countdown(killTime,function(event){
					//时间格式
					var format = event.strftime('记录本次秒杀开始还有: %D天 %H小时 %M分 %S秒');
					seckillBox.html(format);
				}).on('finish.countdown',function(){
					//倒计时结束后的回调事件
					seckill.handleSeckill(seckillId, seckillBox);
				});
			}else{
				//秒杀已开始
				seckill.handleSeckill(seckillId, seckillBox);
			}
		},
		
		//详情页 秒杀逻辑
		detail:{
			init:function(params){
				//1.验证是否登录--这里用phoneNumber简化表示
				var killPhone = $.cookie('killPhone');
				//1.1.验证手机号
				if(!seckill.validatePhone(killPhone)){
					//注册框弹出
					var killPhoneModal = $('#killPhoneModal');
					
					killPhoneModal.modal({
						show:true,//显示弹出层
						backdrop:'static',//禁止位置关闭
						keyboard:false//关闭键盘事件
					});
					
					$('#killPhoneBtn').click(function(){
						var inputPhone = $('#killPhoneKey').val();
						console.log('inputPhone='+inputPhone);
						if(seckill.validatePhone(inputPhone)){
							//验证通过
							$.cookie('killPhone', inputPhone, {expires:7,path:'/seckill'});
							window.location.reload();
						}else{
							$('#killPhoneMessage').hide().html('<label clase="label label-danger">手机号码错误！</label>').show(300);
						}
					});
					
					
				}
				
				//已登录
				var startTime = params['startTime'];
				var endTime = params['endTime'];
				var seckillId = params['seckillId'];
				
				$.get(seckill.URL.now(),{},function(result){
					if(result && result['success']){
						var nowTime = result['data'];
						//根据返回的系统时间来判断
						seckill.countdown(seckillId, nowTime,startTime, endTime);
					}else{
						console.log('result='+result);
					}
				});
				
			}
		}
				
}