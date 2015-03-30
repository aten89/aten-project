/**
 * 
 */
package org.eapp.rpc.session;

import org.eapp.hbean.ActorAccount;

/**
 * @author linliangyi 2008-06-21
 * @version 1.0
 */
public interface ICredenceAlgorithm {
	/**
	 * RPC接入帐号凭证校验
	 * @param actor  RPC接入帐号对象
	 * @param targetCredence 待校验凭证
	 * @return
	 */
	public boolean verifyCredence(ActorAccount actor , String targetCredence);
}
