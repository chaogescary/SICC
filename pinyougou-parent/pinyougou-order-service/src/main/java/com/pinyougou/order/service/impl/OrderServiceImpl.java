package com.pinyougou.order.service.impl;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbOrderItemMapper;
import com.pinyougou.mapper.TbOrderMapper;
import com.pinyougou.mapper.TbPayLogMapper;
import com.pinyougou.order.service.OrderService;
import com.pinyougou.pojo.TbOrder;
import com.pinyougou.pojo.TbOrderExample;
import com.pinyougou.pojo.TbOrderExample.Criteria;
import com.pinyougou.pojo.TbOrderItem;
import com.pinyougou.pojo.TbPayLog;

import entity.Cart;
import entity.PageResult;
import util.IdWorker;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private TbOrderMapper orderMapper;
	
	@Autowired
	private TbOrderItemMapper orderItemMapper;
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Autowired
	private IdWorker idWorker;
	
	@Autowired
	private TbPayLogMapper payLogMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbOrder> findAll() {
		return orderMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbOrder> page=   (Page<TbOrder>) orderMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbOrder order) {
		//新增订单对象
		
		//从redis中获取购物车
		List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("cartList").get(order.getUserId());
		List orderList = new ArrayList();
		
		//求支付订单的总金额
		double totalMoney = 0.0;
		
		//循环购物车，每个购物车生成一个订单
		for (Cart cart : cartList) {
			TbOrder tbOrder = new TbOrder();
			tbOrder.setCreateTime(new Date());
			long orderId = idWorker.nextId(); //订单的ID号
			tbOrder.setOrderId(orderId); //雪花算法生成订单ID
			
			//将所有订单号存入付款订单的orderList属性
			orderList.add(orderId);
			
			tbOrder.setPaymentType("1");  //微信支付
			tbOrder.setReceiver(order.getReceiver()); //收货人
			tbOrder.setReceiverAreaName(order.getReceiverAreaName());//收货地址
			tbOrder.setReceiverMobile(order.getReceiverMobile());//收货人手机号
			tbOrder.setSellerId(cart.getSellerId()); //设置商家的ID
			tbOrder.setSourceType("2");//订单来源：1:app端，2：pc端，3：M端，4：微信端，5：手机qq端
			tbOrder.setStatus("1");//状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭,7、待评价
			tbOrder.setUserId(order.getUserId());
		
			
			double money = 0.0;
			//循环购物车中的商品明细进行订单明细的新增
			List<TbOrderItem> orderItemList = cart.getOrderItemList();
			for (TbOrderItem tbOrderItem : orderItemList) {
				tbOrderItem.setId(idWorker.nextId()); //商品明细也采用雪花算法生成ID
				tbOrderItem.setOrderId(orderId);       // 明细跟订单的多对一关系
				money+=tbOrderItem.getTotalFee().doubleValue();
				
				totalMoney +=money; //将多张订单的金额合计出支付总金额
				
				orderItemMapper.insert(tbOrderItem);
			}
			tbOrder.setPayment(new BigDecimal(money)); //循环所有orderItem中的总金额
			orderMapper.insert(tbOrder);		
		}
		
		if("1".equals(order.getPaymentType())){ //如果是微信支付的话,创建支付订单
			TbPayLog payLog = new TbPayLog();
			payLog.setCreateTime(new Date());
			
			String orderListStr = orderList.toString().replace("[", "").replace("]", "").replace(" ", "");
			payLog.setOrderList(orderListStr);
			payLog.setPayType("1");//微信支付
			System.out.println("===总金额==="+totalMoney);
			payLog.setTotalFee((long)(totalMoney*100)); //将元改成分的进制
			
			
			payLog.setTradeState("0"); //0：未支付  1：已支付
			payLog.setOutTradeNo(idWorker.nextId() + "");  //利用雪花算法生成支付订单ID
			payLog.setUserId(order.getUserId());  //设置当前订单号的用户id
			payLogMapper.insert(payLog);
			
			redisTemplate.boundHashOps("payLog").put(order.getUserId(), payLog);//将支付内容缓存到redis中
		}
		
		//清空redis中的购物车信息
		redisTemplate.boundHashOps("cartList").delete(order.getUserId());
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbOrder order){
		orderMapper.updateByPrimaryKey(order);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbOrder findOne(Long id){
		return orderMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			orderMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbOrder order, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbOrderExample example=new TbOrderExample();
		Criteria criteria = example.createCriteria();
		
		if(order!=null){			
						if(order.getPaymentType()!=null && order.getPaymentType().length()>0){
				criteria.andPaymentTypeLike("%"+order.getPaymentType()+"%");
			}
			if(order.getPostFee()!=null && order.getPostFee().length()>0){
				criteria.andPostFeeLike("%"+order.getPostFee()+"%");
			}
			if(order.getStatus()!=null && order.getStatus().length()>0){
				criteria.andStatusLike("%"+order.getStatus()+"%");
			}
			if(order.getShippingName()!=null && order.getShippingName().length()>0){
				criteria.andShippingNameLike("%"+order.getShippingName()+"%");
			}
			if(order.getShippingCode()!=null && order.getShippingCode().length()>0){
				criteria.andShippingCodeLike("%"+order.getShippingCode()+"%");
			}
			if(order.getUserId()!=null && order.getUserId().length()>0){
				criteria.andUserIdLike("%"+order.getUserId()+"%");
			}
			if(order.getBuyerMessage()!=null && order.getBuyerMessage().length()>0){
				criteria.andBuyerMessageLike("%"+order.getBuyerMessage()+"%");
			}
			if(order.getBuyerNick()!=null && order.getBuyerNick().length()>0){
				criteria.andBuyerNickLike("%"+order.getBuyerNick()+"%");
			}
			if(order.getBuyerRate()!=null && order.getBuyerRate().length()>0){
				criteria.andBuyerRateLike("%"+order.getBuyerRate()+"%");
			}
			if(order.getReceiverAreaName()!=null && order.getReceiverAreaName().length()>0){
				criteria.andReceiverAreaNameLike("%"+order.getReceiverAreaName()+"%");
			}
			if(order.getReceiverMobile()!=null && order.getReceiverMobile().length()>0){
				criteria.andReceiverMobileLike("%"+order.getReceiverMobile()+"%");
			}
			if(order.getReceiverZipCode()!=null && order.getReceiverZipCode().length()>0){
				criteria.andReceiverZipCodeLike("%"+order.getReceiverZipCode()+"%");
			}
			if(order.getReceiver()!=null && order.getReceiver().length()>0){
				criteria.andReceiverLike("%"+order.getReceiver()+"%");
			}
			if(order.getInvoiceType()!=null && order.getInvoiceType().length()>0){
				criteria.andInvoiceTypeLike("%"+order.getInvoiceType()+"%");
			}
			if(order.getSourceType()!=null && order.getSourceType().length()>0){
				criteria.andSourceTypeLike("%"+order.getSourceType()+"%");
			}
			if(order.getSellerId()!=null && order.getSellerId().length()>0){
				criteria.andSellerIdLike("%"+order.getSellerId()+"%");
			}
	
		}
		
		Page<TbOrder> page= (Page<TbOrder>)orderMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}

		@Override
		public TbPayLog searchPayLogFromRedis(String userId) {
			return (TbPayLog) redisTemplate.boundHashOps("payLog").get(userId);
		}
	
}
