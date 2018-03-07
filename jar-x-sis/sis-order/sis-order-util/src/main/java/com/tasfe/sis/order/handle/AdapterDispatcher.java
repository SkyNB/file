package com.tasfe.sis.order.handle;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.tasfe.sis.order.service.SpringContextUtils;
import com.tasfe.sis.order.util.ClassUtil;

public class AdapterDispatcher{
	protected Map<String, AdapterHandler> msgHandlers;
	
	private static Logger logger = LoggerFactory.getLogger(AdapterDispatcher.class);
	
	private static Map<String,String> map = Maps.newHashMap();
	static {
		map.put("OrderImpServiceImpl", "1");
		map.put("BlackListServiceImpl", "1");
		map.put("PersonalCreditServiceImpl", "1");
	}
	// CONSTRUCT
	public AdapterDispatcher() {
		this.msgHandlers = new HashMap<String, AdapterHandler>();
		//List<Class> inter =ClassUtil.getAllClassByInterface(AdapterHandler.class);
		for (Iterator it= map.entrySet().iterator();it.hasNext();) {
			Map.Entry<String,String> value = (Map.Entry)it.next();
			try {
				String clsName = StringUtils.uncapitalize(value.getKey());
				logger.info(clsName);
				AdapterHandler t2 = (AdapterHandler)SpringContextUtils.getBean(clsName);
				logger.info("AdapterHandler"+t2.getClass());
				Type type= t2.getClass().getGenericInterfaces()[0];
				ParameterizedType realType =(ParameterizedType)type;
				Type[] allTypes = realType.getActualTypeArguments();
				String cls =ClassUtil.getLast((allTypes[0]).getTypeName());
				logger.info("cls="+cls);
				this.msgHandlers.put(cls, t2);
			} catch (Exception e) {
				logger.info(e.getMessage());
			}
		}

		return;
	}

	// SINGLETON
	private static final class Singleton {
		public static AdapterDispatcher instance = new AdapterDispatcher();
	}

	public static AdapterDispatcher getDefaultInstance() {
		return (Singleton.instance);
	}

	// HANDLER
	/**
	 * Register as msg handler
	 */
	public synchronized AdapterHandler subscribe(String cls) {
		logger.info(msgHandlers.toString());
		return msgHandlers.get(cls);
	}
}
