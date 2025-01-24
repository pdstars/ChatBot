// ;!function(win, $){
	var IM = (function(){
		var imOpt = {
				ws : '/demo/im/ws'
			};
		function IM(opt){
			this.opt = $.extend(imOpt ,opt || {});
			this.isConnect = false;
			this.headers = {};
		}
		IM.prototype.init = function(){
			this.disconnect();
			this.isConnect = false;
			this.socket = new SockJS(this.opt.ws);
			this.stompClient = Stomp.over(this.socket);
		};
		IM.prototype.initLayIm = function(data){
			if(this.opt.initCallBack){
				this.opt.initCallBack(data);
			}
		};
		IM.prototype.addListener = function(path,fn){
			if(!this.isConnect){
				console.log('未连接');
				return ;
			}
			var $this = this;
			//订阅个人信息推送
            $this.stompClient.subscribe(path, function (response) {
                fn(eval('('+response.body+')'));
            });
		}
		IM.prototype.initListener = function(){
			if(!this.isConnect){
				console.log('未连接');
				return ;
			}
			var $this = this;
			//订阅个人消息
            $this.stompClient.subscribe('/user/queue/personal', function (response) {
				if($this.opt.personalListener){
					$this.opt.personalListener(eval('('+response.body+')'));
				}else{
					$this.initLayIm(eval('('+response.body+')'));
				}
            });
			//订阅个人信息推送
            $this.stompClient.subscribe('/user/queue/init', function (response) {
                $this.initLayIm(eval('('+response.body+')'));
            });
			//发送请求获取个人推送信息 ： 初始化信息
        	$this.stompClient.send("/app/init", {}, JSON.stringify({'ac': 'init'}));
		};

		IM.prototype.send = function(msg){
			if(!this.isConnect){
				console.log('未连接');
				return ;
			}
			console.info(msg)
        	this.stompClient.send('/app/im/send', {}, JSON.stringify(msg));
		}
		IM.prototype.sendMsg = function(path,header,data){
			if(!this.isConnect){
				console.log('未连接');
				return ;
			}
        	this.stompClient.send(path, header, data);
		}
		IM.prototype.connect = function(){
			var $this = this;

			this.stompClient.connect($this.headers, function (frame) {
            	$this.isConnect = true;
				$this.initListener();
			});
		};
		IM.prototype.disconnect = function(){
			if (this.stompClient != null) {
				this.stompClient.disconnect();
				  this.isConnect = false;
			}
		};
		return IM;
	})();
// }(this, jQuery);
