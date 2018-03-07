package com.tasfe.sis.order.handle;

public interface AdapterHandler<T> {
	/**
	 * 内部前置导入
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public boolean handle(T vo) throws Exception;
	/**
	 * 外部前置导入
	 * @param List<vo>
	 * @return
	 * @throws Exception
	 */
	public boolean insertVo	(T vo) throws Exception;
}
